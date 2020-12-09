package com.example.domain.business.infrastructure;

import com.example.domain.business.model.CouponDetailDto;
import com.example.domain.business.model.ExperienceErrorsEnum;
import com.example.domain.business.model.FileCSVEnum;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Log
public class CuponRepositoryImpl implements CuponesRepository {


    @Override
    public boolean findFile(String dateForValidate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FileCSVEnum.PATTERN_SIMPLE_DATE_FORMAT.getId());
        Date dateActual = sdf.parse(sdf.format(new Date()));
        Date dateCompare = sdf.parse(dateForValidate);
        if (dateCompare.compareTo(dateActual) < 0) {
            return true;
        } else if (dateCompare.compareTo(dateActual) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Mono<CouponDetailDto> validateIsExist(Set<String> codes, CouponDetailDto couponDetailDto) {
        if (!codes.add(couponDetailDto.getCode())) {
            Mono.just(ExperienceErrorsEnum.FILE_ERROR_CODE_DUPLICATE);
        }
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> validateDateRegex(String dateForValidate) {
        try {
            String regex = FileCSVEnum.PATTERN_DATE_DEFAULT.getId();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(dateForValidate);
            return Mono.just(matcher.matches());
        } catch (Exception e) {
            log.warning(ExperienceErrorsEnum.FILE_ERROR_DATE_PARSE.toString());

        }
        return Mono.just(true);
    }


}
