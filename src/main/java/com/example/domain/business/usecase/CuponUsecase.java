package com.example.domain.business.usecase;

import com.example.domain.business.model.CouponDetailDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuponUsecase {
    Flux<Boolean> createCupon(final String fileBase64);

}
