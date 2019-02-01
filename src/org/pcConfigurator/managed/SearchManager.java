package org.pcConfigurator.managed;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.services.ArticleService;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named
public class SearchManager {

    @Inject
    private ArticleService articleService;

    private String searchWord = "";

    private List<ArticleTeaserBean> resultList = new ArrayList<>();


    public String search() {
        this.resultList = new ArrayList<>(this.articleService.performSearch(searchWord));
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        return ((HttpServletRequest) ec.getRequest()).getContextPath() + "/searchPage.xhtml";

    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public List<ArticleTeaserBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ArticleTeaserBean> resultList) {
        this.resultList = resultList;
    }
}
