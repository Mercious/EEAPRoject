package org.pcConfigurator.services;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;
import org.pcConfigurator.entities.Configuration;

import java.util.List;
import java.util.Set;

public interface ArticleService {
    public Set<ArticleTeaserBean> getTeaserArticleList();

    Set<ArticleTeaserBean> getCompatibleArticleTeaserBeansOfType(ComponentType componentType, ConfigurationBean currentConfig);

    public void createDummyData();

    public void createDummyPrices();

    public Article getArticleForArticleTeaserBean(final ArticleTeaserBean source);

    public List<ArticleTeaserBean> searchArticleByName(final String articleName);

}
