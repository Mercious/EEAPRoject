package org.pcConfigurator.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Inheritance(
        strategy = InheritanceType.SINGLE_TABLE
)
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
    private Set<PriceRow> priceRows;

    @Enumerated(EnumType.STRING)
    private ComponentType type;

    // Welche Slots dieser Artikel benötigt (Komponente) oder bereitstellt (Motherboard)
    @OneToMany (cascade = CascadeType.ALL)
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
        HashMap<SlotType, Integer> slotRelationOfType = new HashMap<>();
        this.slotRestrictions.stream().filter(slotRestriction -> type.equals(slotRestriction.getType()))
                .forEach(slotRestriction -> slotRelationOfType.merge(slotRestriction.getRelatedSlotType()
                        , slotRestriction.getQuantity(), (a, b) -> a + b));
        return slotRelationOfType;
    }
}
