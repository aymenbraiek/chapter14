package com.packt.modern.api.repository;

import com.github.javafaker.Faker;
import com.packt.jmodern.api.generated.types.Product;
import com.packt.jmodern.api.generated.types.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@org.springframework.stereotype.Repository
public class InMemRepository implements Repository {
    private static final Map<String, Product> productEntities = new ConcurrentHashMap<>();
    private static final Map<String, Tag> tagEntities = new ConcurrentHashMap<>();
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private FluxSink<Product> productsStream;
    private ConnectableFlux<Product> productPublisher;

    public InMemRepository() {
        // Seed data for development purpose, real application must use database
        Faker faker = new Faker();
        IntStream.range(0, faker.number().numberBetween(20, 50)).forEach(number -> {
            String tag = faker.book().genre();
            tagEntities
                    .putIfAbsent(tag, Tag.newBuilder().id(UUID.randomUUID().toString()).name(tag).build());
        });
        IntStream.range(0, faker.number().numberBetween(4, 20)).forEach(number -> {
            String id = String.format("a1s2d3f4-%d", number);
            String title = faker.book().title();
            List<Tag> tags = tagEntities.entrySet().stream()
                    .filter(t -> t.getKey().startsWith(faker.book().genre().substring(0, 1)))
                    .map(Map.Entry::getValue).collect(Collectors.toList());
            if (tags.isEmpty()) {
                tags.add(tagEntities.entrySet().stream().findAny().get().getValue());
            }
            Product product = Product.newBuilder().id(id).name(title)
                    .description(faker.lorem().sentence()).count(faker.number().numberBetween(10, 100))
                    .price(BigDecimal.valueOf(faker.number().randomDigitNotZero()))
                    .imageUrl(String.format("/images/%s.jpeg", title.replace(" ", "")))
                    .tags(tags).build();
            productEntities.put(id, product);
        });
        LOG.info("Seed Data: \n{}", productEntities);
        Flux<Product> publisher = Flux.create(emitter -> {
            productsStream = emitter;
        });
        productPublisher = publisher.publish();
        productPublisher.connect();
    }

    @Override
    public Product getProduct(String id) {
        return null;
    }

    @Override
    public List<Product> getProducts() {
        return null;
    }
}
