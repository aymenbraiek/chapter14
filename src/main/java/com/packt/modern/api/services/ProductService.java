package com.packt.modern.api.services;

import com.packt.jmodern.api.generated.types.Product;
import com.packt.jmodern.api.generated.types.ProductCriteria;

import java.util.List;

public interface ProductService {
    Product getProduct(String id);

    List<Product> getProducts(ProductCriteria criteria);
}
