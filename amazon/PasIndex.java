package com.badals.shop.vendor.amazon;

import java.util.List;

import com.badals.shop.vendor.amazon.pas.mappings.ItemLookupResponse;
import com.badals.shop.vendor.amazon.pas.mappings.ItemNode;

import lombok.Data;

@Data
public class PasIndex {
	String objectID;
	String asin;
	List<ItemNode> doc;
}
