package org.pcConfigurator.strategies;


import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.Configuration;

public interface CompatibilityStrategy {
    boolean isCompatibleToCurrentConfig(Article article, ConfigurationBean currentConfig);
    boolean isApplicable(Article article);
}
