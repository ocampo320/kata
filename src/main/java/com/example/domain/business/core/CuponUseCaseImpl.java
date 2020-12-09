package com.example.domain.business.core;

import com.example.domain.business.infrastructure.CuponesRepository;
import com.example.domain.business.model.CouponDetailDto;
import com.example.domain.business.model.ExperienceErrorsEnum;
import com.example.domain.business.model.FileCSVEnum;
import com.example.domain.business.model.ItemModel;
import com.example.domain.business.usecase.CuponUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Log
@RequiredArgsConstructor
public class CuponUseCaseImpl implements CuponUsecase {
    private static final String EMPTY_STRING = "EMPTY_STRING";
    private final CuponesRepository cuponesRepository;

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

    @Override
    public Flux<CouponDetailDto> createCupon(final String fileBase64) {

        Set<String> codes = new HashSet<>();
        return createFluxFrom(fileBase64).skip(1)
                .flatMap(itemModel -> createItemModel(fileBase64)
                        .map(model -> CouponDetailDto.builder()
                                .code(model.getBono())
                                .dueDate(model.getDate())
                                .totalLinesFile(1)
                                .build())
                        .flatMap(couponDetailDto -> errorOfCoupon(codes, couponDetailDto))

                );

    }

    private Mono<ItemModel> createItemModel(String line) {
        var options = Optional.of(List.of(line.split(FileCSVEnum.CHARACTER_DEFAULT.getId())));
        String bono = getBono(options);

        String date = getDate(options);

        return Mono.just(ItemModel.builder()
                .bono(bono)
                .date(date)
                .build());
    }


    private String getBono(Optional<List<String>> options) {
        String bono;
        bono = options.filter(colums -> !colums.isEmpty())
                .map(colums -> colums.get(0))
                .orElse(EMPTY_STRING);
        return bono;
    }


    private String getDate(Optional<List<String>> options) {
        String date;
        date = options.filter(colums -> !colums.isEmpty() && colums.size() > 1)
                .map(colums -> colums.get(1))
                .orElse(EMPTY_STRING);
        return date;
    }

    private Mono errorOfCoupon(Set<String> codes, CouponDetailDto couponDetailDto) {
        return validateIsEmpty(couponDetailDto)
                .flatMap(couponDetailDto1 -> validateIsEmpty(couponDetailDto1))
                .flatMap(couponDetailDto1 -> cuponesRepository.validateIsExist(codes, couponDetailDto1)
                        .flatMap(couponDetailDto2 -> validateDateRegex(couponDetailDto2.getDueDate()))
                        .flatMap(aBoolean -> validateDateIsMinor(couponDetailDto1.getDueDate())));


    }

    private Mono<CouponDetailDto> validateIsEmpty(CouponDetailDto couponDetailDto) {
        if (couponDetailDto.getDueDate().isBlank()) {
            Mono.just(ExperienceErrorsEnum.FILE_ERROR_COLUMN_EMPTY);
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
            if (cuponesRepository.findFile(dateForValidate)) return Mono.just(true);
        } catch (Exception ignored) {
            log.warning(ExperienceErrorsEnum.FILE_DATE_IS_MINOR_OR_EQUALS.toString());
        }
        return Mono.just(false);
    }


}