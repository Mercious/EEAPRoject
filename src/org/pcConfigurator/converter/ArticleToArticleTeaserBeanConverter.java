package org.pcConfigurator.converter;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.Article;
import org.pcConfigurator.entities.PriceRow;
import org.pcConfigurator.managed.ConfigurationManager;
import org.pcConfigurator.strategies.CompatibilityStrategy;
import org.pcConfigurator.ws.DeliveryTimeWebserviceClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class ArticleToArticleTeaserBeanConverter {
    @Inject
    private Instance<CompatibilityStrategy> compatibilityStrategy;
    @Inject
    private ConfigurationManager configurationManager;
    @Inject
    private Logger LOG;

    public ArticleTeaserBean convert(Article source) {
        return convertConsideringCompatibility(source, true, true);
    }

    public ArticleTeaserBean convertConsideringCompatibility(Article source, boolean checkCompatibility, boolean checkDeliveryTime) {
        if (source == null)
            return null;
        ArticleTeaserBean articleTeaserBean = new ArticleTeaserBean();
        articleTeaserBean.setArticleID(source.getArticleID());
        articleTeaserBean.setArticleName(source.getArticleName());
        articleTeaserBean.setArticleDisplayName(source.getDisplayName());
        articleTeaserBean.setCompStatus(true);
        if (checkCompatibility) {
            ConfigurationBean currentConfig = configurationManager.getCurrentConfiguration();
            compatibilityStrategy.forEach(compatibilityStrategy -> {
                if (compatibilityStrategy.isApplicable(source) && articleTeaserBean.getCompStatus()) {
                    articleTeaserBean.setCompStatus(compatibilityStrategy.isCompatibleToCurrentConfig(source, currentConfig));
                }
            });
        }
        articleTeaserBean.setPrice(this.getLowestTodayValidPrice(source.getPriceRows(), false));
        articleTeaserBean.setDiscountedPrice(this.getLowestTodayValidPrice(source.getPriceRows(), true));
        int deliveryTimeForArticleID = 5;
        if (checkDeliveryTime) {
            try {
                deliveryTimeForArticleID = DeliveryTimeWebserviceClient.getDeliveryTimeForArticleID(source.getArticleID());
            } catch (Exception e) {
                LOG.warning("Calling Delivery Time Webservice for Article " + source.getArticleID() + " has FAILED! " +
                        "Setting default delivery time of 5.");
            }
        }
        // standard Wert von 5 = sehr lange, falls Webservice-Call fehlschlägt
        articleTeaserBean.setDeliveryTime(deliveryTimeForArticleID);
        return articleTeaserBean;
    }

    private String getLowestTodayValidPrice(final Set<PriceRow> priceRows, boolean discounted) {
        BigDecimal lowestPrice = null;
        for (PriceRow priceRow : priceRows) {
            if (new Date().after(priceRow.getValidFrom()) && new Date().before(priceRow.getValidUntil())
                    && priceRow.isPromotion() == discounted) {
                BigDecimal taxedPrice = priceRow.getNetPrice().multiply(new BigDecimal(priceRow.getTaxMultiplier()));
                if (lowestPrice == null)
                    lowestPrice = taxedPrice;
                else
                    lowestPrice = lowestPrice.compareTo(taxedPrice) > 0 ? taxedPrice : lowestPrice;
            }
        }
        return lowestPrice != null ? new DecimalFormat("#0.##").format(lowestPrice) : "";
    }
}
