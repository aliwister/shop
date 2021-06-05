package com.badals.shop.config;

import com.badals.shop.graph.UpperCaseDirective;
import graphql.kickstart.tools.boot.SchemaDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfig {
   @Bean
   public SchemaDirective myCustomDirective() {
      return new SchemaDirective("uppercase", new UpperCaseDirective());
   }
}
