package org.pcConfigurator.annotations;

import javax.xml.ws.BindingType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD})
@BindingType
public @interface CurrentUser {
}
