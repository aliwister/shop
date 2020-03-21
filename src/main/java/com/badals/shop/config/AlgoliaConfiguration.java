package com.badals.shop.config;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.badals.shop.domain.AlgoliaProduct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlgoliaConfiguration {
    /*
     * Module for serialization/deserialization of ConstraintViolationProblem.
     */
    @Bean(name="productIndex")
    SearchIndex initializeProductSearchIndex() {
        SearchClient client =
            DefaultSearchClient.create("ARYN2BAVRZ", "c6c2b32499e3471a1e75cc051b317b6b");
        SearchIndex<AlgoliaProduct> index = client.initIndex("product", AlgoliaProduct.class);
        return index;
    }


}
