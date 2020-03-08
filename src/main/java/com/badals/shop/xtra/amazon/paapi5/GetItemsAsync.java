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
import java.util.List;

import com.amazon.paapi5.v1.ApiClient;
import com.amazon.paapi5.v1.ApiException;
import com.amazon.paapi5.v1.GetItemsRequest;
import com.amazon.paapi5.v1.GetItemsResource;
import com.amazon.paapi5.v1.PartnerType;
import com.amazon.paapi5.v1.api.DefaultApi;
import com.squareup.okhttp.Call;

public class GetItemsAsync {

	public static void main(String[] args) {
		ApiClient client = new ApiClient();

		// Add your credentials
		// Please add your access key here
		client.setAwsAccessKey("<YOUR-ACCESS-KEY-HERE>");
		// Please add your secret key here
		client.setAwsSecretKey("<YOUR-SECRET-KEY-HERE>");

		// Enter your partner tag (store/tracking id)
		String partnerTag = "<YOUR-PARTNER-TAG-HERE>";

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
		getItemsResources.add(GetItemsResource.ITEMINFO_TITLE);
		getItemsResources.add(GetItemsResource.OFFERS_LISTINGS_PRICE);

		// Choose item id(s)
		List<String> itemIds = new ArrayList<String>();
		itemIds.add("059035342X");
		itemIds.add("B00X4WHP55");
		itemIds.add("1401263119");

		// Forming the request
		GetItemsRequest getItemsRequest = new GetItemsRequest().itemIds(itemIds).partnerTag(partnerTag)
				.resources(getItemsResources).partnerType(PartnerType.ASSOCIATES);

		try {
			Call response = api.getItemsAsync(getItemsRequest, Callback.callback_getItems);
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
