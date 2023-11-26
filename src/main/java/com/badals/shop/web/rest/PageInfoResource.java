//package com.badals.shop.web.rest;
//
//import com.badals.shop.domain.tenant.PageInfo;
//import com.badals.shop.security.AuthoritiesConstants;
//import com.badals.shop.service.PageInfoService;
//import com.badals.shop.service.dto.PageInfoDTO;
//import com.fasterxml.jackson.databind.JsonNode;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/page-info")
//public class PageInfoResource {
//    private final Logger log = LoggerFactory.getLogger(PageInfoResource.class);
//
//    private PageInfoService pageInfoService;
//
//    public PageInfoResource(PageInfoService pageInfoService) {
//        this.pageInfoService = pageInfoService;
//    }
//
//    @PostMapping("/{slug}")
//    public PageInfo createPageInfo(@PathVariable String slug, @RequestBody PageInfoDTO pageInfoDTO) {
//        return pageInfoService.createPageInfo(slug, "badals", pageInfoDTO.getLanguage(), pageInfoDTO.getInfo());
//    }
//
//    @GetMapping("/{slug}")
//    public List<PageInfo> getPageInfoBySlug(@PathVariable String slug) {
//        return pageInfoService.getPageInfosBySlugAndAndTenantId(slug, "badals");
//    }
//
//    @PutMapping("/{slug}")
//    public ResponseEntity<PageInfo> updatePageInfo(@PathVariable String slug, @RequestBody PageInfoDTO updatedInfo) {
//        PageInfo pageInfo = pageInfoService.getPageInfoBySlugAndTenantIdAndLanguage(slug, "badals", updatedInfo.getLanguage());
//        if (pageInfo == null) {
//            return ResponseEntity.notFound().build();
//        }
//        pageInfo.setInfo(updatedInfo.getInfo());
//        PageInfo savedPageInfo = pageInfoService.save(pageInfo);
//        return ResponseEntity.ok(savedPageInfo);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePageInfo(@PathVariable int id) {
//        PageInfo pageInfo = pageInfoService.getPageInfoById(id);
//        if (pageInfo == null) {
//            return ResponseEntity.notFound().build();
//        }
//        pageInfoService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//}
