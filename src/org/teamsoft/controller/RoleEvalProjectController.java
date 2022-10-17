package org.teamsoft.controller;

import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.JsfUtil.PersistAction;
import org.teamsoft.entity.RoleEvalProject;
import org.teamsoft.model.RoleEvalProjectFacade;

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

@Named("roleEvalProjectController")
@SessionScoped
public class RoleEvalProjectController implements Serializable {

    @EJB
    private RoleEvalProjectFacade ejbFacade;
    private List<RoleEvalProject> items = null;
    private RoleEvalProject selected;

    public RoleEvalProjectController() {
    }

    public RoleEvalProject getSelected() {
        return selected;
    }

    public void setSelected(RoleEvalProject selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private RoleEvalProjectFacade getFacade() {
        return ejbFacade;
    }

    public RoleEvalProject prepareCreate() {
        selected = new RoleEvalProject();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("RoleEvalProjectCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("RoleEvalProjectUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle_es_ES").getString("RoleEvalProjectDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<RoleEvalProject> getItems() {
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

    public RoleEvalProject getRoleEvalProject(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<RoleEvalProject> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<RoleEvalProject> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = RoleEvalProject.class)
    public static class RoleEvalProjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RoleEvalProjectController controller = (RoleEvalProjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "roleEvalProjectController");
            return controller.getRoleEvalProject(getKey(value));
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
            if (object instanceof RoleEvalProject) {
                RoleEvalProject o = (RoleEvalProject) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), RoleEvalProject.class.getName()});
                return null;
            }
        }

    }

}
