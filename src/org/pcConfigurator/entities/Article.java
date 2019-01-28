package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Set;


@Entity
@Inheritance(
        strategy = InheritanceType.SINGLE_TABLE
)
public class Article {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long articleID;
    // immutable business key
    @Column(unique = true)
    private String articleName;
    // Frontend-Display Name
    private String displayName;

    @OneToMany (cascade = CascadeType.ALL )
    @JoinTable
    private Set<PriceRow> priceRows;

    @Enumerated(EnumType.STRING)
    private ComponentType type;

    public Article(final String articleName) {
        this.articleName = articleName;
    }

    protected Article() {}

    public long getArticleID() {
        return articleID;
    }

    public void setArticleID(long articleID) {
        this.articleID = articleID;
    }

    public String getArticleName() {
        return articleName;
    }

    public Set<PriceRow> getPriceRows() {
        return priceRows;
    }

    public void setPriceRows(Set<PriceRow> priceRows) {
        this.priceRows = priceRows;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
