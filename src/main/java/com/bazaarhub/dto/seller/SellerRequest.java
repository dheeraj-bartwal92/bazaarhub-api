package com.bazaarhub.dto.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SellerRequest(
        @NotBlank String businessName,
        @NotBlank @Email String email,
        String phone
) {}