package com.badals.shop.aop.logging;

public class TenantContext {
   private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();
   private static ThreadLocal<String> currentMerchant = new InheritableThreadLocal<>();

   private static ThreadLocal<Long> currentTenantId = new InheritableThreadLocal<>();
   private static ThreadLocal<Long> currentMerchantId = new InheritableThreadLocal<>();

   public static String getCurrentTenant() {
      return currentTenant.get();
   }

   public static void setCurrentTenant(String tenant) {
      currentTenant.set(tenant);
   }

   public static Long getCurrentTenantId() {
      return currentTenantId.get();
   }

   public static void setCurrentTenantId(Long tenant) {
      currentTenantId.set(tenant);
   }

   public static String getCurrentMerchant() {
      return currentMerchant.get();
   }

   public static void setCurrentMerchant(String tenant) {
      currentMerchant.set(tenant);
   }

   public static Long getCurrentMerchantId() {
      return currentMerchantId.get();
   }

   public static void setCurrentMerchantId(Long tenant) {
      currentMerchantId.set(tenant);
   }

   public static void clear() {
      currentTenant.set(null);
      currentMerchant.set(null);
      currentTenantId.set(null);
      currentMerchantId.set(null);
   }
}
