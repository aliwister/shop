package com.badals.shop.service.query;

import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.ProductDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/*
query {
  product(id: 1) {
    id,
    ref,
    parent,
    sku,
    image,
    images
  }
}
query {
  product(id: 1) {
    ref
    releaseDate
    variationOptions {
      name
      values
    }
    variationAttributes {
      name
      value
    }
    variationDimensions
  }
}

 */

@Component
public class ProductQuery extends ShopQuery implements GraphQLQueryResolver {




    @Autowired
    private ProductService productService;

    public List<ProductDTO> getProducts(final int count) {
        return productService.getAllProducts(count);
        //return null;
    }
    public ProductDTO getProduct(final int id) {
        //return new Product();
        return productService.getProduct(id).get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO getProductAdmin(final int id) {
        //return new Product();
        return productService.getProductAdmin(id).get();
    }

    public ProductDTO getProductAny(final int id) {
        //return new Product();
        return productService.getProduct(id).get();
    }
}

