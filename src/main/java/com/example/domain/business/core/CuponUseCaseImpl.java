package com.example.domain.business.core;

import com.example.domain.business.model.*;
import com.example.domain.business.usecase.CuponUsecase;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Log
public class CuponUseCaseImpl implements CuponUsecase {


    @Override
    public Flux<CouponDetailDto> createCupon(final String fileBase64) {

        Set<String> codes = new HashSet<>();
        AtomicInteger counter = new AtomicInteger(0);
        AtomicReference<ValidateCouponEnum> previousBono = new AtomicReference<>(null);
        return createFluxFrom( fileBase64).skip(1)
                .flatMap(HelperKata -> createItemModel(fileBase64)
                .map(model->CouponDetailDto.builder()
                        .code(model.getBono())
                        .dueDate(model.getDate())
                        .totalLinesFile(1)
                        .build())
                        .flatMap(couponDetailDto -> errorOfCoupon(codes,couponDetailDto))

                );

    }


    private Mono<ItemModel>  createItemModel(String line) {
        var options = Optional.of(List.of(line.split(FileCSVEnum.CHARACTER_DEFAULT.getId())));
        var bono = options.filter(colums -> !colums.isEmpty())
                .map(colums -> colums.get(0))
                .orElse("EMPTY_STRING");

        var date = options.filter(colums -> !colums.isEmpty() && colums.size() > 1)
                .map(colums -> colums.get(1))
                .orElse("EMPTY_STRING");

        return Mono.just(ItemModel.builder()
                .bono(bono)
                .date(date)
                .build());
    }



    private Mono errorOfCoupon(Set<String> codes, CouponDetailDto couponDetailDto) {
       return validateIsEmpty(couponDetailDto)
                .flatMap(couponDetailDto1 -> validateIsEmpty(couponDetailDto1))
                .flatMap(couponDetailDto1 -> validateIsExist(codes, couponDetailDto1)
                        .flatMap(couponDetailDto2 -> validateDateRegex(couponDetailDto2.getDueDate()))
                        .flatMap(aBoolean -> validateDateIsMinor(couponDetailDto1.getDueDate())));


    }

    private static Flux<String> createFluxFrom(String fileBase64) {
        return Flux.using(
                () -> new BufferedReader(new InputStreamReader(
                        new ByteArrayInputStream(decodeBase64(fileBase64))
                )).lines(),
                Flux::fromStream,
                Stream::close
        );
    }


    private static byte[] decodeBase64(final String fileBase64) {
        return Base64.getDecoder().decode(fileBase64);
    }


    private Mono<CouponDetailDto> validateIsEmpty(CouponDetailDto couponDetailDto) {
        if (couponDetailDto.getDueDate().isBlank()) {
            Mono.just(ExperienceErrorsEnum.FILE_ERROR_COLUMN_EMPTY);
        }
        return Mono.empty();

    }
    private Mono<CouponDetailDto> validateIsExist(Set<String> codes, CouponDetailDto couponDetailDto) {
        if (!codes.add(couponDetailDto.getCode())) {
            Mono.just(ExperienceErrorsEnum.FILE_ERROR_CODE_DUPLICATE);
        }
        return Mono.empty();
    }

   private Mono<Boolean> validateDateRegex(String dateForValidate) {
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

    private Mono<Boolean> validateDateIsMinor(String dateForValidate) {
        try {
            if (findFile(dateForValidate)) return Mono.just(true);
        } catch (Exception ignored) {
            log.warning(ExperienceErrorsEnum.FILE_DATE_IS_MINOR_OR_EQUALS.toString());
        }
        return Mono.just(false);
    }

    public static boolean findFile(String dateForValidate) throws ParseException {
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

}