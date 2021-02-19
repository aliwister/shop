package com.badals.shop.service.util;

import java.util.zip.CRC32;

public class ChecksumUtil {
   static CRC32 checksum = new CRC32();

   public static String getChecksum(String message) {
      checksum.update(message.getBytes());
      return String.valueOf(checksum.getValue());
   }
}
