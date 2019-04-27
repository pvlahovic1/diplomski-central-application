package hr.foi.diplomski.central.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.MINUTES;

public class CommonUtils {

    public static final String DEVICE_ACTIVITY_PROOF_MESSAGE = "^DEVICE_ID ([0-9a-zA-z ]{64});ACTIVITY_PROOF;$";
    public static final Long DEVICE_ACTIVITY_PROOF_TIME = 10L;

    public static boolean isSensorActive(Optional<LocalDateTime> lastTimeActive) {
        if (lastTimeActive.isPresent()) {
            return MINUTES.between(lastTimeActive.get(), LocalDateTime.now()) < DEVICE_ACTIVITY_PROOF_TIME;
        } else {
            return false;
        }
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static boolean isSyntaxValid(String syntax, String word) {
        Pattern pattern = Pattern.compile(syntax);
        Matcher m = pattern.matcher(word);

        return m.matches();
    }

    public static List<String> dataFromSyntax(String syntax, String word) {
        List<String> data = new ArrayList<>();

        Pattern pattern = Pattern.compile(syntax);
        Matcher m = pattern.matcher(word);

        if (m.matches()) {
            int start = 1;
            int end = m.groupCount();
            for (int i = start; i <= end; i++) {
                data.add(m.group(i));
            }
        }

        return data;
    }

}
