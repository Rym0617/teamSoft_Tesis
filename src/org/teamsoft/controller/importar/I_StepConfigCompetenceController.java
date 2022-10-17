package org.teamsoft.controller.importar;


import org.teamsoft.controller.importar.tableContent.AssignedAtributeTextValueTable;
import org.teamsoft.controller.importar.tableContent.AssignedCompetenceAtributeValueTable;
import org.teamsoft.controller.importar.tableContent.AssignedCompetencesTable;
import org.teamsoft.controller.importar.tableContent.ConfigNumberCompetences;
import org.teamsoft.controller.util.I_StepUtil;
import org.teamsoft.controller.util.PropertiesUtil;
import org.teamsoft.locale.LocaleConfig;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 * @author jpinas
 */
@ManagedBean
@Named("stepConfCompetences")
@SessionScoped
public class I_StepConfigCompetenceController implements Serializable {

    @Inject
    I_StepSelectCompetenceController selectCompetenceController;
    @Inject
    LocaleConfig localeConfig;
    @Inject
    I_StepUtil util;
    @Inject
    PropertiesUtil propertiesUtil;

    private LinkedList<AssignedCompetencesTable> atributosAsignados;

    // para saber si hay algun cambio y hay que inicializar otra vez
    private LinkedList<AssignedCompetencesTable> atributosAsignadosOriginal;

//      estas propiedades son para cuando existen competencias asignadas que
//      que tienen valor de texto. Se hace para conocer la jerarquía de estos
//      valores

    // lista de competencias asignadas cuyo valor es texto
    private Map<String, LinkedList<AssignedAtributeTextValueTable>> atributosAsignadosTexto;
    private String selectedAssignedText;

    //  estas propiedades son para cuando existen atributos con mas de una competencia
    private Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> competenciaAtrValor;
    private String selectedCompetence;

    private LinkedList<ConfigNumberCompetences> atributosAsignadosNumero;

    /**
     * inicializa los componentes de la clase
     */
    public void init() {
        atributosAsignados = new LinkedList<>(selectCompetenceController.getAtributosAsignados());
        atributosAsignadosOriginal = new LinkedList<>(selectCompetenceController.getAtributosAsignados());

        atributosAsignadosTexto = llenarCompetenciasAsignadasTexto();

        competenciaAtrValor = llenarCompetenciaAtrValor();
        atributosAsignadosNumero = llenarAtributosNumero();

        selectedAssignedText = atributosAsignadosTexto.keySet().stream().findFirst().orElse("");
        selectedCompetence = competenciaAtrValor.keySet().stream().findFirst().orElse("");

        if (propertiesUtil.isDevelopentMode()) {
            generateValues();
        }
        initWeightForTextValues();  // se inicializa la suma desde un inicio para todos los valores que son texto
    }

    private void initWeightForTextValues() {
        List<String> keys = new LinkedList<>(atributosAsignadosTexto.keySet());
        keys.forEach(this::onRowReorder);
    }

    public void clear() {
        atributosAsignados = new LinkedList<>();
        atributosAsignadosTexto = new HashMap<>();
        competenciaAtrValor = new HashMap<>();
        atributosAsignadosNumero = new LinkedList<>();
        selectedAssignedText = "";
        selectedCompetence = "";
    }

    /**
     * genera los valores de todas las tablas de dividiendo la cant de elementos
     * entre 100 y en caso de que la division de con resto, el ultimo valor
     * se le asigna la el resto de la division.
     * <p><br/>
     * Ej. si son 6 valores: <code>100/6 = 16.66</code>, esto se transforma en los valores a cada
     * variable en 0.16, quedando 0.04 por asignar, este valor se le suma al último
     * de la tabla garantizando que la suma de 100
     */
    public void generateValues() {
        generateTextValues();
        generateNumValues();
    }

    /**
     * llena los valores de la tabla de los numeros
     */
    private void generateNumValues() {
        int cantObjetivo = 100;

        competenciaAtrValor.forEach((key, list) -> {
            int cantElementos = list.size();
            int asignar = cantObjetivo / cantElementos;
            int cantSumarUltimo = cantObjetivo - (asignar * cantElementos);
            list.forEach(e -> {
                int index = list.indexOf(e);
                if (index == cantElementos - 1) {
                    e.setPeso((asignar + cantSumarUltimo) / 100f);
                } else {
                    e.setPeso(asignar / 100f);
                }
            });
        });
    }

    /**
     * llena los valores de la tabla de los textos
     */
    private void generateTextValues() {
        int cantObjetivo = 100;
        atributosAsignadosTexto.forEach((key, list) -> {
            int cantElementos = list.size();
            int asignar = cantObjetivo / cantElementos;
            int cantSumarUltimo = cantObjetivo - (asignar * cantElementos);

            list.forEach(e -> {
                int index = list.indexOf(e);
                if (index == cantElementos - 1) {
                    e.setPeso((asignar + cantSumarUltimo) / 100f);
                } else {
                    e.setPeso(asignar / 100f);
                }
            });
        });
    }

    /**
     * filtro y me quedo con los atributos que tienen como valor numeros en el fichero,
     * se itera despues sobre estos buscando el maximo valor y se inserta en la lista de
     * resultado final
     */
    private LinkedList<ConfigNumberCompetences> llenarAtributosNumero() {
        LinkedList<ConfigNumberCompetences> result = new LinkedList<>();

        atributosAsignados.stream().filter(atr -> util.getClassByAtribute(atr.getFileAtribute()).equals(
                localeConfig.getBundleValue("number")
                )
        ).forEach(item -> {
            int max = util.getMaximoValorIntByAtr(item.getFileAtribute());
            int value = getAtributeMaxValue(item.getFileAtribute());

            if (value != -1 && value != max) { // si el valor existe y es diferente al valor real en el fichero
                result.add(new ConfigNumberCompetences(item.getFileAtribute(), value)); // anado el valor modificado ya
            } else {
                result.add(new ConfigNumberCompetences(item.getFileAtribute(), max)); // anado el valor real en el fichero
            }
        });
        return result;
    }

    public int getAtributeMaxValue(String value) {
        int res = -1;
        try {
            Optional<ConfigNumberCompetences> encontrado = atributosAsignadosNumero.
                    stream().
                    filter(item -> item.getFileAtribute().equals(value)).
                    findFirst();
            res = encontrado.map(ConfigNumberCompetences::getMaximoValor).orElse(-1);
        } catch (Exception ignore) {
        }
        return res;
    }


    /**
     * se encarga de iniciar la lista <code>competenciaAtrValor</code>
     */
    private Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> llenarCompetenciaAtrValor() {
        Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> result = new HashMap<>();
        LinkedList<String> compMoreOneValue = selectCompetenceController.getCompetencesWithMoreThanOneAtributeAssigned();

        compMoreOneValue.forEach(comp -> result.put(
                comp, getAtributtesByCompetence(comp))
        );
        return result;
    }

    /**
     * devuelve los atributos con los valores
     * asociados a una competencia
     */
    private LinkedList<AssignedCompetenceAtributeValueTable> getAtributtesByCompetence(String competence) {
        LinkedList<AssignedCompetenceAtributeValueTable> result = new LinkedList<>();

        for (AssignedCompetencesTable atrAsig : atributosAsignados) { // itero por los valores asignados
            LinkedList<String> atrComps = atrAsig.getCompetences(); // dame la lista de comp
            for (String comp : atrComps) { // por cada comp
                if (comp.equalsIgnoreCase(competence)) { // verifico que es la que me interesa
                    AssignedCompetenceAtributeValueTable value;
                    if (!haveCompetenceExistingAtributeValue(comp, atrAsig.getFileAtribute())) {
                        value = new AssignedCompetenceAtributeValueTable( // se inserta
                                atrAsig.getFileAtribute(), 0.0f
                        );
                    } else {
                        value = getCompetenceExistingAtrValue(comp, atrAsig.getFileAtribute());
                    }
                    result.add(value);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * obtiene el objeto que almacena el valor que tiene el atributo en la competencia
     */
    private AssignedCompetenceAtributeValueTable getCompetenceExistingAtrValue(String comp, String fileAtribute) {
        return competenciaAtrValor.get(comp).
                stream().
                filter(item -> item.getAtributo().equals(fileAtribute)).
                findFirst().
                get();
    }

    /**
     * verifica si ya esa competencia tiene asociada el atributo del fichero
     */
    private boolean haveCompetenceExistingAtributeValue(String comp, String fileAtribute) {
        boolean find = false;
        if (competenciaAtrValor != null && competenciaAtrValor.containsKey(comp)) {
            find = competenciaAtrValor.
                    get(comp).
                    stream().
                    anyMatch(item -> item.getAtributo().equals(fileAtribute));
        }
        return find;
    }

    /**
     * se encarga de llenar la lista de atributos
     * asociados como competencias que son texto
     */
    private Map<String, LinkedList<AssignedAtributeTextValueTable>> llenarCompetenciasAsignadasTexto() {
        Map<String, LinkedList<AssignedAtributeTextValueTable>> result = new HashMap<>();

//        me quedo con los que son texto y dsdp es que hago la insercion
        atributosAsignados.stream().filter(atr -> util.getClassByAtribute(atr.getFileAtribute()).equals(
                localeConfig.getBundleValue("text")
                )
        ).forEach(item -> result.put(
                item.getFileAtribute(), getValoresTextoPorAtributo(item.getFileAtribute()))
        );

        return result;
    }

    /**
     * Almacena los valores asociados a un atributo del fichero. Se encarga de
     * obtiene los valores que pertence a la tabla de atributos de texto asignados a las
     * competencias para ir rellenando los valores.
     */
    private LinkedList<AssignedAtributeTextValueTable> getValoresTextoPorAtributo(String atr) {
        LinkedList<AssignedAtributeTextValueTable> result = new LinkedList<>();

        LinkedList<String> valoresAtributo = util.getStringValuesByAtributo(atr);
        for (String val : valoresAtributo) {
            AssignedAtributeTextValueTable assignedCompText;
            if (!haveFileAtributeValue(atr, val)) {
                assignedCompText = new AssignedAtributeTextValueTable(val, 0.0f);
            } else {
                assignedCompText = getFileAtrExistingValue(atr, val);
            }
            result.add(assignedCompText);
        }
        return result;
    }

    /**
     * recupera el objeto que almacena el valore del atributo en el fichero, así
     * como su peso
     */
    private AssignedAtributeTextValueTable getFileAtrExistingValue(String atr, String val) {
        return atributosAsignadosTexto.get(atr).
                stream().
                filter(item -> item.getValorDentroFichero().equals(val)).
                findFirst().
                get();
    }

    /**
     * itera por la lista de atributos asignados como competencias
     * que tienen un valor de tipo texto y pregunta si ya ese existe
     * esa combinacion, con el objetivo de construir la lista inicial y
     * así saber diferenciar cuando se crea a cuando se quiere actualizar
     * los valores
     */
    private boolean haveFileAtributeValue(String atr, String val) {
        boolean find = false;
        if (atributosAsignadosTexto != null && atributosAsignadosTexto.containsKey(atr)) {
            find = atributosAsignadosTexto.get(atr).
                    stream().
                    anyMatch(item -> item.getValorDentroFichero().equals(val));
        }
        return find;
    }

    /**
     * se encarga de verificar que la suma de los valores
     * asignados a los atributos asociados al atributo del
     * fichero sea 1
     */
    public boolean isSumOfTextValuesValid(String atributo) {
        return getSumTexto(atributo) == 1.0f;
    }

    public boolean isSumOfComptAtrValuesValid(String comp) {
        return getSumCompetence(comp) == 1.0f;
    }

    public float getSumOfValuesCompetenceTexto() {
        float suma = 0.0f;
        if (selectedAssignedText != null) {
            suma = getSumTexto(selectedAssignedText);
        }
        return util.round(suma);
    }

    public float getSumOfValuesCompetenceAtr() {
        float suma = 0.0f;
        if (selectedCompetence != null) {
            suma = getSumCompetence(selectedCompetence);
        }
        return util.round(suma);
    }

    public float getSumCompetence(String comp) {
        float suma = 0.0f;
        if (competenciaAtrValor.containsKey(comp)) {
            LinkedList<AssignedCompetenceAtributeValueTable> listToSearch = competenciaAtrValor.get(comp);
            for (AssignedCompetenceAtributeValueTable value : listToSearch) {
                suma += value.getPeso();
            }
        }
        return util.round(suma);
    }

    public float getSumTexto(String atr) {
        float suma = 0.0f;
        if (!atr.isEmpty()) {
            LinkedList<AssignedAtributeTextValueTable> listToSearch = atributosAsignadosTexto.get(atr);
            for (AssignedAtributeTextValueTable value : listToSearch) {
                suma += value.getPeso();
            }
        }
        return util.round(suma);
    }

    public LinkedList<AssignedAtributeTextValueTable> getValuesOfSelectedText(String selectedAtribute) {
        return atributosAsignadosTexto.get(selectedAtribute);
    }

    public void setValuesByTextAtribute(String selectedAtribute, LinkedList<AssignedAtributeTextValueTable> values) {
        atributosAsignadosTexto.put(selectedAtribute, values);
    }

    public LinkedList<String> getKeysOfTextAtributes() {
        return new LinkedList<>(atributosAsignadosTexto.keySet());
    }

    /**
     * para obtener los valores de peso
     * de cada atibuto de la competencia seleccionada
     * se hace de este modo para trabajar en la visual
     */
    public LinkedList<AssignedCompetenceAtributeValueTable> getValuesOfSelectedCompetence(String selectedCompetence) {
        return competenciaAtrValor.get(selectedCompetence);
    }

    /**
     * para obtener las llaves, o sea, las competencias
     * que tienen mas de un atributo asignado
     */
    public LinkedList<String> getKeysOfCompetence() {
        return new LinkedList<>(competenciaAtrValor.keySet());
    }

    /**
     * para el mensaje de error, devuelve los atributos asignados como competencias
     * que faltan por llenar (o sea que a suma de los valores es 1) y tienen asociados
     * un valor de texto en el fichero
     */
    public String getAllAtributosSumaNoCompleta() {
        StringBuilder result = new StringBuilder();
        for (String value : atributosAsignadosTexto.keySet()) {
            if (!isSumOfTextValuesValid(value)) {
                result.append("-").append(value).append("\n");
            }
        }
        return result.toString();
    }

    /**
     * para el mensaje de error, devuelve las competencias que tienen asociados
     * más de un atr y que la suma de los valores no es 1
     */
    public String getAllCompetenciasSumaAtrNoCompleta() {
        StringBuilder result = new StringBuilder();
        for (String value : competenciaAtrValor.keySet()) {
            if (!isSumOfComptAtrValuesValid(value)) {
                result.append("-").append(value).append("\n");
            }
        }
        return result.toString();
    }

    /**
     * busca el peso establecido dado la competencia
     */
    public float getAtributeWeightByCompetence(String competenceName, String atr) {
        return competenciaAtrValor.
                get(competenceName).
                stream().
                filter(e -> e.getAtributo().equals(atr)).
                findFirst().
                get().
                getPeso();
    }

    /**
     * busca el peso establecido dado un atr y su valor dentro del fichero
     */
    public float getAtributeWeightByTextValueAtribute(String atribute, String textInsedeFile) {
        return atributosAsignadosTexto.
                get(atribute).
                stream().
                filter(e -> e.getValorDentroFichero().equals(textInsedeFile)).
                findFirst().
                get().
                getPeso();
    }


//    -------------------------------------GETTERS/SETTERS---------------------------------


    public LinkedList<ConfigNumberCompetences> getAtributosAsignadosNumero() {
        return atributosAsignadosNumero;
    }

    public void setAtributosAsignadosNumero(LinkedList<ConfigNumberCompetences> atributosAsignadosNumero) {
        this.atributosAsignadosNumero = atributosAsignadosNumero;
    }

    public LinkedList<AssignedCompetencesTable> getAtributosAsignados() {
        return atributosAsignados;
    }

    public String getSelectedAssignedText() {
        return selectedAssignedText;
    }

    public void setSelectedAssignedText(String selectedAssignedText) {
        this.selectedAssignedText = selectedAssignedText;
    }

    public Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> getCompetenciaAtrValor() {
        return competenciaAtrValor;
    }

    public void setCompetenciaAtrValor(Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> competenciaAtrValor) {
        this.competenciaAtrValor = competenciaAtrValor;
    }

    public String getSelectedCompetence() {
        return selectedCompetence;
    }

    public void setSelectedCompetence(String selectedCompetence) {
        this.selectedCompetence = selectedCompetence;
    }

    public Map<String, LinkedList<AssignedAtributeTextValueTable>> getAtributosAsignadosTexto() {
        return atributosAsignadosTexto;
    }

    public void setAtributosAsignadosTexto(Map<String, LinkedList<AssignedAtributeTextValueTable>> atributosAsignadosTexto) {
        this.atributosAsignadosTexto = atributosAsignadosTexto;
    }

    /**
     * se llama cuando se realiza un reordenamiento de los valores de la tabla
     * que contiene los valores que son texto. Lo que hace es que divide 1 la cant
     * de valores que hay, desp a cada valor le establece un peso que se calcula por
     * la suma acumulada
     */
    public void onRowReorder(String selctedAtribute) {
//        la jerarquía viene dada por la posicion en el fichero, la primera posicion es la que mayor peso tiene
        try {

            LinkedList<AssignedAtributeTextValueTable> valores = atributosAsignadosTexto.get(selctedAtribute);
            int size = valores.size();
            // valor a otorgar a cada valor por defecto
            float paso = 1f / size;

            float sumaAcumulada = 0f;
            int cont = size - 1; // -1 por el index
            while (cont > -1) { //cuando llega a -1 es que ya temrino
                if (cont == 0) {
                /*para el ultimo paso se le asigna el mayor valor, esto se
                hace en caso de que la cant no sea divisible */
                    sumaAcumulada = 1.0f;
                } else {
                    sumaAcumulada += paso;
                }
                valores.get(cont).setPeso(sumaAcumulada);
                cont--;
            }
            atributosAsignadosTexto.put(selctedAtribute, valores);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void setAtributosAsignados(LinkedList<AssignedCompetencesTable> atributosAsignados) {
        this.atributosAsignados = atributosAsignados;
    }

    public LinkedList<AssignedCompetencesTable> getAtributosAsignadosOriginal() {
        return atributosAsignadosOriginal;
    }

    public void setAtributosAsignadosOriginal(LinkedList<AssignedCompetencesTable> atributosAsignadosOriginal) {
        this.atributosAsignadosOriginal = atributosAsignadosOriginal;
    }
}
