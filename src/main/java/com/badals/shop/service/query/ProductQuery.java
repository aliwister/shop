package com.badals.shop.service.query;

import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.service.CategoryService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.CategoryDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

/*
query {
category(id: 1) {
  children{
    title
  }
}
}
query {
product(slug:"681464590") {
  ref
  categories {
    id
    title
  }
  variations {
    ref
    variationAttributes {
      name
      value
    }
  }
  variationOptions {
    label
    name
    values
  }
  variationAttributes {
      name
      value
  }
}
}
query {
products(category:"home", text:"", type:"") {
  total
  items {
    id
    title
    description
  }
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

    @Autowired
    private CategoryService categoryService;

    public List<ProductDTO> products(final int count) {
        return productService.getAllProducts(count);
        //return null;
    }
   public ProductDTO product (String slug) {
      ProductDTO dto = this.productService.getProductBySlug(slug);
      return dto;
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

    public ProductResponse products(String slug, String text, String type, Integer offset, Integer limit, String lang) {
       return productService.findAllByCategory(slug, offset, limit);
    }

   public List<ProductDTO> relatedProducts(String type, String slug) {
      return productService.findRelated(slug);
   }

   public List<CategoryDTO> categories(String type) {
      return categoryService.findAll();
   }

   public CategoryDTO category(int id) {
      return categoryService.findOne((long) id).orElse(null);
   }
}

