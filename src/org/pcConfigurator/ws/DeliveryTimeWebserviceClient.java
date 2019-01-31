package org.pcConfigurator.ws;

import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceRef;

@WebServiceClient
public class DeliveryTimeWebserviceClient {


    private static DeliveryPortService deliveryPortService = new DeliveryPortService();

    public static int getDeliveryTimeForArticleID(final long articleID) {
        DeliveryPort deliveryPortSoap11 = deliveryPortService.getDeliveryPortSoap11();
        GetDeliveryTimeRequest deliveryTimeRequest = new GetDeliveryTimeRequest();
        deliveryTimeRequest.setArticleID(articleID);
        GetDeliveryTimeResponse deliveryTime = deliveryPortSoap11.getDeliveryTime(deliveryTimeRequest);
        return deliveryTime.getDeliveryTime().getDeliveryTime();
    }
}
