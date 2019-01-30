package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;


@Entity
public class Article {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long articleID;
    // business key
    @Column(unique = true)
    private String articleName;
    // Frontend-Display Name
    private String displayName;

    @OneToMany (cascade = CascadeType.ALL )
    @JoinTable
    private Set<PriceRow> priceRows = Collections.emptySet();

    @Enumerated(EnumType.STRING)
    private ComponentType type = ComponentType.MB;

    // Welche Slots dieser Artikel benötigt (Komponente) oder bereitstellt (Motherboard)
    // Die Restrictions interessieren uns eigentlich IMMER, also fetche sie eager
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SlotRestriction> slotRestrictions = Collections.emptySet();

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

    public Set<SlotRestriction> getSlotRestrictions() {
        return slotRestrictions;
    }

    public void setSlotRestrictions(Set<SlotRestriction> slotRestrictions) {
        this.slotRestrictions = slotRestrictions;
    }

    public HashMap<SlotType, Integer> getSlotRestrictionsOfType(final SlotRestrictionType type) {
        /*HashMap<SlotType, Integer> slotRelationOfType = new HashMap<>();
        this.slotRestrictions.stream().filter(slotRestriction -> type.equals(slotRestriction.getType()))
                .forEach(slotRestriction -> slotRelationOfType.merge(slotRestriction.getRelatedSlotType()
                        , slotRestriction.getQuantity(), (a, b) -> a + b));
        return slotRelationOfType;*/

        // EclipseLink seems to have a bug with lambdas in JPA entities -> do not use them here
        // Hat mich leider auch sehr viel Zeit gekostet
        // see https://stackoverflow.com/questions/8353752/jpa-error-uses-a-non-entity-class-ch-printsoft-mailhouse-usermgr-entity-departm

        HashMap<SlotType, Integer> slotRelationOfType = new HashMap<>();
        for (SlotRestriction slotRestriction : this.slotRestrictions) {
            if (type.equals(slotRestriction.getType())) {
                if (slotRelationOfType.get(slotRestriction.getRelatedSlotType()) != null) {
                    slotRelationOfType.put(slotRestriction.getRelatedSlotType(),
                            slotRelationOfType.get(slotRestriction.getRelatedSlotType()) + slotRestriction.getQuantity());
                } else {
                    slotRelationOfType.put(slotRestriction.getRelatedSlotType(), slotRestriction.getQuantity());
                }
            }
        }
        return slotRelationOfType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(articleName, article.articleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleName);
    }
}
