/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.myConverters;

import org.teamsoft.controller.CompetenceController;
import org.teamsoft.entity.Competence;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author G1lb3rt
 */

@FacesConverter(value = "cmpConverter")
public  class CmpConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CompetenceController controller = (CompetenceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "competenceController");
            return controller.getCompetence(getKey(value));
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
            if (object instanceof Competence) {
                Competence o = (Competence) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Competence.class.getName()});
                return null;
            }
        }

    }
