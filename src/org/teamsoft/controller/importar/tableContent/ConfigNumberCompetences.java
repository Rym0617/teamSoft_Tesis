package org.teamsoft.controller.importar.tableContent;

public class ConfigNumberCompetences {
    String fileAtribute;
    int maximoValor;

    public ConfigNumberCompetences(String fileAtribute, int maximoValor) {
        this.fileAtribute = fileAtribute;
        this.maximoValor = maximoValor;
    }

    public String getFileAtribute() {
        return fileAtribute;
    }

    public void setFileAtribute(String fileAtribute) {
        this.fileAtribute = fileAtribute;
    }

    public int getMaximoValor() {
        return maximoValor;
    }

    public void setMaximoValor(int maximoValor) {
        this.maximoValor = maximoValor;
    }
}
