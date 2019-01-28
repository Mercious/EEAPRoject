package org.pcConfigurator.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


public class FacesMessageUtil {

    static public void addErrorMessage(final String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(message);
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
    }

    static public void addInfoMessage(final String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(message);
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
    }
}
