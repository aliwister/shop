package com.badals.shop.service.mutation;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.service.ProductLangService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProductLangDTO;
import com.badals.shop.xtra.amazon.PasService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


/*
mutation {
  createNewProduct(product: {sku: "9789996910180",
    upc: "9789996910180",
    price: "1.5", currency: "OMR", title: "Asnan Majid", active: true}) {
    ref
    releaseDate
    variationOptions {
      name
      values
    }
  }
}
mutation {
  createNewProduct(product: {ref: 12, parent: 13, sku: "adxbdcdd", upc: 334343, releaseDate: "2017-07-09", price: 123, title: "Title", active: true,
    	variationOptions: [{ name : "Color", values:["REd", "Blue"]}, {name: "Size", values:["Green","Yellow"]}] }) {
    ref
    releaseDate
    variationOptions {
      name
      values
    }
  }
}
mutation {
  addI18n(id: 13, i18n: {lang: "en", title: "Asnan Majid", description:"<h1>Some really cool description</h1>", model: "18839", features:["great book", "for all ages"]}){
    title
    description
    features
  }
}

mutation {
  pasLookup(sku: "B01HLV5HR6") {
    id
    ref
    parent
    sku
    image
    price
    gallery {
      url
    }
    variations {
      ref
    }
  }
}


 */

@Component
public class ProductMutation implements GraphQLMutationResolver {
    @Autowired
    private ProductService productService;

    @Autowired
    private PasService pasService;

    @Autowired
    private ProductLangService productLangService;

    public ProductDTO createProduct(final Long ref, final Long parent, final String sku, final String upc, final LocalDate releaseDate) {
        return this.productService.createProduct(ref, parent, sku, upc, releaseDate);
    }

    public ProductDTO createNewProduct(final ProductDTO product) {
        return this.productService.createNewProduct(product);
    }

    public Attribute indexProduct(final long id) {
        return this.productService.indexProduct(id);
    }

    public ProductLangDTO addI18n(Long id, final ProductLangDTO productI18n) {
        return this.productLangService.addI18n(id, productI18n);
    }

    public ProductDTO pasLookup(String asin) {
        return this.pasService.lookup(asin);
    }


}

