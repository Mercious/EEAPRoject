package org.pcConfigurator.managed;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.services.ArticleService;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ViewScoped
@Named
public class SearchManager {

    @Inject
    private ArticleService articleService;

    private String searchWord = "";

    private Set<ArticleTeaserBean> resultList = new HashSet<>();


    public String search() {
        this.resultList = this.articleService.performSearch(searchWord);
        return FacesContext.getCurrentInstance().getExternalContext().getContext() + "/shop/searchPage.xhtml?faces-redirect=true";
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public Set<ArticleTeaserBean> getResultList() {
        return resultList;
    }

    public void setResultList(Set<ArticleTeaserBean> resultList) {
        this.resultList = resultList;
    }
}
