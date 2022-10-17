package org.teamsoft.controller.util;

public class ImportResult {
    int personasImportadas;
    int personasActualizadas;
    int personasNoImportadas;
    int errores;

    public ImportResult() {
        personasImportadas = 0;
        personasActualizadas = 0;
        personasNoImportadas = 0;
        errores = 0;
    }

    public int getErrores() {
        return errores;
    }

    public void setErrores(int errores) {
        this.errores = errores;
    }

    public void incrementImport() {
        setPersonasImportadas(getPersonasImportadas() + 1);
    }

    public void incrementError() {
        setErrores(getErrores() + 1);
    }

    public void incrementActualizadas() {
        setPersonasActualizadas(getPersonasActualizadas() + 1);
    }

    public void incrementNoImport() {
        setPersonasNoImportadas(getPersonasNoImportadas() + 1);
    }

    public int getPersonasImportadas() {
        return personasImportadas;
    }

    public void setPersonasImportadas(int personasImportadas) {
        this.personasImportadas = personasImportadas;
    }

    public int getPersonasActualizadas() {
        return personasActualizadas;
    }

    public void setPersonasActualizadas(int personasActualizadas) {
        this.personasActualizadas = personasActualizadas;
    }

    public int getPersonasNoImportadas() {
        return personasNoImportadas;
    }

    public void setPersonasNoImportadas(int personasNoImportadas) {
        this.personasNoImportadas = personasNoImportadas;
    }
}
