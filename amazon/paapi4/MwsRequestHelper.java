package com.badals.shop.vendor.amazon;
/*******************************************************************************
 * Copyright 2009-2013 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 *
 * You may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the 
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * Marketplace Web Service Products
 * API Version: 2011-10-01
 * Library Version: 2013-08-01
 * Generated: Wed Sep 25 16:54:41 GMT 2013
 */


import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;

/**
 * Configuration for MarketplaceWebServiceProducts samples.
 */
public class MwsRequestHelper {

    /** Developer AWS access key. */
   // private static final String accessKey = "AKIAIOAQ437ZZB7K4O2A";

    /** Developer AWS secret key. */
   // private static final String secretKey = "6JS96EV8yvAcERdfy38Q2TzzCOwcfEYIHHZbricV";

    /** The client application name. */
    private static final String appName = "Trust";

    /** The client application version. */
    private static final String appVersion = ".1";

    /**
     * The endpoint for region service and version.
     * ex: MwsEndpoints.NA_PROD.toString();
     */
    private static final String serviceURL = "https://mws.amazonservices.com/Products/2011-10-01";

    /** The client, lazy initialized. Async client is also a sync client. */
    private static MarketplaceWebServiceProductsAsyncClient client = null;

    /**
     * Get a client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static MarketplaceWebServiceProductsClient getClient(String accessKey, String secretKey) {
        return getAsyncClient(accessKey, secretKey);
    }

    /**
     * Get an async client connection ready to use.
     *
     * @return A ready to use client connection.
     */
    public static synchronized MarketplaceWebServiceProductsAsyncClient getAsyncClient(String accessKey, String secretKey) {
        if (client==null) {
            MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
            config.setServiceURL(serviceURL);
            // Set other client connection configurations here.
            client = new MarketplaceWebServiceProductsAsyncClient(accessKey, secretKey, 
                    appName, appVersion, config, null);
        }
        return client;
    }

}
