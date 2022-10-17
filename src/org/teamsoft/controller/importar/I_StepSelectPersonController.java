package org.teamsoft.controller.importar;


import org.primefaces.event.DragDropEvent;
import org.teamsoft.controller.importar.tableContent.PersonValueTable;
import org.teamsoft.controller.util.I_StepUtil;
import org.teamsoft.controller.util.PropertiesUtil;
import org.teamsoft.entity.PersonGroup;
import org.teamsoft.entity.Worker;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.WorkerFacade;
import org.teamsoft.model.importar.PersonaModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * @author jpinas
 */
@ManagedBean
@Named("stepSelectPersonController")
@SessionScoped
public class I_StepSelectPersonController implements Serializable {

    @Inject
    WorkerFacade workerFacade;
    @Inject
    I_StepReadFileController stepOneController;
    @Inject
    LocaleConfig localeConfig;
    @Inject
    I_StepUtil util;
    @Inject
    PropertiesUtil propertiesUtil;

    private LinkedList<String> atributosDisponibles; // valores que vienen del fichero que no se han asignado
    private Map<String, Object> atributosPersona; // valores que se muestran en el comobox
    private LinkedList<PersonValueTable> atributosAsignados; // valores que estan el la tabla

    //    esto lo tengo para saber si se hizo un cambio de fichero al volver al paso anterior
    private List<String[]> dataList;

    String llaveNombrePersona;
    String llaveExpPersona;

    @PostConstruct
    public void init() {
        llaveNombrePersona = localeConfig.getBundleValue("person_field_name");
        llaveExpPersona = localeConfig.getBundleValue("person_field_exp");

        atributosDisponibles = new LinkedList<>(stepOneController.getEncabezadoList());
        dataList = new LinkedList<>(stepOneController.getDataList());
        atributosAsignados = new LinkedList<>();
        initAtributosPersona();

        if (propertiesUtil.isDevelopentMode()) {
            initAtributosAsignados();
        }
    }

    public void clear() {
        atributosDisponibles = new LinkedList<>();
        dataList = new LinkedList<>();
        atributosAsignados = new LinkedList<>();
        atributosPersona = new HashMap<>();
    }

    /**
     * busca los atributos que se llaman <code>exp<code/> y <code>nombre<code/>, para
     * asignarlo
     */
    private void initAtributosAsignados() {
        atributosAsignados.add(new PersonValueTable(
                        "exp", llaveExpPersona
                )
        );
        atributosDisponibles.remove("exp");

        atributosAsignados.add(new PersonValueTable(
                        "nombre", llaveNombrePersona
                )
        );
        atributosDisponibles.remove("nombre");
    }

    /**
     * lista de los atributos de las personas a matchear
     */
    private void initAtributosPersona() {
        atributosPersona = new HashMap<>();
        atributosPersona.put(llaveNombrePersona, ""); // se le pone el 1 para saber el tipo de dato que le corresponde
        atributosPersona.put(llaveExpPersona, 1);
    }

    /**
     * se encarga de eliminar el atr seleccionado de los disponibles y
     * añadirlo a los asignados
     */
    public void onDrop(DragDropEvent ddEvent) {
        String atr = ((String) ddEvent.getData());

        atributosAsignados.add(new PersonValueTable(
                        atr, ""
                )
        );
        atributosDisponibles.remove(atr);
    }

    public LinkedList<String> getAtributosDisponibles() {
        return atributosDisponibles;
    }

    public Map<String, Object> getAtributosPersona() {
        return atributosPersona;
    }

    public void setAtributosPersona(Map<String, Object> atributosPersona) {
        this.atributosPersona = atributosPersona;
    }

    public LinkedList<PersonValueTable> getAtributosAsignados() {
        return atributosAsignados;
    }

    public void setAtributosAsignados(LinkedList<PersonValueTable> atributosAsignados) {
        this.atributosAsignados = atributosAsignados;
    }

    public List<String[]> getDataList() {
        return dataList;
    }

    public void setDataList(List<String[]> dataList) {
        this.dataList = dataList;
    }

    public boolean isPropertyMissing() {
        for (PersonValueTable value : atributosAsignados) {
            if (value.getAtributoPersona() == null || value.getAtributoPersona().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMoreValues() {
        return getAtributosAsignados().size() > getAtributosPersona().size();
    }

    public boolean isAtributesDuplicated() {
        Map<String, Integer> dictOcurrencies = new HashMap<>();
        for (PersonValueTable value : atributosAsignados) {
            if (dictOcurrencies.containsKey(value.getAtributoPersona())) {
                return true;
            } else {
                dictOcurrencies.put(value.getAtributoPersona(), 1);
            }
        }
        return false;
    }

    public boolean hasLessValues() {
        return getAtributosAsignados().size() < getAtributosPersona().size();
    }

    /**
     * devuelve el listado de propiedades que presentan las personas, se utiliza
     * para llenar el combo en la visual de las propiedades de las personas
     */
    public LinkedList<String> getPersonAtributes() {
        return new LinkedList<>(atributosPersona.keySet());
    }

    /**
     * se encarga de restablecer el atributo asignado a disponible
     */
    public void removeAtribute(PersonValueTable toRemove) {
        atributosAsignados.remove(toRemove);
        atributosDisponibles.addFirst(toRemove.getAtributoFichero());
    }

    /**
     * verifica si la propiedad que se le pasa por parametro tiene el tipo de dato correcto
     */
    public boolean hasPropertyRightClass(String propertyToCheck) {
//        obtengo la clase que tienen que almacenar esta propiedad
        Object classOfPropertyToCheck = atributosPersona.get(propertyToCheck);

//        voy a la info que almacena el fichero y obtengo el primer dato que viene en la columna
        int indexOfPropertyToCheck = util.getIndexOfProperty(
                getAtributoFicheroByPersonAtribute(propertyToCheck)
        );
//        esto siempre me devuelve un String
        String classOfDataOfProperty = util.getValueInIndex(indexOfPropertyToCheck);
//        reviso si lo que me viene en el fichero coincide con lo que se le asigno
        try {
            Integer.parseInt(classOfDataOfProperty);
            return classOfPropertyToCheck instanceof Integer;
        } catch (Exception e) {
            return classOfPropertyToCheck instanceof String;
        }
    }

    /**
     * itera por todas las propiedades asignadas y verifica que se le asigno un valor que
     * corresponde con el que viene en el fichero
     */
    public boolean isAllPropertyRightValue() {
        for (PersonValueTable value : atributosAsignados) {
            if (!hasPropertyRightClass(value.getAtributoPersona())) {
                return false;
            }
        }
        return true;
    }

    /**
     * busca dentro de los valores asigandos, cuál es el atributo del fichero que le corresponde
     */
    private String getAtributoFicheroByPersonAtribute(String atribute) {
        for (PersonValueTable value : atributosAsignados) {
            if (value.getAtributoPersona().equalsIgnoreCase(atribute)) {
                return value.getAtributoFichero();
            }
        }
        return "";
    }

    /**
     * busca la clase del atributo de la persona seleccionada
     */
    public Object getGetClassByPersonAtribute(String value) {
        return value.isEmpty() ? localeConfig.getBundleValue("label_no_establecido") : util.getClassOf(atributosPersona.get(value).toString());
    }

    public LinkedList<PersonaModel> getAllPersonas() {
        LinkedList<PersonaModel> result = new LinkedList<>();
        int indexNombre = getIndexOfNombre();
        int indexExp = getIndexOfExp();
        dataList.forEach(e -> result.add(new PersonaModel(
                e[indexNombre],
                e[indexExp]
        )));
        return result;
    }

    /**
     * busca en el fichero la posicion del atributo que se
     * relaciono con el nombre de la persona
     */
    public int getIndexOfNombre() {
        return util.getIndexOfProperty(getAtributoFicheroByPersonAtribute(llaveNombrePersona));
    }

    /**
     * busca en el fichero la posicion del atributo que se
     * relaciono con la experiencia de la persona
     */
    public int getIndexOfExp() {
        return util.getIndexOfProperty(getAtributoFicheroByPersonAtribute(llaveExpPersona));
    }

    public boolean existenPersonas() {
        PersonGroup group = stepOneController.getSelectedPersonGroupObject();
        LinkedList<PersonaModel> personasModels = getAllPersonas();
        for (PersonaModel personaModel : personasModels) {
            if (existePersona(personaModel.getNombre(), personaModel.getExp(), group) != -1) {
                return true;
            }
        }
        return false;
    }

//    public int getCantPersRepetidas() {
//        int cont = 0;
//        LinkedList<PersonaModel> personasModels = getAllPersonas();
//        for (PersonaModel personaModel : personasModels) {
//            if (existePersona(personaModel.getNombre(), personaModel.getExp()) != -1) {
//                cont++;
//            }
//        }
//        return cont;
//    }

    /**
     * devuelve el id de la persona si existe, -1 en caso contrario
     */
    public long existePersona(String nombre, String exp, PersonGroup group) {
        List<Worker> allWorkers = workerFacade.findAll();
        int xp = Integer.parseInt(exp);
        for (Worker worker : allWorkers) {
            if (nombre.equals(worker.getFullName()) && worker.getExperience() == xp &&
                    worker.getGroupFk().getId().equals(group.getId())) {
                return worker.getId();
            }
        }
        return -1;
    }
}
