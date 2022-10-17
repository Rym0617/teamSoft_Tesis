package org.teamsoft.myBeans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lesme & jpinas
 */
@Named("algorithmConfController")
@SessionScoped
public class AlgorithmConfController implements Serializable {

    private ServletContext relativePath = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    private String mainPath = relativePath.getRealPath("/");
    private Properties algorithmConf;
    private String initialSolutionConf;
    private ArrayList<String> initialSolutionList;
    private String numberPersonTries;
    private String operatorOpc;
    private ArrayList<String> operatorList;
    private String decimalFormat;
    private String executions;
    private String iterations;
    private boolean calculateTime;
    private boolean validate;
    private String possibleValidateNumber;
    private String HillClimbingRestartCount;
    private String TabuSolutionsMaxelements;
    private String MultiobjectiveHCRestartSizeNeighbors;
    private String MultiobjectiveHCDistanceSizeNeighbors;
    private String MultiobjectiveTabuSolutionsMaxelements;
    private String PC;
    private String PM;
    private String PS;
    private String CountRef;
    private String GeneticAlgorithmTruncation;
    private String cantIntentos;

    private String operatorTypeLabel;
    private ArrayList<String> operatorTypeList;
    private boolean operatorTypeDisabled;
    private String operatorTypeOpc;

    private ArrayList<String> mutationList;
    private ArrayList<String> crossoverList;
    private boolean mutationOperatorActivated = true;
    private boolean crossoverOperatorActivated = true;
    private String mutationOperatorOpc;
    private String crossoverOperatorOpc;

    @PostConstruct
    public void init() {
        this.algorithmConf = new Properties();
        try {
            algorithmConf.load(getClass().getResourceAsStream("/algorithmConf.properties"));
            initialSolutionList = new ArrayList<>();
            initialSolutionList.add("Belbin");
            initialSolutionList.add("Competencias");
            initialSolutionList.add("Mínimo de roles");
            initialSolutionList.add("Jefe de equipo");
            initialSolutionList.add("Aleatorio");

            initialSolutionConf = algorithmConf.getProperty("initialSolutionConf");
            numberPersonTries = algorithmConf.getProperty("numberPersonTries");
            operatorList = new ArrayList<>();
            operatorList.add("Mutaci\u00F3n");
            operatorList.add("Cruzamiento");
            operatorList.add("Aleatorio");
            operatorOpc = algorithmConf.getProperty("operatorOpc");
            mutationOperatorOpc = algorithmConf.getProperty("mutationOperatorOpc");
            crossoverOperatorOpc = algorithmConf.getProperty("crossoverOperatorOpc");
            operatorTypeList = new ArrayList<>();
            operatorListChange(); // inicializa las listas
            decimalFormat = algorithmConf.getProperty("decimalFormat");
            executions = algorithmConf.getProperty("executions");
            iterations = algorithmConf.getProperty("iterations");
            if (algorithmConf.getProperty("calculateTime").equalsIgnoreCase("true")) {
                calculateTime = true;
            }
            if (algorithmConf.getProperty("validate").equalsIgnoreCase("true")) {
                validate = true;
            }
            possibleValidateNumber = algorithmConf.getProperty("possibleValidateNumber");
            HillClimbingRestartCount = algorithmConf.getProperty("HillClimbingRestartCount");
            TabuSolutionsMaxelements = algorithmConf.getProperty("TabuSolutionsMaxelements");
            MultiobjectiveHCRestartSizeNeighbors = algorithmConf.getProperty("MultiobjectiveHCRestartSizeNeighbors");
            MultiobjectiveHCDistanceSizeNeighbors = algorithmConf.getProperty("MultiobjectiveHCDistanceSizeNeighbors");
            MultiobjectiveTabuSolutionsMaxelements = algorithmConf.getProperty("MultiobjectiveTabuSolutionsMaxelements");
            PC = algorithmConf.getProperty("PC");
            PM = algorithmConf.getProperty("PM");
            PS = algorithmConf.getProperty("PS");
            CountRef = algorithmConf.getProperty("CountRef");
            GeneticAlgorithmTruncation = algorithmConf.getProperty("GeneticAlgorithmTruncation");
            cantIntentos = algorithmConf.getProperty("cantIntentos");

            getClass().getResourceAsStream("/algorithmConf.properties").close();
        } catch (IOException ex) {
            Logger.getLogger(AlgorithmConfController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void save() throws IOException, URISyntaxException {
        URL path = Thread.currentThread().getContextClassLoader().getResource("algorithmConf.properties");
        URI uri = path.toURI();
        String p = uri.getPath();
        OutputStream outputStream = new FileOutputStream(p);
        algorithmConf.setProperty("initialSolutionConf", initialSolutionConf);
        algorithmConf.setProperty("numberPersonTries", numberPersonTries);
        algorithmConf.setProperty("operatorOpc", operatorOpc);
        algorithmConf.setProperty("decimalFormat", decimalFormat);
        algorithmConf.setProperty("executions", executions);
        algorithmConf.setProperty("iterations", iterations);
        algorithmConf.setProperty("calculateTime", String.valueOf(calculateTime));
        algorithmConf.setProperty("validate", String.valueOf(validate));
        algorithmConf.setProperty("possibleValidateNumber", possibleValidateNumber);
        algorithmConf.setProperty("HillClimbingRestartCount", HillClimbingRestartCount);
        algorithmConf.setProperty("TabuSolutionsMaxelements", TabuSolutionsMaxelements);
        algorithmConf.setProperty("MultiobjectiveHCRestartSizeNeighbors", MultiobjectiveHCRestartSizeNeighbors);
        algorithmConf.setProperty("MultiobjectiveHCDistanceSizeNeighbors", MultiobjectiveHCDistanceSizeNeighbors);
        algorithmConf.setProperty("MultiobjectiveTabuSolutionsMaxelements", MultiobjectiveTabuSolutionsMaxelements);
        if (operatorOpc.equalsIgnoreCase("0")) {
            algorithmConf.setProperty("mutationOperatorOpc", operatorTypeOpc);
        } else if (operatorOpc.equalsIgnoreCase("1")) {
            algorithmConf.setProperty("crossoverOperatorOpc", operatorTypeOpc);
        }
        algorithmConf.setProperty("PC", PC);
        algorithmConf.setProperty("PM", PM);
        algorithmConf.setProperty("PS", PS);
        algorithmConf.setProperty("CountRef", CountRef);
        algorithmConf.setProperty("GeneticAlgorithmTruncation", GeneticAlgorithmTruncation);
        algorithmConf.setProperty("cantIntentos", cantIntentos);

        algorithmConf.store(outputStream, "# To change this license header, choose License Headers in Project Properties.\n"
                + "To change this template file, choose Tools | Templates\n"
                + "and open the template in the editor.\n"
                + "\n"
                + "PARAMETROS DE CONFIGURACION DE TEAMSOFT\n"
                + "initialSolutionConf #0-Belbin , 1-Competencias, 2-roles minimos de forma aleatoria, 3- jefe necesario en el equipo, Otra - Aleatoria\n"
                + "numberPersonTries\t #Numero de pruebas para la obtencion de una persona en un rol de equipo, en la construccion de la solucion inicial\n"
                + "operatorOpc\t #Operador a utilizar 0 - Sustitucion, 1 - Permutaci\\u00c3\\u00b3n, 2-Mutaci\\u00c3\\u00b3n, 3-Cruzamiento, Otro - Aleatorio\n"
                + "decimalFormat\t #Formato para mostrar la evaluacion de una solucion\n\n"
                + "#PARAMETROS DE CONFIGURACION DE LA ESTRATEGIA  - BICIAM\n"
                + "executions\t #Numero de ejecuciones\n"
                + "iterations\t #Numero de iteraciones para el metodo metaheuristico a utilizar\n"
                + "calculateTime\t #si se desea calcular el tiepo de ejecucion del algoritmo\n"
                + "validate\t #si se desea validar los estados de las vecindades generadas por Biciam\n"
                + "posibleValidateNumber\t #Numero de pruebas para la obtencion de un estado valido en las vecindades generadas por Biciam\n\n"
                + "PARAMETROS DE CONFIGURACION DE ALGORITMOS - BICIAM\n"
                + "HillClimbingRestartCount\t #Representa cada cuantas iteraciones se realizara el reinicio por algoritmo\n"
                + "TabuSolutionsMaxelements\t #representa el tamaño de la lista tabu\n"
                + "MultiobjectiveHCRestartSizeNeighbors\t #Representa el tamaño de la vecindad del estado actual para este algoritmo\n"
                + "MultiobjectiveHCDistanceSizeNeighbors\t #Representa el tamaño de la vecindad del estado actual para este algoritmo\n"
                + "MultiobjectiveTabuSolutionsMaxelements\t #Representa el tamaño de la lista tabu\n\n"
                + "PARAMETROS DE CONFIGURACION DE ALGORITMOS POBLACIONALES\n"
                + "PC\t #Probabilidad de cruzamiento\n"
                + "PM\t #Probabilidad de mutacion\n"
                + "PS\t #Probabilidad de seleccion\n"
                + "CountRef\t \n "
                + "GeneticAlgorithmTruncation\t #Cantidad de individuos a truncar\n\n"
                + "PARAMETRO ESTRATEGIA DE REPARACION\n"
                + "cantIntentos\t #Cantidad de reparaciones antes del rechazo\n"
                + "operatorCruce \t #elije el operador de cruzamiento"
        );
        outputStream.flush();
        outputStream.close();
    }

    public void operatorListChange() {
        if (operatorOpc.equalsIgnoreCase("0")) {
            operatorTypeOpc = mutationOperatorOpc;
            operatorTypeList.clear();
            operatorTypeList.add("Sustituci\u00F3n");
            operatorTypeList.add("Permutaci\u00F3n de roles");
            operatorTypeList.add("Permutaci\u00F3n de proyectos");
            operatorTypeList.add("Sustitución del jefe de equipo");
            operatorTypeList.add("Permutación en todos los roles");
            operatorTypeLabel = "mutación";
        } else if (operatorOpc.equalsIgnoreCase("1")) {
            operatorTypeList.clear();
            operatorTypeOpc = crossoverOperatorOpc;
            operatorTypeList.add("Cruce de vector de proyecto");
            operatorTypeList.add("Cruce de vector de rol");
            operatorTypeList.add("Cruce de vector de persona");
            operatorTypeList.add("Crcue de experiencia en el rol");
            operatorTypeList.add("Cruce de un punto");
            operatorTypeList.add("Cruce de dos puntos");
            operatorTypeList.add("Cruce de vector de mejor estado");
            operatorTypeList.add("Cruce general de mejor estado");
            operatorTypeLabel = "cruzamiento";
        } else {
            operatorTypeLabel = "aleatoriedad";
            operatorTypeList.clear();
        }
    }

    public boolean operatorTypeDisabled() {
        if (operatorOpc.equalsIgnoreCase("0")) {
            operatorTypeDisabled = false;
        } else operatorTypeDisabled = !operatorOpc.equalsIgnoreCase("1");

        return operatorTypeDisabled;
    }

    public Properties getAlgorithmConf() {
        return algorithmConf;
    }

    public void setAlgorithmConf(Properties algorithmConf) {
        this.algorithmConf = algorithmConf;
    }

    public String getNumberPersonTries() {
        return numberPersonTries;
    }

    public void setNumberPersonTries(String numberPersonTries) {
        this.numberPersonTries = numberPersonTries;
    }

    public String getInitialSolutionConf() {
        return initialSolutionConf;
    }

    public void setInitialSolutionConf(String initialSolutionConf) {
        this.initialSolutionConf = initialSolutionConf;
    }

    public String getOperatorOpc() {
        return operatorOpc;
    }

    public void setOperatorOpc(String operatorOpc) {
        this.operatorOpc = operatorOpc;
    }

    public String getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(String decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    public String getExecutions() {
        return executions;
    }

    public void setExecutions(String executions) {
        this.executions = executions;
    }

    public String getIterations() {
        return iterations;
    }

    public void setIterations(String iterations) {
        this.iterations = iterations;
    }

    public boolean isCalculateTime() {
        return calculateTime;
    }

    public void setCalculateTime(boolean calculateTime) {
        this.calculateTime = calculateTime;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getPossibleValidateNumber() {
        return possibleValidateNumber;
    }

    public void setPossibleValidateNumber(String possibleValidateNumber) {
        this.possibleValidateNumber = possibleValidateNumber;
    }

    public String getHillClimbingRestartCount() {
        return HillClimbingRestartCount;
    }

    public void setHillClimbingRestartCount(String HillClimbingRestartCount) {
        this.HillClimbingRestartCount = HillClimbingRestartCount;
    }

    public String getTabuSolutionsMaxelements() {
        return TabuSolutionsMaxelements;
    }

    public void setTabuSolutionsMaxelements(String TabuSolutionsMaxelements) {
        this.TabuSolutionsMaxelements = TabuSolutionsMaxelements;
    }

    public String getMultiobjectiveHCRestartSizeNeighbors() {
        return MultiobjectiveHCRestartSizeNeighbors;
    }

    public void setMultiobjectiveHCRestartSizeNeighbors(String MultiobjectiveHCRestartSizeNeighbors) {
        this.MultiobjectiveHCRestartSizeNeighbors = MultiobjectiveHCRestartSizeNeighbors;
    }

    public String getMultiobjectiveHCDistanceSizeNeighbors() {
        return MultiobjectiveHCDistanceSizeNeighbors;
    }

    public void setMultiobjectiveHCDistanceSizeNeighbors(String MultiobjectiveHCDistanceSizeNeighbors) {
        this.MultiobjectiveHCDistanceSizeNeighbors = MultiobjectiveHCDistanceSizeNeighbors;
    }

    public String getMultiobjectiveTabuSolutionsMaxelements() {
        return MultiobjectiveTabuSolutionsMaxelements;
    }

    public void setMultiobjectiveTabuSolutionsMaxelements(String MultiobjectiveTabuSolutionsMaxelements) {
        this.MultiobjectiveTabuSolutionsMaxelements = MultiobjectiveTabuSolutionsMaxelements;
    }

    public ArrayList<String> getInitialSolutionList() {
        return initialSolutionList;
    }

    public void setInitialSolutionList(ArrayList<String> initialSolutionList) {
        this.initialSolutionList = initialSolutionList;
    }

    public ArrayList<String> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(ArrayList<String> operatorList) {
        this.operatorList = operatorList;
    }

    public ArrayList<String> getMutationList() {
        return mutationList;
    }

    public void setMutationList(ArrayList<String> mutationList) {
        this.mutationList = mutationList;
    }

    public ArrayList<String> getCrossoverList() {
        return crossoverList;
    }

    public void setCrossoverList(ArrayList<String> crossoverList) {
        this.crossoverList = crossoverList;
    }

    public boolean isMutationOperatorActivated() {
        return mutationOperatorActivated;
    }

    public void setMutationOperatorActivated(boolean mutationOperatorActivated) {
        this.mutationOperatorActivated = mutationOperatorActivated;
    }

    public boolean isCrossoverOperatorActivated() {
        return crossoverOperatorActivated;
    }

    public void setCrossoverOperatorActivated(boolean crossoverOperatorActivated) {
        this.crossoverOperatorActivated = crossoverOperatorActivated;
    }

    public String getMutationOperatorOpc() {
        return mutationOperatorOpc;
    }

    public void setMutationOperatorOpc(String mutationOperatorOpc) {
        this.mutationOperatorOpc = mutationOperatorOpc;
    }

    public String getCrossoverOperatorOpc() {
        return crossoverOperatorOpc;
    }

    public void setCrossoverOperatorOpc(String crossoverOperatorOpc) {
        this.crossoverOperatorOpc = crossoverOperatorOpc;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public String getPM() {
        return PM;
    }

    public void setPM(String PM) {
        this.PM = PM;
    }

    public String getPS() {
        return PS;
    }

    public void setPS(String PS) {
        this.PS = PS;
    }

    public String getCountRef() {
        return CountRef;
    }

    public void setCountRef(String CountRef) {
        this.CountRef = CountRef;
    }

    public String getGeneticAlgorithmTruncation() {
        return GeneticAlgorithmTruncation;
    }

    public void setGeneticAlgorithmTruncation(String GeneticAlgorithmTruncation) {
        this.GeneticAlgorithmTruncation = GeneticAlgorithmTruncation;
    }

    public String getCantIntentos() {
        return cantIntentos;
    }

    public void setCantIntentos(String cantIntentos) {
        this.cantIntentos = cantIntentos;
    }

    public String getOperatorTypeLabel() {
        return operatorTypeLabel;
    }

    public void setOperatorTypeLabel(String operatorTypeLabel) {
        this.operatorTypeLabel = operatorTypeLabel;
    }

    public ArrayList<String> getOperatorTypeList() {
        return operatorTypeList;
    }

    public void setOperatorTypeList(ArrayList<String> operatorTypeList) {
        this.operatorTypeList = operatorTypeList;
    }

    public boolean isOperatorTypeDisabled() {
        return operatorTypeDisabled;
    }

    public void setOperatorTypeDisabled(boolean operatorTypeDisabled) {
        this.operatorTypeDisabled = operatorTypeDisabled;
    }

    public String getOperatorTypeOpc() {
        return operatorTypeOpc;
    }

    public void setOperatorTypeOpc(String operatorTypeOpc) {
        this.operatorTypeOpc = operatorTypeOpc;
    }

}
