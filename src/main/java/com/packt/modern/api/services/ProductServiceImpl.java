package com.packt.modern.api.services;

import com.packt.jmodern.api.generated.types.Product;
import com.packt.jmodern.api.generated.types.ProductCriteria;
import com.packt.modern.api.repository.Repository;
import org.apache.logging.log4j.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    private final Repository repository;

    public ProductServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Product getProduct(String id) {
        return repository.getProduct(id);
    }

    @Override
    public List<Product> getProducts(ProductCriteria criteria) {
        List<Predicate<Product>> predicates = new ArrayList<>(2);
        if (!Objects.isNull(criteria)) {
            if (Strings.isNotBlank(criteria.getName())) {
                Predicate<Product> namePredicate = p -> p.getName().contains(criteria.getName());
                predicates.add(namePredicate);
            }
            if (!Objects.isNull(criteria.getTags()) && !criteria.getTags().isEmpty()) {
                List<String> tags = criteria.getTags().stream().map(ti -> ti.getName()).collect(Collectors.toList());
                Predicate<Product> tagsPredicate = p ->
                        p.getTags().stream().filter(t -> tags.contains(t.getName())).count() > 0;
                predicates.add(tagsPredicate);
            }
        }
        if (predicates.isEmpty()) {
            return repository.getProducts();
        }
        return repository.getProducts().stream()
                .filter(p -> predicates.stream().allMatch(pre -> pre.test(p))).collect(Collectors.toList());
    }
}
