/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package org.teamsoft.myBeans;

import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.controller.*;
import org.teamsoft.entity.*;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.CompetenceFacade;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author G1lb3rt & jpinas
 */
@Named("myProjectController")
@SessionScoped
public class MyProjectController implements Serializable {

    @Inject
    ProjectController projectController;
    @Inject
    ProjectStructureController projectStructureController;
    @Inject
    CompetenceFacade competenceFachade;
    @Inject
    RoleController rolController;
    @Inject
    ProjectRolesController projectRoles;
    @Inject
    RoleCompetitionController roleCompetition;
    @Inject
    LocaleConfig localeConfig;
    @Inject
    ProjectTechCompetenceController projectTechCompetenceController;

    private String structureCreate = ""; //el nombre de la estructura que hay en el comboBox
    private boolean newSructure;

    private TreeNode root;//para el arbol del ultimo step (4)
    private TreeNode selectedNode;//para el arbol del ultimo step (4)

    private List<Role> selectedRoles;

    ////// SINGLE PROJECT CREATION///////////////////////////////////////////////////////////////////////////////////
    //////////STEP 2/////////////////
    private List<Role> rolList = new ArrayList<>();   //Lista de roles
    private List<ProjectRoles> persistentRoles; //ProjectRoles a persistir por proyecto
    private List<ProjectRoles> rolesDeleteList; //ProjectRoles a quitar de la lista
    ////STEP 3////////////////
    private List<Competence> techCmpItems; //Lista de competencias técnicas
    private List<ProjectTechCompetence> selectedTechComps; //Para almacenar las competencias a persistir
    private List<ProjectTechCompetence> deleteListTechComps; //Para almacenar las competencias a quitar de la lista

    ////////Manejar botones/////////
    private boolean disableAddRolButton = true;
    private boolean disableTechCompAddButton = true;

    ///////CREATE MANY PROJECTS////////////////////////////////////////////////
    private List<Project> projectList; //lista de proyectos a crear
    private List<Project> projectRemoveList;

    ////////////Datos del proyecto/////
    private ClientEntity clientE;
    private Date inDate;
    private String name;
    private County province;

    ///////Competencias tecnicas//////////////////////////////////////////////////////////////////////////////////////
    List<Competence> technicalCompetenceForRol; //competencias tecnicas seleccionada para agregarle al rol seleccionado (como un buffer)
    Long idRolForTechnicalCompetences; //rol seleccionado para agregar competencias técnicas
    //List<ProjectRoles> rolesForTechnicalCompetences = new ArrayList<>();

    //////////////////////////////////////////
    public void prepareCreate() {

        projectController.prepareCreate();
        projectStructureController.prepareCreate();
        projectRoles.prepareCreate();
        projectTechCompetenceController.prepareCreate();
        roleCompetition.prepareCreate();
        persistentRoles = new ArrayList<>();
        // rolList2 = new ArrayList<>();
        selectedRoles = new ArrayList<>();
        rolList = rolController.getItemsAvailableSelectOne();
        techCmpItems = competenceFachade.getTechnicalCompetences();
        selectedTechComps = new ArrayList<>();
        technicalCompetenceForRol = new ArrayList<>();
    }

    /**
     * Opciones de creacion de proyectos
     *
     * @return
     */
    public String prepareEdit() {

        prepareCreate();
        projectList = null; // por si han estado antes en la opcion create many;

        return "edit-project";
    }

    /**
     * Opciones de creacion de proyectos
     *
     * @return
     */
    public String createOneProject_Listener() {

        prepareCreate();
        projectList = null; // por si han estado antes en la opcion create many;
        return "create-one-project";
    }

    /**
     * Opciones de creacion de proyectos
     *
     * @return
     */
    public String createMultipleProjectHo_Listener() {

        prepareCreate();
        projectList = new ArrayList<>();
        projectRemoveList = new ArrayList<>();
        return "create-homogeneous-projects";
    }

    /**
     * Opciones de creacion de proyectos
     *
     * @return
     */
    public String createMultipleProjectHe_Listener() {

        prepareCreate();

        return "/manageProjects/project/CreateMany";
    }

    /**
     * Para añadir la competencia seleccionada a la lista
     */
    public void addTechComp_Listener() {
        ProjectTechCompetence ptc;
        //---Se busca el ProjectRoles seleccionado ------------/
        ProjectRoles pr = findProjectRolByRolId(persistentRoles, idRolForTechnicalCompetences);
        if (pr.getProjectTechCompetenceList() == null) {
            pr.setProjectTechCompetenceList(new ArrayList<>());
        }
        //------creo los objetos TechnicalCompetence con sus datos por cada comp. seleccionada--------/
        for (Competence tcr : technicalCompetenceForRol) {
            ptc = new ProjectTechCompetence();
            ptc.setLevelFk(projectTechCompetenceController.getSelected().getLevelFk());
            ptc.setCompetenceFk(tcr);
            ptc.setCompetenceImportanceFk(projectTechCompetenceController.getSelected().getCompetenceImportanceFk());
            ptc.setProjectRoles(pr);
            selectedTechComps.add(ptc); //agrego los datos de la nueva competencia a la lista de competencias para mostrar en tabla
            //--------se le asignan las competencias técnicas al ProjectRoles----------/
            pr.getProjectTechCompetenceList().add(ptc);
        }
//-------------------Se limpian los valores de los selectores-----------------------/
        projectTechCompetenceController.getSelected().setLevelFk(null);
        projectTechCompetenceController.getSelected().setCompetenceImportanceFk(null);
        technicalCompetenceForRol.clear();
        idRolForTechnicalCompetences = null;
    }

    /**
     * Para buscar un ProjectRoles en una lista segun su Id de rol
     *
     * @param id
     * @return
     */
    private ProjectRoles findProjectRolByRolId(List<ProjectRoles> lista, Long id) {
        int i = 0;
        boolean find = false;
        ProjectRoles pr = new ProjectRoles();
        while (i < lista.size() && !find) {
            pr = lista.get(i);
            if (pr.getRoleFk().getId().equals(id)) {
                find = true;
            } else {
                i++;
            }
        }
        return pr;
    }

    /**
     * Habilita el boton adicionar rol cuand todos los campos poseen datos.
     */
    public void hadleAddRolButton_Listener() {
        if (!selectedRoles.isEmpty() && !rolList.isEmpty() && projectRoles.getSelected().getRoleLoadFk() != null) {
            if (projectRoles.getSelected().getRoleLoadFk().getId() != null && projectRoles.getSelected().getAmountWorkersRole() > 0) {
                disableAddRolButton = false;
            } else {
                if (!disableAddRolButton) {
                    disableAddRolButton = true;
                }
            }
        } else {
            if (!disableAddRolButton) {
                disableAddRolButton = true;
            }
        }
    }

    /**
     * Para manejar la activasion/desactivasion del boton adicionar competencia
     *
     * @return
     */
    public boolean handleAddCompButton_Listener() {
        boolean active = true;

        if (idRolForTechnicalCompetences != null && !technicalCompetenceForRol.isEmpty()
                && projectTechCompetenceController.getSelected().getLevelFk() != null
                && projectTechCompetenceController.getSelected().getCompetenceImportanceFk() != null) {

            if (projectTechCompetenceController.getSelected().getLevelFk().getId() != null
                    && projectTechCompetenceController.getSelected().getCompetenceImportanceFk().getId() != null) {
                active = false;
            }
        }

        return active;
    }

    /**
     * Para añadir el rol seleccionado a la lista
     */
    public void addRol_Listener() {

        ProjectRoles pr; // nuevo puntero para evitar que al insertar solo se actualicen por referencia los objetos

        for (Role rol : selectedRoles) {
            pr = new ProjectRoles();
            pr.setRoleFk(rol);
            pr.setRoleLoadFk(projectRoles.getSelected().getRoleLoadFk());
            if (rol.isBoss()) { //no puede haber mas de un jefe de projecto por proyecto
                pr.setAmountWorkersRole(1);
            } else {
                pr.setAmountWorkersRole(projectRoles.getSelected().getAmountWorkersRole());
            }
            pr.setProjectStructureFk(projectStructureController.getSelected());
            persistentRoles.add(pr); //agrego los datos del nuevo projectRole a la lista
            rolList.remove(rol); // quito el projectRole de la lista de roles elegibles
        }

//Se limpian los valores de los selectores
//projectRoles.getSelected().setRoleFk(null);
        selectedRoles.clear();
        projectRoles.getSelected().setRoleLoadFk(null);
        projectRoles.getSelected().setAmountWorkersRole(0);
    }

    /**
     * Quitar elementos seleccionados de la tabla de roles (step 2)
     */
    public void removeRoleFromTable_Listener() {

        if (!rolesDeleteList.isEmpty()) {
            for (ProjectRoles item : rolesDeleteList) {
                for (int i = 0; i < persistentRoles.size(); i++) {
                    if (item.getRoleFk().getId().equals(persistentRoles.get(i).getRoleFk().getId())) {
                        persistentRoles.remove(i);// elimino el objeto de la fuente de datos de la tabla
                        rolList.add(item.getRoleFk()); //devuelvo el elemento a la lista de roles disponibles
                    }
                }
            }
            rolesDeleteList.clear(); // una vez eliminados los elementos limpio la lista
        }
    }

    /**
     * Quitar elementos seleccionados de la tabla
     */
    public void removeGenCompFromTable_Listener() {
        int i;
        int tecCompIndexInProjectRoles;
        ProjectTechCompetence ptcAux;
        ProjectRoles prAux;
        if (!deleteListTechComps.isEmpty()) {
            for (ProjectTechCompetence item : deleteListTechComps) {
                i = 0;
                boolean find = false;
                while (i < selectedTechComps.size() && !find) {
                    if (item.getCompetenceFk().getId().equals(selectedTechComps.get(i).getCompetenceFk().getId())) {
                        if (item.getProjectRoles().getRoleFk().getId().equals(selectedTechComps.get(i).getProjectRoles().getRoleFk().getId())) {
                            ptcAux = selectedTechComps.remove(i); // Elimino la competencia del datasource de la tabla
                            prAux = findProjectRolByRolId(persistentRoles, ptcAux.getProjectRoles().getRoleFk().getId()); //busco el ProjectRol
                            tecCompIndexInProjectRoles = findTechCompIndexByCompId(prAux.getProjectTechCompetenceList(), ptcAux.getCompetenceFk().getId());// busco la competencia asociada al PR
                            prAux.getProjectTechCompetenceList().remove(tecCompIndexInProjectRoles);
                            find = true;
                        }
                    } else {
                        i++;
                    }
                }
            }
            deleteListTechComps.clear(); // una vez eliminados los elementos limpio la lista
        }

    }

    /**
     * Manejar el flujo del wizard cuando se crea un proyecto
     *
     * @param event
     * @return
     */
    public String singleCreateFlow_Listener(FlowEvent event) {
        String step = event.getNewStep();

        switch (step) {
            case "StructureData": {
                if (validateProyectInDB(projectController.getSelected().getProjectName())) {
                    step = event.getOldStep();
                    MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("wizarProjectValidateNameInDB"));
                }
            }
            break;
            case "techCompData": {
                //Saltar paso 3 en dependencia de si se crea o no una estructura en el step 2
                boolean exist = projectStructureController.nameExist(projectStructureController.getSelected().getName());
                if (exist) { // si existe la estructura puedo estar habanzando o retrocediendo
                    if (event.getOldStep().equalsIgnoreCase("Confirm")) { //significa que estoy retrocediendo y ya habia seleccionado una estructura
                        step = "StructureData";
                    } else {
                        step = "Confirm"; //salto la asignacion de competencias Step-3, dado que ya estan dentro de la estructura seleccionada
                        //indico que el arbol se debe generar con los roles y competencias de la estructura y no con los entrados por el usuario si hay alguno.
                        fillTreeProjects(true);
                        //Notifico al usuario eltipo de estructura que usara para la creacion del proyecto
                        MyJSF_Util.addSuccessMessage(localeConfig.getBundleValue("wizarProjectExistentStructInf"));
                    }
                }
                if (!exist && persistentRoles.isEmpty()) { //estoy habanzando, es una nueva estructura, y no han declarado roles
                    step = event.getOldStep();
                    MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("wizarProjectNoRolesWng"));
                }
            }
            break;

            case "Confirm": {
                //es una nueva estructura y el arbol debe generarse con los datos que entro el usuario
                fillTreeProjects(false);
                //Notifico al usuario eltipo de estructura que usara para la creacion del proyecto
                MyJSF_Util.addSuccessMessage(localeConfig.getBundleValue("wizarProjectExistentNewStructInf"));
            }
            break;
        }

        return step;
    }

    public String manyCreateFlow_Listener(FlowEvent event) {
        String step = event.getNewStep();

        switch (step) {
            case "StructureData": {
                //Validar que se cree al meos 1 proyecto en el step 1
                if (projectList.isEmpty()) {
                    step = event.getOldStep();
                    MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("wizarProjectNoProjectWng"));
                }
            }
            break;

            case "techCompData": {
                //Saltar paso 3 en dependencia de si se crea o no una estructura en el step 2
                boolean exist = projectStructureController.nameExist(projectStructureController.getSelected().getName());
                if (exist) { // si existe la estructura puedo estar habanzando o retrocediendo
                    if (event.getOldStep().equalsIgnoreCase("Confirm")) { //significa que estoy retrocediendo y ya habia seleccionado una estructura
                        step = "StructureData";
                    } else {
                        step = "Confirm"; //salto la asignacion de competencias Step-3, dado que ya estan dentro de la estructura seleccionada
                        //indico que el arbol se debe generar con los roles y competencias de la estructura y no con los entrados por el usuario si hay alguno.
                        fillTreeProjects(true);
                        //Notifico al usuario el tipo de estructura que usará para la creación del proyecto
                        MyJSF_Util.addSuccessMessage(localeConfig.getBundleValue("wizarProjectExistentStructInf"));
                    }
                }
                if (!exist && persistentRoles.isEmpty()) { //estoy habanzando, es una nueva estructura, y no han declarado roles
                    step = event.getOldStep();
                    MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("wizarProjectNoRolesWng"));
                } else { // se creo una estructura y roles, entonces valido que se defina el jefe de proyecto
                    if (!validateProjectBoss() && !exist) {
                        step = event.getOldStep();
                        MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("most_assign_team_leader"));
                    }
                }
            }
            break;

            case "Confirm": {
                //es una nueva estructura y el arbol debe generarse con los datos que entro el usuario
                fillTreeProjects(false);
                //Notifico al usuario eltipo de estructura que usara para la creacion del proyecto
                MyJSF_Util.addSuccessMessage(localeConfig.getBundleValue("wizarProjectExistentNewStructInf"));
            }
            break;

        }

        return step;
    }

    public boolean validateProjectBoss() {
        int i = 0;
        boolean find = false;
        while (i < persistentRoles.size() && !find) {
            if (persistentRoles.get(i).getRoleFk().isBoss()) {
                find = true;
            } else {
                i++;
            }
        }
        return find;
    }

    /**
     * Mostrar solo las competencias disponibles para un rol (quitando de la
     * lista de competencias seleccionables las que ya se le agregaron)
     */
    public void handleRolSelector_Listener() {
        techCmpItems = competenceFachade.getTechnicalCompetences(); //obtener el listado de todas las competencias tecnicas en el sistema
        ProjectRoles pr = findProjectRolByRolId(persistentRoles, idRolForTechnicalCompetences); //obtener ProjectRoles seleccionado
        List<ProjectTechCompetence> competenceList = pr.getProjectTechCompetenceList(); // ProjectTechCompetence del ProjectRol seleccionado
        if (competenceList != null) { //para cuando se llama al listener antes de que se le asignen competencias al rol
            for (ProjectTechCompetence tcItem : competenceList) { //recorro la lista de TechnicalCompetences del ProjectRoles seleccionado
                int index = findCompetenceIndex(techCmpItems, tcItem.getCompetenceFk().getId());
                if (index != -1) {
                    techCmpItems.remove(index); // elimino la competencia de la lista a mostrar si ya fue asignada
                }
            }
            if (techCmpItems.isEmpty()) { //si tiene todas las competencias tecnicas asignadas
                //se notifica al usuario cuando se agotan las competencias
                MyJSF_Util.addSuccessMessage(localeConfig.getBundleValue("wizarProjectNoCompInf"));
            }
        }
    }

    /**
     * Buscar tecCompIndexInProjectRoles de competencia en una lista
     *
     * @return tecCompIndexInProjectRoles || -1 si no encuentra
     */
    private int findCompetenceIndex(List<Competence> list, Long cmpId) {
        boolean find = false;
        int index = -1;
        int i = 0;
        while (i < list.size() && !find) {
            if (cmpId.equals(list.get(i).getId())) {
                find = true;
                index = i;
            } else {
                i++;
            }
        }
        return index;
    }

    /**
     * Buscar indice de una TechnicalCompetence en una lista de
     * ProjectTechnicalCompetence segun el id de la Competence
     *
     * @return tecCompIndexInProjectRoles || -1 si no encuentra
     */
    private int findTechCompIndexByCompId(List<ProjectTechCompetence> list, Long tcmpId) {
        boolean find = false;
        int index = -1;
        int i = 0;
        while (i < list.size() && !find) {
            if (tcmpId.equals(list.get(i).getCompetenceFk().getId())) {
                find = true;
                index = i;
            } else {
                i++;
            }
        }
        return index;
    }

    /**
     * Comportamiento del boton agregar projecto
     *
     * @return
     */
    public boolean handleAddProjecBtn_Listener() {
        boolean inactive = true;

        if (name != null && inDate != null && clientE != null && province != null) {
            if (!name.equalsIgnoreCase("") && !inDate.toString().equalsIgnoreCase("") && clientE != null && province != null) {
                inactive = false;
            }
        }
        return inactive;
    }

    public boolean handleAddProjecBtn_Listener(SelectEvent event) {
        Date date = (Date) event.getObject();
        return true;
    }

    /**
     * Añadir el proyecto a la lista de proyectos
     */
    public void addProject_Listener() {

        if (!validateProjectInList(name)) { //validar que el nombre no se encuentre en la lista de proyectos a crear
            if (!validateProyectInDB(name)) { //validar que el nombre no se encuentre en BD
                Project newp = new Project();
                newp.setClientEntityFk(clientE);
                newp.setInitialDate(inDate);
                newp.setProjectName(name);
                newp.setProvinceFk(province);
                projectList.add(newp);
                //para limpiar los campos editables
                clientE = null;
                inDate = null;
                name = null;
                province = null;
            } else {
                MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("wizarProjectValidateNameInDB"));
            }
        } else {
            MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("wizarProjectValidateName"));
        }
    }

    /**
     * Retorna true si encuentra un proyecto con el nombre que se intenta
     * ingresar en la lista de proyectosa persistir
     *
     * @return
     */
    private boolean validateProjectInList(String name) {
        int i = 0;
        boolean find = false;
        while (i < projectList.size() && !find) {
            if (projectList.get(i).getProjectName().equalsIgnoreCase(name)) {
                find = !find;
            } else {
                i++;
            }
        }
        return find;
    }

    /**
     * Retorna true si encuentra un proyecto con el nombre que se intenta
     * ingresar en la Base de Datos
     *
     * @return
     */
    private boolean validateProyectInDB(String name) {
        List<Project> listaP = projectController.getItemsAvailableSelectOne();
        int i = 0;
        boolean find = false;
        while (i < listaP.size() && !find) {
            if (listaP.get(i).getProjectName().equalsIgnoreCase(name)) {
                find = !find;
            } else {
                i++;
            }
        }
        return find;
    }

    /**
     * Quitar proyectos de la lista
     */
    public void removeProject_Listener() {
        int i;
        boolean find;
        for (Project item : projectRemoveList) {
            i = 0;
            find = false;
            while (i < projectList.size() && !find) {
                if (item.getProjectName().equalsIgnoreCase(projectList.get(i).getProjectName())) {
                    projectList.remove(projectList.get(i));
                    find = !find;
                }
                i++;
            }
        }
        projectRemoveList.clear();
    }

    public void create() {
        Project p = projectController.getSelected(); //el projecto

        //Creo project nStructure y asigno dependencias
        ProjectStructure ps = projectStructureController.getSelected();
        ps.setProjectRolesList(persistentRoles);
        // ps.setProjectTechCompetenceList(selectedTechComps); //con el nuevo diseño de la BD esto cambia.

        //creo el ciclo y asigno dependencias
        Cycle cycle = new Cycle();
        cycle.setBeginDate(p.getInitialDate());
        cycle.setEndDate(p.getEndDate());
        cycle.setProjectFk(p);

        List<Cycle> cycleList = new ArrayList<>();
        cycleList.add(cycle);

        // asigno su cycleList
        p.setCycleList(cycleList);
        projectController.create();
        if (FacesContext.getCurrentInstance().getMaximumSeverity() != null) {
            if (FacesContext.getCurrentInstance().getMaximumSeverity().equals(FacesMessage.SEVERITY_INFO)) {
                prepareCreate();
            }
        }
    }

    /**
     * Método para crear varios proyectos con un misma estructura
     */
    public void createMany() {
        List<Cycle> cycleList = new ArrayList<>();
        ProjectStructure ps = projectStructureController.getSelected();
        for (Project p : projectList) {
            //creo el ciclo y asigno dependencias
            Cycle cycle = new Cycle();
            cycle.setBeginDate(p.getInitialDate());
            cycle.setEndDate(p.getEndDate());
            cycle.setProjectFk(p);
            cycle.setStructureFk(ps);
            cycleList.add(cycle);
        }
        for (ProjectRoles p : persistentRoles) {
            p.setProjectStructureFk(ps);
        }
        //asigno dependencias
        ps.setProjectRolesList(persistentRoles);
        ps.setCycleList(cycleList);
        //Salavo la estructura con todos los proyectos
        projectStructureController.create();
    }

    /**
     * Para mostrar las dependencias de los proyectos asignados existentStruct
     * == true significa que la estructura ya existe
     *
     * @param existentStruct
     */
    public void fillTreeProjects(boolean existentStruct) {
        ProjectStructure structObject;
        TreeNode nStructure;
        // TreeNode nProjects;
        TreeNode rnode;
        TreeNode tcnode;
        nStructure = new DefaultTreeNode(projectStructureController.getSelected()); //preparo el nodo nStructure
        nStructure.setType("nStruct");
        root = nStructure;
        nStructure.setExpanded(true);
        if (projectList == null) { //quiere decir que estoy creando un solo proyecto
            if (!existentStruct) { //usar datos entrados por el usuario
                for (ProjectRoles role : persistentRoles) {
                    rnode = new DefaultTreeNode(role);//el rol
                    rnode.setType("nRole");
                    nStructure.getChildren().add(rnode);
                    for (ProjectTechCompetence item : selectedTechComps) {
                        if (role.getRoleFk().getRoleName().equalsIgnoreCase(item.getProjectRoles().getRoleFk().getRoleName())) {
                            tcnode = new DefaultTreeNode(item);//las competencias del rol
                            tcnode.setType("nTComp");
                            rnode.getChildren().add(tcnode);
                        }
                    }
                }
            } else {
                //---------------usar datos propios de la estructura--------------------/
                structObject = projectStructureController.findByName(projectStructureController.getSelected().getName());
                if (structObject.getId() != null) { //garantizando que se encuentre una estructura válida
                    for (ProjectRoles role : structObject.getProjectRolesList()) {
                        rnode = new DefaultTreeNode(role);//el rol
                        rnode.setType("nRole");
                        nStructure.getChildren().add(rnode);
                        for (ProjectTechCompetence item : role.getProjectTechCompetenceList()) {
                            if (role.getRoleFk().getRoleName().equalsIgnoreCase(item.getProjectRoles().getRoleFk().getRoleName())) {
                                tcnode = new DefaultTreeNode(item);//las competencias del rol
                                tcnode.setType("nTComp");
                                rnode.getChildren().add(tcnode);
                            }
                        }
                    }
                }
            }
        } else { //significa que estoy creando un multiples proyectos
//añado cada proyecto con sus dependencias a la estructura
            for (Project pItem : projectList) {
                if (!existentStruct) {
                    for (ProjectRoles role : persistentRoles) {
                        rnode = new DefaultTreeNode(role);//el rol
                        rnode.setType("nRole");
                        nStructure.getChildren().add(rnode);
                        if (role.getProjectTechCompetenceList() != null) {
                            for (ProjectTechCompetence item : role.getProjectTechCompetenceList()) {
                                if (role.getRoleFk().getRoleName().equalsIgnoreCase(item.getProjectRoles().getRoleFk().getRoleName())) {
                                    tcnode = new DefaultTreeNode(item);//las competencias del rol
                                    tcnode.setType("nTComp");
                                    rnode.getChildren().add(tcnode);
                                }
                            }
                        }
                    }
                } else {
//----------------------usar datos propios de la estructura---------------------/
                    structObject = projectStructureController.findByName(projectStructureController.getSelected().getName());
                    if (structObject.getId() != null) { //garantizando que se encuentre una estructura válida
                        for (ProjectRoles role : structObject.getProjectRolesList()) {
                            rnode = new DefaultTreeNode(role);//el rol
                            rnode.setType("nRole");
                            nStructure.getChildren().add(rnode);
                            for (ProjectTechCompetence item : role.getProjectTechCompetenceList()) {
                                if (role.getRoleFk().getRoleName().equalsIgnoreCase(item.getProjectRoles().getRoleFk().getRoleName())) {
                                    tcnode = new DefaultTreeNode(item);//las competencias del rol
                                    tcnode.setType("nTComp");
                                    rnode.getChildren().add(tcnode);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public ProjectRoles getProjectRolesByRolId(Long idRol) {
        ProjectRoles pr = null;
        int i = 0;
        boolean find = false;
        ProjectRoles aux = null;
        while (i < persistentRoles.size() && !find) {
            aux = persistentRoles.get(i);
            if (aux.getRoleFk().getId().equals(idRol)) {
                pr = aux;
                find = true;
            }
            i++;
        }
        return pr;
    }

    public List<ProjectTechCompetence> getSelectedTechComps() {
        return selectedTechComps;
    }

    public void setSelectedTechComps(List<ProjectTechCompetence> selectedTechComps) {
        this.selectedTechComps = selectedTechComps;
    }

    public List<ProjectTechCompetence> getDeleteListTechComps() {
        return deleteListTechComps;
    }

    public void setDeleteListTechComps(List<ProjectTechCompetence> deleteListTechComps) {
        this.deleteListTechComps = deleteListTechComps;
    }

    public boolean isDisableTechCompAddButton() {
        return disableTechCompAddButton;
    }

    public void setDisableTechCompAddButton(boolean disableTechCompAddButton) {
        this.disableTechCompAddButton = disableTechCompAddButton;
    }

    public List<Competence> getTechCmpItems() {
        return techCmpItems;
    }

    public void setTechCmpItems(List<Competence> techCmpItems) {
        this.techCmpItems = techCmpItems;
    }

    public List<Role> getRolList() {
        return rolList;
    }

    public void setRolList(List<Role> rolList) {
        this.rolList = rolList;
    }

    public List<ProjectRoles> getPersistentRoles() {
        return persistentRoles;
    }

    public void setPersistentRoles(List<ProjectRoles> persistentRoles) {
        this.persistentRoles = persistentRoles;
    }

    public List<ProjectRoles> getRolesDeleteList() {
        return rolesDeleteList;
    }

    public void setRolesDeleteList(List<ProjectRoles> rolesDeleteList) {
        this.rolesDeleteList = rolesDeleteList;
    }

//    public List<ProjectRoles> getRolsForTechnicalCompetences() {
//        return rolsForTechnicalCompetences;
//    }
//
//    public void setRolsForTechnicalCompetences(List<ProjectRoles> rolsForTechnicalCompetences) {
//        this.rolsForTechnicalCompetences = rolsForTechnicalCompetences;
//    }
    public Long getIdRolForTechnicalCompetences() {
        return idRolForTechnicalCompetences;
    }

    public void setIdRolForTechnicalCompetences(Long id) {
        this.idRolForTechnicalCompetences = id;
    }

    public boolean isDisableAddRolButton() {
        return disableAddRolButton;
    }

    public void setDisableAddRolButton(boolean disableAddRolButton) {
        this.disableAddRolButton = disableAddRolButton;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public List<Project> getProjectRemoveList() {
        return projectRemoveList;
    }

    public void setProjectRemoveList(List<Project> projectRemoveList) {
        this.projectRemoveList = projectRemoveList;
    }

    public ClientEntity getClientE() {
        return clientE;
    }

    public void setClientE(ClientEntity clientE) {
        this.clientE = clientE;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public County getProvince() {
        return province;
    }

    public void setProvince(County province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStructureCreate() {
        return projectStructureController.getSelected().getName();
    }

    public void setStructureCreate(String structureCreate) {
        newSructure = true; //asumir que es una nueva estructura
        List<ProjectStructure> structures = projectStructureController.getItemsAvailableSelectOne();

        int i = 0;
        boolean find = false;
        while (i < structures.size() && !find) {
            if (structureCreate.equalsIgnoreCase(structures.get(i).getName())) {
                find = true;
                newSructure = false; //demostrado que la estructura existe
                projectStructureController.setSelected(structures.get(i)); //se prepara para editar
            } else {
                i++;
            }
        }
        if (!find) {
            projectStructureController.setSelected(new ProjectStructure());    //por si acaso queda ninguna instancia vieja en la session
            projectStructureController.getSelected().setName(structureCreate); //si es nueva se prepara para crear
        }
    }

    public boolean isNewSructure() {
        return newSructure;
    }

    public void setNewSructure(boolean newSructure) {
        this.newSructure = newSructure;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public List<Competence> getTechnicalCompetenceForRol() {
        return technicalCompetenceForRol;
    }

    public void setTechnicalCompetenceForRol(List<Competence> technicalCompetenceForRol) {
        this.technicalCompetenceForRol = technicalCompetenceForRol;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
}
