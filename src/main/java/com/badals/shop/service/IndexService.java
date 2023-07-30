package com.badals.shop.service;

import com.badals.shop.service.dto.IndexProductDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.TenantProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.typesense.api.Client;
import org.typesense.api.Configuration;
import org.typesense.resources.Node;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

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

    public Mono<Void> indexMulti(IndexProductDTO dto) {
        return Mono.fromRunnable(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                //dto.setTenantId("badals");
                //dto.setId("AZ-" + dto.getSku());
                if (
                        dto.getTitle() == null ||
                                dto.getTitle().isEmpty() ||
                                dto.getPrice() == null ||
                                dto.getTitle().toLowerCase().indexOf("dildo") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("hunting scope") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("riflescope") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("adult toy") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("fake vagina") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("penis") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("vibrator") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("seeds") > -1 ||
                                dto.getTitle().toLowerCase().indexOf("sex wand") > -1
                ) return;
                String doc = mapper.writeValueAsString(dto);

                client.collections("products_en").documents().create(doc);
                //index.addDocuments(doc);
                //throw new RuntimeException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
