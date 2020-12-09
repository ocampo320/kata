package com.example.domain.business.infrastructure;

import com.example.domain.business.model.CouponDetailDto;
import com.example.domain.business.model.ExperienceErrorsEnum;
import com.example.domain.business.model.FileCSVEnum;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class CuponRepositoryImpl implements  CuponesRepository{



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

}
