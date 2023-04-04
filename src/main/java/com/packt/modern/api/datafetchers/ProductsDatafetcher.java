package com.packt.modern.api.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.InputArgument;
import com.packt.jmodern.api.generated.DgsConstants;
import com.packt.jmodern.api.generated.types.Product;
import com.packt.jmodern.api.generated.types.ProductCriteria;
import com.packt.modern.api.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@DgsComponent
public class ProductsDatafetcher {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final ProductService service;

    public ProductsDatafetcher(ProductService service) {
        this.service = service;
    }

    @DgsData(
            parentType = DgsConstants.QUERY_TYPE,
            field = DgsConstants.QUERY.Products
    )
    public List<Product> getProducts(@InputArgument("filter") ProductCriteria criteria) {
        return service.getProducts(criteria);
    }
}
