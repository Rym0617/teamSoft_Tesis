/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.controller.LevelsController;
import org.teamsoft.controller.RoleCompetitionController;
import org.teamsoft.controller.RoleController;
import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.entity.Competence;
import org.teamsoft.entity.Role;
import org.teamsoft.entity.RoleCompetition;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.CompetenceFacade;
import org.teamsoft.model.RoleFacade;
import org.teamsoft.model.importar.RolModel;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author G1lb3rt & jpinas
 */
@Named("myRoleController")
@SessionScoped
public class MyRoleController implements Serializable {

    @Inject
    RoleController roleController;
    @Inject
    RoleCompetitionController roleCompetition;
    @Inject
    LevelsController levelsController;
    @Inject
    LocaleConfig localeConfig;
    @Inject
    CompetenceFacade competenceFachade;
    @Inject
    RoleFacade roleFacade;


    //    private boolean roleImpact; // adaptador para convertir boolean a 0 y 1
    private List<Competence> genericCompetences; //competencias genéricas

    private List<RoleCompetition> roleCompetitionList; //competencias a persistir
    private List<RoleCompetition> roleCompetitionRemoveList; //competencias a quitar de la lista
    private List<Role> incompatibility = null; //para almacenar las incompatibilidades

    private List<Role> posibleIncompatibilities = null;

    public String prepareCreate() {
        roleController.prepareCreate();
        roleCompetition.prepareCreate();
        levelsController.prepareCreate();

        genericCompetences = competenceFachade.getGenericCompetences();
        roleCompetitionList = new ArrayList<>();
        roleCompetitionRemoveList = new ArrayList<>();
        incompatibility = new ArrayList<>();

        return "role-create";
    }


    /**
     * ell csv tiene que ser así:
     * 0-nombre rol
     * 1-descripcion
     * 2-impacto
     * 3-incompatibilidades separados por coma (estos roles tienen q existir en el mismo fichero)
     * <p>
     * Ej -> Jefe | rol no se que cosa... | 0.2 | Limpiador, Albañil
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {

            UploadedFile uf = event.getFile(); // se captura el fichero que se selecciona
            File file = getFile(uf); // convierto el fichero al tipo file

//        para que pueda leer las tildes
            InputStream targetStream = new FileInputStream(file);
            Reader readerR = new InputStreamReader(targetStream, StandardCharsets.UTF_16);

            List<RolModel> rolesToCreate = new CsvToBeanBuilder(readerR)
                    .withType(RolModel.class)
                    .withSeparator(',')
                    .build()
                    .parse();

            AtomicBoolean existRoles = new AtomicBoolean(false);
            List<Role> rolesToInser = new LinkedList<>();
            AtomicInteger cantDuplicatedRole = new AtomicInteger();

//            se crean los roles
            rolesToCreate.stream().filter(r -> !r.getNombre().isEmpty()).forEach(rolModel -> {
                if (roleFacade.findByName(rolModel.getNombre()) == null) { // si no existe
                    Role role = new Role();
                    role.setRoleName(rolModel.getNombre());
                    role.setRoleDesc(rolModel.getDescripcion());
                    role.setImpact(rolModel.getImpacto());
                    rolesToInser.add(role);
                    roleFacade.create(role);
                } else if (!existRoles.get()) {
                    existRoles.set(true);
                    cantDuplicatedRole.getAndIncrement();
                } else {
                    cantDuplicatedRole.getAndIncrement();
                }
            });
//          se establecen las incompatibilidades
            rolesToCreate.stream().filter(rolFilter -> !rolFilter.getNombre().isEmpty()).forEach(rolModel -> {
                String[] roleInc = rolModel.getIncompatibleRoles();
                Role roleEntity = roleFacade.findByName(rolModel.getNombre());
                for (String s : roleInc) {
                    Role rolToSetIncompatible = roleFacade.findByName(s.trim());
                    roleEntity.getRoleList().add(rolToSetIncompatible);
                }
                roleFacade.edit(roleEntity);
            });

            if (cantDuplicatedRole.get() > 1) {
                JsfUtil.addWarningMessage(localeConfig.getBundleValue("where_imported") + rolesToInser.size() +
                        " " + localeConfig.getBundleValue("roles_and_not_imported") +
                        localeConfig.getBundleValue("because_alredy_exist") + cantDuplicatedRole + " roles");
            } else {
                JsfUtil.addSuccessMessage(localeConfig.getBundleValue("where_imported") + rolesToInser.size() + " roles");
            }
        } catch (IOException e) {
            JsfUtil.addErrorMessage(e, localeConfig.getBundleValue("error_reading_file"));
            e.printStackTrace();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(localeConfig.getBundleValue("unsupported_file"));
            e.printStackTrace();
        }
    }

    private File getFile(UploadedFile uploadedFile) {
        try {
            Path tmpFile = Files.createTempFile(FilenameUtils.getBaseName(uploadedFile.getFileName()), "." + FilenameUtils.getExtension(uploadedFile.getFileName()));
            Files.copy(uploadedFile.getInputstream(), tmpFile, StandardCopyOption.REPLACE_EXISTING);
            return tmpFile.toFile();
        } catch (IOException e) {
            return null;
        }
    }

    public String prepareEdit() {
        String toReturn = "";
        Role selected = roleController.getSelected();

        if (selected != null) {
            if (selected.getId() != null) {
                roleCompetition.prepareCreate();
                levelsController.prepareCreate();

                genericCompetences = competenceFachade.getGenericCompetences();
                roleCompetitionList = selected.getRoleCompetitionList();
                roleCompetitionRemoveList = new ArrayList<>();
                incompatibility = selected.getRoleList();

                //quitar de la lista de competencias elegibles las que fueron previamente asignadas al rol 
                for (RoleCompetition item : roleCompetitionList) {
                    for (int i = 0; i < genericCompetences.size(); i++) {
                        if (item.getCompetenceFk().getId().equals(genericCompetences.get(i).getId())) {
                            genericCompetences.remove(i);
                        }
                    }
                }

                //quitar de la lista de roles incompatibles seleccionables el que se modifica actualmente
                posibleIncompatibilities = roleController.getItemsAvailableSelectOne();
                int i = 0;
                boolean found = false;
                while (i < posibleIncompatibilities.size() && !found) {
                    if (posibleIncompatibilities.get(i).getId().equals(selected.getId())) {
                        posibleIncompatibilities.remove(i);
                        found = true;
                    }
                    i++;
                }
                toReturn = "role-edit";
            } else {
                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("select_table_record"));
            }
        } else {
            MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("select_table_record"));
        }

        return toReturn;
    }

    /**
     * Para manejar la activasion/desactivasion del boton adicionar competencia
     *
     * @return
     */
    public boolean handleAddButton_Listener() {
        boolean active = true;

        if (roleCompetition.getSelected().getCompetenceFk() != null && !genericCompetences.isEmpty()) {
            if (roleCompetition.getSelected().getCompetenceFk().getId() != null && roleCompetition.getSelected().getCompImportanceFk() != null && roleCompetition.getSelected().getLevelsFk() != null) {
                active = false;
            }
        }

        return active;
    }

    /**
     * Para añadir la competencia seleccionada a la lista
     */
    public void addGenComp_Listener() {

        RoleCompetition rc = new RoleCompetition(); // nuevo puntero para evitar que en al insertar solo se actualicen por referencia los objetos
        rc.setLevelsFk(roleCompetition.getSelected().getLevelsFk());
        rc.setCompetenceFk(roleCompetition.getSelected().getCompetenceFk());
        rc.setCompImportanceFk(roleCompetition.getSelected().getCompImportanceFk());
        rc.setRolesFk(roleController.getSelected());

        roleCompetitionList.add(rc); //agrego los datos de la nueva competencia (rolCompetence) del rol a la lista

        genericCompetences.remove(roleCompetition.getSelected().getCompetenceFk()); // quito la competencia seleccionada de la lista de comp. elegibles

        //Se limpian los valores de los selectores
        roleCompetition.getSelected().setLevelsFk(null);
        roleCompetition.getSelected().setCompetenceFk(null);
        roleCompetition.getSelected().setCompImportanceFk(null);
    }

    /**
     * Quitar elementos seleccionados de la tabla
     */
    public void removeGenCompFromTable_Listener() {

        if (!roleCompetitionRemoveList.isEmpty()) {
            for (RoleCompetition item : roleCompetitionRemoveList) {
                for (int i = 0; i < roleCompetitionList.size(); i++) {
                    if (item.getCompetenceFk().getId().equals(roleCompetitionList.get(i).getCompetenceFk().getId())) {
                        roleCompetitionList.remove(i);// elimino el objeto de la fuente de datos de la tabla
                        genericCompetences.add(item.getCompetenceFk()); //devuelvo el elemento a la lista de competencias disponibles
                    }
                }
            }
            roleCompetitionRemoveList.clear(); // una vez eliminados los elementos limpio la lista
        }
    }

    /**
     * Para persistir el rol con sus dependencias
     */
    public void create() {

        roleController.getSelected().setRoleList(incompatibility);
        roleController.getSelected().setRoleCompetitionList(roleCompetitionList);

        if (!roleCompetitionList.isEmpty()) {
            roleController.create();
        } else {
            MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("role_need_competences_assigned"));
        }
        if (FacesContext.getCurrentInstance().getMaximumSeverity() != null) {
            if (FacesContext.getCurrentInstance().getMaximumSeverity().equals(FacesMessage.SEVERITY_INFO)) {
                prepareCreate();
            }
        }
    }

    public List<Competence> getGenericCompetences() {
        return genericCompetences;
    }

    public void setGenericCompetences(List<Competence> genericCompetences) {
        this.genericCompetences = genericCompetences;
    }

    public List<RoleCompetition> getRoleCompetitionRemoveList() {
        return roleCompetitionRemoveList;
    }

    public void setRoleCompetitionRemoveList(List<RoleCompetition> roleCompetitionRemoveList) {
        this.roleCompetitionRemoveList = roleCompetitionRemoveList;
    }

    public List<RoleCompetition> getRoleCompetitionList() {
        return roleCompetitionList;
    }

    public void setRoleCompetitionList(List<RoleCompetition> roleCompetitionList) {
        this.roleCompetitionList = roleCompetitionList;
    }

    public List<Role> getIncompatibility() {
        return incompatibility;
    }

    public void setIncompatibility(List<Role> incompatibility) {
        this.incompatibility = incompatibility;
    }

    public List<Role> getPosibleIncompatibilities() {
        return posibleIncompatibilities;
    }

    public void setPosibleIncompatibilities(List<Role> posibleIncompatibilities) {
        this.posibleIncompatibilities = posibleIncompatibilities;
    }

    public void importar() {
    }
}
