package org.pcConfigurator.services;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;

import java.util.List;
import java.util.Set;

public interface ArticleService {
    Set<ArticleTeaserBean> getTeaserArticleList();

    Set<ArticleTeaserBean> getCompatibleArticleTeaserBeansOfType(ComponentType componentType, ConfigurationBean currentConfig);

    void createDummyData();

    void createDummyPrices();

    Article getArticleForArticleTeaserBean(final ArticleTeaserBean source);

    List<ArticleTeaserBean> searchArticleByName(final String articleName);

    Set<ArticleTeaserBean> performSearch(final String searchWord);

}
