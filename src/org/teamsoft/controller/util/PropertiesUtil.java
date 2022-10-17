package org.teamsoft.controller.util;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

@Named("propertiesUtil")
@ManagedBean
@SessionScoped
public class PropertiesUtil implements Serializable {

    private Properties properties;

    /**
     * verifica que el obj properties no sea null, si es null
     * lo lee y lo devuelve, sino devuelve la instancia ya creada
     */
    public Properties getProperty() {
        try {
            if (properties == null) {
                properties = new Properties();
                properties.load(Thread.
                        currentThread().
                        getContextClassLoader().
                        getResourceAsStream("importar.properties")
                );
            }
        } catch (Exception e) {

        }
        return properties;
    }

    /**
     * salva el fichero de las propiedades, busca el fichero con el nombre importar.properties y escribe en
     * él las propiedades seleccionadas
     */
    public void save() throws IOException, URISyntaxException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("src/importar.properties");
        URI uri = path.toURI();
        String p = uri.getPath();
        OutputStream outputStream = new FileOutputStream(p);
        properties.store(outputStream, null);
        outputStream.close();
    }

    /**
     * obtinen el valor que almacena el parametro
     */
    public String getValue(String key) {
        return getProperty().getProperty(key);
    }

    /**
     * establece la propiedad para esa llave y se salva en caso de que se
     * quiera salvar
     *
     * @param key   llave a cambiar el valor
     * @param value valor a establecer para esa llave
     * @param save  si se salva el fichero de conf
     */
    public void setProperties(String key, String value, boolean save) throws IOException, URISyntaxException {
        getProperty().setProperty(key, value);
        if (save) save();
    }

    /**
     * busca en el fichero de configuracion el valor establecido
     * para considerar un rol como preferido
     */
    public float getPtoCorte() {
        return Float.parseFloat(getProperty().getProperty("punto_corte"));
    }

    /**
     * busca en el fichero de configuracion el valor establecido
     * para clasificar el maximo de anos de experiencia
     */
    public int getMaximoAnoExperiencia() {
        return Integer.parseInt(
                getProperty().getProperty("anos_maximo_experiencia")
        );
    }

    /**
     * para agilizar el proceso de desarrollo con las pruebas e inicializacion de datos
     */
    public boolean isDevelopentMode() {
        return Boolean.parseBoolean(
                getProperty().getProperty("development_mode")
        );
    }

    public boolean isUpdateIfExist() {
        return Boolean.parseBoolean(
                getProperty().getProperty("if_exist_update")
        );
    }

    public boolean isDeleteOldValues() {
        return Boolean.parseBoolean(
                getProperty().getProperty("on_update_delete_old_values")
        );
    }

    public boolean insertGenericCompetencesAutomaticly() {
        return Boolean.parseBoolean(getProperty().getProperty("generate_generic_competences"));
    }
}
