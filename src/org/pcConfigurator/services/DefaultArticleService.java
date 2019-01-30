package org.pcConfigurator.services;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.beans.ConfigurationBean;
import org.pcConfigurator.converter.ArticleToArticleTeaserBeanConverter;
import org.pcConfigurator.entities.*;
import org.pcConfigurator.repositories.ArticleRepository;
import org.pcConfigurator.strategies.CompatibilityStrategy;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Stateless
public class DefaultArticleService implements ArticleService, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private ArticleRepository articleRepository;

    @Inject
    Instance<CompatibilityStrategy> compatibilityStrategies;

    @Inject
    private ArticleToArticleTeaserBeanConverter articleToArticleTeaserBeanConverter;

    @Override
    public Set<ArticleTeaserBean> getTeaserArticleList() {
        Set<Article> allDiscountedArticles = this.articleRepository.findAllDiscountedArticles();
        if (allDiscountedArticles != null) {
            return allDiscountedArticles.stream().limit(6).map(article -> this.articleToArticleTeaserBeanConverter.convert(article))
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public Set<ArticleTeaserBean> getCompatibleArticleTeaserBeansOfType(ComponentType componentType, ConfigurationBean currentConfig) {
        Set<Article> articles = this.articleRepository.findByComponentType(componentType);
        return articles.stream().filter(article -> {
            for (CompatibilityStrategy compatibilityStrategy : compatibilityStrategies) {
                if (!compatibilityStrategy.isCompatibleToCurrentConfig(article, currentConfig))
                    return false;
            }
            return true;
        }).map(article -> this.articleToArticleTeaserBeanConverter
                .convertConsideringCompatibility(article, false)).collect(Collectors.toSet());
    }

    public Article getArticleForArticleTeaserBean(final ArticleTeaserBean source) {
       return this.articleRepository.findOne(source.getArticleID());
    }

    @Override
    public List<ArticleTeaserBean> searchArticleByName(String articleName) {
        return null;
    }

    @Override
    public void createDummyData() {
        // start of with the slot types
        // ######################## GPU ########################################################################
        SlotType gpu_3 = new SlotType();
        gpu_3.setSlotName("PCI-E");
        gpu_3.setDescription("This slot fits GPUs and is the most common slot type for modern graphic cards.");
        entityManager.persist(gpu_3);

        SlotType gpu_1 = new SlotType();
        gpu_1.setSlotName("PCI");
        gpu_1.setDescription("This slot fits GPUs but is slower and older than the PCI-E slot type.");
        entityManager.persist(gpu_1);

        SlotType gpu_2 = new SlotType();
        gpu_2.setSlotName("AGP");
        gpu_2.setDescription("This slot fits GPUs and is a very special type of slot for graphic cars.");
        entityManager.persist(gpu_2);

        // ######################## CPU ########################################################################
        SlotType cpu_1 = new SlotType();
        cpu_1.setSlotName("LGA 1155");
        cpu_1.setDescription("This slot fits CPUs and is used to fit intel processors of the series i3, i5 and i7 on it.");
        entityManager.persist(cpu_1);

        SlotType cpu_2 = new SlotType();
        cpu_2.setSlotName("LGA 1150");
        cpu_2.setDescription("This slot fits CPUs and is used to fit Haswell and Broadwell CPUs on it.");
        entityManager.persist(cpu_2);

        SlotType cpu_3 = new SlotType();
        cpu_3.setSlotName("AM4");
        cpu_3.setDescription("This slot fits CPUs and is used to fit Ryzen CPUs with bristol-ridge architecture on it.");
        entityManager.persist(cpu_3);

        SlotType cpu_4 = new SlotType();
        cpu_4.setSlotName("AM3");
        cpu_4.setDescription("This slot fits CPUs and is used to fit Ryzen CPUs on it.");
        entityManager.persist(cpu_4);

        // ######################## RAM ########################################################################
        SlotType ram_1 = new SlotType();
        ram_1.setSlotName("DDR3");
        ram_1.setDescription("This slot fits RAM and is used to fit all DDR3-memorysticks on it.");
        entityManager.persist(ram_1);

        SlotType ram_2 = new SlotType();
        ram_2.setSlotName("DDR4");
        ram_2.setDescription("This slot fits RAM and is used to fit all DDR4-memorysticks on it.");
        entityManager.persist(ram_2);

        // ######################## HDD ########################################################################
        // note: SATA is actually backwards compatible, so SATA 3 HDDs can fit any other SATA'-slot type on the motherboard
        // and vice versa. But for the sake of demonstration, we will pretend that this is not the case, so we can have some more
        // slot types and incompatibility-scenarios
        SlotType hdd_1 = new SlotType();
        hdd_1.setSlotName("SATA1");
        hdd_1.setDescription("This slot fits HDDs and SSDs with a slow speed.");
        entityManager.persist(hdd_1);

        SlotType hdd_2 = new SlotType();
        hdd_2.setSlotName("SATA2");
        hdd_2.setDescription("This slot fits HDDs and SSDs with a medium speed.");
        entityManager.persist(hdd_2);

        SlotType hdd_3 = new SlotType();
        hdd_3.setSlotName("SATA3");
        hdd_3.setDescription("This slot fits HDDs and SSDs with a fast speed.");
        entityManager.persist(hdd_3);

        // ######################## Peripheral ########################################################################
        SlotType per_1 = new SlotType();
        per_1.setSlotName("USBx");
        per_1.setDescription("This slot fits peripheral devices such as mouses, keyboards, etc and are backwards compatible.");
        entityManager.persist(per_1);

        SlotType per_2 = new SlotType();
        per_2.setSlotName("USB-C");
        per_2.setDescription("This slot fits peripheral devices such as mouses, keyboards, etc but is not compatible to USBx.");
        entityManager.persist(per_2);

        SlotType per_3 = new SlotType();
        per_3.setSlotName("audio jack");
        per_3.setDescription("This slot fits peripheral devices, typically phones or headphones.");
        entityManager.persist(per_3);

        // Relevant only for old monitors
        SlotType per_4 = new SlotType();
        per_4.setSlotName("VGA");
        per_4.setDescription("This slot fits monitors, usually ending up using onboard-graphiccards on old motherboards.");
        entityManager.persist(per_4);



        // next is a few motherboards with a wild combination of supported slot types
        // these are NOT supposed to be realistic, they are just supposed to support a good amount of variety in terms of combination

        Article dummyMb_1 = new Article("Dummy Motherboard 1");
        Set<SlotRestriction> slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(cpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_1.setSlotRestrictions(slotRestrictions);
        dummyMb_1.setDisplayName("Dummy Motherboard 1");
        dummyMb_1.setPriceRows(null);
        entityManager.persist(dummyMb_1);

        Article dummyMb_2 = new Article("Dummy Motherboard 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_1.setSlotRestrictions(slotRestrictions);
        dummyMb_2.setDisplayName("Dummy Motherboard 2");
        dummyMb_2.setPriceRows(null);
        entityManager.persist(dummyMb_2);

        Article dummyMb_3 = new Article("Dummy Motherboard 3");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_3.setSlotRestrictions(slotRestrictions);
        dummyMb_3.setDisplayName("Dummy Motherboard 3");
        dummyMb_3.setPriceRows(null);
        entityManager.persist(dummyMb_3);

        Article dummyMb_4 = new Article("Dummy Motherboard 4");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_4.setSlotRestrictions(slotRestrictions);
        dummyMb_4.setDisplayName("Dummy Motherboard 4");
        dummyMb_4.setPriceRows(null);
        entityManager.persist(dummyMb_4);

        Article dummyMb_5 = new Article("Dummy Motherboard 5");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_5.setSlotRestrictions(slotRestrictions);
        dummyMb_5.setDisplayName("Dummy Motherboard 5");
        dummyMb_5.setPriceRows(null);
        entityManager.persist(dummyMb_5);

        Article dummyMb_6 = new Article("Dummy Motherboard 6");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_6.setSlotRestrictions(slotRestrictions);
        dummyMb_6.setDisplayName("Dummy Motherboard 6");
        dummyMb_6.setPriceRows(null);
        entityManager.persist(dummyMb_6);


        Article dummyMb_7 = new Article("Dummy Motherboard 7");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_7.setSlotRestrictions(slotRestrictions);
        dummyMb_7.setDisplayName("Dummy Motherboard 7");
        dummyMb_7.setPriceRows(null);
        entityManager.persist(dummyMb_7);

        Article dummyMb_8 = new Article("Dummy Motherboard 8");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_8.setSlotRestrictions(slotRestrictions);
        dummyMb_8.setDisplayName("Dummy Motherboard 8");
        dummyMb_8.setPriceRows(null);
        entityManager.persist(dummyMb_8);

        Article dummyMb_9 = new Article("Dummy Motherboard 9");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_9.setSlotRestrictions(slotRestrictions);
        dummyMb_9.setDisplayName("Dummy Motherboard 9");
        dummyMb_9.setPriceRows(null);
        entityManager.persist(dummyMb_9);

        Article dummyMb_10 = new Article("Dummy Motherboard 10");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_10.setSlotRestrictions(slotRestrictions);
        dummyMb_10.setDisplayName("Dummy Motherboard 10");
        dummyMb_10.setPriceRows(null);
        entityManager.persist(dummyMb_10);

        Article dummyMb_11 = new Article("Dummy Motherboard 11");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_11.setSlotRestrictions(slotRestrictions);
        dummyMb_11.setDisplayName("Dummy Motherboard 11");
        dummyMb_11.setPriceRows(null);
        entityManager.persist(dummyMb_11);

        Article dummyMb_12 = new Article("Dummy Motherboard 12");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_12.setSlotRestrictions(slotRestrictions);
        dummyMb_12.setDisplayName("Dummy Motherboard 12");
        dummyMb_12.setPriceRows(null);
        entityManager.persist(dummyMb_12);


        Article dummyMb_13 = new Article("Dummy Motherboard 13");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_13.setSlotRestrictions(slotRestrictions);
        dummyMb_13.setDisplayName("Dummy Motherboard 13");
        dummyMb_13.setPriceRows(null);
        entityManager.persist(dummyMb_13);

        Article dummyMb_14 = new Article("Dummy Motherboard 14");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_14.setSlotRestrictions(slotRestrictions);
        dummyMb_14.setDisplayName("Dummy Motherboard 14");
        dummyMb_14.setPriceRows(null);
        entityManager.persist(dummyMb_14);

        Article dummyMb_15 = new Article("Dummy Motherboard 15");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_2, SlotRestrictionType.PROVIDES));
        dummyMb_15.setSlotRestrictions(slotRestrictions);
        dummyMb_15.setDisplayName("Dummy Motherboard 15");
        dummyMb_15.setPriceRows(null);
        entityManager.persist(dummyMb_15);

        Article dummyMb_16 = new Article("Dummy Motherboard 16");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(gpu_2, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(hdd_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.PROVIDES));
        slotRestrictions.add(new SlotRestriction(ram_1, SlotRestrictionType.PROVIDES));
        dummyMb_16.setSlotRestrictions(slotRestrictions);
        dummyMb_16.setDisplayName("Dummy Motherboard 16");
        dummyMb_16.setPriceRows(null);
        entityManager.persist(dummyMb_16);

        // CPUs
        Article dummyCPU_1 = new Article("Dummy CPU 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1));
        dummyCPU_1.setDisplayName("Dummy CPU 1");
        dummyCPU_1.setPriceRows(null);
        dummyCPU_1.setType(ComponentType.CPU);
        dummyCPU_1.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_1);

        Article dummyCPU_2 = new Article("Dummy CPU 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1));
        dummyCPU_2.setDisplayName("Dummy CPU 2");
        dummyCPU_2.setPriceRows(null);
        dummyCPU_2.setType(ComponentType.CPU);
        dummyCPU_2.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_2);

        Article dummyCPU_3 = new Article("Dummy CPU 3");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1));
        dummyCPU_3.setDisplayName("Dummy CPU 3");
        dummyCPU_3.setPriceRows(null);
        dummyCPU_3.setType(ComponentType.CPU);
        dummyCPU_3.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_3);

        Article dummyCPU_4 = new Article("Dummy CPU 4");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_1));
        dummyCPU_4.setDisplayName("Dummy CPU 4");
        dummyCPU_4.setPriceRows(null);
        dummyCPU_4.setType(ComponentType.CPU);
        dummyCPU_4.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_4);

        Article dummyCPU_5 = new Article("Dummy CPU 5");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2));
        dummyCPU_5.setDisplayName("Dummy CPU 5");
        dummyCPU_5.setPriceRows(null);
        dummyCPU_5.setType(ComponentType.CPU);
        dummyCPU_5.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_5);

        Article dummyCPU_6 = new Article("Dummy CPU 6");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2));
        dummyCPU_6.setDisplayName("Dummy CPU 6");
        dummyCPU_6.setPriceRows(null);
        dummyCPU_6.setType(ComponentType.CPU);
        dummyCPU_6.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_6);

        Article dummyCPU_7 = new Article("Dummy CPU 7");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2));
        dummyCPU_7.setDisplayName("Dummy CPU 7");
        dummyCPU_7.setPriceRows(null);
        dummyCPU_7.setType(ComponentType.CPU);
        dummyCPU_7.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_7);

        Article dummyCPU_8 = new Article("Dummy CPU 8");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_2));
        dummyCPU_8.setDisplayName("Dummy CPU 8");
        dummyCPU_8.setPriceRows(null);
        dummyCPU_8.setType(ComponentType.CPU);
        dummyCPU_8.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_8);

        Article dummyCPU_9 = new Article("Dummy CPU 9");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_3));
        dummyCPU_9.setDisplayName("Dummy CPU 9");
        dummyCPU_9.setPriceRows(null);
        dummyCPU_9.setType(ComponentType.CPU);
        dummyCPU_9.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_9);

        Article dummyCPU_10 = new Article("Dummy CPU 10");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_3));
        dummyCPU_10.setDisplayName("Dummy CPU 10");
        dummyCPU_10.setPriceRows(null);
        dummyCPU_10.setType(ComponentType.CPU);
        dummyCPU_10.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_10);

        Article dummyCPU_11 = new Article("Dummy CPU 11");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_3));
        dummyCPU_11.setDisplayName("Dummy CPU 11");
        dummyCPU_11.setPriceRows(null);
        dummyCPU_11.setType(ComponentType.CPU);
        dummyCPU_11.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_11);

        Article dummyCPU_12 = new Article("Dummy CPU 12");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_3));
        dummyCPU_12.setDisplayName("Dummy CPU 12");
        dummyCPU_12.setPriceRows(null);
        dummyCPU_12.setType(ComponentType.CPU);
        dummyCPU_12.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_12);

        Article dummyCPU_13 = new Article("Dummy CPU 13");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4));
        dummyCPU_13.setDisplayName("Dummy CPU 13");
        dummyCPU_13.setPriceRows(null);
        dummyCPU_13.setType(ComponentType.CPU);
        dummyCPU_13.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_13);

        Article dummyCPU_14 = new Article("Dummy CPU 14");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4));
        dummyCPU_14.setDisplayName("Dummy CPU 14");
        dummyCPU_14.setPriceRows(null);
        dummyCPU_14.setType(ComponentType.CPU);
        dummyCPU_14.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_14);

        Article dummyCPU_15 = new Article("Dummy CPU 15");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4));
        dummyCPU_15.setDisplayName("Dummy CPU 15");
        dummyCPU_15.setPriceRows(null);
        dummyCPU_15.setType(ComponentType.CPU);
        dummyCPU_15.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_15);

        Article dummyCPU_16 = new Article("Dummy CPU 16");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(cpu_4));
        dummyCPU_16.setDisplayName("Dummy CPU 16");
        dummyCPU_16.setPriceRows(null);
        dummyCPU_16.setType(ComponentType.CPU);
        dummyCPU_16.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyCPU_16);



        // GPUs
        Article dummyGPU_1 = new Article("Dummy GPU 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_1));
        dummyGPU_1.setDisplayName("Dummy GPU 1");
        dummyGPU_1.setPriceRows(null);
        dummyGPU_1.setType(ComponentType.GPU);
        dummyGPU_1.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_1);

        Article dummyGPU_2 = new Article("Dummy GPU 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_1));
        dummyGPU_2.setDisplayName("Dummy GPU 2");
        dummyGPU_2.setPriceRows(null);
        dummyGPU_2.setType(ComponentType.GPU);
        dummyGPU_2.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_2);

        Article dummyGPU_3 = new Article("Dummy GPU 3");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_1));
        dummyGPU_3.setDisplayName("Dummy GPU 3");
        dummyGPU_3.setPriceRows(null);
        dummyGPU_3.setType(ComponentType.GPU);
        dummyGPU_3.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_3);

        Article dummyGPU_4 = new Article("Dummy GPU 4");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_2));
        dummyGPU_4.setDisplayName("Dummy GPU 4");
        dummyGPU_4.setPriceRows(null);
        dummyGPU_4.setType(ComponentType.GPU);
        dummyGPU_4.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_4);

        Article dummyGPU_5 = new Article("Dummy GPU 5");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_2));
        dummyGPU_5.setDisplayName("Dummy GPU 5");
        dummyGPU_5.setPriceRows(null);
        dummyGPU_5.setType(ComponentType.GPU);
        dummyGPU_5.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_5);

        Article dummyGPU_6 = new Article("Dummy GPU 6");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_2));
        dummyGPU_6.setDisplayName("Dummy GPU 6");
        dummyGPU_6.setPriceRows(null);
        dummyGPU_6.setType(ComponentType.GPU);
        dummyGPU_6.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_6);

        Article dummyGPU_7 = new Article("Dummy GPU 7");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_2));
        dummyGPU_7.setDisplayName("Dummy GPU 7");
        dummyGPU_7.setPriceRows(null);
        dummyGPU_7.setType(ComponentType.GPU);
        dummyGPU_7.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_7);

        Article dummyGPU_8 = new Article("Dummy GPU 8");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_3));
        dummyGPU_8.setDisplayName("Dummy GPU 8");
        dummyGPU_8.setPriceRows(null);
        dummyGPU_8.setType(ComponentType.GPU);
        dummyGPU_8.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_8);

        Article dummyGPU_9 = new Article("Dummy GPU 9");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_3));
        dummyGPU_9.setDisplayName("Dummy GPU 9");
        dummyGPU_9.setPriceRows(null);
        dummyGPU_9.setType(ComponentType.GPU);
        dummyGPU_9.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_9);

        Article dummyGPU_10 = new Article("Dummy GPU 10");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_3));
        dummyGPU_10.setDisplayName("Dummy GPU 10");
        dummyGPU_10.setPriceRows(null);
        dummyGPU_10.setType(ComponentType.GPU);
        dummyGPU_10.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_10);

        Article dummyGPU_11 = new Article("Dummy GPU 11");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(gpu_3));
        dummyGPU_11.setDisplayName("Dummy GPU 11");
        dummyGPU_11.setPriceRows(null);
        dummyGPU_11.setType(ComponentType.GPU);
        dummyGPU_11.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyGPU_11);

        // RAM
        Article dummyRAM_1 = new Article("Dummy RAM 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(ram_1));
        dummyRAM_1.setDisplayName("Dummy RAM 1");
        dummyRAM_1.setPriceRows(null);
        dummyRAM_1.setType(ComponentType.RAM);
        dummyRAM_1.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyRAM_1);

        Article dummyRAM_2 = new Article("Dummy RAM 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(ram_1));
        dummyRAM_2.setDisplayName("Dummy RAM 2");
        dummyRAM_2.setPriceRows(null);
        dummyRAM_2.setType(ComponentType.RAM);
        dummyRAM_2.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyRAM_2);

        Article dummyRAM_3 = new Article("Dummy RAM 3");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(ram_1));
        dummyRAM_3.setDisplayName("Dummy RAM 3");
        dummyRAM_3.setPriceRows(null);
        dummyRAM_3.setType(ComponentType.RAM);
        dummyRAM_3.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyRAM_3);

        Article dummyRAM_4 = new Article("Dummy RAM 4");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(ram_2));
        dummyRAM_4.setDisplayName("Dummy RAM 4");
        dummyRAM_4.setPriceRows(null);
        dummyRAM_4.setType(ComponentType.RAM);
        dummyRAM_4.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyRAM_4);

        Article dummyRAM_5 = new Article("Dummy RAM 5");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(ram_2));
        dummyRAM_5.setDisplayName("Dummy RAM 5");
        dummyRAM_5.setPriceRows(null);
        dummyRAM_5.setType(ComponentType.RAM);
        dummyRAM_5.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyRAM_5);

        Article dummyRAM_6 = new Article("Dummy RAM 6");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(ram_2));
        dummyRAM_6.setDisplayName("Dummy RAM 6");
        dummyRAM_6.setPriceRows(null);
        dummyRAM_6.setType(ComponentType.RAM);
        dummyRAM_6.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyRAM_6);

        Article dummyRAM_7 = new Article("Dummy RAM 7");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(ram_2));
        dummyRAM_7.setDisplayName("Dummy RAM 7");
        dummyRAM_7.setPriceRows(null);
        dummyRAM_7.setType(ComponentType.RAM);
        dummyRAM_7.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyRAM_7);

        // HDD

        Article dummyHDD_1 = new Article("Dummy HDD 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_1));
        dummyHDD_1.setDisplayName("Dummy HDD 1");
        dummyHDD_1.setPriceRows(null);
        dummyHDD_1.setType(ComponentType.HDD);
        dummyHDD_1.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_1);

        Article dummyHDD_2 = new Article("Dummy HDD 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_1));
        dummyHDD_2.setDisplayName("Dummy HDD 2");
        dummyHDD_2.setPriceRows(null);
        dummyHDD_2.setType(ComponentType.HDD);
        dummyHDD_2.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_1);

        Article dummyHDD_3 = new Article("Dummy HDD 3");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_1));
        dummyHDD_3.setDisplayName("Dummy HDD 3");
        dummyHDD_3.setPriceRows(null);
        dummyHDD_3.setType(ComponentType.HDD);
        dummyHDD_3.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_3);

        Article dummyHDD_4 = new Article("Dummy HDD 4");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_2));
        dummyHDD_4.setDisplayName("Dummy HDD 4");
        dummyHDD_4.setPriceRows(null);
        dummyHDD_4.setType(ComponentType.HDD);
        dummyHDD_4.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_4);

        Article dummyHDD_5 = new Article("Dummy HDD 5");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_2));
        dummyHDD_5.setDisplayName("Dummy HDD 5");
        dummyHDD_5.setPriceRows(null);
        dummyHDD_5.setType(ComponentType.HDD);
        dummyHDD_5.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_5);

        Article dummyHDD_6 = new Article("Dummy HDD 6");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_2));
        dummyHDD_6.setDisplayName("Dummy HDD 6");
        dummyHDD_6.setPriceRows(null);
        dummyHDD_6.setType(ComponentType.HDD);
        dummyHDD_6.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_6);

        Article dummyHDD_7 = new Article("Dummy HDD 7");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_3));
        dummyHDD_7.setDisplayName("Dummy HDD 7");
        dummyHDD_7.setPriceRows(null);
        dummyHDD_7.setType(ComponentType.HDD);
        dummyHDD_7.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_7);

        Article dummyHDD_8 = new Article("Dummy HDD 8");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_3));
        dummyHDD_8.setDisplayName("Dummy HDD 8");
        dummyHDD_8.setPriceRows(null);
        dummyHDD_8.setType(ComponentType.HDD);
        dummyHDD_8.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_8);

        Article dummyHDD_9 = new Article("Dummy HDD 9");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(hdd_3));
        dummyHDD_9.setDisplayName("Dummy HDD 9");
        dummyHDD_9.setPriceRows(null);
        dummyHDD_9.setType(ComponentType.HDD);
        dummyHDD_9.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyHDD_9);


        // PSU - they have no requirements
        Article dummyPSU_1 = new Article("Dummy PSU 1");
        dummyPSU_1.setDisplayName("Dummy PSU 1");
        dummyPSU_1.setPriceRows(null);
        dummyPSU_1.setType(ComponentType.PSU);
        entityManager.persist(dummyPSU_1);

        Article dummyPSU_2 = new Article("Dummy PSU 2");
        dummyPSU_2.setDisplayName("Dummy PSU 2");
        dummyPSU_2.setPriceRows(null);
        dummyPSU_2.setType(ComponentType.PSU);
        entityManager.persist(dummyPSU_1);

        Article dummyPSU_3 = new Article("Dummy PSU 3");
        dummyPSU_3.setDisplayName("Dummy PSU 3");
        dummyPSU_3.setPriceRows(null);
        dummyPSU_3.setType(ComponentType.PSU);
        entityManager.persist(dummyPSU_3);

        // Peripheral stuf
        Article dummyPeriph_1 = new Article("Dummy Maus 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.REQUIRES));
        dummyPeriph_1.setDisplayName("Dummy Maus 1");
        dummyPeriph_1.setPriceRows(null);
        dummyPeriph_1.setType(ComponentType.PERIPHERAL);
        dummyPeriph_1.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_1);

        Article dummyPeriph_2 = new Article("Dummy Maus 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.REQUIRES));
        dummyPeriph_2.setDisplayName("Dummy Maus 2");
        dummyPeriph_2.setPriceRows(null);
        dummyPeriph_2.setType(ComponentType.PERIPHERAL);
        dummyPeriph_2.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_2);

        Article dummyPeriph_3 = new Article("Dummy Keyboard 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.REQUIRES));
        dummyPeriph_3.setDisplayName("Dummy Keyboard 1");
        dummyPeriph_3.setPriceRows(null);
        dummyPeriph_3.setType(ComponentType.PERIPHERAL);
        dummyPeriph_3.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_3);

        Article dummyPeriph_4 = new Article("Dummy Keyboard 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.REQUIRES));
        dummyPeriph_4.setDisplayName("Dummy Keyboard 2");
        dummyPeriph_4.setPriceRows(null);
        dummyPeriph_4.setType(ComponentType.PERIPHERAL);
        dummyPeriph_4.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_4);

        Article dummyPeriph_5 = new Article("Dummy Headset 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_1, SlotRestrictionType.REQUIRES));
        dummyPeriph_5.setDisplayName("Dummy Headset 1");
        dummyPeriph_5.setPriceRows(null);
        dummyPeriph_5.setType(ComponentType.PERIPHERAL);
        dummyPeriph_5.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_5);

        Article dummyPeriph_6 = new Article("Dummy Headset 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_2, SlotRestrictionType.REQUIRES));
        dummyPeriph_6.setDisplayName("Dummy Headset 2");
        dummyPeriph_6.setPriceRows(null);
        dummyPeriph_6.setType(ComponentType.PERIPHERAL);
        dummyPeriph_6.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_6);

        Article dummyPeriph_7 = new Article("Dummy Lautsprecher 1");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_3, SlotRestrictionType.REQUIRES));
        dummyPeriph_7.setDisplayName("Dummy Lautsprecher 1");
        dummyPeriph_7.setPriceRows(null);
        dummyPeriph_7.setType(ComponentType.PERIPHERAL);
        dummyPeriph_7.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_7);

        Article dummyPeriph_8 = new Article("Dummy Lautsprecher 2");
        slotRestrictions = new HashSet<>();
        slotRestrictions.add(new SlotRestriction(per_4, SlotRestrictionType.REQUIRES));
        dummyPeriph_8.setDisplayName("Dummy Lautsprecher 2");
        dummyPeriph_8.setPriceRows(null);
        dummyPeriph_8.setType(ComponentType.PERIPHERAL);
        dummyPeriph_8.setSlotRestrictions(slotRestrictions);
        entityManager.persist(dummyPeriph_8);

    }

    @Override
    public void createDummyPrices() {
        List<Article> articleList = entityManager.createQuery("select e from Article e", Article.class).getResultList();
        articleList.forEach(article -> {
            Set<PriceRow> priceRowList = new HashSet<>();
            PriceRow basePrice = new PriceRow();
            basePrice.setNetPrice(BigDecimal.valueOf(ThreadLocalRandom.current()
                    .nextDouble(100, 501)).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            basePrice.setTaxMultiplier(1.19);
            basePrice.setPromotion(false);
            basePrice.setValidFrom(new Date());
            Calendar currentTimeCalendar = Calendar.getInstance();
            currentTimeCalendar.add(Calendar.YEAR, 10);
            basePrice.setValidUntil(currentTimeCalendar.getTime());
            priceRowList.add(basePrice);

            if (article.getArticleID() % 3 == 0) {
                PriceRow promotionPrice = new PriceRow();
                promotionPrice.setNetPrice(basePrice.getNetPrice().multiply(BigDecimal.valueOf(0.85))
                        .setScale(2, BigDecimal.ROUND_HALF_EVEN));
                promotionPrice.setTaxMultiplier(basePrice.getTaxMultiplier());
                promotionPrice.setValidFrom(basePrice.getValidFrom());
                promotionPrice.setValidUntil(basePrice.getValidUntil());
                promotionPrice.setPromotion(true);
                priceRowList.add(promotionPrice);
            }
            article.setPriceRows(priceRowList);
            // hier kein expliziter Aufruf an den entityManager, da die entities durch das vorherige Laden aus der DB
            // (Query) im "Managed" state sind und somit automatisch auch Richtung Datenbank synchronisiert werden

        });

    }
}
