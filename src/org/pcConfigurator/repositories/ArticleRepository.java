package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;

import java.util.Set;

public interface  ArticleRepository {
    public Set<Article> findAll();
    public Article findOne(final long articleID);
    public Set<Article> findByArticleName(final String articleName);
    public Set<Article> findAllDiscountedArticles();
    public Set<Article> findByComponentType(final ComponentType componentType);

}
