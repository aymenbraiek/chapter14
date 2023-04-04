package com.packt.modern.api.repository;

import com.packt.jmodern.api.generated.types.Product;

import java.util.List;

public interface Repository {
    Product getProduct(String id);

    List<Product> getProducts();


}
