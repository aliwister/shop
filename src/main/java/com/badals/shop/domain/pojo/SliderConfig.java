package com.badals.shop.domain.pojo;

import lombok.Data;
import org.apache.commons.collections4.map.HashedMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SliderConfig implements Serializable {
   Map<String, List<String>> map = new HashedMap<>();
}

