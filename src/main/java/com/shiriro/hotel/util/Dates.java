package com.shiriro.hotel.util;

import java.time.LocalDate;

public class Dates {

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String toCompactDateString(LocalDate date) {
        return date.toString().replace("-", "");
    }

}
