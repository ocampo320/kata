package com.example.domain.business.core;

import com.example.domain.business.infrastructure.CuponesRepository;
import com.example.domain.business.model.CouponDetailDto;
import com.example.domain.business.model.ExperienceErrorsEnum;
import com.example.domain.business.model.ItemModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class CuponUseCaseImplTest {


    private CuponesRepository cuponesRepository;
    private CuponUseCaseImpl underTest;
    private String file = "Q09ESUdPO0ZFQ0hBX1ZFTkNJQ0lNCjExMTE7MDEvMDEvMjAxMQozMzMzOzE4LzA5LzIwCjQ0NDQ7MTgvMDkvMjAKMTExMTsxMy8wNy8yMDExCjExMTE7MTMvMTIvMjAyMgoxMTExOzE4LzEzLzIwMjIKMjIyMjsxOC8wOS8yMAo";

    @BeforeEach
    void setUp() {
        cuponesRepository = Mockito.mock(CuponesRepository.class);
        underTest = new CuponUseCaseImpl(cuponesRepository);

    }


    @Test
    void TestUseCase2() throws ParseException {
        Set<String> codes = new HashSet<>();
        codes.add("1");
        codes.add("2");
        codes.add("3");
        Mockito.when(cuponesRepository.findFile(new Date().toString())).thenReturn(true);
        Mockito.when(cuponesRepository.validateIsExist(codes, getCouponDetailDto())).thenReturn(Mono.error(() -> new RuntimeException("FILE_ERROR_COLUMN_EMPTY")));
        Flux<Boolean> result = underTest.createCupon(file);
        CouponDetailDto couponDetailDto1 = getCouponDetailDto3();

        StepVerifier.create(result)
                .expectNextMatches(couponDetailDto -> {

                    assertEquals(codes.iterator().next(), couponDetailDto1.getCode());
                    return false;

                })

                .expectComplete()
                .verifyLater();

        verify(cuponesRepository).findFile(new Date().toString());
        verify(cuponesRepository).validateDateRegex(new Date().toString());
        verify(cuponesRepository, times(1)).validateIsExist(codes, getCouponDetailDto2());


    }

    @Test
    void TestUseCase() {
        Flux<Boolean> result = underTest.createCupon(file);
        CouponDetailDto couponDetailDto1 = getCouponDetailDto();
        StepVerifier.create(result)
                .expectNextMatches(couponDetailDto -> {
                    assertEquals(couponDetailDto1, couponDetailDto);
                    assertEquals(couponDetailDto1.getCode(), couponDetailDto1.getCode());
                    assertEquals(couponDetailDto1.getDueDate(),(couponDetailDto1.getDueDate()));
                    return false;
                })
                .expectComplete()
                .verifyLater();


    }
    @Test
    void TestMessageError(){
        Flux<Boolean> result = underTest.createCupon(file);
        CouponDetailDto couponDetailDto1 = getCouponDetailDto();
        StepVerifier.create(result)
                .consumeErrorWith(throwable -> {
                    assertEquals(ExperienceErrorsEnum.class, throwable.getMessage());
                    assertEquals("FILE_DATE_IS_MINOR_OR_EQUALS",throwable.getMessage());

                })
                .verifyLater();

    }

    @Test
    void TestUseCase3() {
        Set<String> codes = new HashSet<>();
        codes.add("1");
        codes.add("2");
        codes.add("3");
        Flux<Boolean> result = underTest.createCupon(file);
        CouponDetailDto couponDetailDto1 = getCouponDetailDto5();
        when(cuponesRepository.validateIsExist(codes, couponDetailDto1)).thenReturn(Mono.just(getCouponDetailDto2()));
        StepVerifier.create(result)
                .expectNextMatches(throwable -> {
                    assertEquals(ExperienceErrorsEnum.class, throwable.getClass());
                    assertEquals("", codes.iterator().next());
                    return true;
                })
                .expectComplete()
                .verifyLater();


    }

    @Test
    void TestUseCase4() {
        Set<String> codes = new HashSet<>();
        codes.add("1");
        codes.add("2");
        codes.add("3");
        Flux<Boolean> result = underTest.createCupon(file);
        CouponDetailDto couponDetailDto1 = getCouponDetailDto6();
        when(cuponesRepository.validateIsExist(codes, couponDetailDto1)).thenReturn(Mono.just(getCouponDetailDto2()));
        StepVerifier.create(result)
                .consumeErrorWith(throwable -> {
                    assertEquals(ExperienceErrorsEnum.class, throwable.getMessage());
                    assertEquals("FILE_ERROR_COLUMN_EMPTY", throwable.getMessage());

                })
                .verifyLater();


    }


    private ItemModel getItemMode() {
        return ItemModel.builder()
                .date(new Date().toString())
                .bono("bono")
                .build();

    }

    private CouponDetailDto getCouponDetailDto() {
        return CouponDetailDto.builder()
                .totalLinesFile(12)
                .dueDate(new Date().toString())
                .code("1111")
                .messageError("FILE_DATE_IS_MINOR_OR_EQUALS")
                .numberLine(1)
                .build();
    }

    private CouponDetailDto getCouponDetailDto2() {
        return CouponDetailDto.builder()
                .totalLinesFile(2)
                .dueDate(null)
                .code("1")
                .messageError("FILE_ERROR_DATE_PARSE")
                .numberLine(1)
                .build();
    }

    private CouponDetailDto getCouponDetailDto3() {
        return CouponDetailDto.builder()
                .totalLinesFile(3)
                .dueDate(null)
                .code("4444")
                .messageError("FILE_ERROR_DATE_PARSE")
                .numberLine(1)
                .build();
    }

    private CouponDetailDto getCouponDetailDto4() {
        return CouponDetailDto.builder()
                .totalLinesFile(0)
                .dueDate(null)
                .code("1")
                .messageError("FILE_ERROR_CODE_DUPLICATE")
                .numberLine(0)
                .build();
    }

    private CouponDetailDto getCouponDetailDto5() {
        return CouponDetailDto.builder()
                .totalLinesFile(2)
                .dueDate(null)
                .code("1")
                .messageError("FILE_ERROR_DATE_PARSE")
                .numberLine(1)
                .build();
    }

    private CouponDetailDto getCouponDetailDto6() {
        return CouponDetailDto.builder()
                .totalLinesFile(0)
                .dueDate("")
                .code("0")
                .messageError("FILE_ERROR_COLUMN_EMPTY")
                .numberLine(0)
                .build();
    }


}