package com.example.domain.business.core;

import com.example.domain.business.model.CouponDetailDto;
import com.example.domain.business.usecase.CuponUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;


class CuponUseCaseImplTest {
    private CuponUsecase cuponUsecase;
    private CuponUseCaseImpl underTest;
   private String file = "Q09ESUdPO0ZFQ0hBX1ZFTkNJQ0lNCjExMTE7MDEvMDEvMjAxMQozMzMzOzE4LzA5LzIwCjQ0NDQ7MTgvMDkvMjAKMTExMTsxMy8wNy8yMDExCjExMTE7MTMvMTIvMjAyMgoxMTExOzE4LzEzLzIwMjIKMjIyMjsxOC8wOS8yMAo";

    @BeforeEach
    void setUp() {
        cuponUsecase = Mockito.mock(CuponUsecase.class);
        underTest = new CuponUseCaseImpl();
    }


    @Test
    void TestUseCase(){
        CouponDetailDto couponDetailDto= getCouponDetailDto();
        Flux<CouponDetailDto> result=underTest.createCupon(file);
        StepVerifier.create(result)
                .expectNext(getCouponDetailDto());


    }
    private CouponDetailDto getCouponDetailDto(){
        return CouponDetailDto.builder()
                .totalLinesFile(12)
                .dueDate(new Date().toString())
                .code("1111")
                .messageError("FILE_DATE_IS_MINOR_OR_EQUALS")
                .numberLine(1)
                .build();
    }



}