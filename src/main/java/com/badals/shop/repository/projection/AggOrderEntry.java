package com.badals.shop.repository.projection;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AggOrderEntry {
   String getPeriod();
   BigDecimal getTotal();
   Integer getCount();
}
