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

package com.badals.shop.xtra.amazon.paapi5;

import java.util.List;
import java.util.Map;

import com.amazon.paapi5.v1.ApiCallback;
import com.amazon.paapi5.v1.ApiException;
import com.amazon.paapi5.v1.BrowseNode;
import com.amazon.paapi5.v1.ErrorData;
import com.amazon.paapi5.v1.GetBrowseNodesResponse;
import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.GetVariationsResponse;
import com.amazon.paapi5.v1.Item;
import com.amazon.paapi5.v1.SearchItemsResponse;

public class Callback {
	public static ApiCallback<SearchItemsResponse> callback_seachItems = new ApiCallback<SearchItemsResponse>() {

		@Override
		public void onFailure(ApiException exception, int arg1, Map<String, List<String>> arg2) {
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
		}

		@Override
		public void onSuccess(SearchItemsResponse searchItemsResponse, int arg1, Map<String, List<String>> arg2) {
			System.out.println("API called successfully");
			System.out.println("Complete response: " + searchItemsResponse);

			// Parsing the request
			if (searchItemsResponse.getSearchResult() != null) {
				System.out.println("Printing first item information in SearchResult:");
				Item item = searchItemsResponse.getSearchResult().getItems().get(0);
				if (item != null) {
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
						System.out.println(
								"Buying price: " + item.getOffers().getListings().get(0).getPrice().getDisplayAmount());
					}
				}
			}
			if (searchItemsResponse.getErrors() != null) {
				System.out.println("Printing errors:\nPrinting Errors from list of Errors");
				for (ErrorData error : searchItemsResponse.getErrors()) {
					System.out.println("Error code: " + error.getCode());
					System.out.println("Error message: " + error.getMessage());
				}
			}
		}

		@Override
		public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onDownloadProgress:" + arg1);
		}

		@Override
		public void onUploadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onUploadProgress:" + arg1);
		}
	};

	public static ApiCallback<GetItemsResponse> callback_getItems = new ApiCallback<GetItemsResponse>() {

		@Override
		public void onFailure(ApiException exception, int arg1, Map<String, List<String>> arg2) {
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
		}

		@Override
		public void onSuccess(GetItemsResponse getItemsResponse, int arg1, Map<String, List<String>> arg2) {
			System.out.println("API called successfully");
			System.out.println("Complete response: " + getItemsResponse);

			// Parsing the request
			if (getItemsResponse.getItemsResult() != null && getItemsResponse.getItemsResult().getItems() != null) {
				System.out.println("Printing all item information in ItemsResult:");
				for (Item item : getItemsResponse.getItemsResult().getItems()) {
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
						System.out.println(
								"Buying price: " + item.getOffers().getListings().get(0).getPrice().getDisplayAmount());
					}
				}
			}
			if (getItemsResponse.getErrors() != null) {
				System.out.println("Printing errors:\nPrinting Errors from list of Errors");
				for (ErrorData error : getItemsResponse.getErrors()) {
					System.out.println("Error code: " + error.getCode());
					System.out.println("Error message: " + error.getMessage());
				}
			} else {
				System.out.println("Internal server error!");
			}
		}

		@Override
		public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onDownloadProgress:" + arg1);
		}

		@Override
		public void onUploadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onUploadProgress:" + arg1);
		}
	};

	public static ApiCallback<GetVariationsResponse> callback_getVariations = new ApiCallback<GetVariationsResponse>() {

		@Override
		public void onFailure(ApiException exception, int arg1, Map<String, List<String>> arg2) {
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
		}

		@Override
		public void onSuccess(GetVariationsResponse getVariationsResponse, int arg1, Map<String, List<String>> arg2) {
			System.out.println("API called successfully");
			System.out.println("Complete response: " + getVariationsResponse);

			// Parsing the request
			if (getVariationsResponse.getVariationsResult() != null) {
				System.out.println("Printing VariationSummary: ");
				if (getVariationsResponse.getVariationsResult().getVariationSummary() != null && getVariationsResponse
						.getVariationsResult().getVariationSummary().getVariationCount() != null) {
					System.out.println("VariationCount: "
							+ getVariationsResponse.getVariationsResult().getVariationSummary().getVariationCount());
				}
				System.out.println("Printing first item information in VariationsResult:");
				Item item = getVariationsResponse.getVariationsResult().getItems().get(0);
				if (item != null) {
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
						System.out.println(
								"Buying price: " + item.getOffers().getListings().get(0).getPrice().getDisplayAmount());
					}
				}
			}
			if (getVariationsResponse.getErrors() != null) {
				System.out.println("Printing errors:\nPrinting Errors from list of Errors");
				for (ErrorData error : getVariationsResponse.getErrors()) {
					System.out.println("Error code: " + error.getCode());
					System.out.println("Error message: " + error.getMessage());
				}
			}
		}

		@Override
		public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onDownloadProgress:" + arg1);
		}

		@Override
		public void onUploadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onUploadProgress:" + arg1);
		}
	};

	public static ApiCallback<GetBrowseNodesResponse> callback_browseNodes = new ApiCallback<GetBrowseNodesResponse>() {

		@Override
		public void onFailure(ApiException exception, int arg1, Map<String, List<String>> arg2) {
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
		}

		@Override
		public void onSuccess(GetBrowseNodesResponse getBrowseNodesResponse, int arg1, Map<String, List<String>> arg2) {
			System.out.println("API called successfully");
			System.out.println("Complete response: " + getBrowseNodesResponse);

			if (getBrowseNodesResponse.getBrowseNodesResult() != null
					&& getBrowseNodesResponse.getBrowseNodesResult().getBrowseNodes() != null) {
				// Parsing the response
				System.out.println("Printing all brose node information in BrowseNodesResults:");
				for (BrowseNode browseNode : getBrowseNodesResponse.getBrowseNodesResult().getBrowseNodes()) {
					if (browseNode.getId() != null) {
						// BrowseNodeID
						System.out.println("BrowseNodeID: " + browseNode.getId());
					}
					if (browseNode.getDisplayName() != null) {
						// DisplayName
						System.out.println("DisplayName: " + browseNode.getDisplayName());
					}
					if (browseNode.getContextFreeName() != null) {
						// ContextFreeName
						System.out.println("ContextFreeName: " + browseNode.getContextFreeName());
					}
				}
			}
			if (getBrowseNodesResponse.getErrors() != null) {
				System.out.println("Printing Errors:\nPrinting Errors from list of Errors");
				for (ErrorData error : getBrowseNodesResponse.getErrors()) {
					System.out.println("Error code: " + error.getCode());
					System.out.println("Error message: " + error.getMessage());
				}
			} else {
				System.out.println("Internal server error!");
			}
		}

		@Override
		public void onDownloadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onDownloadProgress:" + arg1);
		}

		@Override
		public void onUploadProgress(long arg0, long arg1, boolean arg2) {
			System.out.println("onUploadProgress:" + arg1);
		}
	};

}
