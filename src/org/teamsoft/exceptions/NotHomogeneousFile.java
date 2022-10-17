package org.teamsoft.exceptions;

public class NotHomogeneousFile extends Exception {
    public NotHomogeneousFile(String message, Throwable cause) {
        super(message, cause);
    }
    public NotHomogeneousFile(String message) {
        super(message);
    }
}
