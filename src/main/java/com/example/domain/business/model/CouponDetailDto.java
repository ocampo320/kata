package com.example.domain.business.model;

import lombok.*;

import java.util.Objects;

@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Getter
@Setter
@ToString
public class CouponDetailDto {
    private String code;
    private String dueDate;
    private int numberLine;
    private String messageError;
    private int totalLinesFile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponDetailDto that = (CouponDetailDto) o;
        return numberLine == that.numberLine &&
                totalLinesFile == that.totalLinesFile &&
                Objects.equals(code, that.code) &&
                Objects.equals(dueDate, that.dueDate) &&
                Objects.equals(messageError, that.messageError);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, dueDate, numberLine, messageError, totalLinesFile);
    }

}
