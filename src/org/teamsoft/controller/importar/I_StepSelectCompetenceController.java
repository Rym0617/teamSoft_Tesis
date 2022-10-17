package org.teamsoft.controller.importar;


import org.primefaces.event.DragDropEvent;
import org.teamsoft.controller.importar.tableContent.AssignedCompetencesTable;
import org.teamsoft.controller.util.PropertiesUtil;
import org.teamsoft.entity.Competence;
import org.teamsoft.model.CompetenceFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;


/**
 * @author jpinas
 */
@ManagedBean
@Named("stepSelectCompetencesController")
@SessionScoped
public class I_StepSelectCompetenceController implements Serializable {

    @EJB
    CompetenceFacade ejbFacade;

    @Inject
    I_StepSelectPersonController stepSelectPersonController;
    @Inject
    PropertiesUtil propertiesUtil;

    private List<Competence> competenceList;
    private static LinkedList<String> atributosDisponibles;
    private LinkedList<String> atributosDisponiblesOriginal;
    private LinkedList<AssignedCompetencesTable> atributosAsignados;

    @PostConstruct
    public void init() {
//        hago una copia de lo que quedó en el paso anterior
        atributosDisponibles = new LinkedList<>(stepSelectPersonController.getAtributosDisponibles());
//        hago otra copia para saber si hubo algun cambio
        atributosDisponiblesOriginal = new LinkedList<>(stepSelectPersonController.getAtributosDisponibles());
        atributosAsignados = new LinkedList<>();
        competenceList = ejbFacade.findAll();

        if (propertiesUtil.isDevelopentMode()) {
            initAtributosAsignados();
        }
    }

    public void clear() {
        atributosDisponibles = new LinkedList<>();
        atributosAsignados = new LinkedList<>();
        atributosDisponiblesOriginal = new LinkedList<>();
        competenceList = new LinkedList<>();
    }

    //    -----------------------------------------METODOS--------------------------------------------

    /**
     * escoge de forma aleatoria los 3 primeros atributos disponibles y los
     * asigna
     */
    private void initAtributosAsignados() {
        int i = 0;
        while (i++ < 3) {
            String atr = getRandomAtribute();
            atributosAsignados.add(new AssignedCompetencesTable(
                    getRandomCompetences(), atr)
            );
            atributosDisponibles.remove(atr);
        }
    }

    /**
     * escoge la cant de competencias de forma aleatoria, despues se escogen
     * las competencias igual de forma aleatoria
     */
    private LinkedList<String> getRandomCompetences() {
        LinkedList<String> result = new LinkedList<>();
        Random r = new SecureRandom();

        int cantTotal = competenceList.size();
        int cantAGenerar = r.nextInt(cantTotal);
        if (cantAGenerar == 0) cantAGenerar += 1;
        int cont = 0;

        while (cont < cantAGenerar) {
            Competence comp = competenceList.get(r.nextInt(cantTotal));
            if (!result.contains(comp.getCompetitionName())) {
                result.add(comp.getCompetitionName());
                cont++;
            }
        }
        return result;
    }

    /**
     * obtiene un atributo de forma aleatoria
     */
    private String getRandomAtribute() {
        Random r = new SecureRandom();
        int cantAtr = atributosDisponibles.size();
        return atributosDisponibles.get(r.nextInt(cantAtr));
    }

    public void removeAtribute(AssignedCompetencesTable toRemove) {
        atributosAsignados.remove(toRemove);
        atributosDisponibles.addFirst(toRemove.getFileAtribute());
    }


    /**
     * se encarga de eliminar el atr seleccionado de los disponibles y
     * añadirlo a los asignados
     */
    public void onDrop(DragDropEvent ddEvent) {
        String atr = ((String) ddEvent.getData());

        atributosAsignados.add(new AssignedCompetencesTable(
                new LinkedList<>(), atr)
        );
        atributosDisponibles.remove(atr);
    }

    /**
     * <p>
     * en caso de que exista algun cambio entre los pasos 2 y 3 en los
     * atributos disponibles, este metodo se encaga de actualizar los valores
     * </p>
     * <p>
     * <ul>
     *     <li>actualiza los elementos disponible</li>
     *     <li>busca en lo que esta asignado si hay alguno en los disponibles,
     *     en caso de que exista, se borra de los disponibles</li>
     *     <li>busca entre los elementos asignados si hay alguno que no exista
     *     entre los disponibles, en caso de que no exista se borra de los
     *     asignados</li>
     * </ul>
     * </p>
     */
    public void update() {
//        actualiza los elementos disponible
        setAtributosDisponibles(stepSelectPersonController.getAtributosDisponibles());
        setAtributosDisponiblesOriginal(stepSelectPersonController.getAtributosDisponibles());

        deleteIfNotExistAsignadosInDisponibles();
        deleteIfExitsAsignadosInDisponibles();
    }

    private void deleteIfNotExistAsignadosInDisponibles() {
        LinkedList<AssignedCompetencesTable> toDelete = new LinkedList<>();
        for (AssignedCompetencesTable value : atributosAsignados) {
            if (!atributosDisponibles.contains(value.getFileAtribute())) {
                toDelete.add(value);
            }
        }
        atributosAsignados.removeAll(toDelete);
    }

    private void deleteIfExitsAsignadosInDisponibles() {
        for (AssignedCompetencesTable value : atributosAsignados) {
            atributosDisponibles.remove(value.getFileAtribute());
        }
    }

    /**
     * para el mensaje de error, devuelve los atributos asignados que no tienen competencia establecida
     */
    public String getAllAssignedAtributesWithOutCompetence() {
        StringBuilder result = new StringBuilder();
        for (AssignedCompetencesTable value : atributosAsignados) {
            if (value.getCompetences().isEmpty()) {
                result.append(value.getFileAtribute()).append(", ");
            }
        }
        String str = result.toString();
        if (str.endsWith(", "))
            str = str.substring(0, str.length() - 2); // se le quita la ultima coma
        return str;
    }

    public LinkedList<String> getCompetencesWithMoreThanOneAtributeAssigned() {
        LinkedList<String> result = new LinkedList<>();

        /*lista que se utiliza como auxiliar para tener el control de que
         * ya existía la competencia antes*/
        LinkedList<String> aux = new LinkedList<>();

        for (AssignedCompetencesTable value : atributosAsignados) {
            if (value.getCompetences() != null && !value.getCompetences().isEmpty()) { // si tiene competencias
                for (String comp : value.getCompetences()) {
                    // si ya se inserto una vez y no se ha insertado en la lista final
                    if (aux.contains(comp) && !result.contains(comp)) {
                        result.add(comp);
                    } else {
                        aux.add(comp);
                    }
                }
            }
        }
        return result;
    }

    /**
     * crea un hashMap con los todas las competencias y los atributos
     * relacionados a esta
     */
    public Map<String, LinkedList<String>> getAllCompetencesWithAllAtributes() {
        Map<String, LinkedList<String>> result = new HashMap<>();
        atributosAsignados.forEach(e -> {
            if (e.getCompetences() != null) {
                e.getCompetences().forEach(c -> {
                    if (result.containsKey(c)){
                        if (!result.get(c).contains(e.getFileAtribute())){
                            result.get(c).add(e.getFileAtribute());
                        }
                    }else {
                        LinkedList<String> list = new LinkedList<>();
                        list.add(e.getFileAtribute());
                        result.put(c, list);
                    }
                });
            }
        });
        return result;
    }


    //    -------------------------------------GET/SET-------------------------------------------------------


    public LinkedList<String> getAtributosDisponiblesOriginal() {
        return atributosDisponiblesOriginal;
    }

    public void setAtributosDisponiblesOriginal(LinkedList<String> atributosDisponiblesOriginal) {
        this.atributosDisponiblesOriginal = atributosDisponiblesOriginal;
    }

    public LinkedList<String> getAtributosDisponibles() {
        return atributosDisponibles;
    }

    public void setAtributosDisponibles(LinkedList<String> atributosDisponibles) {
        this.atributosDisponibles = atributosDisponibles;
    }

    public LinkedList<AssignedCompetencesTable> getAtributosAsignados() {
        return atributosAsignados;
    }

    public void setAtributosAsignados(LinkedList<AssignedCompetencesTable> atributosAsignados) {
        this.atributosAsignados = atributosAsignados;
    }

    public List<Competence> getCompetenceList() {
        return competenceList;
    }

    public void setCompetenceList(List<Competence> competenceList) {
        this.competenceList = competenceList;
    }
}
