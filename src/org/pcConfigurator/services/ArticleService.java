package org.pcConfigurator.services;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;
import org.pcConfigurator.entities.Configuration;

import java.util.Set;

public interface ArticleService {
    public Set<ArticleTeaserBean> getTeaserArticleList();

    Set<Article> getCompatibleArticlesOfType(ComponentType componentType, Configuration currentConfig);

    public void createDummyData();

    public void createDummyPrices();
}
