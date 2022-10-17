/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package org.teamsoft.myConverters;

import org.teamsoft.controller.ProjectRolesController;
import org.teamsoft.entity.ProjectRoles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yoyo
 */
@FacesConverter(value = "ProjectRolesConverter")
    public class ProjectRolesConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProjectRolesController controller = (ProjectRolesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "projectRolesController");
            return controller.getProjectRoles(getKey(value));
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
            if (object instanceof ProjectRoles) {
                ProjectRoles o = (ProjectRoles) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ProjectRoles.class.getName()});
                return null;
            }
        }

    }