package org.teamsoft.controller;

import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.JsfUtil.PersistAction;
import org.teamsoft.entity.ProjectTechCompetence;
import org.teamsoft.model.ProjectTechCompetenceFacade;

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

@Named("projectTechCompetenceController")
@SessionScoped
public class ProjectTechCompetenceController implements Serializable {

    @EJB
    private ProjectTechCompetenceFacade ejbFacade;
    private List<ProjectTechCompetence> items = null;
    private ProjectTechCompetence selected;

    public ProjectTechCompetenceController() {
    }

    public ProjectTechCompetence getSelected() {
        return selected;
    }

    public void setSelected(ProjectTechCompetence selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProjectTechCompetenceFacade getFacade() {
        return ejbFacade;
    }

    public ProjectTechCompetence prepareCreate() {
        selected = new ProjectTechCompetence();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("ProjectTechCompetenceCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("ProjectTechCompetenceUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle_es_ES").getString("ProjectTechCompetenceDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ProjectTechCompetence> getItems() {
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

    public ProjectTechCompetence getProjectTechCompetence(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ProjectTechCompetence> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ProjectTechCompetence> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ProjectTechCompetence.class)
    public static class ProjectTechCompetenceControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProjectTechCompetenceController controller = (ProjectTechCompetenceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "projectTechCompetenceController");
            return controller.getProjectTechCompetence(getKey(value));
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
            if (object instanceof ProjectTechCompetence) {
                ProjectTechCompetence o = (ProjectTechCompetence) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProjectTechCompetence.class.getName()});
                return null;
            }
        }

    }

}
