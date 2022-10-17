package org.teamsoft.controller;

import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.JsfUtil.PersistAction;
import org.teamsoft.entity.PersonGroup;
import org.teamsoft.model.PersonGroupFacade;

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

@Named("personGroupController")
@SessionScoped
public class PersonGroupController implements Serializable {

    @EJB
    private PersonGroupFacade ejbFacade;
    private List<PersonGroup> items = null;
    private PersonGroup selected;
    private PersonGroup oldSelected;

    public PersonGroupController() {
    }

    public PersonGroup getSelected() {
        return selected;
    }

    public void setSelected(PersonGroup selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PersonGroupFacade getFacade() {
        return ejbFacade;
    }

    public PersonGroup prepareCreate() {
        oldSelected = selected;
        selected = new PersonGroup();
        initializeEmbeddableKey();
        return selected;
    }
    public void close() {
        selected = oldSelected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("PersonGroupCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("PersonGroupUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle_es_ES").getString("PersonGroupDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PersonGroup> getItems() {
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

    public PersonGroup getPersonGroup(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<PersonGroup> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PersonGroup> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PersonGroup.class)
    public static class PersonGroupControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonGroupController controller = (PersonGroupController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personGroupController");
            return controller.getPersonGroup(getKey(value));
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
            if (object instanceof PersonGroup) {
                PersonGroup o = (PersonGroup) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PersonGroup.class.getName()});
                return null;
            }
        }

    }

}
