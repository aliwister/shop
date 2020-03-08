/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

/*
 * ProductAdvertisingAPI
 *
 * https://webservices.amazon.com/paapi5/documentation/index.html
 */

/*
 * This sample code snippet is for ProductAdvertisingAPI 5.0's GetItems API
 * For more details, refer:
 * https://webservices.amazon.com/paapi5/documentation/get-items.html
 */

package com.badals.shop.xtra.amazon.paapi5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazon.paapi5.v1.ApiClient;
import com.amazon.paapi5.v1.ApiException;
import com.amazon.paapi5.v1.ApiResponse;
import com.amazon.paapi5.v1.ErrorData;
import com.amazon.paapi5.v1.GetItemsRequest;
import com.amazon.paapi5.v1.GetItemsResource;
import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.Item;
import com.amazon.paapi5.v1.PartnerType;
import com.amazon.paapi5.v1.api.DefaultApi;



public class GetItemsWithHttpInfo {

	/**
	 * Returns Item mapped to ASIN
	 *
	 * @param items
	 *            Item
	 * @return Items mapped to ASIN
	 */
	private static Map<String, Item> parse_response(List<Item> items) {
		Map<String, Item> mappedResponse = new HashMap<String, Item>();
		for (Item item : items) {
			mappedResponse.put(item.getASIN(), item);
		}
		return mappedResponse;
	}

	public static void main(String[] args) {
		ApiClient client = new ApiClient();

		// Add your credentials
		// Please add your access key here
		client.setAwsAccessKey("AKIAJH3HSRU4OMRAU5NA");
		// Please add your secret key here
		client.setAwsSecretKey("yu6R/dsS4DD+Ws3zZcAkmLqoMx4Nhg4Utcm5DCEO");

		// Enter your partner tag (store/tracking id)
		String partnerTag = "deseneoma-20";

		/*
		 * PAAPI Host and Region to which you want to send request. For more
		 * details refer:
		 * https://webservices.amazon.com/paapi5/documentation/common-request-
		 * parameters.html#host-and-region
		 */
		client.setHost("webservices.amazon.com");
		client.setRegion("us-east-1");

		DefaultApi api = new DefaultApi(client);

		// Request initialization
		/*
		 * Choose resources you want from GetItemsResource enum For more
		 * details, refer:
		 * https://webservices.amazon.com/paapi5/documentation/get-items.html#
		 * resources-parameter
		 */
		List<GetItemsResource> getItemsResources = new ArrayList<GetItemsResource>();
		getItemsResources.add(GetItemsResource.ITEMINFO_BYLINEINFO);
		getItemsResources.add(GetItemsResource.ITEMINFO_PRODUCTINFO);

		// Choose item id(s)
		List<String> itemIds = new ArrayList<String>();
		itemIds.add("059035342X");
		//itemIds.add("B00X4WHP55");
		itemIds.add("1401263119");

		// Forming the request
		GetItemsRequest getItemsRequest = new GetItemsRequest().itemIds(itemIds).partnerTag(partnerTag)
				.resources(getItemsResources).partnerType(PartnerType.ASSOCIATES);

		try {
			ApiResponse<GetItemsResponse> response = api.getItemsWithHttpInfo(getItemsRequest);

			System.out.println("API called successfully");
			System.out.println("Complete response (data): " + response.getData());
			System.out.println("HTTP Info: " + response.getHeaders());

			// Parsing the request
			if (response.getData() != null && response.getData().getItemsResult() != null) {
				System.out.println("Printing all item information in ItemsResult:");
				Map<String, Item> responseList = parse_response(response.getData().getItemsResult().getItems());
				for (String itemId : itemIds) {
					if (response.getData().getItemsResult().getItems() != null) {
						System.out.println("Printing information about the ASIN: " + itemId);
						if (responseList.get(itemId) != null) {
							Item item = responseList.get(itemId);
							if (item.getASIN() != null) {
								System.out.println("ASIN: " + item.getASIN());
							}
							if (item.getDetailPageURL() != null) {
								System.out.println("DetailPageURL: " + item.getDetailPageURL());
							}
							if (item.getItemInfo() != null && item.getItemInfo().getTitle() != null
									&& item.getItemInfo().getTitle().getDisplayValue() != null) {
								System.out.println("Title: " + item.getItemInfo().getTitle().getDisplayValue());
							}
							if (item.getOffers() != null && item.getOffers().getListings() != null
									&& item.getOffers().getListings().get(0).getPrice() != null
									&& item.getOffers().getListings().get(0).getPrice().getDisplayAmount() != null) {
								System.out.println("Buying price: "
										+ item.getOffers().getListings().get(0).getPrice().getDisplayAmount());
							}
						}
					}
				}
			}
			if (response.getData() != null && response.getData().getErrors() != null) {
				System.out.println("Printing errors:\nPrinting Errors from list of Errors");
				for (ErrorData error : response.getData().getErrors()) {
					System.out.println("Error code: " + error.getCode());
					System.out.println("Error message: " + error.getMessage());
				}
			}
		} catch (ApiException exception) {
			// Exception handling
			System.out.println("Error calling PA-API 5.0!");
			System.out.println("Status code: " + exception.getCode());
			System.out.println("Errors: " + exception.getResponseBody());
			System.out.println("Message: " + exception.getMessage());
			if (exception.getResponseHeaders() != null) {
				// Printing request reference
				System.out.println("Request ID: " + exception.getResponseHeaders().get("x-amzn-RequestId"));
			}
			// exception.printStackTrace();
		} catch (Exception exception) {
			System.out.println("Exception message: " + exception.getMessage());
			// exception.printStackTrace();
		}
	}
}
