package org.teamsoft.controller;

import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.entity.Role;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.RoleFacade;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("roleController")
@SessionScoped
public class RoleController implements Serializable {

    @EJB
    private RoleFacade ejbFacade;
    @Inject
    LocaleConfig localeConfig;

    private List<Role> items = null;
    private Role selected;

    public RoleController() {
    }

    public Role getSelected() {
        return selected;
    }

    public void setSelected(Role selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private RoleFacade getFacade() {
        return ejbFacade;
    }

    public Role prepareCreate() {
        selected = new Role();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(JsfUtil.PersistAction.CREATE, localeConfig.getBundleValue("RoleCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, localeConfig.getBundleValue("RoleUpdated"));
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, localeConfig.getBundleValue("RoleDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Role> getItems() {
        items = getFacade().findAll();
        return items;
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    getFacade().edit(selected);
                    JsfUtil.addSuccessMessage(successMessage);
                } else {
                    if (selected != null) {
                        if (selected.getId() != null) {
                            if (!selected.isBoss()) { //no se puede eliminar el jefe de proyecto
                                getFacade().remove(selected);
                                JsfUtil.addSuccessMessage(successMessage);
                            } else {
                                MyJSF_Util.addErrorMessage("El rol jefe de proyecto no debe ser eliminado");
                            }
                        }
                    }
                }
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    if (persistAction != JsfUtil.PersistAction.DELETE) {
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

    public Role getRole(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Role> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Role> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }


    /**
     * verifica si se puede habilitar el check de establecer lider
     *
     * @param isUpdate si se va a realizar la verificación desde la vista update o la vista create
     */
    public boolean disableCheckTeamLeader(boolean isUpdate) {
        boolean isPresent = ejbFacade.findBoss() != null;
        if (!isUpdate) // si está en la vista crear rol
            return isPresent;
        else { // si está en la de editar rol
            if (selected.isBoss()) { // si el rol seleccionado es lider
                return false; // se puede editar
            } else {
                return isPresent;
            }
        }
    }

    @FacesConverter(forClass = Role.class)
    public static class RoleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RoleController controller = (RoleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "roleController");
            return controller.getRole(getKey(value));
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
            if (object instanceof Role) {
                Role o = (Role) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Role.class.getName()});
                return null;
            }
        }

    }

}
