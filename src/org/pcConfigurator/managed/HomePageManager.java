package org.pcConfigurator.managed;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.services.ArticleService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;

@RequestScoped
@Named
public class HomePageManager {

    private Set<ArticleTeaserBean> articleTeaserList;

    @Inject
    private ArticleService articleService;


    // Wir nutzen hier ein Postconstruct um die redundanten Aufrufe von JSF in ihrer Auswirkung zu mindern.
    // Die articleTeaserList wird einmal pro request (da Bean requestscoped) von der DB geladen und dann für jeden
    // Aufuf von getArticleTeaserList() nur noch zurückgegeben (Das ist nötig, weil JSF getArticleTeaserList() etliche male
    // pro request aufruft, -> siehe JSF deferred expression)
    @PostConstruct
    public void loadTeaserList() {
        //this.articleService.createDummyData();
        // this.articleService.createDummyPrices();
        this.articleTeaserList = articleService.getTeaserArticleList();
    }


    public Set<ArticleTeaserBean> getArticleTeaserList() {

        return this.articleTeaserList;
    }
}
