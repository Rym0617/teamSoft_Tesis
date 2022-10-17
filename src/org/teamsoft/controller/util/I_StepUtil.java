package org.teamsoft.controller.util;

import org.teamsoft.POJOS.MyJSF_Util;
import org.teamsoft.controller.importar.I_StepReadFileController;
import org.teamsoft.entity.County;
import org.teamsoft.locale.LocaleConfig;
import org.teamsoft.model.CountyFacade;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.teamsoft.controller.util.MyDateUtils.*;

@Named("stepUtils")
@ManagedBean
@SessionScoped
public class I_StepUtil implements Serializable {

    @Inject
    CountyFacade countyFacade;
    @Inject
    LocaleConfig localeConfig;
    @Inject
    private I_StepReadFileController stepOneController;

    /**
     * devuelve el primer valor que hay en el fichero en la columna i
     */
    public String getValueInIndex(int i) {
        return stepOneController.getDataList().get(0)[i];
    }

    /**
     * busca la posicion que ocupa la propiedad en el encabezado del fichero
     */
    public int getIndexOfProperty(String propertyToCheck) {
        return stepOneController.getEncabezadoList().indexOf(
                propertyToCheck
        );
    }

    /**
     * busca la clase que le corresponde al valor que almacena el atributo
     */
    public String getClassByAtribute(String atr) {
        int index = getIndexOfProperty(atr);
        String value = getValueInIndex(index);
        return getClassOf(value);
    }

    /**
     * devuelve <code>Texto</code> o <code>Número</code> en función de lo que
     * contenga el string pasado por parámetros
     */
    public String getClassOf(String value) {
        try {
            Integer.parseInt(value);
            return localeConfig.getBundleValue("number");
        } catch (Exception e) {
            return localeConfig.getBundleValue("text");
        }
    }

    /**
     * devuelve los valores que contiene el atributo en el fichero de
     * entrada sin repetición
     */
    public LinkedList<String> getStringValuesByAtributo(String atr) {
        int index = getIndexOfProperty(atr);
        LinkedList<String> result = new LinkedList<>();
        LinkedList<String> listaToSearch = stepOneController.getValuesByAtributeIndex(index);

        for (String val : listaToSearch) {
            if (!result.contains(val)) {
                result.add(val);
            }
        }
        return result;
    }

    /**
     * solo funciona si todos los valores en la columna son integers
     */
    public int getMaximoValorIntByAtr(String atr) {
        int result;
        LinkedList<String> valores = getStringValuesByAtributo(atr);
        try {
            result = Integer.parseInt(valores.stream().max(Comparator.comparing(Integer::parseInt)).get());
        } catch (Exception e) {
            result = -1;
            MyJSF_Util.addErrorMessage(
                    String.format(localeConfig.getBundleValue("error_values_non_numeric"), atr)
            );
        }
        return result;
    }

    public float round(float value) {
        // numero dsps de la coma a mostrar en la suma
        int precision = 2;
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

    public String getRandomPhoneNumber() {
        Random random = new SecureRandom();
        StringBuilder result = new StringBuilder();
        result.append(5);
        for (int i = 0; i < 7; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    public County generateRandomCounty() {
        Random random = new SecureRandom();
        return countyFacade.findAll().get(random.nextInt(countyFacade.findAll().size()));
    }

    public String generateRandomCI(char sexo) {
        StringBuilder ci = new StringBuilder();
        LocalDate randomDate = generateRandomDate();

        return ci.append(getYearAsString(randomDate.getYear())).
                append(getStringByNumber(randomDate.getMonth().getValue())).
                append(getStringByNumber(randomDate.getDayOfMonth())).
                append(generateLast5Digits(sexo)).toString();
    }

    /**
     * obtiene de todos los tipos mb uno de forma aleatoria.
     */
    public String getRandomMbType() {
        Random random = new SecureRandom();
        List<String> lista = new LinkedList<>();
        lista.add("ESTJ");
        lista.add("ENTJ");
        lista.add("ESFJ");
        lista.add("ENFJ");
        lista.add("ESTP");
        lista.add("ENTP");
        lista.add("ESFP");
        lista.add("ENFP");
        lista.add("ISTJ");
        lista.add("INTJ");
        lista.add("ISFJ");
        lista.add("INFJ");
        lista.add("ISTP");
        lista.add("INTP");
        lista.add("ISFP");
        lista.add("INFP");
        return lista.get(random.nextInt(lista.size()));
    }

    public String generateRandomEmail(String personName) {
        return personName.replace(" ", "").toLowerCase() + "@ceis.cujae.edu.cu";
    }

    /**
     * solo verifica que coincida el string que se pasa por parametro
     * con el valor que se tiene establecido para que sea numero y devuelve
     * el resultado de la comparacion
     */
    public boolean isNumber(String str) {
        return localeConfig.getBundleValue("number").equals(str);
    }

    public char getRandomSex() {
        Random random = new SecureRandom();
        char[] sexo = new char[]{'F', 'M'};
        return sexo[random.nextInt(2)];
    }

    public String generateLast5Digits(char sexo) {
        Random random = new SecureRandom();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i == 3) {
                result.append(getSexDigit(sexo));
            } else {
                result.append(random.nextInt(10));
            }
        }
        return result.toString();
    }

    private int getSexDigit(char sexo) {
        Random random = new SecureRandom();
        int sexDigit;
        int[] girl = new int[]{1, 3, 5, 7, 9};
        int[] boy = new int[]{0, 2, 4, 6, 8};
        if (sexo == 'F') {
            sexDigit = girl[random.nextInt(girl.length)];
        } else {
            sexDigit = boy[random.nextInt(boy.length)];
        }
        return sexDigit;
    }
}
