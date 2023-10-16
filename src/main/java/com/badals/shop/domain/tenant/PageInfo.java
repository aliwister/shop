package com.badals.shop.domain.tenant;

import com.badals.shop.domain.converter.JsonNodeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "page_info")
@Data
public class PageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "slug")
    private String slug;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "info")
    private String info;
    @Column(name = "language")
    private String language;

}
