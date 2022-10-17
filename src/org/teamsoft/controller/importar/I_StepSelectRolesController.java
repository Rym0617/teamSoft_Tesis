package org.teamsoft.controller.importar;


import org.primefaces.event.DragDropEvent;
import org.teamsoft.controller.importar.tableContent.AssignedAtributeRoleTable;
import org.teamsoft.controller.util.I_StepUtil;
import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.PropertiesUtil;
import org.teamsoft.entity.Role;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.RoleFacade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/**
 * @author jpinas
 */
@ManagedBean
@Named("stepSelectRolesController")
@SessionScoped
public class I_StepSelectRolesController implements Serializable {

    @EJB
    RoleFacade ejbFacade;
    @Inject
    LocaleConfig localeConfig;
    @Inject
    PropertiesUtil propertiesUtil;
    @Inject
    I_StepUtil util;
    @Inject
    I_StepSelectCompetenceController selectCompetenceController;

    private LinkedList<String> atributosDisponibles; // lista de los atributos que no han sido asignados

    // lista de los atributos asignados como roles
    private LinkedList<AssignedAtributeRoleTable> atributosAsignados;

    //tengo qesta copia para conocer cuando se haga algun cambio en el paso anterior
    private LinkedList<String> atributosDisponiblesOriginal;

    // rol seleccionado en la tabla de roles asignados
    private AssignedAtributeRoleTable selectedRole;

    // lista que contiene todos los roles disponibles
    private List<Role> roles;

    // lista de los roles a mostrar
    private List<Role> rolesAMostrar;

    // almacena el valor del spinner
    private int anosMaxim;

    @PostConstruct
    public void init() {
        // se crea una nueva referencia al objeto. todos los cambios se hacen sobre esta lista
        atributosDisponibles = new LinkedList<>(selectCompetenceController.getAtributosDisponibles());

        // esta lista no se modifica
        atributosDisponiblesOriginal = new LinkedList<>(selectCompetenceController.getAtributosDisponibles());
        anosMaxim = propertiesUtil.getMaximoAnoExperiencia();
        atributosAsignados = new LinkedList<>();
        roles = ejbFacade.findAll();
        rolesAMostrar = new LinkedList<>(roles);

        if (propertiesUtil.isDevelopentMode()) {
            initAtributosAsignados();
        }
    }

    /**
     * inicializa los atributos asignados con los 3 primeros de forma aleatoria
     */
    public void initAtributosAsignados() {
        int i = 0;
        while (i++ < 3) {
            String atr = getRandomAtr();
            Role rol = getRandomRole();
            atributosAsignados.add(new AssignedAtributeRoleTable(
                    rol, atr)
            );
            atributosDisponibles.remove(atr);
        }
    }

    /**
     * obtiene un rol aleatorio distinto de los que ya estan asignados
     */
    private Role getRandomRole() {
        Random r = new SecureRandom();
        int cantTotal = roles.size();
        Role toAdd;
        int cont = 0;
        do {
            cont++;
            toAdd = roles.get(r.nextInt(cantTotal));
        } while (isRoleAssigned(toAdd) && cont < 10);
        if (cont == 10) {
            toAdd = null;
        }
        return toAdd;
    }

    /**
     * obtiene un atributo de los disponibles de forma aleatoria
     */
    private String getRandomAtr() {
        Random r = new SecureRandom();
        int cantTotal = atributosDisponibles.size();
        boolean isNumber;
        String result = "";
        do {
            result = atributosDisponibles.get(r.nextInt(cantTotal));
            isNumber = util.isNumber(util.getClassByAtribute(result));
        } while (!isNumber);

        return result;
    }

    public void onDrop(DragDropEvent ddEvent) {
        String atr = ((String) ddEvent.getData()); // obtengo el atr a anadir como rol
        atributosAsignados.addFirst(new AssignedAtributeRoleTable(null, atr));
        atributosDisponibles.remove(atr);
    }

    public void clear() {
        atributosDisponibles = new LinkedList<>();
        atributosAsignados = new LinkedList<>();
        atributosDisponiblesOriginal = new LinkedList<>();
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
        setAtributosDisponibles(selectCompetenceController.getAtributosDisponibles());
        setAtributosDisponiblesOriginal(selectCompetenceController.getAtributosDisponibles());

        deleteIfNotExistAsignadosInDisponibles();
        deleteIfExitsAsignadosInDisponibles();

    }

    /**
     * elimina aquellos atributos que estan asignados que no estan
     * como disponibles
     */
    private void deleteIfNotExistAsignadosInDisponibles() {
        LinkedList<AssignedAtributeRoleTable> toDelete = new LinkedList<>();
        atributosAsignados.stream().
                filter(item -> !atributosDisponibles.contains(item.getFileAtribute())).
                forEach(toDelete::add);
        atributosAsignados.removeAll(toDelete);
    }

    /**
     * elimina de los disponibles aquellos que estan ya asignados
     */
    private void deleteIfExitsAsignadosInDisponibles() {
        atributosAsignados.forEach(e -> atributosDisponibles.remove(e.getFileAtribute()));
    }

    public void removeAtribute(AssignedAtributeRoleTable toRemove) {
        atributosAsignados.remove(toRemove);
        atributosDisponibles.addFirst(toRemove.getFileAtribute());
    }

    public void checkRoleSelected() {
        boolean match = atributosAsignados.stream().anyMatch(e -> atributosAsignados.stream().anyMatch(
                f -> !f.getFileAtribute().equals(e.getFileAtribute()) && f.getRol() != null && e.getRol() != null &&
                        f.getRol().equals(e.getRol())
                )
        );
        if (match) {
            JsfUtil.addErrorMessage(localeConfig.getBundleValue("duplicated_role"));
        }
    }

    private boolean isRoleAssigned(Role roleToCheck) {
        return atributosAsignados.stream().
                anyMatch(e -> {
                    if (e.getRol() != null) {
                        return e.getRol().equals(roleToCheck);
                    }
                    return false;
                });
    }

    /**busca si existe algún atributo que no tiene asignado ningún rol*/
    boolean isAnyAtributeWithOutRoleAssigned(){
        return atributosAsignados.stream().anyMatch(e -> e.getRol() == null);
    }

//    -----------------------------METODOS-----------------------------------


    public List<Role> getRolesAMostrar() {
        return rolesAMostrar;
    }

    public void setRolesAMostrar(List<Role> rolesAMostrar) {
        this.rolesAMostrar = rolesAMostrar;
    }

    public I_StepSelectCompetenceController getSelectCompetenceController() {
        return selectCompetenceController;
    }

    public void setSelectCompetenceController(I_StepSelectCompetenceController selectCompetenceController) {
        this.selectCompetenceController = selectCompetenceController;
    }

    public LinkedList<String> getAtributosDisponibles() {
        return atributosDisponibles;
    }

    public void setAtributosDisponibles(LinkedList<String> atributosDisponibles) {
        this.atributosDisponibles = atributosDisponibles;
    }

    public LinkedList<AssignedAtributeRoleTable> getAtributosAsignados() {
        return atributosAsignados;
    }

    public void setAtributosAsignados(LinkedList<AssignedAtributeRoleTable> atributosAsignados) {
        this.atributosAsignados = atributosAsignados;
    }

    public LinkedList<String> getAtributosDisponiblesOriginal() {
        return atributosDisponiblesOriginal;
    }

    public void setAtributosDisponiblesOriginal(LinkedList<String> atributosDisponiblesOriginal) {
        this.atributosDisponiblesOriginal = atributosDisponiblesOriginal;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public int getAnosMaxim() {
        return anosMaxim;
    }

    public void setAnosMaxim(int anosMaxim) {
        this.anosMaxim = anosMaxim;
    }

    public AssignedAtributeRoleTable getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(AssignedAtributeRoleTable selectedRole) {
        this.selectedRole = selectedRole;
    }

    public void selectRole(AssignedAtributeRoleTable atr) {
        selectedRole = atr;
    }
}
