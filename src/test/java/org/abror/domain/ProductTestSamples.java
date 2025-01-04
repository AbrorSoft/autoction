package org.abror.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product().id(1L).lotNumber(1L).authorName("authorName1").description("description1");
    }

    public static Product getProductSample2() {
        return new Product().id(2L).lotNumber(2L).authorName("authorName2").description("description2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .lotNumber(longCount.incrementAndGet())
            .authorName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
