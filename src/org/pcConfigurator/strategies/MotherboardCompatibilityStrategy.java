package org.pcConfigurator.strategies;

import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.entities.*;

import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class MotherboardCompatibilityStrategy extends AbstractCompatbilityStrategy implements CompatibilityStrategy {

    @Override
    public boolean isCompatibleToCurrentConfig(Article article, ConfigurationBean currentConfig) {
       if (!this.isApplicable(article))
            return true;

        HashMap<SlotType, Integer> providedSlots = article.getSlotRestrictionsOfType(SlotRestrictionType.PROVIDES);



        /*
        Set<Article> notProvidedForArticles = new HashSet<>();

        // Suche alle Artikel in dieser Konfiguration, die durch das hinzuzufügende Motherboard (parameter article) nicht
        // in ihren benötigten Slots abgedeckt sind.
        // [Der Einfachheit halber haben wir hier nicht die Logik der Quantität abgebildet -> wie viele Slots eines Typen ein Motherboard
        // unterstützt und wie viele Typen eines Slots eine Komponente benötigt. => Das wäre etwas, was in Zukunft über die Erweiterung
        // der ComptatibilityStrategies möglich wäre. ]
        for (Article configuredArticle : currentConfig.getConfiguredComponents()) {
            Set<SlotType> requiredSlots = getSlotTypesOfRestrictionTypeForArticle(configuredArticle, SlotRestrictionType.REQUIRES);
            for (SlotType slotType : requiredSlots) {
                if (providedSlots.contains(slotType)) {
                    requiredSlots.remove(slotType);
                    providedSlots.remove(slotType);
                }
            }
            if (!requiredSlots.isEmpty())
                notProvidedForArticles.add(configuredArticle);
        }

        // Alle Artikel werden vom Motherboard unterstützt -> alles gut
        if(notProvidedForArticles.isEmpty())
            return true;


        // In diesem Fall gibt es Artikel, die nicht durch die Slots des Motherboards abgedeckt werden
        // Es gilt jetzt noch prüfen, ob eventuell andere Artikel in der Konfiguration diese Slots bereitstellen

        // Welcher Artikel provided welchen Slots?
        HashMap<Article, Set<SlotType>> articleProvidedSlotsMap = new HashMap<>();
        // Motherboard (hinzufügender ArtikeL) mit aufnehmen, da es ja teilweise den Bedarf abdecken könnte
        // (neu berechnen, da da providedSlots ja in der vorherigen Logik reduziert wurde)
        articleProvidedSlotsMap.put(article, getSlotTypesOfRestrictionTypeForArticle(article, SlotRestrictionType.PROVIDES));
        currentConfig.getConfiguredComponents().forEach(configuredArticle ->
                articleProvidedSlotsMap.put(configuredArticle, getSlotTypesOfRestrictionTypeForArticle(configuredArticle, SlotRestrictionType.PROVIDES)));

        // Für jeden bisher nicht abgedeckten Artikel überprüfen wir, ob es durch die Menge der durch andere Artikel provideten Slots abgedeckt werden kann
        for (Article notProvidedForArticle : notProvidedForArticles) {
            Set<SlotType> requiredArticleSlots = getSlotTypesOfRestrictionTypeForArticle(notProvidedForArticle, SlotRestrictionType.REQUIRES);
            for (Article potentiallyProvidingArticle : articleProvidedSlotsMap.keySet()) {
                // Artikel dürfen nicht für sich selber providen
                if (!potentiallyProvidingArticle.equals(notProvidedForArticle)) {
                    for (SlotType slotType : articleProvidedSlotsMap.get(potentiallyProvidingArticle)) {
                        if (requiredArticleSlots.contains(slotType)) {
                            requiredArticleSlots.remove(slotType);
                            // Dieser Slot ist jetzt natürlich "verbraucht" -> entfernen
                            articleProvidedSlotsMap.get(potentiallyProvidingArticle).remove(slotType);
                        }
                    }
                }
            }

            if (!requiredArticleSlots.isEmpty())
                return false;
        }
        // Es gab keinen Artikel, für den nicht vollständig provided wurde -> alles gut
        return true;*/




    }

    @Override
    public boolean isApplicable(Article article) {
        return ComponentType.MB.equals(article.getType());
    }
}
