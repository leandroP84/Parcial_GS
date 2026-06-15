package com.smartcv.util;

import com.smartcv.exception.InvalidDateRangeException;

import java.time.LocalDate;

public final class DateRangeValidator {

    private DateRangeValidator() {}

    /** Valida que la fecha fin no sea anterior a la fecha inicio. */
    public static void validate(LocalDate fechaInicio, LocalDate fechaFin, String context) {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new InvalidDateRangeException(
                    "La fecha de fin no puede ser anterior a la fecha de inicio en " + context);
        }
    }
}
