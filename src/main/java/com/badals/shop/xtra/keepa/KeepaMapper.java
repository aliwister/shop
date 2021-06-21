package com.badals.shop.xtra.keepa;

import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.xtra.amazon.PasItemNode;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.badals.shop.service.util.AccessUtil.opt;
import static com.badals.shop.xtra.keepa.ProductType.VARIATION_PARENT;

@Mapper(componentModel = "spring", uses = {})
public interface KeepaMapper {

   KeepaMapper INSTANCE = Mappers.getMapper(KeepaMapper.class);


   @Mapping(source = "asin", target = "id")
   @Mapping(source = "isEligibleForSuperSaverShipping", target = "superSaver")
   @Mapping(source = "isAdultProduct", target = "adultProduct")
   //@Mapping(source = "imagesCSV", target = "gallery")
   //@Mapping(target = "gallery", expression = "java(Arrays.asList(source.getImagesCSV().split(\",\")))")

   //@Mapping(source = "", target="starRating")
   @Mapping(target = "cost", ignore = true)
   @Mapping(target = "prime", ignore = true)
   @Mapping(target = "availabilityType", ignore = true)
   @Mapping(target = "variations", ignore = true)
   PasItemNode itemToPasItemNode(KProduct item);


   @AfterMapping
   default void afterMapping(@MappingTarget PasItemNode target, KProduct source) {
      // Hardcoded weight units
      target.setItemWeightUnit("Grams");

      // URL
      target.setUrl("https://www.amazon.com/dp/"+source.getAsin());

      // List Price + savingsAmount + savingsPercentage

      // Images
      if (source.getImagesCSV() != null)
         target.setGallery(Arrays.stream(source.getImagesCSV().split(",")).filter(x->x!=null).collect(Collectors.toList()));

      if (target.getGallery() != null)
         target.setImage(target.getGallery().get(0));

      // starRating
      List<List<Integer>> csv = (List<List<Integer>>) source.getCsv();
      if (csv.get(CsvIndex.RATING.getValue()) != null) {
         target.setStarRating(String.valueOf(csv.get(CsvIndex.RATING.getValue()).get(1)/10.0));
      }
      if(source.getAvailabilityAmazon() == 0) {
         target.setPrime(true);
         Double price = csv.get(CsvIndex.AMAZON.getValue()).get(1)/100.0;
         target.setCost(BigDecimal.valueOf(price));
         target.setMaxHours("48");
         target.setAvailabilityType("Now");
      }

      //browseNode
      if(source.getCategoryTree() !=null) {
         String browseNode = source.getCategoryTree().stream()
                 .map(CategoryTree::getName)
                 .collect(Collectors.joining(" > "));
         target.setBrowseNode(browseNode);
      }

      //upc
      if(source.getUpcList() != null)
         target.setUpc(source.getUpcList().get(0));

      //ean
      if(source.getEanList() != null)
         target.setEan(source.getEanList().get(0));

      //   List<String> similarities = new ArrayList<>();
      //   List<String> HierarchialCategories = new ArrayList<>();
      //   List<String> numHierarchialCategories = new ArrayList<>();
      //   List<String> categories = new ArrayList<>();
      //   List<String> numCategories = new ArrayList<>();

      //variationType

      //variationAttributes
      //variationDimensions
      //variations

      target.setVariationType(VariationType.SIMPLE);
      if (source.getVariations() != null) {
         target.setVariationType(VariationType.CHILD);

         //variations
         source.getVariations().stream().forEach(
           x-> {
              target.getVariations().put(x.getAsin(), x.getAttributes().stream().map(y->new com.badals.shop.domain.pojo.Attribute(y.getDimension(), y.getValue())).collect(Collectors.toList()));
              if(x.getAsin().equals(source.getAsin())) {
                 target.setVariationAttributes(x.getAttributes().stream().map(y->new com.badals.shop.domain.pojo.Attribute(y.getDimension(), y.getValue())).collect(Collectors.toList()));
              }
           }
         );

         //variationAttributes

         //variationDimensions


      }

      if (source.getProductType() == VARIATION_PARENT)
         target.setVariationType(VariationType.PARENT);
   }
}
