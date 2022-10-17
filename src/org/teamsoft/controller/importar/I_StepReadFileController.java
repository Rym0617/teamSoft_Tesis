package org.teamsoft.controller.importar;


import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.teamsoft.controller.util.I_StepUtil;
import org.teamsoft.controller.util.JsfUtil;
import org.teamsoft.controller.util.PropertiesUtil;
import org.teamsoft.entity.PersonGroup;
import org.teamsoft.exceptions.NotHomogeneousFile;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.PersonGroupFacade;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.*;


/**
 * @author jpinas
 */
@ManagedBean
@Named("stepFileUploadController")
@SessionScoped
public class I_StepReadFileController implements Serializable {

    @Inject
    PersonGroupFacade ejbFacade; // para realizar las consultas a la bd
    @Inject
    I_StepSelectPersonController stepAtributosPersonaController;
    @Inject
    I_StepSelectCompetenceController stepSelectCompetencesController;
    @Inject
    I_StepConfigCompetenceController stepConfigCompetenceController;
    @Inject
    I_StepSelectRolesController stepSelectRolesController;
    @Inject
    I_StepVerifyDataController stepVerifyData;
    @Inject
    LocaleConfig localeConfig;
    @Inject
    PropertiesUtil propertiesUtil;
    @Inject
    I_StepUtil util;

    static LinkedList<String> encabezadoList = new LinkedList<>(); // encabezado del fichero
    static List<String[]> dataList = new LinkedList<>(); // información que viene en el archivo
    static List<Integer> indexList = new LinkedList<>(); // lista que sirve para el control de las columnas en la visual
    static List<PersonGroup> personGroupList = new LinkedList<>();
    static boolean newGroup = false;
    static PersonGroup selectedPersonGroup = new PersonGroup();


    public void clear() {
        encabezadoList = new LinkedList<>();
        dataList = new LinkedList<>();
        indexList = new LinkedList<>();
        personGroupList = new LinkedList<>();
        newGroup = false;
        selectedPersonGroup = new PersonGroup();
    }

    /**
     * se encarga de subir el archivo seleccionado y leerlo
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile uf = event.getFile(); // se captura el fichero que se selecciona
        File file = getFile(uf); // convierto el fichero al tipo file

//        configurando el parser
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

//        para que pueda leer las tildes
        InputStream targetStream = new FileInputStream(file);
        Reader readerR = new InputStreamReader(targetStream, StandardCharsets.UTF_8);

        try (CSVReader reader = new CSVReaderBuilder(readerR)
                .withCSVParser(csvParser)
                .build()) {
            dataList = reader.readAll();
            encabezadoList = new LinkedList<>(Arrays.asList(dataList.get(0))); // establezco el encabezado
            dataList.remove(0); // quito el encabezado de los datos

            checkHomogeneousData();

            createIndexList(); // se crea la lista con la misma cant de elementos en dataList
            personGroupList = ejbFacade.findAll();

            JsfUtil.addSuccessMessage(uf.getFileName() + " " + localeConfig.getBundleValue("correctly_loaded"));

            if (propertiesUtil.isDevelopentMode()) {
                initAll();
            }
        } catch (IOException e) {
            JsfUtil.addErrorMessage(e, localeConfig.getBundleValue("error_reading_file"));
            e.printStackTrace();
        } catch (NotHomogeneousFile e) {
            JsfUtil.addErrorMessage(e, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * verifica que los datos del fichero sean homogeneos, o sea, que todas las celdas de una
     * misma columna sean de la misma clase
     *
     * @throws org.teamsoft.exceptions.NotHomogeneousFile en caso de que existan columnas con valores
     *                                                    de tipos de datos diferentes
     */
    private void checkHomogeneousData() throws NotHomogeneousFile {
        List<Integer> badColumns = new LinkedList<>();
        int cantCol = dataList.get(0).length;
        for (int i = 0; i < cantCol; i++) {
            if (!isColumnDataRight(i)) {
                badColumns.add(i);
            }
        }
        if (!badColumns.isEmpty()) {
            dataList = null;
            throw new NotHomogeneousFile(construirNotHomogeneusFileException(badColumns));
        }
    }

    /**
     * @param pos columna a verificar si los datos son correctos
     * @return <code>true</code> si los datos de la columna son todos de la misma clase
     */
    private boolean isColumnDataRight(int pos) {
        boolean isRight = true;
        int contador = 1;
        int maxSize = dataList.size();
        String classToCheck = util.getClassByAtribute(encabezadoList.get(pos));
        while (contador < maxSize && isRight) {
            String toCompare = dataList.get(contador)[pos];
            if (!util.getClassOf(toCompare).equals(classToCheck)) {
                isRight = false;
            } else {
                contador++;
            }
        }
        return isRight;
    }

    private String construirNotHomogeneusFileException(List<Integer> badColumns) {
        StringBuilder finalMessage = new StringBuilder();
        finalMessage.append(localeConfig.getBundleValue("error_homogeneus"));
        badColumns.forEach(badIndex -> {
            finalMessage.append(encabezadoList.get(badIndex).isEmpty() ? "<_>" : encabezadoList.get(badIndex));
            if (badColumns.indexOf(badIndex) != badColumns.size() - 1) {
                finalMessage.append(", ");
            }
        });
        finalMessage.append(" ").append(localeConfig.getBundleValue("error_text_not_homogeneous"));
        return finalMessage.toString();
    }

    private void initAll() {
        setSelectedPersonGroup(getRandomGroupName());
        stepAtributosPersonaController.init();
        stepSelectCompetencesController.init();
        stepConfigCompetenceController.init();
        stepSelectRolesController.init();
        stepVerifyData.init();
    }

    /**
     * obtiene un nombre de grupo aleatorio, para inicializar el valor por defecto
     */
    private String getRandomGroupName() {
        String result = "Mi grupo"; //eso lo pongo para cuando esta todo vacio y no hay ningun grupo
        Random r = new SecureRandom();
        int cantMax = personGroupList.size();
        try {
            result = personGroupList.get(r.nextInt(cantMax)).getName();
        } catch (Exception ignore) {
        }
        return result;
    }

    public String getSelectedPersonGroup() {
        String result = "";
        try {
            result = selectedPersonGroup.getName();
        } catch (Exception ignore) {
        }
        return result;
    }

    /**
     * se encarga de establecer el grupo de personas sobre el cual
     * se insertaran los datos gestionados, en el caso de que ya
     * exista (se seleccione del combo) se establece normal, en caso
     * de que no (que lo escriba manual), pues se crea uno nuevo y se
     * establece como seleccionado
     */
    public void setSelectedPersonGroup(String name) {
        if (!name.isEmpty()) {
            Optional<PersonGroup> exist = personGroupList.stream().filter(e -> e.getName().equals(name)).findFirst();
            if (!exist.isPresent()) {
                newGroup = true;
                selectedPersonGroup = new PersonGroup();
                selectedPersonGroup.setName(name);
            } else {
                selectedPersonGroup = exist.get();
            }
        }
    }

    public List<PersonGroup> getPersonsGroupList() {
        return personGroupList;
    }

    public void setPersonGroupList(List<PersonGroup> personGroup) {
        personGroupList = personGroup;
    }

    public boolean isNewGroup() {
        return newGroup;
    }

    public void setNewGroup(boolean newGroup) {
        I_StepReadFileController.newGroup = newGroup;
    }

    private void createIndexList() {
        indexList = new ArrayList<>();
        int count = 0;
        while (count != encabezadoList.size()) {
            indexList.add(count++);
        }
    }

    /**
     * @param fila es <code>String</code> pq en la visual se trata así
     *             <p/>
     *             obtienen el valore específico de la tabla
     */
    public String getValue(String fila, int col) {
        return dataList.get(Integer.parseInt(fila))[col];
    }

    /**
     * copia el fichero a subir a una carpeta temporal en el servidor y convierte el archivo
     * de la clase <code>UploadedFile</code> a <code>File</code> para poder trabajar con la
     * ubicacion
     */
    private File getFile(UploadedFile uploadedFile) {
        try {
            Path tmpFile = Files.createTempFile(FilenameUtils.getBaseName(uploadedFile.getFileName()), "." + FilenameUtils.getExtension(uploadedFile.getFileName()));
            Files.copy(uploadedFile.getInputstream(), tmpFile, StandardCopyOption.REPLACE_EXISTING);
            return tmpFile.toFile();
        } catch (IOException e) {
            return null;
        }
    }

    public LinkedList<String> getValuesByAtributeIndex(int index) {
        LinkedList<String> values = new LinkedList<>();

        for (String[] lista : dataList) {
            String temp = lista[index];
            if (!values.contains(temp)) {
                values.add(temp);
            }
        }
        return values;
    }

    /**
     * busca el index de la persona por el nombre y la experiencia
     */
    public int getIndexByPersona(int indexNombre, int indexExp, String personName, int experience) {
        for (int i = 0; i < dataList.size(); i++) {
            String[] data = dataList.get(i);
            if (data[indexNombre].equals(personName) && Integer.parseInt(data[indexExp]) == experience) {
                return i;
            }
        }
        return -1;
    }

    public List<String[]> getDataList() {
        if (dataList == null) {
            return new LinkedList<>();
        }
        return dataList;
    }

    public void setDataList(List<String[]> dataList) {
        this.dataList = dataList;
    }

    public LinkedList<String> getEncabezadoList() {
        return encabezadoList;
    }

    public void setEncabezadoList(LinkedList<String> encabezadoList) {
        this.encabezadoList = encabezadoList;
    }

    public String getEncabezadoByPos(int pos) {
        return encabezadoList.get(pos);
    }

    public List<Integer> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<Integer> indexList) {
        this.indexList = indexList;
    }

    public PersonGroup getSelectedPersonGroupObject() {
        return selectedPersonGroup;
    }
}
