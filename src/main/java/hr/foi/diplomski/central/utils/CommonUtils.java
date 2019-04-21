package hr.foi.diplomski.central.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CommonUtils {

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
