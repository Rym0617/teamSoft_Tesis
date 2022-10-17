package org.teamsoft.controller;

import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.JsfUtil.PersistAction;
import org.teamsoft.entity.ProjectStructure;
import org.teamsoft.model.ProjectStructureFacade;

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

@Named("projectStructureController")
@SessionScoped
public class ProjectStructureController implements Serializable {

    @EJB
    private ProjectStructureFacade ejbFacade;
    private List<ProjectStructure> items = null;
    private ProjectStructure selected;

    public ProjectStructureController() {
    }

    public ProjectStructure getSelected() {
        return selected;
    }

    public void setSelected(ProjectStructure selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ProjectStructureFacade getFacade() {
        return ejbFacade;
    }

    public ProjectStructure prepareCreate() {
        selected = new ProjectStructure();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("ProjectStructureCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("ProjectStructureUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle_es_ES").getString("ProjectStructureDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ProjectStructure> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public ProjectStructure findByName(String name) {
        return ejbFacade.findByName(name);
    }

    public boolean nameExist(String name) {
        return ejbFacade.nameExist(name);
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
                    if (persistAction != PersistAction.DELETE) {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle_es_ES").getString("PersistenceErrorInsert"));
                    } else {
                        JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle_es_ES").getString("PersistenceErrorDelete"));
                    }
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle_es_ES").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle_es_ES").getString("PersistenceErrorOccured"));
            }
        }
    }

    public ProjectStructure getProjectStructure(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<ProjectStructure> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ProjectStructure> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = ProjectStructure.class)
    public static class ProjectStructureControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProjectStructureController controller = (ProjectStructureController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "projectStructureController");
            return controller.getProjectStructure(getKey(value));
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
            if (object instanceof ProjectStructure) {
                ProjectStructure o = (ProjectStructure) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProjectStructure.class.getName()});
                return null;
            }
        }

    }

}
