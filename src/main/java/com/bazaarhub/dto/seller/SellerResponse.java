package com.bazaarhub.dto.seller;

import java.math.BigDecimal;

public record SellerResponse(
        Long id,
        String businessName,
        String email,
        String phone,
        BigDecimal rating,
        int totalRatings,
        boolean verified,
        boolean active
) {}