/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.controller.CompetenceValueController;
import org.teamsoft.controller.ConflictIndexController;
import org.teamsoft.controller.LevelsController;
import org.teamsoft.controller.ProjectController;
import org.teamsoft.controller.RoleController;
import org.teamsoft.controller.RoleEvalController;
import org.teamsoft.controller.RoleEvaluationController;
import org.teamsoft.controller.WorkerConflictController;
import org.teamsoft.controller.WorkerController;
import org.teamsoft.controller.util.MemberToEvaluate;
import org.teamsoft.entity.AssignedRole;
import org.teamsoft.entity.Competence;
import org.teamsoft.entity.CompetenceDimension;
import org.teamsoft.entity.CompetenceValue;
import org.teamsoft.entity.ConflictIndex;
import org.teamsoft.entity.Cycle;
import org.teamsoft.entity.Levels;
import org.teamsoft.entity.Project;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.RoleEval;
import org.teamsoft.entity.RoleEvaluation;
import org.teamsoft.entity.Worker;
import org.teamsoft.entity.WorkerConflict;
import org.teamsoft.model.CompetenceFacade;
import org.teamsoft.model.CompetenceValueFacade;
import org.teamsoft.model.RoleEvaluationFacade;
import org.teamsoft.model.RoleFacade;
import org.teamsoft.model.WorkerConflictFacade;

/**
 *
 * @author Derly
 */
@Named(value = "closeProjectBean")
@SessionScoped
public class closeProjectController implements Serializable{


    @Inject
    WorkerController workerController;
    @Inject
    ProjectController projectController;
    @Inject
    CompetenceValueController compValController;
    @Inject
    LevelsController levelscontroller;
    @EJB
    CompetenceFacade competenceFacade;
    @EJB
    RoleFacade roleFacade;
    @EJB
    RoleEvaluationFacade roleEvaluationFacade;
    @Inject
    RoleEvalController roleEvalController;
    @Inject
    RoleEvaluationController roleEvaluationController;  
    @Inject
    RoleController roleController;
    @Inject
    ConflictIndexController conflictIndexController;
    @Inject
    WorkerConflictController workerConflictController; 
    @EJB
    WorkerConflictFacade workerConflictFacade;
    @EJB
    CompetenceValueFacade competenceValueFacade;
    

    Project projectSelected;
    Worker workerSelected;
    MemberToEvaluate memberSelected;
    MemberToEvaluate memberConflictSelected;
    
    
    private List<RoleEval> roleEvalList = null;
    private RoleEval noteRol; 
    private RoleEval noteProject = new RoleEval();
    private List<ConflictIndex> conflictsIndexList = null;
    
    // Para Mostrar las competencias de la persona
    private List<Competence> genCmpItems = null;
    private List<Competence> techCmpItems = null;

    private List<CompetenceDimension> competenceDimensionList;

    //Para almacenar la competencia seleccionada
    private Competence selectedGenComp;
    private Competence selectedTechComp;
    //Para almacenar el nivel seleccionado
    private Levels selectedGenLvl;
    private Levels selectedTechLvl;

    //Para manejar la habilitacion/deshabilitacion de algunos componentes de la GUI
    private boolean disableGenCompAddButton = true;
    private boolean disableTechCompAddButton = true;

    //Para ir almacenando las competencias agregadas
    private List<CompetenceValue> genCmpLvlItems = null;
    private List<CompetenceValue> techCmpLvlItems = null;

    //Para almacenar los elementos seleccionados de la tabla
    private List<CompetenceValue> genCmpLvlTableSelection = null;
    private List<CompetenceValue> techCmpLvlTableSelection = null;

    private List<CompetenceValue> compValueList;  //Para almacenar la lista de competenceValue de la entidad Worker que será persistida en "CASCADE"

    List<Project> projectsByLoggedBoss = null;
    List<MemberToEvaluate> membersSelectedProject = new ArrayList<>(); // lista de miembros a evaluar en el proyecto seleccionado
    //List<Project> projectsByLoggedBoss = filterTeamBossProjects(projectController.getItems());
    
    List<MemberToEvaluate> membersConflict = new ArrayList<>(); // lista para la tabla de editar niveles de compatibilidad
    
    List<Project> finalizedProjects = null;
    
    private boolean buttonFinalize;
    
    private String notEvaluated = "No evaluado"; 
    private String notConflict = "Indiferente";
    List<Worker> workers;

    private Worker userlogged = new Worker();
    
    @PostConstruct   // para levantar los datos iniciales
    public void asd() {
        roleEvalList = roleEvalController.getItems();
        conflictsIndexList = buildConflictIndexList();
        projectsByLoggedBoss = filterTeamBossProjects(projectController.getItems());
        noteProject.setSignificance("Seleccione");
        
    }

    public boolean boosValidation(List<Worker> persons, String userID) {
    boolean isBoss = false;

    return isBoss;
    }
    
    public List<Project> filterTeamBossProjects(List<Project> projectList) {
        List<Project> loggedBossProjects = new ArrayList<>(); // lista de proyectos de los que el usuario logueado es jefe de equipo o el DT
        userlogged = getUserById();
        for (int i = 0; i < projectList.size(); i++) {
            //List<AssignedRole> items = projectList.get(i).getCycleList().get(projectList.get(i).getCycleList().size() - 1).getAssignedRoleList(); // aqui se obtiene la lista de miembros que tiene asignado un  proyecto en su ultimo ciclo
           
            //if(projectList.get(i).getFinalize() == false){
                if(projectList.get(i).getFinalize() == true && projectList.get(i).getClose() == false){
                
                List<AssignedRole> items = lastProjectCycle(projectList.get(i)).getAssignedRoleList();

                for (int j = 0; j < items.size(); j++) {

                 if (items.get(j).getWorkersFk().getId().compareTo(userlogged.getId()) == 0) {  // aki no se si comparar con el id del usuario o con el objeto entero
                    if (items.get(j).getRolesFk().getRoleName().equalsIgnoreCase("Jefe de Proyecto")) {
                        loggedBossProjects.add(projectList.get(i));
                    } 

                 }
               }
            }            
        }

        return loggedBossProjects;
    }

    //---------------Rutas--------------------------
    public String membersToEvaluateRoute() {  //para el action q m lleva a la pagina seleccionar miembros para evaluar
        if(membersSelectedProject.isEmpty()){
             membersSelectedProject = getMembersToEvaluate(projectSelected);
        }
        else{
            findEvaluation(membersSelectedProject);
        }
       buttonFinalize = !verifyEvaluations(); //negar la verificacion
       deleteExtraConflictIndexes();
       return "select-member-evaluation";
    }

    public String backFromMemberToProjectRoute() {// regresa de la seleccion de miembros a la seleccion de proyectos

        return "finalizeProject";
    }

    public String listMembersToEvaluationRoute() {  // al seleccionar un miembro a evaluar lleva a la pagina donde se evalua
        //workerSelected = memberSelected.getMember();
        return prepareEdit();
        //return "member-evaluation";
    }
    
    
    public String finalizeSelectProjectAgain(){// cuando se finaliza un proyecto volver al menu de seleccion de proyectos
        
        projectController.setSelected(projectSelected);
        //projectController.getSelected().setFinalize(true);
        projectController.getSelected().setClose(true);
       //projectController.getSelected().setClose(true);
        //projectController.getSelected().setRolevEvalFk(noteProject);
        projectController.create();
        roleEvalList = roleEvalController.getItems();
        conflictsIndexList = buildConflictIndexList();
        projectsByLoggedBoss = filterTeamBossProjects(projectController.getItems());
       
        
        return "finalizeProject";
    }
    
    public String finalizeToHomeRoute(){// en el menu de seleccionar proyectos regresar al menu de TS
        
        return "to-index";
    }

    //---------Miembros a evaluar del proyecto seleccionado
    public List<MemberToEvaluate> getMembersToEvaluate(Project projectSelected) {
        
        // poner if q verifiq q el userlogged es DT o JE para llenar la lista con los miembros o solo con el jefe
        List<MemberToEvaluate> selectedTeamMembers = new ArrayList<>();
        userlogged = getUserById();
        Cycle lastProjectCycle = lastProjectCycle(projectSelected);
        List<AssignedRole> items = lastProjectCycle.getAssignedRoleList(); // de nuevo los miembros del ultimo ciclo
        boolean repeat;
        int indexRepeatedMember = 0;
      

        for (int i = 0; i < items.size(); i++) {
            /*
           if (items.get(i).getWorkersFk().getId().compareTo(userlogged.getId()) != 0) {
                
                repeat = false;
            
                
                for(int j = 0; j < selectedTeamMembers.size() && !repeat; j++){
                    if(selectedTeamMembers.get(j).getMember().getId().compareTo(items.get(i).getWorkersFk().getId()) == 0){
                        repeat = true;
                        indexRepeatedMember = j;
                    }
                    
                }
                
                if(repeat){
                    //selectedTeamMembers.get(indexRepeatedMember).getRoleMemberList().add(items.get(i).getRolesFk());
                    selectedTeamMembers.get(indexRepeatedMember).getRoleEvaluationsMember().add(findMemberRoleEvaluation(items.get(i).getWorkersFk(), lastProjectCycle, items.get(i).getRolesFk()));
                }
                else{
                    // MemberToEvaluate member = new MemberToEvaluate(items.get(i).getWorkersFk());
                MemberToEvaluate member = new MemberToEvaluate(workerController.getWorker(items.get(i).getWorkersFk().getId()));                           
                
                member.getRoleEvaluationsMember().add(findMemberRoleEvaluation(member.getMember(), lastProjectCycle, items.get(i).getRolesFk()));
                                
                selectedTeamMembers.add(member); //agrego a la lista todos los miembros menos el jefe xq el no se puede evaluar a si mismo

                }                
          
            }
           */
            
              if (items.get(i).getWorkersFk().getId().compareTo(userlogged.getId()) == 0) {
                
                repeat = false;
            
                
                for(int j = 0; j < selectedTeamMembers.size() && !repeat; j++){
                    if(selectedTeamMembers.get(j).getMember().getId().compareTo(items.get(i).getWorkersFk().getId()) == 0){
                        repeat = true;
                        indexRepeatedMember = j;
                    }
                    
                }
                
                if(repeat){
                    //selectedTeamMembers.get(indexRepeatedMember).getRoleMemberList().add(items.get(i).getRolesFk());
                    selectedTeamMembers.get(indexRepeatedMember).getRoleEvaluationsMember().add(findMemberRoleEvaluation(items.get(i).getWorkersFk(), lastProjectCycle, items.get(i).getRolesFk()));
                }
                else{
                    // MemberToEvaluate member = new MemberToEvaluate(items.get(i).getWorkersFk());
                MemberToEvaluate member = new MemberToEvaluate(workerController.getWorker(items.get(i).getWorkersFk().getId()));                           
                
                member.getRoleEvaluationsMember().add(findMemberRoleEvaluation(member.getMember(), lastProjectCycle, items.get(i).getRolesFk()));
                                
                selectedTeamMembers.add(member); //agrego a la lista todos los miembros menos el jefe xq el no se puede evaluar a si mismo

                }                
          
            }
        }
        
        findEvaluation(selectedTeamMembers);
        
        return selectedTeamMembers;

    }

    //------------------Posibles niveles para editar en las competencias del miembro seleccionado
    public List<Levels> getPossibleLevels(Competence comp) {
        List<Levels> possibleLevels = new ArrayList<>();
        boolean found = false;
        Levels currentLevel = null;
        List<CompetenceValue> items = compValController.getItems();
        List<Levels> allLevels = levelscontroller.getItems();

        // recorrer tabla de competence value con el id del worker seleccionado y de la competencia para encontrar el nivel acutal de la competencia en esa persona
        for (int i = 0; i < items.size() && !found; i++) {
            if (items.get(i).getWorkersFk().getId().compareTo(workerSelected.getId()) == 0) {
                if (items.get(i).getCompetenceFk().getId().compareTo(comp.getId()) == 0) {
                    found = true;
                    currentLevel = items.get(i).getLevelFk();
                }
            }
        }

        //si en la tabla aparece un nivel con valor mayor o igual al del actual se agrega a la lista de posibles niveles
        for (Levels level : allLevels) {
            if (level.getLevels() >= currentLevel.getLevels()) {
                possibleLevels.add(level);
            }
        }

        return possibleLevels;
    }

    public Worker getUserById() {
        workers = workerController.getItems();
        Worker w = null;
        boolean found = false;
        for (Worker worker : workers) {
            if (worker.getId() == 2388) {
                w = worker;
                found = true;
            }
        }
        if (found) {
            System.out.println("I found user");
        } else {
            System.out.println("I didnt find user");
        }
        return w;
    }

    /**
     *
     * @param project
     * @return
     */
    public static Cycle lastProjectCycle(Project project) {
        Cycle cycle = null;
        List<Cycle> cycleList = project.getCycleList();

        int i = 0;
        boolean found = false;
        while (i < cycleList.size() && !found) {
            if (cycleList.get(i).getEndDate() == null) {
                cycle = cycleList.get(i);
                found = true;
            }
            i++;
        }
        return cycle;

    }

    /**
     * Habilita/deshabilita el boton adicionar competencia generica
     */
    public void hadleGenCompAddButton_Listener() {
        if (selectedGenLvl != null && selectedGenComp != null) {
            disableGenCompAddButton = false;
        } else {
            if (!disableGenCompAddButton) {
                disableGenCompAddButton = true;
            }
        }
    }

    /**
     * Para poder mostrar las dimensiones del nivel asociado a una competencia
     * generica
     *
     * @return list
     */
    public List<CompetenceDimension> getGenCompetenceDimensionList() {
        List<CompetenceDimension> list = new ArrayList<>();

        if (selectedGenComp != null && selectedGenLvl != null) {
            if (selectedGenComp.getCompetenceDimensionList() != null) {  //deberia preguntar esto o si esta vacia?
                for (CompetenceDimension competenceDimension : selectedGenComp.getCompetenceDimensionList()) {
                    if (competenceDimension.getLevelFk().equals(selectedGenLvl)) {
                        list.add(competenceDimension);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Se encarga de ir almacenando la seleccion del usuario via ajax en la
     * lista de competencias genericas ( genCmpItems)
     */
    public void addGenCompLvl_Listener() {
        CompetenceValue cv = competenceValueFacade.findCompetenceValue(memberSelected.getMember().getId(), selectedGenComp.getId());
        
         if(cv != null){
              compValController.setSelected(cv);
              cv.setLevelFk(selectedGenLvl);
          }
          
          else{
              cv = new CompetenceValue();
              cv.setWorkersFk(memberSelected.getMember());
              cv.setCompetenceFk(selectedGenComp);
              cv.setLevelFk(selectedGenLvl);
          }                        

        genCmpLvlItems.add(cv);//se añade el Competence value
        genCmpItems.remove(selectedGenComp); //elimino de la lista la competencia agregada 
//se añade el Competence value
        selectedGenComp = null;// inavilido la competencia seleccionada para actualizar componentes del formulario
        selectedGenLvl = null; //inavilido la seleccion del level para actualizar componentes del formulario
    }

    /**
     * Para manejar la eliminacion del las competencias genericas seleccionadas
     * en la tabla de competencias tecnicas
     */
    public void removeGenCompFromTable_Listener() {

        if (!genCmpLvlTableSelection.isEmpty()) {
            for (CompetenceValue competence : genCmpLvlTableSelection) {
                for (int i = 0; i < genCmpLvlItems.size(); i++) {
                    if (competence.getCompetenceFk().getId().equals(genCmpLvlItems.get(i).getCompetenceFk().getId())) {
                        genCmpLvlItems.remove(i); // elimino el objeto de la fuente de datos de la tabla
                        genCmpItems.add(competence.getCompetenceFk()); //devuelvo el elemento a la lista de competencias disponibles
                    }
                }
            }
            genCmpLvlTableSelection.clear();// elimino el elemento de la lista que almacena los elementos seleccionados
        }
    }

    /**
     * Habilita el boton adicionar competencia tecnica
     */
    public void hadleTechCompAddButton_Listener() {
        if (selectedTechLvl != null && selectedTechComp != null) {
            disableTechCompAddButton = false;
        } else {
            if (!disableTechCompAddButton) {
                disableTechCompAddButton = true;
            }
        }
    }

    /**
     * Para poder mostrar las dimensiones del nivel asociado a una competencia
     * tecnica
     *
     * @return list
     */
    public List<CompetenceDimension> getTechCompetenceDimensionList() {
        List<CompetenceDimension> list = new ArrayList<>();

        if (selectedTechComp != null && selectedTechLvl != null) {
            if (selectedTechComp.getCompetenceDimensionList() != null) {
                for (CompetenceDimension competenceDimension : selectedTechComp.getCompetenceDimensionList()) {
                    if (competenceDimension.getLevelFk().equals(selectedTechLvl)) {
                        list.add(competenceDimension);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Se encarga de ir almacenando la seleccion del usuario via ajax en la
     * lista de competencias tecnicas ( techCmpItems)
     */
    public void addTechCompLvl_Listener() {
        CompetenceValue cv = competenceValueFacade.findCompetenceValue(memberSelected.getMember().getId(), selectedTechComp.getId());
        
        if(cv != null){
              compValController.setSelected(cv);
              cv.setLevelFk(selectedTechLvl);
          }
          
          else{
              cv = new CompetenceValue();
              cv.setWorkersFk(memberSelected.getMember());
              cv.setCompetenceFk(selectedTechComp);
              cv.setLevelFk(selectedTechLvl);
          } 
        
       
        techCmpLvlItems.add(cv);//se añade el competence_level
        techCmpItems.remove(selectedTechComp); //elimino de la lista la competencia agregada 

        selectedTechComp = null;// inavilito la competencia seleccionada para actualizar componentes del formulario
        selectedTechLvl = null; //inavilito la seleccion del level para actualizar componentes del formulario
    }

    /**
     * Para manejar la eliminacion del las competencias tecnicas seleccionadas
     * en la tabla de competencias tecnicas
     */
    public void removeTechCompFromTable_Listener() {

        if (!techCmpLvlTableSelection.isEmpty()) {
            for (CompetenceValue competence : techCmpLvlTableSelection) {
                for (int i = 0; i < techCmpLvlItems.size(); i++) {
                    if (competence.getCompetenceFk().getId().equals(techCmpLvlItems.get(i).getCompetenceFk().getId())) {
                        techCmpLvlItems.remove(i); // elimino el objeto de la fuente de datos de la tabla
                        techCmpItems.add(competence.getCompetenceFk()); //devuelvo el elemento a la lista de competencias disponibles
                    }
                }
            }
            techCmpLvlTableSelection.clear();// elimino el elemento de la lista que almacena los elementos seleccionados
        }
    }

//-------------------------------INICIALIZAR DATOS DE LOS TABS THECH Y GEN
    /**
     * Inicializar parametros necesarios para la edicion de los datos de una
     * persona
     *
     * @return
     */
    public String prepareEdit() {

        String toReturn = "";
        //Worker sel = workerSelected;
        Worker sel = memberSelected.getMember();
        membersConflict = loadConflictTable();

        if (sel != null) {
            if (sel.getId() != null) {

                
                
                List<Competence> cmp = competenceFacade.findAll(); // todas las competencias en BD
                List<CompetenceValue> cmpValue = memberSelected.getMember().getCompetenceValueList(); //competenceValue que posee la persona

                cmp = unassignedCompetences(cmp, cmpValue); //obtener solo competencias disponibles para asignar

                genCmpItems = new ArrayList<>(classifyCompetences_Generic(cmp));//clasificar competencias (obtener genericas)disponibles
                techCmpItems = new ArrayList<>(classifyCompetences_Technical(cmp));//clasificar competencias (obtener tecnicas)disponibles

                genCmpLvlItems = new ArrayList<>(classifyCmpValue_Generic(cmpValue));//clasificar competencias (obtener genericas) asignadas a la persona
                techCmpLvlItems = new ArrayList<>(classifyCmpValue_Technical(cmpValue));//clasificar competencias (obtener tecnicas) asignadas a la persona

                compValueList = new ArrayList<>();

                /*
                personalInterests = workerController.getSelected().getPersonalInterestsList();
                roles = roleFacade.findAll();

                for (PersonalInterests pi : personalInterests) { //quitar de la lista de roles los que la  persona ya tiene asignados
                    int i = 0;
                    boolean found = false;
                    while (i < roles.size() && !found) {
                        if (pi.getRolesFk().getId().equals(roles.get(i).getId())) {
                            roles.remove(i);//eliminar de la lista
                            found = true;
                        }
                        i++;
                    }
                }
                 */
                // toReturn = "/closeProject/memberEvaluation";
                toReturn = "member-evaluation";

            } else {
                MyJSF_Util.addWarningMessage("Seleccione un registro de la tabla.");
            }
        } else {
            MyJSF_Util.addWarningMessage("Seleccione un registro de la tabla.");
        }

        return toReturn;
    }
/*  ---------------------------------------------el create del boton
    public void create() {

        /*
        int i = 0;
        boolean a = false;
        for(; i < membersSelectedProject.size() && !a ;i++){
            if(membersSelectedProject.get(i).getMember().getId().compareTo(memberSelected.getMember().getId()) == 0){
                memberSelected = membersSelectedProject.get(i);
                System.out.println("entro a comparar los members");
                a = true;
            }
            
        }
        membersSelectedProject.get(i).setEvaluate(true);
        //memberSelected.setEvaluate(true);
        System.out.println("prepare create");
        
       memberSelected.getMember().setStatus(JsfUtil.workerStatus.Active.toString());
       memberSelected.getMember().setWorkload(0f);
       for(int j =0; j< memberSelected.getMember().getCompetenceValueList().size(); j++){
            compValController.setSelected(memberSelected.getMember().getCompetenceValueList().get(j));
            compValController.destroy();
       } 
         
        
        
        if (memberSelected.getMember().getCompetenceValueList().size() < compValueList.size()) {
            MyJSF_Util.addWarningMessage("La persona debe tener al menos la misma cantidad de competencias que tenía inicialmente");

        } else {
            if (buildCompValueList()) {
                memberSelected.getMember().setCompetenceValueList(compValueList); //agregar competencias  
            }

            memberSelected.setEvaluate(true);
            workerController.setSelected(memberSelected.getMember());
            workerController.create(); //persistir worker
           
            roleEvaluationController.setSelected(loadRoleEvaluation());
            roleEvaluationController.create();
            
            List<WorkerConflict> wcList = loadConflitstoDB();
            for(int i=0; i <  wcList.size();i++) {
            
               workerConflictController.setSelected(wcList.get(i));
               workerConflictController.create();
              }
            
            if (FacesContext.getCurrentInstance().getMaximumSeverity() != null) {
                if (FacesContext.getCurrentInstance().getMaximumSeverity().equals(FacesMessage.SEVERITY_INFO)) {
                    prepareCreate();
                    

                }
            }
        }

    }
   */
    
    public void create() {
        if (buildCompValueList()) {
            //agregar competencias  
            memberSelected.getMember().setCompetenceValueList(compValueList);
            
           /* List<CompetenceValue> cvList = loadCompetenceValueDB();
            for (int j = 0; j < cvList.size(); j++) {
               compValController.setSelected(cvList.get(j));
               compValController.create();
            }
            
             memberSelected.getMember().setCompetenceValueList(cvList); */
           
        }     
                   
            
            
            workerController.setSelected(memberSelected.getMember());
            
            for(int i = 0; i < memberSelected.getRoleEvaluationsMember().size(); i++){
                 if(!notEvaluated.equals(memberSelected.getRoleEvaluationsMember().get(i).getRolEvalFk().getSignificance()) ){
                // loadRoleEvaluation(memberSelected.getRoleEvaluationsMember().get(i));
                 roleEvaluationController.setSelected(loadRoleEvaluation(memberSelected.getRoleEvaluationsMember().get(i)));  //editar/crear nota en roles evaluados
                 //roleEvaluationController.setSelected(memberSelected.getRoleEvaluationsMember().get(i));
                 roleEvaluationController.create();
                 }
            } 
            
          /*  List<WorkerConflict> wcList = loadConflitstoDB(); 
            for(int i=0; i <  wcList.size();i++) {    //editar/crear los niveles de conflicto
                if(!.equals(wcList.get(i).getIndexFk().getDescription())){
                    workerConflictController.setSelected(wcList.get(i));
                    workerConflictController.create();
                 }
               
              }*/
            
            for(int i=0; i <  membersConflict.size();i++) {    //editar/crear los niveles de conflicto
             
                 WorkerConflict wc = saveConflictDB(membersConflict.get(i));
                 if(wc.getIndexFk().getDescription().equals(notConflict)){
                     if(wc.getWorkerConflictFk() != null ){
                         workerConflictController.setSelected(wc);
                         workerConflictController.destroy();
                     }  
                 }
                 else{
                     workerConflictController.setSelected(wc);
                     workerConflictController.create();
                 }
              
              }
            

       // workerController.getSelected().setStatus(JsfUtil.workerStatus.Active.toString());
       // workerController.getSelected().setWorkload(0f);

        workerController.create(); //persistir worker
        if (FacesContext.getCurrentInstance().getMaximumSeverity() != null) {
            if (FacesContext.getCurrentInstance().getMaximumSeverity().equals(FacesMessage.SEVERITY_INFO)) {
                prepareCreate();
            }
        }
        
    }
    
     public List<MemberToEvaluate> loadConflictTable(){  // para mostrar tabla q asigna las compatibilidades a los demas miembros del equipo
        List<MemberToEvaluate> selectedTeamMembers = new ArrayList<>();
       boolean repeat;
        List<AssignedRole> items = lastProjectCycle(projectSelected).getAssignedRoleList(); // de nuevo los miembros del ultimo ciclo

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getWorkersFk().getId().compareTo(memberSelected.getMember().getId()) != 0) {
                 repeat = false;
                for(int j = 0; j < selectedTeamMembers.size() && !repeat;j++){
                    if(selectedTeamMembers.get(j).getMember().getId().compareTo(items.get(i).getWorkersFk().getId()) == 0 ){
                        repeat = true;
                    }
                   
                }
                if(!repeat){
                  MemberToEvaluate member = new MemberToEvaluate(workerController.getWorker(items.get(i).getWorkersFk().getId()));
                  //member.setRole(items.get(i).getRolesFk().getRoleName());
                  member.setCompatibilityLevel(findConflicts(memberSelected.getMember().getId(),member.getMember()));
                  selectedTeamMembers.add(member); //agrego a la lista todos los miembros menos el jefe xq el no se puede evaluar a si mismo
                }
                

            }
        }
        return selectedTeamMembers;
    }
    
    
    public List<WorkerConflict> loadConflitstoDB(){  //guardar en la bd los conflictos, essto ya no lo estoy usando
        List<WorkerConflict> currentConflicts = new ArrayList<>();
        List<MemberToEvaluate> workersc = membersConflict; 
                
        for(int i = 0; i < workersc.size() ;i++){
            
            WorkerConflict wc = workerConflictFacade.findworkerConflict(memberSelected.getMember().getId(), workersc.get(i).getMember().getId());
            
            if(wc != null){
                workerConflictController.setSelected(wc);             
                                              
            }            
            else {/*
                wc = new WorkerConflict();
                wc.setWorkerFk(memberSelected.getMember());
                wc.setWorkerConflictFk(workersc.get(i).getMember());*/
                wc = workersc.get(i).getCompatibilityLevel();
                
            }             
            
            /* for(int j =0; j < conflictIndexController.getItems().size(); j++){
                    if(workersc.get(i).isCompatible()){
                        if(Math.toIntExact(conflictIndexController.getItems().get(j).getWeight()) == 0){
                            //wc.setIndexFk(conflictIndexController.getItems().get(j));
                            //if(BigInteger.valueOf(conflictIndexController.getItems().get(j).getWeight()).compareTo(BigInteger.ZERO) == 0){
                            wc.setIndexFk(conflictIndexController.getItems().get(j));
                        }
                    }
                     else{
                       if(Math.toIntExact(conflictIndexController.getItems().get(j).getWeight()) ==1 ){
                           //if(BigInteger.valueOf(conflictIndexController.getItems().get(j).getWeight()).compareTo(BigInteger.ONE) == 0){
                            wc.setIndexFk(conflictIndexController.getItems().get(j));
                        }
                    }
                } 
              */
            currentConflicts.add(wc);
        }    
        return currentConflicts;
    }
    
    public WorkerConflict saveConflictDB(MemberToEvaluate member2){
        WorkerConflict wc = workerConflictFacade.findworkerConflict(memberSelected.getMember().getId(), member2.getMember().getId());
        if(wc != null){
            wc.setIndexFk(member2.getCompatibilityLevel().getIndexFk());
            workerConflictController.setSelected(wc);            
        }
        else{
            wc = new WorkerConflict();
            wc.setWorkerFk(memberSelected.getMember());
            wc.setWorkerConflictFk(member2.getMember());
            wc.setIndexFk(member2.getCompatibilityLevel().getIndexFk());
        }
        return wc;
    }
    
    
    public RoleEvaluation loadRoleEvaluation( RoleEvaluation roleTempMember){   // datos del role evaluation para ir a la BD, no lo estoy usando
        
            RoleEvaluation re = roleEvaluationFacade.findRolEvaluation(memberSelected.getMember().getId(), lastProjectCycle(projectSelected).getId(), roleTempMember.getRoleFk().getId());
            if(re != null){
                roleEvaluationController.setSelected(re);
                re.setRolEvalFk(roleTempMember.getRolEvalFk());
            }
            else{
                
            re = new RoleEvaluation();
            re.setCycleFk(lastProjectCycle(projectSelected));
            re.setWorkerFk(memberSelected.getMember());
            re.setRolEvalFk(roleTempMember.getRolEvalFk());
            re.setRoleFk(roleTempMember.getRoleFk()); //TODO           
            }                                       
            return re;
    }
    
    public List<CompetenceValue> loadCompetenceValueDB(){ // esto no hizo falta es para guardar las competencias en la bd pero se guardaron por el worker
        
        List<CompetenceValue> currentCompetences = new ArrayList<>();
        
        for(int i = 0; i < compValueList.size(); i++ ){
            
          CompetenceValue cv = competenceValueFacade.findCompetenceValue(memberSelected.getMember().getId(), compValueList.get(i).getCompetenceFk().getId());
          
          if(cv != null){
              compValController.setSelected(cv);
              cv.setLevelFk(compValueList.get(i).getLevelFk());
          }
          
          else{
              cv = new CompetenceValue();
              cv.setWorkersFk(memberSelected.getMember());
              cv.setCompetenceFk(compValueList.get(i).getCompetenceFk());
              cv.setLevelFk(compValueList.get(i).getLevelFk());
          }
        }
        
        return currentCompetences;
    }
    
     public void findEvaluation(List<MemberToEvaluate> selectedmembers){ //para verificar los miembros q se hayan evaluado en varias sesiones
        boolean evalExists;
        
         /*RoleEvaluation re = roleEvaluationFacade.findRolEvaluation(member.getMember().getId(), cycleid, member.getRoleId());
            if(re != null){
                evalExists = true;
            }
        */
         for(int i = 0; i < selectedmembers.size(); i++){
             evalExists = true;
             for(int j = 0; j < selectedmembers.get(i).getRoleEvaluationsMember().size() && evalExists; j++){
                 if(notEvaluated.equals(selectedmembers.get(i).getRoleEvaluationsMember().get(j).getRolEvalFk().getSignificance())){                 
                     evalExists = false;
                 }
             }
             selectedmembers.get(i).setEvaluate(evalExists);
             
         }        
      
    }
     
     public WorkerConflict findConflicts(long idSelected, Worker worker2){  // lo mismo para las incompatibilidades
                  
         WorkerConflict wc = workerConflictFacade.findworkerConflict(idSelected, worker2.getId());
         if(wc == null){            
                wc = new WorkerConflict();
                wc.setWorkerFk(memberSelected.getMember());                
                wc.setWorkerConflictFk(worker2);
                
                ConflictIndex temp = new ConflictIndex();
                temp.setDescription(notConflict);
                wc.setIndexFk(temp);
         }
                  
         return wc;
     }
     
     public RoleEvaluation findMemberRoleEvaluation(Worker member, Cycle cycle, Role role){//cargar en la tabla la nota de la bd
         
         RoleEvaluation re = roleEvaluationFacade.findRolEvaluation(member.getId(), cycle.getId(), role.getId());
         
         if(re == null){
              re = new RoleEvaluation();
              re.setWorkerFk(member);
              re.setCycleFk(cycle);
              re.setRoleFk(role);
              
              RoleEval temp = new RoleEval();
              temp.setSignificance(notEvaluated);
              re.setRolEvalFk(temp);             
         }
         
         return re;
     }
     
     // para cuando todos los miembros tengan evaluacion en el rol habilitar el boton q permite terminar el proceso finalizar proyecto
     public boolean verifyEvaluations(){ 
         boolean allmembers = true;
         
         for(int i =0; i < membersSelectedProject.size(); i++){
             if(!(membersSelectedProject.get(i)).isEvaluate()){
                 allmembers = false;
                 break;
             }
         }
         
         return allmembers;
     }
     
     public List<ConflictIndex> buildConflictIndexList(){
         
         conflictsIndexList = conflictIndexController.getItems();
         ConflictIndex ci = new ConflictIndex();
         ci.setDescription(notConflict);
         conflictsIndexList.add(ci);
         return conflictsIndexList;
     }
     
     public void deleteExtraConflictIndexes(){
         List<ConflictIndex> indexToDelete = conflictIndexController.getItemsAvailableSelectOne();
         for(int i =0; i < indexToDelete.size(); i++){
             if(indexToDelete.get(i).getDescription().equals(notConflict)){
                 conflictIndexController.setSelected(indexToDelete.get(i));
                 conflictIndexController.destroy();
             }
         }
     }
    
    /**
     * Para construir la lista de competenceValue de la entidad Worker que será
     * persistida en "CASCADE"
     *
     * @return false si no tiene competencias seleccionadas
     */
    public boolean buildCompValueList() {
        boolean haveCompetences = false;

        //agregar referencia del trabajador a cada competencias tecnica seleccionada
        if (!techCmpLvlItems.isEmpty()) {
            for (CompetenceValue Item : techCmpLvlItems) {
                Item.setWorkersFk(memberSelected.getMember());
                compValueList.add(Item);
            }
            haveCompetences = true;
        }
        //agregar referencia del trabajador a cada competencia generica seleccionada
        if (!genCmpLvlItems.isEmpty()) {
            for (CompetenceValue Item : genCmpLvlItems) {
                Item.setWorkersFk(memberSelected.getMember());
                compValueList.add(Item);
            }
            if (!haveCompetences) {
                haveCompetences = true;
            }
        }

        return haveCompetences;
    }

    /**
     * Inicializar parametros necesarios para la insercion de los datos de una
     * persona
     *
     * @return
     */
    public String prepareCreate() {
        workerController.prepareCreate();
        genCmpItems = new ArrayList();
        techCmpItems = new ArrayList<>();

        genCmpLvlItems = new ArrayList<>(); //Estos 2 almacenaran las listas 
        techCmpLvlItems = new ArrayList<>(); // de cada tipo de competencia disponible

        compValueList = new ArrayList<>();

        List<Competence> cmp = competenceFacade.findAll();

        genCmpItems = new ArrayList<>(classifyCompetences_Generic(cmp));
        techCmpItems = new ArrayList<>(classifyCompetences_Technical(cmp));
        
        deleteExtraConflictIndexes();

        return "save-member-evaluation";
    }

    /**
     * Dada la lista de todas las competencias, y la lista de los
     * competencesValue de la persona, devolver solo las competencias
     * disponibles para su asignacion. (aquellas que la persona no posee)
     *
     * @param allCmp
     * @param personCmpValue
     * @return
     */
    public List<Competence> unassignedCompetences(List<Competence> allCmp, List<CompetenceValue> personCmpValue) {

        List<Competence> unassigned = new ArrayList<>(allCmp);

        if (allCmp != null && personCmpValue != null) {

            Competence toDelete;
            for (CompetenceValue item : personCmpValue) { //para todos los compValue de la persona
                toDelete = item.getCompetenceFk();

                int j = 0;
                boolean found = false;
                while (j < unassigned.size() && !found) { //para cada competencia
                    if (toDelete.getId().equals(unassigned.get(j).getId())) { //buscar las que posee la persona
                        unassigned.remove(j);//eliminar de la lista
                        found = true;
                    }
                    j++;
                }
            }
        }
        return unassigned;
    }

    /**
     * retornar lista con CompetenceValue marcadas como genericas
     *
     * @param compList
     * @return
     */
    public List<CompetenceValue> classifyCmpValue_Generic(List<CompetenceValue> compList) {
        List<CompetenceValue> genCmp = new ArrayList<>();
        if (compList != null) {
            for (CompetenceValue item : compList) {
                if (!item.getCompetenceFk().getTechnical()) {
                    genCmp.add(item);
                }
            }
        }
        return genCmp;
    }

    /**
     * retornar lista con CompetenceValue marcadas como tecnicas
     *
     * @param compList
     * @return
     */
    public List<CompetenceValue> classifyCmpValue_Technical(List<CompetenceValue> compList) {
        List<CompetenceValue> genCmp = new ArrayList<>();

        if (compList != null) {
            for (CompetenceValue item : compList) {
                if (item.getCompetenceFk().getTechnical()) {
                    genCmp.add(item);
                }
            }
        }
        return genCmp;
    }

    /**
     * Dada una lista de competencias devolver las que sean genéricas
     *
     * @param compList
     * @return
     */
    public List<Competence> classifyCompetences_Generic(List<Competence> compList) {
        List<Competence> genCmp = new ArrayList<>();

        if (compList != null) {
            for (Competence item : compList) {
                if (!item.getTechnical()) {
                    genCmp.add(item);
                }
            }
        }
        return genCmp;
    }

    /**
     * Dada una lista de competencias devolver las que sean tecnicas
     *
     * @param compList
     * @return
     */
    public List<Competence> classifyCompetences_Technical(List<Competence> compList) {
        List<Competence> genCmp = new ArrayList<>();

        if (compList != null) {
            for (Competence item : compList) {
                if (item.getTechnical()) {
                    genCmp.add(item);
                }
            }
        }
        return genCmp;
    }
    
   
    
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Product Edited", event.getObject().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", event.getObject().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        /*
        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }*/
    }
    
    
    //-------------------------------------------------------------PARA CERRAR PROYECTOS--------------------------------------------------------
    
    public List<Project> getfinalizedProjects(){
        List<Project> projectlist = new ArrayList<>();
        
        List<Project> allprojects = projectController.getItems();
        
        for(int i = 0; i < allprojects.size(); i++){
            if(allprojects.get(i).getFinalize() == true){
                projectlist.add(allprojects.get(i));
            }
        }
        
        return projectlist;
    }
    
    public List<CompetenceValue> getGenCmpLvlTableSelection() {
        return genCmpLvlTableSelection;
    }

    public void setGenCmpLvlTableSelection(List<CompetenceValue> genCmpLvlTableSelection) {
        this.genCmpLvlTableSelection = genCmpLvlTableSelection;
    }

    public List<CompetenceValue> getTechCmpLvlTableSelection() {
        return techCmpLvlTableSelection;
    }

    public void setTechCmpLvlTableSelection(List<CompetenceValue> techCmpLvlTableSelection) {
        this.techCmpLvlTableSelection = techCmpLvlTableSelection;
    }

    public List<Competence> getGenCmpItems() {
        return genCmpItems;
    }

    public void setGenCmpItems(List<Competence> genCmpItems) {
        this.genCmpItems = genCmpItems;
    }

    public List<Competence> getTechCmpItems() {
        return techCmpItems;
    }

    public void setTechCmpItems(List<Competence> techCmpItems) {
        this.techCmpItems = techCmpItems;
    }

    public List<CompetenceDimension> getCompetenceDimensionList() {
        return competenceDimensionList;
    }

    public void setCompetenceDimensionList(List<CompetenceDimension> competenceDimensionList) {
        this.competenceDimensionList = competenceDimensionList;
    }

    public Competence getSelectedGenComp() {
        return selectedGenComp;
    }

    public void setSelectedGenComp(Competence selectedGenComp) {
        this.selectedGenComp = selectedGenComp;
    }

    public Competence getSelectedTechComp() {
        return selectedTechComp;
    }

    public void setSelectedTechComp(Competence selectedTechComp) {
        this.selectedTechComp = selectedTechComp;
    }

    public Levels getSelectedGenLvl() {
        return selectedGenLvl;
    }

    public void setSelectedGenLvl(Levels selectedGenLvl) {
        this.selectedGenLvl = selectedGenLvl;
    }

    public Levels getSelectedTechLvl() {
        return selectedTechLvl;
    }

    public void setSelectedTechLvl(Levels selectedTechLvl) {
        this.selectedTechLvl = selectedTechLvl;
    }

    public boolean isDisableGenCompAddButton() {
        return disableGenCompAddButton;
    }

    public void setDisableGenCompAddButton(boolean disableGenCompAddButton) {
        this.disableGenCompAddButton = disableGenCompAddButton;
    }

    public boolean isDisableTechCompAddButton() {
        return disableTechCompAddButton;
    }

    public void setDisableTechCompAddButton(boolean disableTechCompAddButton) {
        this.disableTechCompAddButton = disableTechCompAddButton;
    }

    public List<CompetenceValue> getGenCmpLvlItems() {
        return genCmpLvlItems;
    }

    public void setGenCmpLvlItems(List<CompetenceValue> genCmpLvlItems) {
        this.genCmpLvlItems = genCmpLvlItems;
    }

    public List<CompetenceValue> getTechCmpLvlItems() {
        return techCmpLvlItems;
    }

    public void setTechCmpLvlItems(List<CompetenceValue> techCmpLvlItems) {
        this.techCmpLvlItems = techCmpLvlItems;
    }

    public LevelsController getLevelscontroller() {
        return levelscontroller;
    }

    public void setLevelscontroller(LevelsController levelscontroller) {
        this.levelscontroller = levelscontroller;
    }

    public CompetenceValueController getCompValController() {
        return compValController;
    }

    public void setCompValController(CompetenceValueController compValController) {
        this.compValController = compValController;
    }

    public MemberToEvaluate getMemberSelected() {
        return memberSelected;
    }

    public void setMemberSelected(MemberToEvaluate memberSelected) {
        this.memberSelected = memberSelected;
    }

    public Project getProjectSelected() {
        return projectSelected;
    }

    public void setProjectSelected(Project projectSelected) {
        this.projectSelected = projectSelected;
    }

    public Worker getWorkerSelected() {
        return workerSelected;
    }

    public void setWorkerSelected(Worker workerSelected) {
        this.workerSelected = workerSelected;
    }

    public List<MemberToEvaluate> getMembersSelectedProject() {
        return membersSelectedProject;
    }

    public void setMembersSelectedProject(List<MemberToEvaluate> membersSelectedProject) {
        this.membersSelectedProject = membersSelectedProject;
    }

    public WorkerController getWorkerController() {
        return workerController;
    }

    public void setWorkerController(WorkerController workerController) {
        this.workerController = workerController;
    }

    public ProjectController getProjectController() {
        return projectController;
    }

    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }

    public List<Project> getProjectsByLoggedBoss() {
        return projectsByLoggedBoss;
    }

    public void setProjectsByLoggedBoss(List<Project> projectsByLoggedBoss) {
        this.projectsByLoggedBoss = projectsByLoggedBoss;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public Worker getUserlogged() {
        return userlogged;
    }

    public void setUserlogged(Worker userlogged) {
        this.userlogged = userlogged;
    }

    
    
    public RoleController getRoleController() {
        return roleController;
    }

    public void setRoleController(RoleController roleController) {
        this.roleController = roleController;
    }

    public MemberToEvaluate getMemberConflictSelected() {
        return memberConflictSelected;
    }

    public void setMemberConflictSelected(MemberToEvaluate memberConflictSelected) {
        this.memberConflictSelected = memberConflictSelected;
    }
        
    public CompetenceFacade getCompetenceFacade() {
        return competenceFacade;
    }

    public void setCompetenceFacade(CompetenceFacade competenceFacade) {
        this.competenceFacade = competenceFacade;
    }

    public RoleFacade getRoleFacade() {
        return roleFacade;
    }

    public void setRoleFacade(RoleFacade roleFacade) {
        this.roleFacade = roleFacade;
    }

    public RoleEvalController getRoleEvalController() {
        return roleEvalController;
    }

    public void setRoleEvalController(RoleEvalController roleEvalController) {
        this.roleEvalController = roleEvalController;
    }

    public RoleEvaluationController getRoleEvaluationController() {
        return roleEvaluationController;
    }

    public void setRoleEvaluationController(RoleEvaluationController roleEvaluationController) {
        this.roleEvaluationController = roleEvaluationController;
    }

    public List<RoleEval> getRoleEvalList() {
        return roleEvalList;
    }

    public void setRoleEvalList(List<RoleEval> roleEvalList) {
        this.roleEvalList = roleEvalList;
    }

    public RoleEval getNoteRol() {
        return noteRol;
    }

    public void setNoteRol(RoleEval noteRol) {
        this.noteRol = noteRol;
    }

    public List<CompetenceValue> getCompValueList() {
        return compValueList;
    }

    public void setCompValueList(List<CompetenceValue> compValueList) {
        this.compValueList = compValueList;
    }

    public List<MemberToEvaluate> getMembersConflict() {
        return membersConflict;
    }

    public void setMembersConflict(List<MemberToEvaluate> membersConflict) {
        this.membersConflict = membersConflict;
    }

    public RoleEvaluationFacade getRoleEvaluationFacade() {
        return roleEvaluationFacade;
    }

    public void setRoleEvaluationFacade(RoleEvaluationFacade roleEvaluationFacade) {
        this.roleEvaluationFacade = roleEvaluationFacade;
    }

    public ConflictIndexController getConflictIndexController() {
        return conflictIndexController;
    }

    public void setConflictIndexController(ConflictIndexController conflictIndexController) {
        this.conflictIndexController = conflictIndexController;
    }

    public WorkerConflictController getWorkerConflictController() {
        return workerConflictController;
    }

    public void setWorkerConflictController(WorkerConflictController workerConflictController) {
        this.workerConflictController = workerConflictController;
    }

    public WorkerConflictFacade getWorkerConflictFacade() {
        return workerConflictFacade;
    }

    public void setWorkerConflictFacade(WorkerConflictFacade workerConflictFacade) {
        this.workerConflictFacade = workerConflictFacade;
    }

    public CompetenceValueFacade getCompetenceValueFacade() {
        return competenceValueFacade;
    }

    public void setCompetenceValueFacade(CompetenceValueFacade competenceValueFacade) {
        this.competenceValueFacade = competenceValueFacade;
    }

    public List<Project> getFinalizedProjects() {
        return finalizedProjects;
    }

    public void setFinalizedProjects(List<Project> finalizedProjects) {
        this.finalizedProjects = finalizedProjects;
    }

    public boolean isButtonFinalize() {
        return buttonFinalize;
    }

    public void setButtonFinalize(boolean buttonFinalize) {
        this.buttonFinalize = buttonFinalize;
    }

    public RoleEval getNoteProject() {
        return noteProject;
    }

    public void setNoteProject(RoleEval noteProject) {
        this.noteProject = noteProject;
    }

    public List<ConflictIndex> getConflictsIndexList() {
        return conflictsIndexList;
    }

    public void setConflictsIndexList(List<ConflictIndex> conflictsIndexList) {
        this.conflictsIndexList = conflictsIndexList;
    }

    public String getNotEvaluated() {
        return notEvaluated;
    }

    public void setNotEvaluated(String notEvaluated) {
        this.notEvaluated = notEvaluated;
    }

    public String getNotConflict() {
        return notConflict;
    }

    public void setNotConflict(String notConflict) {
        this.notConflict = notConflict;
    }
    
    

    
}
