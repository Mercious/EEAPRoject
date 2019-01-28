package org.pcConfigurator.strategies;


import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.Configuration;

public interface CompatibilityStrategy {
    boolean isCompatibleToCurrentConfig(Article article, Configuration currentConfig);
    boolean isApplicable(Article article);
}
