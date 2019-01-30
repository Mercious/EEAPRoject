package org.pcConfigurator.managed;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.services.ArticleService;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@Named
public class SearchManager {

    @Inject
    private ArticleService articleService;

    private List<ArticleTeaserBean> resultList = new ArrayList<>();


    public List<ArticleTeaserBean> getResultList() {
        return resultList;
    }
}
