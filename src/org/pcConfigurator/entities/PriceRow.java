package org.pcConfigurator.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
public class PriceRow {

    @GeneratedValue
    @Id
    private Long id;

    private BigDecimal netPrice;
    private double taxMultiplier;
    @Temporal(TemporalType.DATE)
    private Date validFrom;
    @Temporal(TemporalType.DATE)
    private Date validUntil;
    private boolean promotion;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public double getTaxMultiplier() {
        return taxMultiplier;
    }

    public void setTaxMultiplier(double taxMultiplier) {
        this.taxMultiplier = taxMultiplier;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == null) return super.equals(o);
        PriceRow priceRow = (PriceRow) o;
        return Objects.equals(id, priceRow.id);
    }

    @Override
    public int hashCode() {
        if (id == null) return super.hashCode();
        return Objects.hash(id);
    }
}
