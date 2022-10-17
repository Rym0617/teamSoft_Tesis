package org.teamsoft.controller.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;

@FacesConverter("EntityConverter")
public class EntityConverter implements Converter {

	private static final String KEY = "EntityConverter";

	private Map<String, Object> getViewMap(FacesContext context) {
	    Map<String, Object> viewMap = context.getViewRoot().getViewMap();
	    @SuppressWarnings({ "unchecked", "rawtypes" })
	    Map<String, Object> idMap = (Map) viewMap.get(KEY);
	    if (idMap == null) {
	        idMap = new HashMap<>();
	        viewMap.put(KEY, idMap);
	    }
	    return idMap;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (context == null || component == null) { 
			throw new NullPointerException(); 
		} 
	    return getViewMap(context).get(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (context == null || component == null) { 
			throw new NullPointerException(); 
		} 
		if (value != null) {
	    String id = value.toString();
	    getViewMap(context).put(id, value.getClass().cast(value));
	    return id;
		}
	   return "";
	}

}
