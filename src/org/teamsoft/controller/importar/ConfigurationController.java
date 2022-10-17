package org.teamsoft.controller.importar;


import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.PropertiesUtil;
import org.teamsoft.locale.LocaleConfig;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author jpinas
 */
@Named("configurationController")
@SessionScoped
public class ConfigurationController implements Serializable {

    @Inject
    LocaleConfig localeConfig;
    @Inject
    PropertiesUtil propertiesUtil;

    float puntoCorte; // valor establecido por el slider
    int maxExpValue;
    boolean updateIfExist;
    boolean updateIfExistMemory;
    boolean onUpdateDeleteOldValues;
    boolean onUpdateDeleteOldValuesMemory;

    public void init() {
        puntoCorte = propertiesUtil.getPtoCorte();
        maxExpValue = propertiesUtil.getMaximoAnoExperiencia();
        updateIfExist = propertiesUtil.isUpdateIfExist();
        onUpdateDeleteOldValues = propertiesUtil.isDeleteOldValues();
    }

    public void save() {
        try {
            propertiesUtil.setProperties("anos_maximo_experiencia", String.valueOf(maxExpValue), false);
            propertiesUtil.setProperties("if_exist_update", String.valueOf(updateIfExist), false);
            propertiesUtil.setProperties("on_update_delete_old_values", String.valueOf(onUpdateDeleteOldValues), false);
            propertiesUtil.setProperties("punto_corte", String.valueOf(puntoCorte), true);
            JsfUtil.addSuccessMessage(localeConfig.getBundleValue("mensaje_modificado_exito"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e.getLocalizedMessage());
            System.out.println("");
        }
    }

    public void cancel() {
        puntoCorte = propertiesUtil.getPtoCorte();
        maxExpValue = propertiesUtil.getMaximoAnoExperiencia();
        updateIfExist = propertiesUtil.isUpdateIfExist();
        onUpdateDeleteOldValues = propertiesUtil.isDeleteOldValues();
    }

//    ------------------------------------GET/SET--------------------------------------------


    public boolean isUpdateIfExistMemory() {
        return updateIfExistMemory;
    }

    public void setUpdateIfExistMemory(boolean updateIfExistMemory) {
        this.updateIfExistMemory = updateIfExistMemory;
    }

    public boolean isOnUpdateDeleteOldValuesMemory() {
        return onUpdateDeleteOldValuesMemory;
    }

    public void setOnUpdateDeleteOldValuesMemory(boolean onUpdateDeleteOldValuesMemory) {
        this.onUpdateDeleteOldValuesMemory = onUpdateDeleteOldValuesMemory;
    }

    public boolean isUpdateIfExist() {
        return updateIfExist;
    }

    public void setUpdateIfExist(boolean updateIfExist) {
        this.updateIfExist = updateIfExist;
    }

    public boolean isOnUpdateDeleteOldValues() {
        return onUpdateDeleteOldValues;
    }

    public void setOnUpdateDeleteOldValues(boolean onUpdateDeleteOldValues) {
        this.onUpdateDeleteOldValues = onUpdateDeleteOldValues;
    }

    public float getPuntoCorte() {
        return puntoCorte;
    }

    public void setPuntoCorte(float puntoCorte) {
        this.puntoCorte = puntoCorte;
    }

    public int getMaxExpValue() {
        return maxExpValue;
    }

    public void setMaxExpValue(int maxExpValue) {
        this.maxExpValue = maxExpValue;
    }
}
