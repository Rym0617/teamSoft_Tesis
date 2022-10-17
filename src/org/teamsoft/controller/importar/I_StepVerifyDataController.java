package org.teamsoft.controller.importar;

import org.teamsoft.controller.importar.tableContent.*;
import org.teamsoft.entity.PersonGroup;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;

@ManagedBean
@Named("stepVerifyData")
@SessionScoped
public class I_StepVerifyDataController implements Serializable {

    @Inject
    private I_StepReadFileController readFileController;
    @Inject
    private I_StepSelectPersonController selectPersonController;
    @Inject
    private I_StepSelectCompetenceController selectCompetenceController;
    @Inject
    private I_StepConfigCompetenceController configCompetenceController;
    @Inject
    private I_StepSelectRolesController rolesController;

    private PersonGroup selectedPersonGroup;
    private LinkedList<PersonValueTable> atributosPersona;
    private LinkedList<AssignedCompetencesTable> atributosCompetencias;
    private LinkedList<AssignedAtributeRoleTable> atributosRoles;

    // competencias con mas de un atr asociado
    private Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> competenciaRep;
    // competencias que tienen valor numerico
    private LinkedList<ConfigNumberCompetences> competenciasValorNum;
    // competencias que tienen valor texto
    private Map<String, LinkedList<AssignedAtributeTextValueTable>> competenciasValorTexto;

    /**
     * se encarga de inicializar los datos para la visual
     */
    public void init() {
        selectedPersonGroup = readFileController.getSelectedPersonGroupObject();
        atributosPersona = selectPersonController.getAtributosAsignados();
        atributosCompetencias = selectCompetenceController.getAtributosAsignados();
        atributosRoles = rolesController.getAtributosAsignados();

        competenciaRep = configCompetenceController.getCompetenciaAtrValor();
        competenciasValorNum = configCompetenceController.getAtributosAsignadosNumero();
        competenciasValorTexto = configCompetenceController.getAtributosAsignadosTexto();
    }

    /**
     * limpia los datos para cuando hay un cambio en los datos del fichero
     */
    public void clear() {
        selectedPersonGroup = null;
        atributosPersona = null;
        atributosCompetencias = null;
        atributosRoles = null;
        competenciaRep = null;
        competenciasValorNum = null;
        competenciasValorTexto = null;
    }

//-----------------------------GETTERS/SETTERS-------------------------------------------------


    public Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> getCompetenciaRep() {
        return competenciaRep;
    }

    public void setCompetenciaRep(Map<String, LinkedList<AssignedCompetenceAtributeValueTable>> competenciaRep) {
        this.competenciaRep = competenciaRep;
    }

    public LinkedList<ConfigNumberCompetences> getCompetenciasValorNum() {
        return competenciasValorNum;
    }

    public void setCompetenciasValorNum(LinkedList<ConfigNumberCompetences> competenciasValorNum) {
        this.competenciasValorNum = competenciasValorNum;
    }

    public Map<String, LinkedList<AssignedAtributeTextValueTable>> getCompetenciasValorTexto() {
        return competenciasValorTexto;
    }

    public void setCompetenciasValorTexto(Map<String, LinkedList<AssignedAtributeTextValueTable>> competenciasValorTexto) {
        this.competenciasValorTexto = competenciasValorTexto;
    }

    public PersonGroup getSelectedPersonGroup() {
        return selectedPersonGroup;
    }

    public void setSelectedPersonGroup(PersonGroup selectedPersonGroup) {
        this.selectedPersonGroup = selectedPersonGroup;
    }

    public LinkedList<PersonValueTable> getAtributosPersona() {
        return atributosPersona;
    }

    public void setAtributosPersona(LinkedList<PersonValueTable> atributosPersona) {
        this.atributosPersona = atributosPersona;
    }

    public LinkedList<AssignedCompetencesTable> getAtributosCompetencias() {
        return atributosCompetencias;
    }

    public void setAtributosCompetencias(LinkedList<AssignedCompetencesTable> atributosCompetencias) {
        this.atributosCompetencias = atributosCompetencias;
    }

    public LinkedList<AssignedAtributeRoleTable> getAtributosRoles() {
        return atributosRoles;
    }

    public void setAtributosRoles(LinkedList<AssignedAtributeRoleTable> atributosRoles) {
        this.atributosRoles = atributosRoles;
    }
}
