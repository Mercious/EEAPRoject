package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;

import java.util.Set;

public interface ArticleRepository {
    Set<Article> findAll();

    Article findOne(final long articleID);

    Set<Article> findByArticleName(final String articleName);

    Set<Article> findAllDiscountedArticles();

    Set<Article> findByComponentType(final ComponentType componentType);

    Set<Article> searchByArticleName(final String articleName);

    Set<Article> performSearch(final String searchWord);

}
