package org.teamsoft.controller;

import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.JsfUtil.PersistAction;
import org.teamsoft.entity.Municipality;
import org.teamsoft.model.MunicipalityFacade;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("municipalityController")
@SessionScoped
public class MunicipalityController implements Serializable {

    @EJB
    private MunicipalityFacade ejbFacade;
    private List<Municipality> items = null;
    private Municipality selected;

    public MunicipalityController() {
    }

    public Municipality getSelected() {
        return selected;
    }

    public void setSelected(Municipality selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MunicipalityFacade getFacade() {
        return ejbFacade;
    }

    public Municipality prepareCreate() {
        selected = new Municipality();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("MunicipalityCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("MunicipalityUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle_es_ES").getString("MunicipalityDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Municipality> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle_es_ES").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle_es_ES").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Municipality getMunicipality(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Municipality> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Municipality> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Municipality.class)
    public static class MunicipalityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MunicipalityController controller = (MunicipalityController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "municipalityController");
            return controller.getMunicipality(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Municipality) {
                Municipality o = (Municipality) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Municipality.class.getName()});
                return null;
            }
        }

    }

}
