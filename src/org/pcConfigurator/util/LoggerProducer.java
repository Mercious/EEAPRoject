package org.pcConfigurator.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

public class LoggerProducer {
    @Produces
    public Logger getLogger(InjectionPoint p) {
        return Logger.getLogger(p.getClass().getCanonicalName());
    }
}
