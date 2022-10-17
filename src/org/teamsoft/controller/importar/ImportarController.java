package org.teamsoft.controller.importar;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FlowEvent;
import org.teamsoft.controller.importar.tableContent.AssignedAtributeRoleTable;
import org.teamsoft.controller.importar.tableContent.AssignedCompetencesTable;
import org.teamsoft.controller.util.I_StepUtil;
import org.teamsoft.controller.util.ImportResult;
import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.PropertiesUtil;
import org.teamsoft.entity.*;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.*;
import org.teamsoft.model.importar.PersonaModel;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.teamsoft.controller.util.MyDateUtils.convertLocalDateToDate;

/**
 * @author jpinas
 */
@Named("importarController")
@SessionScoped
public class ImportarController implements Serializable {

    @Inject
    WorkerFacade workerFacade;
    @Inject
    CompetenceImportanceFacade importanceFacade;
    @Inject
    LevelsFacade levelsFacade;
    @Inject
    CompetenceFacade competenceFacade;
    @Inject
    CompetenceValueFacade competenceValueFacade;
    @Inject
    RoleExperienceFacade roleExperienceFacade;
    @Inject
    PersonalInterestsFacade personalInterestsFacade;
    @Inject
    PersonGroupFacade personGroupFacade;
    @Inject
    RoleFacade roleFacade;

    @Inject
    LocaleConfig localeConfig;
    @Inject
    I_StepReadFileController stepLeerFicheroController;
    @Inject
    I_StepSelectPersonController stepAtributosPersonaController;
    @Inject
    I_StepSelectCompetenceController stepSelectCompetencesController;
    @Inject
    I_StepConfigCompetenceController stepConfigCompetenceController;
    @Inject
    I_StepSelectRolesController stepSelectRolesController;
    @Inject
    I_StepVerifyDataController stepVerifyData;
    @Inject
    I_StepUtil util;
    @Inject
    PropertiesUtil propertiesUtil;

    int contador = 0; // para saber la cant de veces que se muestra el sms de no existencia de competencias y roles
    static String mensajeDespuesImportar = "";

    /**
     * se declara como una variable de la clase para no
     * tener que estar llamando el metodo cada vez que se importa
     * a una persona, porque los datos no cambian, o sea, la lista
     * siempre va a ser la misma durante el proceso de importacion
     */
    Map<String, LinkedList<String>> competencesWithAtributes;

    /**
     * se encarga de determinar cuál es el próximo paso y de validarlos
     */
    public String onFlowProcess(FlowEvent event) {
        String step = event.getNewStep();
        String oldStep = event.getOldStep();

        switch (step) {
            case "select-person": {
                if (propertiesUtil.isDevelopentMode() && oldStep.equals("select-file") && !stepLeerFicheroController.getDataList().isEmpty()) {
                    return "verify-data";
                } else {
//                si no se ha seleccionado un fichero
                    if (stepLeerFicheroController.getDataList().isEmpty()) {
                        JsfUtil.addWarningMessage(
                                localeConfig.getBundleValue("error_necessary_select_file")
                        );
                        step = event.getOldStep();
//                si se cambia de fichero
                    } else if (checkIfExistRoleAndCompetences()) {
                        showMessageIfExistRoleAndCompetences();
                        step = event.getOldStep();
                    } else if (stepLeerFicheroController.getSelectedPersonGroup() == null ||
                            stepLeerFicheroController.getSelectedPersonGroup().trim().isEmpty()) {
                        JsfUtil.addWarningMessage(
                                localeConfig.getBundleValue("required_message_group_name")
                        );
                        step = event.getOldStep();
                    } else if (changeSelectedFile()) {
                        stepAtributosPersonaController.init();
                        stepSelectCompetencesController.clear();
                    }
                }
                break;
            }
            case "select-competence": {
                if (stepAtributosPersonaController.hasLessValues()) {
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_missing_atributes") + " " +
                                    stepAtributosPersonaController.getAtributosPersona().size() + " " +
                                    localeConfig.getBundleValue("attributes")
                    );
                    step = event.getOldStep();
                } else if (stepAtributosPersonaController.hasMoreValues()) {
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_surplus_attributes") +
                                    stepAtributosPersonaController.getAtributosPersona().size() + " " +
                                    localeConfig.getBundleValue("attributes")
                    );
                    step = event.getOldStep();
                } else if (stepAtributosPersonaController.isPropertyMissing()) {
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_fill_properties_person")
                    );
                    step = event.getOldStep();
                } else if (stepAtributosPersonaController.isAtributesDuplicated()) {
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("person_field_repeated")
                    );
                    step = event.getOldStep();
                } else if (!stepAtributosPersonaController.isAllPropertyRightValue()) {
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("error_tipo_campos")
                    );
                    step = event.getOldStep();
                }

                if (stepSelectCompetencesController.getAtributosDisponibles() == null) {
                    stepSelectCompetencesController.init();
                } else if (changeAtributesFromStep2To3()) {
                    stepSelectCompetencesController.update();
                }
                break;
            }
            case "config-competences": {
                if (!stepSelectCompetencesController.getAtributosAsignados().isEmpty()) {
                    // obtengo los atributos que faltan por asignarle competencias
                    String atrWithOutCompetenc = stepSelectCompetencesController.getAllAssignedAtributesWithOutCompetence();
                    if (!atrWithOutCompetenc.isEmpty()) { // si existen atr sin relacionar competencias
                        step = event.getOldStep();
                        JsfUtil.addWarningMessage(
                                localeConfig.getBundleValue("message_asociate_competences") +
                                        (atrWithOutCompetenc.split(", ").length == 1 ? localeConfig.getBundleValue("next_attribute") :
                                                localeConfig.getBundleValue("next_attributes")) +
                                        atrWithOutCompetenc
                        );
                    } else {
                        if (changeAtributesFromStepCompToConfComp())
                            stepConfigCompetenceController.init();
                    }
                } else {
                    step = event.getOldStep();
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_missing_competences")
                    );
                }
                break;
            }
            case "select-role": {
                /*// obtengo los atributos que no tienen la suma completa
                String atrWithSumaIncompleta = stepConfigCompetenceController.getAllAtributosSumaNoCompleta();
                // si existen atributos con la suma de los valores del fichero incompleta
                if (!atrWithSumaIncompleta.isEmpty()) {
                    step = event.getOldStep();
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_attr_sum_not_complete") + atrWithSumaIncompleta
                    );
                } else {*/ // si todos los atr tienen la suma de los valores del fichero igual a 1
                /* verifico si aquellos atr que tienen asociados competencias con valores de texto
                 * se le asignó un peso a cada una y la suma es igual a 1*/
                String competenciasFaltantes = stepConfigCompetenceController.getAllCompetenciasSumaAtrNoCompleta();
                if (!competenciasFaltantes.isEmpty()) {
                    step = event.getOldStep();
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_comp_sum_not_complete") + competenciasFaltantes
                    );
                }
//                }

                // inicializo o actualizo los valores de la ventana
                if (stepSelectRolesController.getAtributosDisponibles() == null) { // si no se ha accedido todavia
                    stepSelectRolesController.init(); // inicializo todo
                } else if (changeAtributesFromStepCompToRoles()) { // si no se actualizan los cambios
                    stepSelectRolesController.update();
                }
                break;
            }
            case "verify-data": {
                if (stepSelectRolesController.getAtributosAsignados().isEmpty()) {
                    step = event.getOldStep();
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_at_least_one_role")
                    );
                } else if (stepSelectRolesController.isAnyAtributeWithOutRoleAssigned()) {
                    step = event.getOldStep();
                    JsfUtil.addWarningMessage(
                            localeConfig.getBundleValue("message_attributes_without_role")
                    );
                } else {
                    stepVerifyData.init();
                }
            }
        }
        return step;
    }

    private boolean changeAtributesFromStepCompToRoles() {
        LinkedList<String> disp = stepSelectCompetencesController.getAtributosDisponibles();
        LinkedList<String> ori = stepSelectRolesController.getAtributosDisponiblesOriginal();
        return !disp.equals(ori);
    }

    private boolean changeAtributesFromStepCompToConfComp() {
        LinkedList<AssignedCompetencesTable> disp = stepSelectCompetencesController.getAtributosAsignados();
        LinkedList<AssignedCompetencesTable> ori = stepConfigCompetenceController.getAtributosAsignadosOriginal();
        return !disp.equals(ori);
    }

    private boolean changeSelectedFile() {
        return !stepLeerFicheroController.getDataList().equals(stepAtributosPersonaController.getDataList());
    }

    private boolean changeAtributesFromStep2To3() {
        return !stepAtributosPersonaController.getAtributosDisponibles().equals(stepSelectCompetencesController.getAtributosDisponiblesOriginal());
    }

    /**
     * construye el sms a partir de los datos resultantes de la
     * importacion
     */
    private String construirSms(ImportResult importResult) {
        String sms = localeConfig.getBundleValue("msg_after_import");
        return String.format(
                sms,
                importResult.getPersonasImportadas(),
                importResult.getPersonasActualizadas(),
                importResult.getPersonasNoImportadas(),
                importResult.getErrores()
        );
    }

    /**
     * metodo que se encarga de manejar el click en el btn importar.
     * <p>
     * Se verifica si existen alguna de las personas a insertar ya en la bd,
     * en caso de que existan, se pregunta si quiere actualizar los valores,
     * en caso que no quira actualizar los valores se insertar aquellos que
     * no existían.
     */
    public void actionBtnImportar() {
        if (stepAtributosPersonaController.existenPersonas()) {
            executeImportar(propertiesUtil.isUpdateIfExist());
        } else {
            executeImportar(false);
        }
    }

    /**
     * metodo comun para realizar la importacion
     */
    public void executeImportar(boolean updateData) {
        ImportResult importResult = importar(updateData);
        mensajeDespuesImportar = construirSms(importResult);
        if (importResult.getPersonasImportadas() != 0 || importResult.getPersonasActualizadas() != 0) {
            PrimeFaces.current().executeScript("PF('dialogMessage').show()");
        } else {
            JsfUtil.addWarningMessage(mensajeDespuesImportar);
        }
    }

    /**
     * este metodo se llama cuando se muestra el dialogo informando el resultado de la
     * importacion
     */
    public void redirectToFirstStep() {
        clearAllTabsValues();
        PrimeFaces.current().executeScript("PF('wizard').loadStep('select-file',false)");
    }

    /**
     * prepara para volver a importar un nuevo fichero, para esto
     * limpia todos los valores actuales en los tabs.
     */
    private void clearAllTabsValues() {
        stepLeerFicheroController.clear();
        stepAtributosPersonaController.clear();
        stepSelectCompetencesController.clear();
        stepConfigCompetenceController.clear();
        stepSelectRolesController.clear();
        stepVerifyData.clear();
    }

    /**
     * importa los datos del fichero
     */
    public ImportResult importar(boolean updateData) {
        return insert(updateData);
    }

    /**
     * <p>
     * itera por todas las personas a insertar y busca si ya existen,
     * en caso de que existan pregunta si se quiere actualizar
     * los valores, en caso de que no se quieran actualizar los
     * valores se pasa para el siguiente. En caso de que no exista
     * la persona se inserta, en este proceso de insercion se genera
     * un ci, sexo, provincia, grupo de forma aleatoria
     * </p>
     * <br/>
     * <p>
     * ademas, inserta/actualiza los valores de las personas en las
     * competencias; además establece la experiencia de las personas
     * en los roles, junto con la rpeferencia de las personas por ellos
     *
     * @param updateData si se va a actualizar la informacion
     * @return devuelve el objeto <code>ImportResult</code> que contiene
     * la informacion de la importacion: ej. cant personas importadas,
     * cant de personas actualizadas y cantidad de personas no insertadas
     */
    private ImportResult insert(boolean updateData) {
        LinkedList<PersonaModel> personList = stepAtributosPersonaController.getAllPersonas();
        competencesWithAtributes = stepSelectCompetencesController.getAllCompetencesWithAllAtributes();

        ImportResult importResult = new ImportResult();
        PersonGroup group = getPersonGroup();
        personList.forEach(personaModel -> {
            try {
                Worker worker;
                long id = stepAtributosPersonaController.existePersona(personaModel.getNombre(), personaModel.getExp(), group);
                if (id != -1) { // si existe
                    worker = workerFacade.find(id);
                    if (updateData) { // si se quiere actualizar
                        setPersonData(worker, personaModel, group); // se escriben nuevos datos
                        workerFacade.edit(worker);
                        setCompetenceData(worker, true); // se actualizan los valores de las competencias
                        setRoleData(worker, true);
                        importResult.incrementActualizadas();
                    } else {
                        importResult.incrementNoImport();
                    }
                } else { // si no existe se crea uno nuevo
                    worker = new Worker();
                    setPersonData(worker, personaModel, group); // se escriben los datos
                    workerFacade.create(worker); // ya el objeto coge el id
                    setCompetenceData(worker, false); // se insertan los valores de las competencias
                    setRoleData(worker, false);
                    importResult.incrementImport();
                }
            } catch (Exception exception) {
                Logger.getGlobal().log(Level.SEVERE, "Ya existe: " + personaModel.getNombre() + " con " + personaModel.getExp() + " deexp");
                importResult.incrementError();
            }
        });
        return importResult;
    }

    /**
     * inserta la experiencia de las personas por los roles, ademas
     * de establecer la preferencia de las personas por los roles, siguiendo
     * un pto de corte. En caso de que ya exista la combinacion de rol con worker
     * en funcion de lo establecido en la conf se actualiza/inserta/borra los
     * valores
     */
    private void setRoleData(Worker worker, boolean isUpdate) {
        int indexPersona = stepLeerFicheroController.getIndexByPersona(
                stepAtributosPersonaController.getIndexOfNombre(),
                stepAtributosPersonaController.getIndexOfExp(),
                worker.getPersonName(), worker.getExperience()
        );

        /*valores establecidos en la configuracion*/
        int anosMax = propertiesUtil.getMaximoAnoExperiencia();
        float ptoCorte = propertiesUtil.getPtoCorte();
        boolean deleteOld = propertiesUtil.isDeleteOldValues();

        /*si se quiere borrar los valores viejos se borran todos los valores asociado
         * a esa persona*/
        if (deleteOld) {
            personalInterestsFacade.deleteByWorker(worker);
            roleExperienceFacade.deleteByWorker(worker);
        }

        LinkedList<AssignedAtributeRoleTable> roleAtrList = stepSelectRolesController.getAtributosAsignados();
        roleAtrList.forEach(e -> {
            int indexAtr = util.getIndexOfProperty(e.getFileAtribute());
            /*exp de la persona en el rol*/
            int anosXpEnRol = Integer.parseInt(stepLeerFicheroController.getValue(
                    String.valueOf(indexPersona), indexAtr
            ));

            float worker_exp = worker.getExperience() >= anosMax ? 1f : (float) worker.getExperience() / anosMax;
            float role_experience = (float) anosXpEnRol / worker.getExperience();

            Optional<RoleExperience> roleExperienceToCheck = Optional.empty();
            Optional<PersonalInterests> valueToCheck = Optional.empty();
            if (!deleteOld) {
                /*verificar si existe ese rol para esa persona*/
                roleExperienceToCheck = roleExperienceFacade.findByRoleAndWorker(
                        e.getRol(), worker
                );
                /*verifico si exise ese rol para esa persona, si no existe se crea y si existe
                 * se devuelve la instancia para modificar su valor*/
                valueToCheck = personalInterestsFacade.findByRoleAndWorker(
                        e.getRol(), worker
                );
            }
            RoleExperience roleExperienceToCreate = roleExperienceToCheck.orElseGet(RoleExperience::new);
            roleExperienceToCreate.setRoleFk(e.getRol());
            roleExperienceToCreate.setWorkerFk(worker);
            roleExperienceToCreate.setIndexes(worker_exp);

            PersonalInterests persIntToCreate = valueToCheck.orElseGet(PersonalInterests::new);
            persIntToCreate.setRolesFk(e.getRol());
            persIntToCreate.setWorkersFk(worker);

            /*la preferecnia se determina a partir de la cant de años
             * que tiene de experiencia como profesional y la exeriencia
             * en el rol, si es mayor que el valor definido como pto de corte
             * entonces lo prefiere*/
            persIntToCreate.setPreference(role_experience >= ptoCorte);

            if (isUpdate) {
                personalInterestsFacade.edit(persIntToCreate);
                roleExperienceFacade.edit(roleExperienceToCreate);
            } else {
                personalInterestsFacade.create(persIntToCreate);
                roleExperienceFacade.create(roleExperienceToCreate);
            }
        });
    }

    /**
     * inserta a los valores de las competencias
     *
     * @param isUpdate en este caso el isUpdate se utiliza para saber si se crea
     *                 o se actualiza el valor, no para saltarlo
     * @param worker   persona a la que se le insertan los valores
     */
    private void setCompetenceData(Worker worker, boolean isUpdate) {
        Map<String, Float> values = getCompetencesValue(worker);

        /*en caso de que no se quieran conservar los datos antiguos
         * se borran todas las competencias*/
        boolean deleteOld = propertiesUtil.isDeleteOldValues();
        if (deleteOld) {
            competenceValueFacade.deleteCompetencesByWorker(worker);
        }
        values.forEach((competence, value) -> {
            Levels level = getLevelByValue(value); // level a setear
            Competence competenceObj = competenceFacade.findByName(competence); // competencia a setear
            CompetenceValue compValueToCreate; // objeto a insertar/actualizar

            /*esto lo hago para verificar si existe una competencia para esta persona antes
             * para que en caso de que exista modificarle el level a esta*/
            Optional<CompetenceValue> compValueToCheck = Optional.empty();
            if (!deleteOld) {
                compValueToCheck = competenceValueFacade.findByCompetenceAndWorker(
                        competenceObj, worker
                );
            }

            compValueToCreate = compValueToCheck.orElseGet(CompetenceValue::new);
            compValueToCreate.setCompetenceFk(competenceObj);
            compValueToCreate.setLevelFk(level);
            compValueToCreate.setWorkersFk(worker);

            if (isUpdate) {
                competenceValueFacade.edit(compValueToCreate);
            } else {
                competenceValueFacade.create(compValueToCreate);
            }
        });

        /*esto lo realizo pq desde la visual me da problemas, o sea
         * no lo puedo realizar de forma manual*/
        if (propertiesUtil.insertGenericCompetencesAutomaticly()) {
            List<Competence> genericCompetences = competenceFacade.getGenericCompetences();
            genericCompetences.forEach(e -> {
                if (!values.containsKey(e.getCompetitionName())) {
                    CompetenceValue competenceValue = new CompetenceValue();
                    competenceValue.setWorkersFk(worker);
                    competenceValue.setLevelFk(getLevelByValue(0.15f));
                    competenceValue.setCompetenceFk(e);
                    competenceValueFacade.create(competenceValue);
                }
            });
        }
    }

    /**
     * busca el <code>level</code> que le corresponde al valor de la competencia,
     * para esto busca todos los niveles que hay en la bd, se crea una especie de
     * intervalos, que donde caiga el valor, es el nivel que le corresponde
     * <p>
     * Ej. <code>value=0.8; levelList.size()=4 tamanoPorciones=0.25<code/> cae
     * en el ultimo nivel o intervalo
     *
     * @param value valor de la competencia
     * @return Level que le corresponde a ese valor
     */
    private Levels getLevelByValue(Float value) {
        List<Levels> levelList = levelsFacade.findAll();
        levelList.sort(Comparator.comparing(Levels::getLevels)); // ordeno la lista de menor a mayor
        int cantLevels = levelList.size();
        float tamanoPorciones = 1f / cantLevels;
        float valorAcumulado = tamanoPorciones;

        int pos = 0;
        while (value > valorAcumulado && pos < cantLevels) {
            valorAcumulado += tamanoPorciones;
            pos++;
        }
        return levelList.get(pos);
    }

    /**
     * obtiene el listado de valores en las competencias para la persona. para esto
     * se buca el la posicion de la persona en el fichero, despues, para cada competencia
     * se verifica si tiene un solo artibuto asociado, o más de uno. En caso de que tenga
     * más de uno, para cada uno se pregunta si el atr es numerico o texto, en caso de que
     * sea numerico, se busca el valore de la persona en el fichero y se divide entre el maximo
     * valor encontrado para ese atr, en caso de que sea texto, se busca el peso que tiene
     * ese atributo y se multiplica por el valor establecido. en caso de que sea uno solo,
     * solamente se hace el proceso para un valor
     *
     * @param worker persona que se quieren calcular los valores en las competencias
     * @return <code>Map<String,Float></></code> con las competencia y los valores
     */
    private Map<String, Float> getCompetencesValue(Worker worker) {
        Map<String, Float> result = new HashMap<>();

        int indexPersona = stepLeerFicheroController.getIndexByPersona(
                stepAtributosPersonaController.getIndexOfNombre(),
                stepAtributosPersonaController.getIndexOfExp(),
                worker.getPersonName(), worker.getExperience()
        );
        competencesWithAtributes.forEach((competenceName, atributesList) -> {
            float valorFinal = 0;
            if (atributesList.size() > 1) {
                for (String atr : atributesList) {
                    float atrWeight = stepConfigCompetenceController.getAtributeWeightByCompetence(competenceName, atr);
                    /*en funcion de la clase que almacena el atr relacionado con
                    la competencia es que se calcula busca el valor */
                    if (util.isNumber(util.getClassByAtribute(atr))) {
                        /*si es numero el valor que hay que buscar numerico y, por tanto,
                         * hay que dividirlo entre la cant max detectada */
                        float valorPersona = Float.parseFloat(
                                stepLeerFicheroController.getValue(
                                        String.valueOf(indexPersona), util.getIndexOfProperty(atr)
                                )
                        );
                        float valorMax = stepConfigCompetenceController.getAtributeMaxValue(atr);
                        valorFinal += valorPersona / valorMax * atrWeight;
                    } else {
                        /*si es un texto entonces hay que buscar el valor numerico
                         * establecido*/
                        float atrWeightText = stepConfigCompetenceController.getAtributeWeightByTextValueAtribute(
                                atr, stepLeerFicheroController.getValue(
                                        String.valueOf(indexPersona), util.getIndexOfProperty(atr)
                                )
                        );
                        valorFinal += atrWeight * atrWeightText;
                    }
                }
            } else {
                String atr = atributesList.get(0);
                if (util.isNumber(util.getClassByAtribute(atr))) {
                    float valorPersona = Float.parseFloat(
                            stepLeerFicheroController.getValue(
                                    String.valueOf(indexPersona), util.getIndexOfProperty(atr)
                            )
                    );
                    float maxValue = stepConfigCompetenceController.getAtributeMaxValue(atr);
                    valorFinal = valorPersona / maxValue;
                } else {
                    valorFinal = stepConfigCompetenceController.getAtributeWeightByTextValueAtribute(
                            atr, stepLeerFicheroController.getValue(
                                    String.valueOf(indexPersona), util.getIndexOfProperty(atr)
                            )
                    );
                }
            }
            result.put(competenceName, valorFinal);
        });
        return result;
    }

    /**
     * metodo que se encarga de actualizar los valores al <code>Worker</code>
     */
    private void setPersonData(Worker worker, PersonaModel e, PersonGroup group) {
        worker.setGroupFk(group);
        worker.setPersonName(e.getNombre());
        worker.setInDate(convertLocalDateToDate(LocalDate.now()));
        worker.setExperience(Integer.parseInt(e.getExp()));
        worker.setStatus(JsfUtil.workerStatus.Active.toString());
        worker.setWorkload(0f);
        worker.setEmail(util.generateRandomEmail(worker.getPersonName()));
        worker.setPhone(String.valueOf(util.getRandomPhoneNumber()));
        worker.setSex(util.getRandomSex());
        worker.setIdCard(util.generateRandomCI(worker.getSex()));
        worker.setCountyFk(util.generateRandomCounty());

        if (worker.getWorkerTest() == null) {
            WorkerTest workerTest = new WorkerTest();
            workerTest.setWorkerFk(worker);
            workerTest.setCE('I');
            workerTest.setCH('I');
            workerTest.setCO('I');
            workerTest.setES('I');
            workerTest.setID('I');
            workerTest.setIF('I');
            workerTest.setIR('I');
            workerTest.setIS('I');
            workerTest.setME('I');
            workerTest.setTipoMB(util.getRandomMbType());
            worker.setWorkerTest(workerTest);
        }

    }

    /**
     * devuelve el grupo establecido en el primer paso si es un grupo
     * que ya existia, si no, lo crea y entonces se develve
     */
    private PersonGroup getPersonGroup() {
        PersonGroup group = stepLeerFicheroController.getSelectedPersonGroupObject();
        if (stepLeerFicheroController.isNewGroup()) {
            personGroupFacade.create(group);
        }
        return group;
    }

    public String getMensajeDespuesImportar() {
        return mensajeDespuesImportar;
    }

    public void showMessageIfExistRoleAndCompetences() {
        StringBuilder message = new StringBuilder();
        String plu = localeConfig.getBundleValue("is_necessary");

        List<String> sms = new LinkedList<>();
        if (roleFacade.findAll().isEmpty()) {
            sms.add(localeConfig.getBundleValue("roles"));
        }
        if (competenceFacade.findAll().isEmpty()) {
            sms.add(localeConfig.getBundleValue("competences"));
        }
        if (levelsFacade.findAll().isEmpty()) {
            sms.add(localeConfig.getBundleValue("levels"));
        }
        if (importanceFacade.findAll().isEmpty()) {
            sms.add(localeConfig.getBundleValue("competence_importance"));
        }
        if (!sms.toString().isEmpty()) {
            message.append(plu);
            sms.forEach(s -> {
                message.append(s);
                if (sms.indexOf(s) != sms.size() - 1)
                    message.append(", ");
            });
            message.append(" ").append(localeConfig.getBundleValue("to_function"));
            JsfUtil.addErrorMessage(message.toString());
        }
    }

    public boolean checkIfExistRoleAndCompetences() {
        return roleFacade.findAll().isEmpty() || competenceFacade.findAll().isEmpty() ||
                levelsFacade.findAll().isEmpty();
    }

    public void actionClickImport() {
        if (checkIfExistRoleAndCompetences() && contador != 0) {
            showMessageIfExistRoleAndCompetences();
        }
        contador++;
    }
}
