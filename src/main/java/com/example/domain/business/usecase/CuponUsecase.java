package com.example.domain.business.usecase;

import reactor.core.publisher.Flux;

public interface CuponUsecase {
    Flux<Boolean> createCupon(final String fileBase64);
}
