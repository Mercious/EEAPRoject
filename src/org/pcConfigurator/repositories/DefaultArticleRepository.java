package org.pcConfigurator.repositories;

import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.ComponentType;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class DefaultArticleRepository implements ArticleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Article> findAll() {
        return null;
    }

    @Override
    public Article findOne(long articleID) {
        return null;
    }

    @Override
    public Set<Article> findByArticleName(String articleName) {
        return null;
    }

    @Override
    public Set<Article> findAllDiscountedArticles() {
        return new HashSet<>(entityManager.createQuery("select a from Article a, PriceRow p where p MEMBER OF a.priceRows" +
                " AND p.promotion = TRUE", Article.class)
                .getResultList());
    }

    @Override
    public Set<Article> findByComponentType(ComponentType componentType) {
        TypedQuery<Article> query = entityManager.createQuery("select a from Article a where a.type = :componentType", Article.class);
        query.setParameter("componentType", componentType);
        return new HashSet<>(query.getResultList());
    }
}
