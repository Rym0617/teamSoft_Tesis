/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myBeans;

import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.controller.CompetenceController;
import org.teamsoft.controller.CompetenceDimensionController;
import org.teamsoft.controller.LevelsController;
import org.teamsoft.entity.Competence;
import org.teamsoft.entity.CompetenceDimension;
import org.teamsoft.entity.Levels;
import org.teamsoft.locale.LocaleConfig;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G1lb3rt & jpinas
 */
@Named("myCompetenceController")
@SessionScoped
public class MyCompetenceController implements Serializable {

    @Inject
    private CompetenceController competenceController;
    @Inject
    private LevelsController levelsController;
    @Inject
    private CompetenceDimensionController competenceDimensionController;
    @Inject
    private LocaleConfig localeConfig;

    private String dim = "";  // campo "descripcion" del formulario

    private List<CompetenceDimension> dimensions;//guarda las dimensiones que se van insertando
    private List<CompetenceDimension> dimensionsDelete; //almacena las dimensiones que seran eliminadas

    // activar/desactivar boton add dimension
    private boolean diableAddDimButton = true;

    public String prepareCreate() {
        competenceDimensionController.prepareCreate();
        levelsController.prepareCreate();
        competenceController.prepareCreate();
        dim = "";
        dimensions = new ArrayList<>();

        return "competence-create";
    }

    public String prepareEdit() {
        String toReturn = "";

        Competence selected = competenceController.getSelected();
        if (selected != null) {
            if (selected.getId() != null) {

                competenceDimensionController.prepareCreate();
                levelsController.prepareCreate();

                dimensions = new ArrayList<>();
                dimensions.addAll(selected.getCompetenceDimensionList());

                toReturn = "competence-edit";
            } else {
                MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("select_table_record"));
            }
        } else {
            MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("select_table_record"));
        }

        return toReturn;
    }

    /**
     * Persistir Competencia con sus dependencias
     */
    public void create() {

        if (validateCreate()) {
            competenceController.getSelected().setCompetenceDimensionList(dimensions); //agrego dependencias a la competencia que se crea
            competenceController.create();
            if (FacesContext.getCurrentInstance().getMaximumSeverity() != null) {
                if (FacesContext.getCurrentInstance().getMaximumSeverity().equals(FacesMessage.SEVERITY_INFO)) {
                    prepareCreate();
                }
            }
        } else {
            MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("lvlValidationFailed"));
        }
    }

    /**
     * Para validar que cada nivel tenga asignada al menos una coducta
     * observable por nivel
     *
     * @return
     */
    public boolean validateCreate() {
        List<Levels> levelsList = levelsController.getItemsAvailableSelectOne();
        int i = 0;
        boolean isValid = true;

        while (i < levelsList.size() && isValid) {    //Por cada nivel
            boolean find = false;
            int j = 0;
            while (j < dimensions.size() && !find) { //Por cada dimension insertada
                if (dimensions.get(j).getLevelFk().getId() != null) {
                    if (dimensions.get(j).getLevelFk().getId().equals(levelsList.get(i).getId())) { // Fue asignado el nivel?
                        find = true;
                    } else {
                        j++;
                    }
                }
            }
            if (find) {
                i++;
            } else {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Para insertar elementos en la tabla de dimensiones siempre que el
     * registro sea valido y no este duplicado
     */
    public void addDimension_Listener() {
        boolean duplicate = false;

        if (dim.isEmpty()) { //si la dimension esta en blanco
            MyJSF_Util.addWarningMessage(localeConfig.getBundleValue("noDimMessageBody"));
        } else {

            for (CompetenceDimension item : dimensions) { //si esta repetida
                if (item.getName().equals(dim) && item.getLevelFk().getLevels() == levelsController.getSelected().getLevels()) {
                    MyJSF_Util.addErrorMessage(localeConfig.getBundleValue("duplicateDimInsertion"));
                    duplicate = true;
                }
            }

            if (!duplicate) { //inserto si es valida
                CompetenceDimension cd = new CompetenceDimension();
                cd.setName(dim);
                cd.setCompetenceFk(competenceController.getSelected());
                cd.setLevelFk(levelsController.getSelected());
                dimensions.add(cd);
                dim = ""; //borro el nombre para limpiar el campo de la GUI
            }

        }
    }

    /**
     * Maneja la eliminacion de elementos de la tabla de dimensiones
     *
     */
    public void delDimension_Listener() {

        for (CompetenceDimension item : dimensionsDelete) {
            for (int i = 0; i < dimensions.size(); i++) {
                if (dimensions.get(i).getName().equals(item.getName()) && dimensions.get(i).getLevelFk().getLevels() == item.getLevelFk().getLevels()) {
                    dimensions.remove(i);
                }
            }
        }
        dimensionsDelete.clear(); //borrados todos los elementos seleccionaos limpio el buffer
    }

    /**
     * Para activar/desactivar el voton se insercion
     */
    public void handleAddDimButton_Listener() {
        diableAddDimButton = true;
        if (levelsController.getSelected() != null && !dim.equals("")) {
            diableAddDimButton = false;
        }
    }

    public List<CompetenceDimension> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<CompetenceDimension> dimensions) {
        this.dimensions = dimensions;
    }

    public List<CompetenceDimension> getDimensionsDelete() {
        return dimensionsDelete;
    }

    public void setDimensionsDelete(List<CompetenceDimension> dimensionsDelete) {
        this.dimensionsDelete = dimensionsDelete;
    }

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }

    public boolean isDiableAddDimButton() {
        return diableAddDimButton;
    }

    public void setDiableAddDimButton(boolean diableAddDimButton) {
        this.diableAddDimButton = diableAddDimButton;
    }

    public String prepareCancel() {
        return "/manageCompetences/competence/Edit.xhtml";
    }

}
