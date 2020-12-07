package com.example.domain.business.usecase;

import com.example.domain.business.model.CouponDetailDto;
import reactor.core.publisher.Flux;

public interface CuponUsecase {
    Flux<CouponDetailDto> createCupon(final String fileBase64);
}
