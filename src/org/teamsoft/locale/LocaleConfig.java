/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.locale;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author jpinas
 */
@Named("localeBean")
@ManagedBean
@SessionScoped
public class LocaleConfig implements Serializable {

    // idiomas soportados
    private static final Locale ENGLISH = new Locale("en", "US");
    private static final Locale SPANISH = new Locale("es", "ES");
    private Locale language;

    @PostConstruct
    public void init() {
        language = SPANISH; // se establece el español como idioma por defecto
        FacesContext.getCurrentInstance().getViewRoot().setLocale(language); // esto es para que la visual lo coja
    }

    public Locale getLanguage() {
        return (language);
    }

    public void setLanguage(String str) {
        if (str.equals("en")) {
            language = ENGLISH;
        } else {
            language = SPANISH;
        }
        FacesContext.getCurrentInstance().getViewRoot().setLocale(language);
    }

    /**
     * obtiene el recurso en funcion del idioma establecido en el sitema
     * como por defecto
     */
    public String getBundleValue(String key) {
        return ResourceBundle.getBundle("/Bundle_" + getCurrentLocaleTag()).getString(key);
    }

    /**
     * me devuelve la etiqueta del idioma actua
     * <p>
     * Ej. es_ES si es el español, en_US si es el ingles
     */
    public String getCurrentLocaleTag() {
        return language.toString();
    }
}
