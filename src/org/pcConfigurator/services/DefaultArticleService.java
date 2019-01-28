package org.pcConfigurator.services;

import org.pcConfigurator.beans.ArticleTeaserBean;
import org.pcConfigurator.converter.ArticleToArticleTeaserBeanConverter;
import org.pcConfigurator.entities.*;
import org.pcConfigurator.repositories.ArticleRepository;
import org.pcConfigurator.strategies.CompatibilityStrategy;
import sun.security.krb5.Config;

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
    public Set<Article> getCompatibleArticlesOfType(ComponentType componentType, Configuration currentConfig) {
        Set<Article> articles = this.articleRepository.findByComponentType(componentType);
        return articles.stream().filter(article -> {
            for (CompatibilityStrategy compatibilityStrategy : compatibilityStrategies) {
                if (!compatibilityStrategy.isCompatibleToCurrentConfig(article, currentConfig))
                    return false;
            }
            return true;
        }).collect(Collectors.toSet());
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

        Motherboard dummyMb_1 = new Motherboard("Dummy Motherboard 1");
        Set<SlotType> slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_1);
        slotTypeSet.add(gpu_1);
        slotTypeSet.add(hdd_1);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_2);
        slotTypeSet.add(per_3);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_1);
        dummyMb_1.setProvidedSlots(slotTypeSet);
        dummyMb_1.setDisplayName("Dummy Motherboard 1");
        dummyMb_1.setPriceRows(null);
        entityManager.persist(dummyMb_1);

        Article dummyMb_2 = new Motherboard("Dummy Motherboard 2");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_2);
        slotTypeSet.add(gpu_2);
        slotTypeSet.add(hdd_2);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_2);
        slotTypeSet.add(ram_2);
        dummyMb_1.setProvidedSlots(slotTypeSet);
        dummyMb_2.setDisplayName("Dummy Motherboard 2");
        dummyMb_2.setPriceRows(null);
        entityManager.persist(dummyMb_2);

        Motherboard dummyMb_3 = new Motherboard("Dummy Motherboard 3");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_3);
        slotTypeSet.add(gpu_3);
        slotTypeSet.add(hdd_3);
        slotTypeSet.add(per_3);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_2);
        dummyMb_3.setProvidedSlots(slotTypeSet);
        dummyMb_3.setDisplayName("Dummy Motherboard 3");
        dummyMb_3.setPriceRows(null);
        entityManager.persist(dummyMb_3);

        Motherboard dummyMb_4 = new Motherboard("Dummy Motherboard 4");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_1);
        slotTypeSet.add(gpu_2);
        slotTypeSet.add(hdd_3);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_2);
        slotTypeSet.add(per_3);
        slotTypeSet.add(ram_1);
        dummyMb_4.setProvidedSlots(slotTypeSet);
        dummyMb_4.setDisplayName("Dummy Motherboard 4");
        dummyMb_4.setPriceRows(null);
        entityManager.persist(dummyMb_4);

        Motherboard dummyMb_5 = new Motherboard("Dummy Motherboard 5");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_4);
        slotTypeSet.add(gpu_3);
        slotTypeSet.add(hdd_2);
        slotTypeSet.add(per_1);
        slotTypeSet.add(ram_2);
        dummyMb_5.setProvidedSlots(slotTypeSet);
        dummyMb_5.setDisplayName("Dummy Motherboard 5");
        dummyMb_5.setPriceRows(null);
        entityManager.persist(dummyMb_5);

        Motherboard dummyMb_6 = new Motherboard("Dummy Motherboard 6");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_2);
        slotTypeSet.add(gpu_3);
        slotTypeSet.add(hdd_1);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_2);
        slotTypeSet.add(per_3);
        slotTypeSet.add(ram_1);
        dummyMb_6.setProvidedSlots(slotTypeSet);
        dummyMb_6.setDisplayName("Dummy Motherboard 6");
        dummyMb_6.setPriceRows(null);
        entityManager.persist(dummyMb_6);


        Motherboard dummyMb_7 = new Motherboard("Dummy Motherboard 7");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_2);
        slotTypeSet.add(gpu_3);
        slotTypeSet.add(hdd_1);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_2);
        slotTypeSet.add(per_3);
        slotTypeSet.add(ram_1);
        dummyMb_7.setProvidedSlots(slotTypeSet);
        dummyMb_7.setDisplayName("Dummy Motherboard 7");
        dummyMb_7.setPriceRows(null);
        entityManager.persist(dummyMb_7);

        Motherboard dummyMb_8 = new Motherboard("Dummy Motherboard 8");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_4);
        slotTypeSet.add(gpu_3);
        slotTypeSet.add(hdd_3);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_2);
        slotTypeSet.add(per_3);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_2);
        dummyMb_8.setProvidedSlots(slotTypeSet);
        dummyMb_8.setDisplayName("Dummy Motherboard 8");
        dummyMb_8.setPriceRows(null);
        entityManager.persist(dummyMb_8);

        Motherboard dummyMb_9 = new Motherboard("Dummy Motherboard 9");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_2);
        slotTypeSet.add(gpu_3);
        slotTypeSet.add(hdd_3);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_2);
        slotTypeSet.add(per_3);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_2);
        dummyMb_9.setProvidedSlots(slotTypeSet);
        dummyMb_9.setDisplayName("Dummy Motherboard 9");
        dummyMb_9.setPriceRows(null);
        entityManager.persist(dummyMb_9);

        Motherboard dummyMb_10 = new Motherboard("Dummy Motherboard 10");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_2);
        slotTypeSet.add(gpu_2);
        slotTypeSet.add(hdd_1);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_3);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_1);
        dummyMb_10.setProvidedSlots(slotTypeSet);
        dummyMb_10.setDisplayName("Dummy Motherboard 10");
        dummyMb_10.setPriceRows(null);
        entityManager.persist(dummyMb_10);

        Motherboard dummyMb_11 = new Motherboard("Dummy Motherboard 11");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_1);
        slotTypeSet.add(gpu_2);
        slotTypeSet.add(hdd_2);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_3);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_1);
        dummyMb_11.setProvidedSlots(slotTypeSet);
        dummyMb_11.setDisplayName("Dummy Motherboard 11");
        dummyMb_11.setPriceRows(null);
        entityManager.persist(dummyMb_11);

        Motherboard dummyMb_12 = new Motherboard("Dummy Motherboard 12");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_3);
        slotTypeSet.add(gpu_1);
        slotTypeSet.add(hdd_2);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_2);
        dummyMb_12.setProvidedSlots(slotTypeSet);
        dummyMb_12.setDisplayName("Dummy Motherboard 12");
        dummyMb_12.setPriceRows(null);
        entityManager.persist(dummyMb_12);


        Motherboard dummyMb_13 = new Motherboard("Dummy Motherboard 13");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_4);
        slotTypeSet.add(gpu_2);
        slotTypeSet.add(hdd_2);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_3);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_2);
        dummyMb_13.setProvidedSlots(slotTypeSet);
        dummyMb_13.setDisplayName("Dummy Motherboard 13");
        dummyMb_13.setPriceRows(null);
        entityManager.persist(dummyMb_13);

        Motherboard dummyMb_14 = new Motherboard("Dummy Motherboard 14");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_4);
        slotTypeSet.add(gpu_1);
        slotTypeSet.add(hdd_1);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_3);
        slotTypeSet.add(ram_1);
        dummyMb_14.setProvidedSlots(slotTypeSet);
        dummyMb_14.setDisplayName("Dummy Motherboard 14");
        dummyMb_14.setPriceRows(null);
        entityManager.persist(dummyMb_14);

        Motherboard dummyMb_15 = new Motherboard("Dummy Motherboard 15");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_1);
        slotTypeSet.add(gpu_1);
        slotTypeSet.add(hdd_1);
        slotTypeSet.add(per_4);
        slotTypeSet.add(ram_2);
        dummyMb_15.setProvidedSlots(slotTypeSet);
        dummyMb_15.setDisplayName("Dummy Motherboard 15");
        dummyMb_15.setPriceRows(null);
        entityManager.persist(dummyMb_15);

        Motherboard dummyMb_16 = new Motherboard("Dummy Motherboard 16");
        slotTypeSet = new HashSet<>();
        slotTypeSet.add(cpu_3);
        slotTypeSet.add(gpu_2);
        slotTypeSet.add(hdd_3);
        slotTypeSet.add(per_1);
        slotTypeSet.add(per_3);
        slotTypeSet.add(ram_1);
        dummyMb_16.setProvidedSlots(slotTypeSet);
        dummyMb_16.setDisplayName("Dummy Motherboard 16");
        dummyMb_16.setPriceRows(null);
        entityManager.persist(dummyMb_16);

        // CPUs
        Component dummyCPU_1 = new Component("Dummy CPU 1");
        dummyCPU_1.setRequiredSlot(cpu_1);
        dummyCPU_1.setDisplayName("Dummy CPU 1");
        dummyCPU_1.setPriceRows(null);
        dummyCPU_1.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_1);

        Component dummyCPU_2 = new Component("Dummy CPU 2");
        dummyCPU_2.setRequiredSlot(cpu_1);
        dummyCPU_2.setDisplayName("Dummy CPU 2");
        dummyCPU_2.setPriceRows(null);
        dummyCPU_2.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_2);

        Component dummyCPU_3 = new Component("Dummy CPU 3");
        dummyCPU_3.setRequiredSlot(cpu_1);
        dummyCPU_3.setDisplayName("Dummy CPU 3");
        dummyCPU_3.setPriceRows(null);
        dummyCPU_3.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_3);

        Component dummyCPU_4 = new Component("Dummy CPU 4");
        dummyCPU_4.setRequiredSlot(cpu_1);
        dummyCPU_4.setDisplayName("Dummy CPU 4");
        dummyCPU_4.setPriceRows(null);
        dummyCPU_4.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_4);

        Component dummyCPU_5 = new Component("Dummy CPU 5");
        dummyCPU_5.setRequiredSlot(cpu_2);
        dummyCPU_5.setDisplayName("Dummy CPU 5");
        dummyCPU_5.setPriceRows(null);
        dummyCPU_5.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_5);

        Component dummyCPU_6 = new Component("Dummy CPU 6");
        dummyCPU_6.setRequiredSlot(cpu_2);
        dummyCPU_6.setDisplayName("Dummy CPU 6");
        dummyCPU_6.setPriceRows(null);
        dummyCPU_6.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_6);

        Component dummyCPU_7 = new Component("Dummy CPU 7");
        dummyCPU_7.setRequiredSlot(cpu_2);
        dummyCPU_7.setDisplayName("Dummy CPU 7");
        dummyCPU_7.setPriceRows(null);
        dummyCPU_7.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_7);

        Component dummyCPU_8 = new Component("Dummy CPU 8");
        dummyCPU_8.setRequiredSlot(cpu_2);
        dummyCPU_8.setDisplayName("Dummy CPU 8");
        dummyCPU_8.setPriceRows(null);
        dummyCPU_8.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_8);

        Component dummyCPU_9 = new Component("Dummy CPU 9");
        dummyCPU_9.setRequiredSlot(cpu_3);
        dummyCPU_9.setDisplayName("Dummy CPU 9");
        dummyCPU_9.setPriceRows(null);
        dummyCPU_9.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_9);

        Component dummyCPU_10 = new Component("Dummy CPU 10");
        dummyCPU_10.setRequiredSlot(cpu_3);
        dummyCPU_10.setDisplayName("Dummy CPU 10");
        dummyCPU_10.setPriceRows(null);
        dummyCPU_10.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_10);

        Component dummyCPU_11 = new Component("Dummy CPU 11");
        dummyCPU_11.setRequiredSlot(cpu_3);
        dummyCPU_11.setDisplayName("Dummy CPU 11");
        dummyCPU_11.setPriceRows(null);
        dummyCPU_11.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_11);

        Component dummyCPU_12 = new Component("Dummy CPU 12");
        dummyCPU_12.setRequiredSlot(cpu_3);
        dummyCPU_12.setDisplayName("Dummy CPU 12");
        dummyCPU_12.setPriceRows(null);
        dummyCPU_12.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_12);

        Component dummyCPU_13 = new Component("Dummy CPU 13");
        dummyCPU_13.setRequiredSlot(cpu_4);
        dummyCPU_13.setDisplayName("Dummy CPU 13");
        dummyCPU_13.setPriceRows(null);
        dummyCPU_13.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_13);

        Component dummyCPU_14 = new Component("Dummy CPU 14");
        dummyCPU_14.setRequiredSlot(cpu_4);
        dummyCPU_14.setDisplayName("Dummy CPU 14");
        dummyCPU_14.setPriceRows(null);
        dummyCPU_14.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_14);

        Component dummyCPU_15 = new Component("Dummy CPU 15");
        dummyCPU_15.setRequiredSlot(cpu_4);
        dummyCPU_15.setDisplayName("Dummy CPU 15");
        dummyCPU_15.setPriceRows(null);
        dummyCPU_15.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_15);

        Component dummyCPU_16 = new Component("Dummy CPU 16");
        dummyCPU_16.setRequiredSlot(cpu_4);
        dummyCPU_16.setDisplayName("Dummy CPU 16");
        dummyCPU_16.setPriceRows(null);
        dummyCPU_16.setType(ComponentType.CPU);
        entityManager.persist(dummyCPU_16);



        // GPUs
        Component dummyGPU_1 = new Component("Dummy GPU 1");
        dummyGPU_1.setRequiredSlot(gpu_1);
        dummyGPU_1.setDisplayName("Dummy GPU 1");
        dummyGPU_1.setPriceRows(null);
        dummyGPU_1.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_1);

        Component dummyGPU_2 = new Component("Dummy GPU 2");
        dummyGPU_2.setRequiredSlot(gpu_1);
        dummyGPU_2.setDisplayName("Dummy GPU 2");
        dummyGPU_2.setPriceRows(null);
        dummyGPU_2.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_2);

        Component dummyGPU_3 = new Component("Dummy GPU 3");
        dummyGPU_3.setRequiredSlot(gpu_1);
        dummyGPU_3.setDisplayName("Dummy GPU 3");
        dummyGPU_3.setPriceRows(null);
        dummyGPU_3.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_3);

        Component dummyGPU_4 = new Component("Dummy GPU 4");
        dummyGPU_4.setRequiredSlot(gpu_2);
        dummyGPU_4.setDisplayName("Dummy GPU 4");
        dummyGPU_4.setPriceRows(null);
        dummyGPU_4.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_4);

        Component dummyGPU_5 = new Component("Dummy GPU 5");
        dummyGPU_5.setRequiredSlot(gpu_2);
        dummyGPU_5.setDisplayName("Dummy GPU 5");
        dummyGPU_5.setPriceRows(null);
        dummyGPU_5.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_5);

        Component dummyGPU_6 = new Component("Dummy GPU 6");
        dummyGPU_6.setRequiredSlot(gpu_2);
        dummyGPU_6.setDisplayName("Dummy GPU 6");
        dummyGPU_6.setPriceRows(null);
        dummyGPU_6.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_6);

        Component dummyGPU_7 = new Component("Dummy GPU 7");
        dummyGPU_7.setRequiredSlot(gpu_2);
        dummyGPU_7.setDisplayName("Dummy GPU 7");
        dummyGPU_7.setPriceRows(null);
        dummyGPU_7.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_7);

        Component dummyGPU_8 = new Component("Dummy GPU 8");
        dummyGPU_8.setRequiredSlot(gpu_3);
        dummyGPU_8.setDisplayName("Dummy GPU 8");
        dummyGPU_8.setPriceRows(null);
        dummyGPU_8.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_8);

        Component dummyGPU_9 = new Component("Dummy GPU 9");
        dummyGPU_9.setRequiredSlot(gpu_3);
        dummyGPU_9.setDisplayName("Dummy GPU 9");
        dummyGPU_9.setPriceRows(null);
        dummyGPU_9.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_9);

        Component dummyGPU_10 = new Component("Dummy GPU 10");
        dummyGPU_10.setRequiredSlot(gpu_3);
        dummyGPU_10.setDisplayName("Dummy GPU 10");
        dummyGPU_10.setPriceRows(null);
        dummyGPU_10.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_10);

        Component dummyGPU_11 = new Component("Dummy GPU 11");
        dummyGPU_11.setRequiredSlot(gpu_3);
        dummyGPU_11.setDisplayName("Dummy GPU 11");
        dummyGPU_11.setPriceRows(null);
        dummyGPU_11.setType(ComponentType.GPU);
        entityManager.persist(dummyGPU_11);

        // RAM
        Component dummyRAM_1 = new Component("Dummy RAM 1");
        dummyRAM_1.setRequiredSlot(ram_1);
        dummyRAM_1.setDisplayName("Dummy RAM 1");
        dummyRAM_1.setPriceRows(null);
        dummyRAM_1.setType(ComponentType.RAM);
        entityManager.persist(dummyRAM_1);

        Component dummyRAM_2 = new Component("Dummy RAM 2");
        dummyRAM_2.setRequiredSlot(ram_1);
        dummyRAM_2.setDisplayName("Dummy RAM 2");
        dummyRAM_2.setPriceRows(null);
        dummyRAM_2.setType(ComponentType.RAM);
        entityManager.persist(dummyRAM_2);

        Component dummyRAM_3 = new Component("Dummy RAM 3");
        dummyRAM_3.setRequiredSlot(ram_1);
        dummyRAM_3.setDisplayName("Dummy RAM 3");
        dummyRAM_3.setPriceRows(null);
        dummyRAM_3.setType(ComponentType.RAM);
        entityManager.persist(dummyRAM_3);

        Component dummyRAM_4 = new Component("Dummy RAM 4");
        dummyRAM_4.setRequiredSlot(ram_2);
        dummyRAM_4.setDisplayName("Dummy RAM 4");
        dummyRAM_4.setPriceRows(null);
        dummyRAM_4.setType(ComponentType.RAM);
        entityManager.persist(dummyRAM_4);

        Component dummyRAM_5 = new Component("Dummy RAM 5");
        dummyRAM_5.setRequiredSlot(ram_2);
        dummyRAM_5.setDisplayName("Dummy RAM 5");
        dummyRAM_5.setPriceRows(null);
        dummyRAM_5.setType(ComponentType.RAM);
        entityManager.persist(dummyRAM_5);

        Component dummyRAM_6 = new Component("Dummy RAM 6");
        dummyRAM_6.setRequiredSlot(ram_2);
        dummyRAM_6.setDisplayName("Dummy RAM 6");
        dummyRAM_6.setPriceRows(null);
        dummyRAM_6.setType(ComponentType.RAM);
        entityManager.persist(dummyRAM_6);

        Component dummyRAM_7 = new Component("Dummy RAM 7");
        dummyRAM_7.setRequiredSlot(ram_2);
        dummyRAM_7.setDisplayName("Dummy RAM 7");
        dummyRAM_7.setPriceRows(null);
        dummyRAM_7.setType(ComponentType.RAM);
        entityManager.persist(dummyRAM_7);

        // HDD

        Component dummyHDD_1 = new Component("Dummy HDD 1");
        dummyHDD_1.setRequiredSlot(hdd_1);
        dummyHDD_1.setDisplayName("Dummy HDD 1");
        dummyHDD_1.setPriceRows(null);
        dummyHDD_1.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_1);

        Component dummyHDD_2 = new Component("Dummy HDD 2");
        dummyHDD_2.setRequiredSlot(hdd_1);
        dummyHDD_2.setDisplayName("Dummy HDD 2");
        dummyHDD_2.setPriceRows(null);
        dummyHDD_2.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_1);

        Component dummyHDD_3 = new Component("Dummy HDD 3");
        dummyHDD_3.setRequiredSlot(hdd_1);
        dummyHDD_3.setDisplayName("Dummy HDD 3");
        dummyHDD_3.setPriceRows(null);
        dummyHDD_3.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_3);

        Component dummyHDD_4 = new Component("Dummy HDD 4");
        dummyHDD_4.setRequiredSlot(hdd_2);
        dummyHDD_4.setDisplayName("Dummy HDD 4");
        dummyHDD_4.setPriceRows(null);
        dummyHDD_4.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_4);

        Component dummyHDD_5 = new Component("Dummy HDD 5");
        dummyHDD_5.setRequiredSlot(hdd_2);
        dummyHDD_5.setDisplayName("Dummy HDD 5");
        dummyHDD_5.setPriceRows(null);
        dummyHDD_5.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_5);

        Component dummyHDD_6 = new Component("Dummy HDD 6");
        dummyHDD_6.setRequiredSlot(hdd_2);
        dummyHDD_6.setDisplayName("Dummy HDD 6");
        dummyHDD_6.setPriceRows(null);
        dummyHDD_6.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_6);

        Component dummyHDD_7 = new Component("Dummy HDD 7");
        dummyHDD_7.setRequiredSlot(hdd_3);
        dummyHDD_7.setDisplayName("Dummy HDD 7");
        dummyHDD_7.setPriceRows(null);
        dummyHDD_7.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_7);

        Component dummyHDD_8 = new Component("Dummy HDD 8");
        dummyHDD_8.setRequiredSlot(hdd_3);
        dummyHDD_8.setDisplayName("Dummy HDD 8");
        dummyHDD_8.setPriceRows(null);
        dummyHDD_8.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_8);

        Component dummyHDD_9 = new Component("Dummy HDD 9");
        dummyHDD_9.setRequiredSlot(hdd_3);
        dummyHDD_9.setDisplayName("Dummy HDD 9");
        dummyHDD_9.setPriceRows(null);
        dummyHDD_9.setType(ComponentType.HDD);
        entityManager.persist(dummyHDD_9);


        // PSU - they have no requirements
        Component dummyPSU_1 = new Component("Dummy PSU 1");
        dummyPSU_1.setDisplayName("Dummy PSU 1");
        dummyPSU_1.setPriceRows(null);
        dummyPSU_1.setType(ComponentType.PSU);
        entityManager.persist(dummyPSU_1);

        Component dummyPSU_2 = new Component("Dummy PSU 2");
        dummyPSU_2.setDisplayName("Dummy PSU 2");
        dummyPSU_2.setPriceRows(null);
        dummyPSU_2.setType(ComponentType.PSU);
        entityManager.persist(dummyPSU_1);

        Component dummyPSU_3 = new Component("Dummy PSU 3");
        dummyPSU_3.setDisplayName("Dummy PSU 3");
        dummyPSU_3.setPriceRows(null);
        dummyPSU_3.setType(ComponentType.PSU);
        entityManager.persist(dummyPSU_3);

        // Peripheral stuf
        Component dummyPeriph_1 = new Component("Dummy Maus 1");
        dummyPeriph_1.setDisplayName("Dummy Maus 1");
        dummyPeriph_1.setPriceRows(null);
        dummyPeriph_1.setType(ComponentType.PERIPHERAL);
        entityManager.persist(dummyPeriph_1);

        Component dummyPeriph_2 = new Component("Dummy Maus 2");
        dummyPeriph_2.setDisplayName("Dummy Maus 2");
        dummyPeriph_2.setPriceRows(null);
        dummyPeriph_2.setType(ComponentType.PERIPHERAL);
        entityManager.persist(dummyPeriph_2);

        Component dummyPeriph_3 = new Component("Dummy Keyboard 1");
        dummyPeriph_3.setDisplayName("Dummy Keyboard 1");
        dummyPeriph_3.setPriceRows(null);
        dummyPeriph_3.setType(ComponentType.PERIPHERAL);
        entityManager.persist(dummyPeriph_3);

        Component dummyPeriph_4 = new Component("Dummy Keyboard 2");
        dummyPeriph_4.setDisplayName("Dummy Keyboard 2");
        dummyPeriph_4.setPriceRows(null);
        dummyPeriph_4.setType(ComponentType.PERIPHERAL);
        entityManager.persist(dummyPeriph_4);

        Component dummyPeriph_5 = new Component("Dummy Headset 1");
        dummyPeriph_5.setDisplayName("Dummy Headset 1");
        dummyPeriph_5.setPriceRows(null);
        dummyPeriph_5.setType(ComponentType.PERIPHERAL);
        entityManager.persist(dummyPeriph_5);

        Component dummyPeriph_6 = new Component("Dummy Headset 2");
        dummyPeriph_6.setDisplayName("Dummy Headset 2");
        dummyPeriph_6.setPriceRows(null);
        dummyPeriph_6.setType(ComponentType.PERIPHERAL);
        entityManager.persist(dummyPeriph_6);

        Component dummyPeriph_7 = new Component("Dummy Lautsprecher 1");
        dummyPeriph_7.setDisplayName("Dummy Lautsprecher 1");
        dummyPeriph_7.setPriceRows(null);
        dummyPeriph_7.setType(ComponentType.PERIPHERAL);
        entityManager.persist(dummyPeriph_7);

        Component dummyPeriph_8 = new Component("Dummy Lautsprecher 2");
        dummyPeriph_8.setDisplayName("Dummy Lautsprecher 2");
        dummyPeriph_8.setPriceRows(null);
        dummyPeriph_8.setType(ComponentType.PERIPHERAL);
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
