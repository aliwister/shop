package com.badals.shop.xtra.amazon;

import com.badals.shop.xtra.amazon.mappings.*;
import lombok.Data;

import java.util.List;

@Data
public class PasIndex {
	String objectID;
	String asin;
	List<ItemNode> doc;
}
