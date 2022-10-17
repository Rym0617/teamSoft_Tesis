package org.teamsoft.controller;

import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.JsfUtil.PersistAction;
import org.teamsoft.entity.Competence;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.CompetenceFacade;
import org.teamsoft.model.importar.CompetenceModel;

import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("competenceController")
@SessionScoped
public class CompetenceController implements Serializable {

    @Inject
    private CompetenceFacade ejbFacade;
    @Inject
    LocaleConfig localeConfig;

    private List<Competence> items = null;
    private Competence selected;

    public CompetenceController() {
    }

    public Competence getSelected() {
        return selected;
    }

    public void setSelected(Competence selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CompetenceFacade getFacade() {
        return ejbFacade;
    }

    /**
     * ell csv tiene que ser así:
     * 0-nombre
     * 1-descripcion
     * 2-isTechnical
     * <p>
     * Ej -> inteligente | rol no se que cosa... | true
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile uf = event.getFile(); // se captura el fichero que se selecciona
            File file = getFile(uf); // convierto el fichero al tipo file

//        para que pueda leer las tildes
            InputStream targetStream = new FileInputStream(file);
            Reader readerR = new InputStreamReader(targetStream, StandardCharsets.UTF_16);

            List<CompetenceModel> competenceToCreate = new CsvToBeanBuilder(readerR)
                    .withType(CompetenceModel.class)
                    .withSeparator(',')
                    .build()
                    .parse();

            AtomicBoolean existCompetences = new AtomicBoolean(false);
            List<Competence> competenceToInsert = new LinkedList<>();
            AtomicInteger cantDuplicatedCompetence = new AtomicInteger();

//            se crean los roles
            competenceToCreate.stream().filter(c -> !c.getNombre().isEmpty()).forEach(competenceModel -> {
                if (getFacade().findByName(competenceModel.getNombre()) == null) { // si no existe
                    Competence role = new Competence();
                    role.setCompetitionName(competenceModel.getNombre());
                    role.setDescription(competenceModel.getDescripcion());
                    role.setTechnical(competenceModel.isTechnical());
                    competenceToInsert.add(role);
                    getFacade().create(role);
                } else if (!existCompetences.get()) {
                    existCompetences.set(true);
                    cantDuplicatedCompetence.getAndIncrement();
                } else {
                    cantDuplicatedCompetence.getAndIncrement();
                }
            });

            if (cantDuplicatedCompetence.get() > 1) {
                JsfUtil.addWarningMessage("Se importaron " + competenceToInsert.size() + " competencias y no se importaron " +
                        "debido a que ya existían " + cantDuplicatedCompetence.get() + " competencias");
            } else {
                JsfUtil.addSuccessMessage("Se importaron " + competenceToInsert.size() + " competencias");
            }
        } catch (IOException e) {
            JsfUtil.addErrorMessage(e, localeConfig.getBundleValue("error_reading_file"));
            e.printStackTrace();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Fichero no compatible");
            e.printStackTrace();
        }
    }

    private File getFile(UploadedFile uploadedFile) {
        try {
            Path tmpFile = Files.createTempFile(FilenameUtils.getBaseName(uploadedFile.getFileName()), "." + FilenameUtils.getExtension(uploadedFile.getFileName()));
            Files.copy(uploadedFile.getInputstream(), tmpFile, StandardCopyOption.REPLACE_EXISTING);
            return tmpFile.toFile();
        } catch (IOException e) {
            return null;
        }
    }

    public Competence prepareCreate() {
        selected = new Competence();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("CompetenceCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle_es_ES").getString("CompetenceUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle_es_ES").getString("CompetenceDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Competence> getItems() {
        items = getFacade().findAll();
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

    public Competence getCompetence(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Competence> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Competence> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Competence.class)
    public static class CompetenceControllerConverter implements Converter {

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

}
