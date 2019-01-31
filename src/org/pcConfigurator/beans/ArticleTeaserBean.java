package org.pcConfigurator.beans;

public class ArticleTeaserBean {

    private long articleID;
    private String price;
    private String discountedPrice;
    private String articleName;
    private String articleDisplayName;
    private boolean compStatus;
    private int deliveryTime;


    public long getArticleID() {
        return articleID;
    }

    public void setArticleID(long articleID) {
        this.articleID = articleID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleDisplayName() {
        return articleDisplayName;
    }

    public void setArticleDisplayName(String articleDisplayName) {
        this.articleDisplayName = articleDisplayName;
    }

    public boolean getCompStatus() {
        return compStatus;
    }

    public void setCompStatus(boolean compStatus) {
        this.compStatus = compStatus;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
