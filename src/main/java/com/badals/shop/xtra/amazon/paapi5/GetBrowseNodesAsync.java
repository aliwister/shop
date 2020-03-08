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
 * This sample code snippet is for ProductAdvertisingAPI 5.0's GetBrowseNodes API
 * For more details, refer:
 * https://webservices.amazon.com/paapi5/documentation/getbrowsenodes.html
 */

package com.badals.shop.xtra.amazon.paapi5;

import java.util.ArrayList;
import java.util.List;

import com.amazon.paapi5.v1.ApiClient;
import com.amazon.paapi5.v1.ApiException;
import com.amazon.paapi5.v1.GetBrowseNodesRequest;
import com.amazon.paapi5.v1.GetBrowseNodesResource;
import com.amazon.paapi5.v1.PartnerType;
import com.amazon.paapi5.v1.api.DefaultApi;
import com.squareup.okhttp.Call;

public class GetBrowseNodesAsync {

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
		 * Choose resources you want from GetBrowseNodesResource enum For more
		 * details, refer:
		 * https://webservices.amazon.com/paapi5/documentation/getbrowsenodes.
		 * html#resources-parameter
		 */
		List<GetBrowseNodesResource> getBrowseNodesResources = new ArrayList<GetBrowseNodesResource>();
		getBrowseNodesResources.add(GetBrowseNodesResource.ANCESTOR);
		getBrowseNodesResources.add(GetBrowseNodesResource.CHILDREN);

		// Choose browsenode id(s)
		List<String> browseNodeIds = new ArrayList<String>();
		browseNodeIds.add("3040");
		browseNodeIds.add("0");
		browseNodeIds.add("3045");

		// Forming the request
		GetBrowseNodesRequest getBrowseNodesRequest = new GetBrowseNodesRequest().browseNodeIds(browseNodeIds)
				.partnerTag(partnerTag).resources(getBrowseNodesResources).partnerType(PartnerType.ASSOCIATES);

		try {
			// Sending the request
			Call response = api.getBrowseNodesAsync(getBrowseNodesRequest, Callback.callback_browseNodes);
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
