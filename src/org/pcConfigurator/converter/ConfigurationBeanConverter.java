package org.pcConfigurator.converter;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.services.ConfigurationService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ConfigurationBeanConverter implements Converter {

    @Inject
    private ConfigurationService configurationService;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String submittedValue) {
        if (submittedValue == null || submittedValue.isEmpty()) {
            return null;
        }

        try {
            return this.configurationService.getConfigurationBeanForId(Long.valueOf(submittedValue));
        } catch (NumberFormatException  e) {
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid Configuration ID", submittedValue)), e);
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object objectValue) {
        if (objectValue == null) {
            return "";
        }

        if (objectValue instanceof ConfigurationBean) {
            return String.valueOf(((ConfigurationBean) objectValue).getConfigurationId());
        } else {
            throw new ConverterException(new FacesMessage(String.format("%s is not a valid Configuration", objectValue)));
        }
    }
}
