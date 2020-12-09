package com.example.domain.business.infrastructure;

import com.example.domain.business.model.CouponDetailDto;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.Set;

public interface CuponesRepository {
    boolean findFile(String dateForValidate) throws ParseException;

    Mono<CouponDetailDto> validateIsExist(Set<String> codes, CouponDetailDto couponDetailDto);
    Mono<Boolean> validateDateRegex(String dateForValidate);

}
