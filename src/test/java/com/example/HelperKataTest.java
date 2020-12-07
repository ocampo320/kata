package com.example;

import com.example.domain.business.model.CouponDetailDto;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class HelperKataTest {
    String file = "Q09ESUdPO0ZFQ0hBX1ZFTkNJQ0lNCjExMTE7MDEvMDEvMjAxMQozMzMzOzE4LzA5LzIwCjQ0NDQ7MTgvMDkvMjAKMTExMTsxMy8wNy8yMDExCjExMTE7MTMvMTIvMjAyMgoxMTExOzE4LzEzLzIwMjIKMjIyMjsxOC8wOS8yMAo";

    @Test
    void exampleExecutor() {

        StepVerifier
                .create(HelperKata.getListFromBase64File(file))
                .expectNext(CouponDetailDto.builder()
                        .code("1111")
                        .dueDate(null)
                        .numberLine(1)
                        .messageError("FILE_DATE_IS_MINOR_OR_EQUALS")
                        .totalLinesFile(1)
                        .build()
                )
                .expectNext(CouponDetailDto.builder()
                        .code("3333")
                        .dueDate(null)
                        .numberLine(2)
                        .messageError("FILE_ERROR_DATE_PARSE")
                        .totalLinesFile(1)
                        .build())
                .expectNext(CouponDetailDto.builder()
                        .code("4444")
                        .dueDate(null)
                        .numberLine(3)
                        .messageError("FILE_ERROR_DATE_PARSE")
                        .totalLinesFile(1)
                        .build())
                .expectNext(CouponDetailDto.builder()
                        .code("1111")
                        .dueDate(null)
                        .numberLine(4)
                        .messageError("FILE_ERROR_CODE_DUPLICATE")
                        .totalLinesFile(1)
                        .build())
                .expectNext(CouponDetailDto.builder()
                        .code("1111")
                        .dueDate(null)
                        .numberLine(5)
                        .messageError("FILE_ERROR_CODE_DUPLICATE")
                        .totalLinesFile(1)
                        .build())
                .expectNext(CouponDetailDto.builder()
                        .code("1111")
                        .dueDate(null)
                        .numberLine(6)
                        .messageError("FILE_ERROR_CODE_DUPLICATE")
                        .totalLinesFile(1)
                        .build())
                .expectNext(CouponDetailDto.builder()
                        .code("2222")
                        .dueDate(null)
                        .numberLine(7)
                        .messageError("FILE_ERROR_DATE_PARSE")
                        .totalLinesFile(1)
                        .build())
                .verifyComplete();

    }
}