package com.badals.shop.service;

import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.api.Configuration;
import org.typesense.resources.Node;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class IndexService {

    private final Client client;

    public IndexService() {
        //client = new Client(new Config("http://207.154.202.65", "masterKey"));
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(
            new Node(
                "https", // For Typesense Cloud use https
                "7jugv4fr8seoc1mkp-1.a1.typesense.net", // For Typesense Cloud use xxx.a1.typesense.net
                "443" // For Typesense Cloud use 443
            )
        );

        Configuration configuration = new Configuration(nodes, Duration.ofSeconds(2), "fS3s13NCinhQ2IG3CtzSbZmjgbYTJ2FT");
        client = new Client(configuration);
    }


    public String getSearchKey(String tenantId) {
        HashMap map = new HashMap<String, Object>();
        map.put("filter_by", "tenantId:"+tenantId);
        String scopedKey = client.keys().generateScopedSearchKey("Qurq1kjWMAQJMQfwdB0OmHhtRjLRx0jx",map);
        return scopedKey;
    }
}
