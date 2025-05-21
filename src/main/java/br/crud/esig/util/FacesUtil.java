package br.crud.esig.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtil {

    public static void addInfoMessage(String message) {
        addMessage(FacesMessage.SEVERITY_INFO, message);
    }

    public static void addErrorMessage(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, message);
    }

    private static void addMessage(FacesMessage.Severity severity, String message) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, message, null));
    }
}