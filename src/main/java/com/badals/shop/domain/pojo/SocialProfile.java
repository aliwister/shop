package com.badals.shop.domain.pojo;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;

import java.io.Serializable;
import java.util.Map;

@Data
public class SocialProfile implements Serializable {
   Map<String, Map<SocialPlatform, String>> map = new HashedMap<>();
/*   public static Object valueOf(String name) {

   }*/
}

