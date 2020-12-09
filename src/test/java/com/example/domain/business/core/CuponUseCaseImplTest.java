package com.example.domain.business.core;

import com.example.domain.business.infrastructure.CuponesRepository;
import com.example.domain.business.model.CouponDetailDto;
import com.example.domain.business.usecase.CuponUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.objenesis.instantiator.basic.NewInstanceInstantiator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class CuponUseCaseImplTest {
    private CuponUsecase cuponUsecase;

    private CuponesRepository cuponesRepository;
    private CuponUseCaseImpl underTest;
   private String file = "Q09ESUdPO0ZFQ0hBX1ZFTkNJQ0lNCjExMTE7MDEvMDEvMjAxMQozMzMzOzE4LzA5LzIwCjQ0NDQ7MTgvMDkvMjAKMTExMTsxMy8wNy8yMDExCjExMTE7MTMvMTIvMjAyMgoxMTExOzE4LzEzLzIwMjIKMjIyMjsxOC8wOS8yMAo";

    @BeforeEach
    void setUp() {
        cuponesRepository=Mockito.mock(CuponesRepository.class);
        cuponUsecase = Mockito.mock(CuponUsecase.class);
        underTest = new CuponUseCaseImpl(cuponesRepository);

    }


    @Test
    void TestUseCase2() throws ParseException {
        Set<String> codes = new HashSet<>();
        codes.add("1");
        codes.add("2");
        codes.add("3");
        Mockito.when(cuponesRepository.findFile(new Date().toString())).thenReturn(true);
        Flux<CouponDetailDto> result = underTest.createCupon(file);
        CouponDetailDto couponDetailDto1 = getCouponDetailDto2();
        StepVerifier.create(result)
                .expectNextMatches(couponDetailDto -> {

                    assertEquals(codes, couponDetailDto1.getCode());
                    return false;

                })

                .expectComplete()
                .verifyLater();

        verify(cuponesRepository).findFile(new Date().toString());


    }

    @Test
    void TestUseCase(){
        Flux<CouponDetailDto> result=underTest.createCupon(file);
        CouponDetailDto couponDetailDto1 = getCouponDetailDto();
        StepVerifier.create(result)
                .expectNextMatches(couponDetailDto -> {
                    assertEquals(couponDetailDto1.getDueDate(),couponDetailDto.getDueDate());
                    return  false;

                })
                .expectComplete()
                .verifyLater();



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

    private CouponDetailDto getCouponDetailDto2(){
        return CouponDetailDto.builder()
                .totalLinesFile(2)
                .dueDate(null)
                .code("1")
                .messageError("FILE_ERROR_CODE_DUPLICATE")
                .numberLine(1)
                .build();
    }


}