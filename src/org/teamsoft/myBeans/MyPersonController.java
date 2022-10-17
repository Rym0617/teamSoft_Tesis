/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.controller.WorkerController;
import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.entity.*;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.CompetenceFacade;
import org.teamsoft.model.PersonalProjectInterestsFacade;
import org.teamsoft.model.ProjectFacade;
import org.teamsoft.model.RoleFacade;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt
 */
@Named("myPersonController")
@SessionScoped
public class MyPersonController implements Serializable {

    @Inject
    WorkerController workerController;

    //Para poder insertar las competencias e intereses
    @EJB
    CompetenceFacade competenceFacade;
    @EJB
    RoleFacade roleFacade;
    @EJB
    ProjectFacade projectFacade;
    @EJB
    PersonalProjectInterestsFacade personalProjectInterestsFacade;
    @Inject
    LocaleConfig localeConfig;

    // Para Mostrar las competencias de la persona
    private List<Competence> genCmpItems = null;
    private List<Competence> techCmpItems = null;

    //Para almacenar la competencia seleccionada
    private Competence selectedGenComp;
    private Competence selectedTechComp;
    //Para almacenar el nivel seleccionado
    private Levels selectedGenLvl;
    private Levels selectedTechLvl;

    //Para ir almacenando las competencias agregadas
    private List<CompetenceValue> genCmpLvlItems = null;
    private List<CompetenceValue> techCmpLvlItems = null;
    //Para almacenar los elementos seleccionados de la tabla
    private List<CompetenceValue> genCmpLvlTableSelection = null;
    private List<CompetenceValue> techCmpLvlTableSelection = null;

    private List<CompetenceValue> compValueList;  //Para almacenar la lista de competenceValue de la entidad Worker que será persistida en "CASCADE"

    //INTERESES POR ROL
    private String preference = ""; //si prefiere o evita desempeñar un rol
    private List<Role> roles = null;  //lista de roles para elegir preferencia
    private List<Role> selectedRoles = null;  //lista de roles para elegir preferencia
    private List<PersonalInterests> personalInterests = null;  //Para almacenar la lista de Roles preferidos/evitados por la persona y persistirla en "CASCADE"
    private List<PersonalInterests> personalInterestsDelete = null;  //Para quitar de la tabla preferencias por rol

    private String projectPreference = "";
    private List<Project> projects = null;
    private List<Project> selectedProjects = null;
    private List<PersonalProjectInterests> personalProjectInterests = null;
    private List<PersonalProjectInterests> deletedPersonalProjectInterests = null;
    private List<PersonalProjectInterests> selectedPersonalProjectInterests = null;

    //Para manejar la habilitacion/deshabilitacion de algunos componentes de la GUI
    private boolean disableGenCompAddButton = true;
    private boolean disableTechCompAddButton = true;

    private String mbtiType = null;

    private char IF;
    private char IS;
    private char ID;

    private char CE;
    private char ES;
    private char ME;

    private char CO;
    private char CH;
    private char IR;

    private List<WorkerTest> workerTests = null;

    /**
     * Inicializar parametros necesarios para la insercion de los datos de una
     * persona
     */
    public String prepareCreate() {
        workerController.prepareCreate();
        genCmpItems = new ArrayList<>();
        techCmpItems = new ArrayList<>();

        genCmpLvlItems = new ArrayList<>(); //Estos 2 almacenaran las listas 
        techCmpLvlItems = new ArrayList<>(); // de cada tipo de competencia disponible

        compValueList = new ArrayList<>();
        personalInterests = new ArrayList<>();
        personalProjectInterests = new ArrayList<>(); //rafiki
        deletedPersonalProjectInterests = new ArrayList<>();

        List<Competence> cmp = competenceFacade.findAll();

        genCmpItems = new ArrayList<>(classifyCompetences_Generic(cmp));
        techCmpItems = new ArrayList<>(classifyCompetences_Technical(cmp));

        roles = roleFacade.findAll();
        projects = projectFacade.findAll();

        return "person-create";
    }

    /**
     * Inicializar parametros necesarios para la edicion de los datos de una
     * persona
     *
     */
    public String prepareEdit() {

        String toReturn = "";
        Worker sel = workerController.getSelected();

        if (sel != null) {
            if (sel.getId() != null) {

                List<Competence> cmp = competenceFacade.findAll(); // todas las competencias en BD
                List<CompetenceValue> cmpValue = workerController.getSelected().getCompetenceValueList(); //competenceValue que posee la persona

                cmp = unassignedCompetences(cmp, cmpValue); //obtener solo competencias disponibles para asignar

                genCmpItems = new ArrayList<>(classifyCompetences_Generic(cmp));//clasificar competencias (obtener genericas)disponibles
                techCmpItems = new ArrayList<>(classifyCompetences_Technical(cmp));//clasificar competencias (obtener tecnicas)disponibles

                genCmpLvlItems = new ArrayList<>(classifyCmpValue_Generic(cmpValue));//clasificar competencias (obtener genericas) asignadas a la persona
                techCmpLvlItems = new ArrayList<>(classifyCmpValue_Technical(cmpValue));//clasificar competencias (obtener tecnicas) asignadas a la persona

                compValueList = new ArrayList<>();
                deletedPersonalProjectInterests = new ArrayList<>();
                personalInterests = workerController.getSelected().getPersonalInterestsList();
                mbtiType = workerController.getSelected().getWorkerTest().getTipoMB();
                roles = roleFacade.findAll();
                projects = projectFacade.findAll();

                //agregando
                personalProjectInterests = workerController.getSelected().getPersonalProjectInterestsList();

                //terminando agregado
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
                for (PersonalProjectInterests ppi : personalProjectInterests) {
                    int i = 0;
                    boolean found = false;
                    while (i < projects.size() && !found) {
                        if (ppi.getProjectFk().getId().equals(projects.get(i).getId())) {
                            projects.remove(i);//eliminar de la lista
                            found = true;
                        }
                        i++;
                    }
                }

                toReturn = "/person/worker/Edit";

            } else {
                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("select_table_record"));
            }
        } else {
            MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("select_table_record"));
        }

        return toReturn;
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
     * Para definir la preferencia de una persona por un rol dado
     */
    public void addRolPreference_Listener() {
        PersonalInterests interest;

        for (Role sr : selectedRoles) { //para cada rol seleccionado quitar de la lista de roles disponibles
            int i = 0;
            boolean found = false;
            while (i < roles.size() && !found) {
                if (sr.getId().equals(roles.get(i).getId())) {
                    roles.remove(i);//eliminar de la lista
                    found = true;
                }
                i++;
            }

            if (found) {
                String pref = localeConfig.getBundleValue("rolInterestsPrefer");

                //agregar a la lista intereses definidos
                interest = new PersonalInterests();
                interest.setRolesFk(sr);
                interest.setPreference(preference.equalsIgnoreCase(pref));
                interest.setWorkersFk(workerController.getSelected());
                personalInterests.add(interest);
            }
        }
        preference = null;
        selectedRoles.clear();
    }

    /**
     * Para quitar la preferencia de una persona por un rol dado
     */
    public void removeRolPreference_Listener() {

        for (PersonalInterests pi : personalInterestsDelete) { //quitar los seleccionados de la lista de roles disponibles
            int i = 0;
            boolean found = false;
            while (i < personalInterests.size() && !found) {
                if (pi.getRolesFk().getId().equals(personalInterests.get(i).getRolesFk().getId())) {
                    roles.add(personalInterests.get(i).getRolesFk());//añadir rol a la lista de roles disponibles
                    personalInterests.remove(i);//quitar preferencia
                    found = true;
                }
                i++;
            }
        }
        personalInterestsDelete.clear();
    }

    public void create() {
        if (buildCompValueList()) {
            workerController.getSelected().setCompetenceValueList(compValueList); //agregar competencias
        }
        if (!personalInterests.isEmpty()) {
            workerController.getSelected().setPersonalInterestsList(personalInterests); // agregar roles preferidos
        }
        if (!personalProjectInterests.isEmpty()) {
            workerController.getSelected().setPersonalProjectInterestsList(personalProjectInterests); // agregar roles preferidos
        }
        for (PersonalProjectInterests deletedPersonalProjectInterest : deletedPersonalProjectInterests) {
            personalProjectInterestsFacade.remove(deletedPersonalProjectInterest);
        }
        workerController.getSelected().setStatus(JsfUtil.workerStatus.Active.toString());
        workerController.getSelected().setWorkload(0f);

        if (workerController.getSelected().getWorkerTest() == null) {
            WorkerTest workerTest = new WorkerTest();
            workerTest.setWorkerFk(workerController.getSelected());
            workerTest.setCE('I');
            workerTest.setCH('I');
            workerTest.setCO('I');
            workerTest.setES('I');
            workerTest.setID('I');
            workerTest.setIF('I');
            workerTest.setIR('I');
            workerTest.setIS('I');
            workerTest.setME('I');

            workerController.getSelected().setWorkerTest(workerTest);
        }
        workerController.getSelected().getWorkerTest().setTipoMB(mbtiType);
        workerController.create(); //persistir worker
        if (FacesContext.getCurrentInstance().getMaximumSeverity() != null) {
            if (FacesContext.getCurrentInstance().getMaximumSeverity().equals(FacesMessage.SEVERITY_INFO)) {
                prepareCreate();
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
                Item.setWorkersFk(workerController.getSelected());
                compValueList.add(Item);
            }
            haveCompetences = true;
        }
        //agregar referencia del trabajador a cada competencia generica seleccionada
        if (!genCmpLvlItems.isEmpty()) {
            for (CompetenceValue Item : genCmpLvlItems) {
                Item.setWorkersFk(workerController.getSelected());
                compValueList.add(Item);
            }
            if (!haveCompetences) {
                haveCompetences = true;
            }
        }

        return haveCompetences;
    }

    /**
     * Se encarga de ir almacenando la seleccion del usuario via ajax en la
     * lista de competencias tecnicas ( techCmpItems)
     */
    public void addTechCompLvl_Listener() {
        CompetenceValue cv = new CompetenceValue();
        cv.setLevelFk(selectedTechLvl);
        cv.setCompetenceFk(selectedTechComp);

        techCmpLvlItems.add(cv);//se añade el competence_level
        techCmpItems.remove(selectedTechComp); //elimino de la lista la competencia agregada 

        selectedTechComp = null;// inavilito la competencia seleccionada para actualizar componentes del formulario
        selectedTechLvl = null; //inavilito la seleccion del level para actualizar componentes del formulario
    }

    /**
     * Se encarga de ir almacenando la seleccion del usuario via ajax en la
     * lista de competencias genericas ( genCmpItems)
     */
    public void addGenCompLvl_Listener() {
        CompetenceValue cv = new CompetenceValue();
        cv.setLevelFk(selectedGenLvl);
        cv.setCompetenceFk(selectedGenComp);

        genCmpLvlItems.add(cv);//se añade el Competence value
        genCmpItems.remove(selectedGenComp); //elimino de la lista la competencia agregada 

        selectedGenComp = null;// inavilido la competencia seleccionada para actualizar componentes del formulario
        selectedGenLvl = null; //inavilido la seleccion del level para actualizar componentes del formulario
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
     * Para informarle al usuario cuando se muestra ninguna competencia tecnica
     * elegible
     */
    public void noTechCompItemsWng_Listener() {
        if (genCmpItems.isEmpty()) {
            JsfUtil.addSuccessMessage(localeConfig.getBundleValue("noTechnicalsItemsWarning"));
        }
    }

    /**
     * Para informarle al usuario cuando no se muestra ninguna competencia
     * generica elegible
     */
    public void noGenCompItemsWng_Listener() {
        if (genCmpItems.isEmpty()) {
            JsfUtil.addSuccessMessage(localeConfig.getBundleValue("noGenericItemsWarning"));
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

    public List<PersonalProjectInterests> getDeletedPersonalProjectInterests() {
        return deletedPersonalProjectInterests;
    }

    public void setDeletedPersonalProjectInterests(List<PersonalProjectInterests> deletedPersonalProjectInterests) {
        this.deletedPersonalProjectInterests = deletedPersonalProjectInterests;
    }

    public char getIF() {
        return IF;
    }

    public void setIF(char IF) {
        this.IF = IF;
    }

    public char getIS() {
        return IS;
    }

    public void setIS(char IS) {
        this.IS = IS;
    }

    public char getID() {
        return ID;
    }

    public void setID(char ID) {
        this.ID = ID;
    }

    public char getCE() {
        return CE;
    }

    public void setCE(char CE) {
        this.CE = CE;
    }

    public char getES() {
        return ES;
    }

    public void setES(char ES) {
        this.ES = ES;
    }

    public char getME() {
        return ME;
    }

    public void setME(char ME) {
        this.ME = ME;
    }

    public char getCO() {
        return CO;
    }

    public void setCO(char CO) {
        this.CO = CO;
    }

    public char getCH() {
        return CH;
    }

    public void setCH(char CH) {
        this.CH = CH;
    }

    public char getIR() {
        return IR;
    }

    public void setIR(char IR) {
        this.IR = IR;
    }

    public List<WorkerTest> getWorkerTests() {
        return workerTests;
    }

    public void setWorkerTests(List<WorkerTest> workerTests) {
        this.workerTests = workerTests;
    }

    public List<PersonalInterests> getPersonalInterestsDelete() {
        return personalInterestsDelete;
    }

    public void setPersonalInterestsDelete(List<PersonalInterests> personalInterestsDelete) {
        this.personalInterestsDelete = personalInterestsDelete;
    }

    public List<PersonalInterests> getPersonalInterests() {
        return personalInterests;
    }

    public void setPersonalInterests(List<PersonalInterests> personalInterests) {
        this.personalInterests = personalInterests;
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

    public List<CompetenceValue> getCompValueList() {
        return compValueList;
    }

    public void setCompValueList(List<CompetenceValue> compValueList) {
        this.compValueList = compValueList;
    }

    public List<Competence> getTechCmpItems() {
        return techCmpItems;
    }

    public void setTechCmpItems(List<Competence> techCmpItems) {
        this.techCmpItems = techCmpItems;
    }

    public List<Competence> getGenCmpItems() {
        return genCmpItems;
    }

    public void setGenCmpItems(List<Competence> genCmpItems) {
        this.genCmpItems = genCmpItems;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public List<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public String getProjectPreference() {
        return projectPreference;
    }

    public void setProjectPreference(String projectPreference) {
        this.projectPreference = projectPreference;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getSelectedProjects() {
        return selectedProjects;
    }

    public void setSelectedProjects(List<Project> selectedProjects) {
        this.selectedProjects = selectedProjects;
    }

    public List<PersonalProjectInterests> getPersonalProjectInterests() {
        return personalProjectInterests;
    }

    public void setPersonalProjectInterests(List<PersonalProjectInterests> personalProjectInterests) {
        this.personalProjectInterests = personalProjectInterests;
    }

    public List<PersonalProjectInterests> getSelectedPersonalProjectInterests() {
        return selectedPersonalProjectInterests;
    }

    public void setSelectedPersonalProjectInterests(List<PersonalProjectInterests> selectedPersonalProjectInterests) {
        this.selectedPersonalProjectInterests = selectedPersonalProjectInterests;
    } 

    public String getMbtiType() {
        return mbtiType;
    }

    public void setMbtiType(String mbtiType) {
        this.mbtiType = mbtiType;
    }

    /**
     * Para definir la preferencia de una persona por un proyecto dado
     */
    public void addProjectPreference_Listener() {
        PersonalProjectInterests interest;

        for (Project sp : selectedProjects) { //para cada proyecto seleccionado quitar de la lista de proyectos disponibles
            int i = 0;
            boolean found = false;
            while (i < projects.size() && !found) {
                if (sp.getId().equals(projects.get(i).getId())) {
                    projects.remove(i);//eliminar de la lista
                    found = true;
                }
                i++;
            }

            if (found) {
                String pref = localeConfig.getBundleValue("projectInterestsPrefer");

                //agregar a la lista intereses definidos
                interest = new PersonalProjectInterests();
                interest.setProjectFk(sp);
                interest.setPreference(projectPreference.equalsIgnoreCase(pref));
                interest.setWorkersFk(workerController.getSelected());
                personalProjectInterests.add(interest);
            }
        }
        projectPreference = null;
        selectedProjects.clear();
    }

    public void removeProjectPreferenceEditing_Listener() {
        deletedPersonalProjectInterests.clear();
        for (PersonalProjectInterests ppi : selectedPersonalProjectInterests) { //quitar los seleccionados de la lista de proyectos disponibles
            int i = 0;
            boolean found = false;
            while (i < selectedPersonalProjectInterests.size() && !found) {
                if (ppi.getProjectFk().getId().equals(personalProjectInterests.get(i).getProjectFk().getId())) {
                    projects.add(personalProjectInterests.get(i).getProjectFk());//añadir projecto a la lista de roles disponibles
                    personalProjectInterests.remove(i);//quitar preferencia
                    found = true;
                }
                i++;
            }
            deletedPersonalProjectInterests.add(ppi);
        }
        selectedPersonalProjectInterests.clear();
    }

    public void removeProjectPreferenceCreating_Listener() {

        for (PersonalProjectInterests ppi : selectedPersonalProjectInterests) { //quitar los seleccionados de la lista de proyectos disponibles
            int i = 0;
            boolean found = false;
            while (i < selectedPersonalProjectInterests.size() && !found) {
                if (ppi.getProjectFk().getId().equals(personalProjectInterests.get(i).getProjectFk().getId())) {
                    projects.add(personalProjectInterests.get(i).getProjectFk());//añadir projecto a la lista de roles disponibles
                    personalProjectInterests.remove(i);//quitar preferencia
                    found = true;
                }
                i++;
            }

        }
        selectedPersonalProjectInterests.clear();
    }
}
