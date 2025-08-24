package com.elearning.elearning_support.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import com.elearning.elearning_support.constants.CharacterConstants;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

    public static final String FORMAT_DATE_DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";

    public static final String FORMAT_DATE_HH_MM_YYYY_HH_MM = "HH:mm dd/MM/yyyy";
    public static final String FORMAT_DATE_DD_MM_YY_HH_MM_SS = "dd/MM/yy HH:mm:ss";
    public static final String FORMAT_DATE_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMAT_DATE_YYYY_MMDD_HHMM = "yyyyMMddHHmm";
    public static final String FORMAT_DATE_YYYY_MMDD_HHMMSS = "yyyyMMddHHmmss";
    public static final String FORMAT_DATE_DD_MM_YYYY_HH_MM_A = "dd/MM/yyyy hh:mma";
    public static final ZoneId ZONE_ID = ZoneId.of("UTC");
    public static final ZoneId ICT_ZONE_ID = ZoneId.of("Asia/Jakarta");
    public static final ZoneId SYS_ZONE_ID = ZoneId.systemDefault();
    public static final Clock offsetClockICT = Clock.offset(Clock.systemUTC(), Duration.ofHours(+7));
    public static final String FORMAT_DATE_DD_MM_YYYY = "ddMMyyyy";
    public static final String FORMAT_DATE_YYYY_MM_DD = "yyyyMMdd";
    public static final String FORMAT_DATE_DD_MM_YYYY_SLASH="dd/MM/yyyy";
    public static final String FORMAT_DATE_DD_MM_SLASH = "dd/MM";
    public static final String TIME_ZONE = "Asia/Ho_Chi_Minh";
    public static final String FORMAT_DATE_YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_DATE_YYYY_MM_DD_T_HH_MM_SS_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String FORMAT_DATE_HH_MM_DD_MM_YYYY = "HH:mm dd/MM/yyyy";

    public static final String FORMAT_DATE_HH_MM = "HH:mm";

    public static final String FORMAT_EE_HH_MM = "EE - HH:mm";

    public static final String FORMAT_DATE_FILE_DD_MM_YYYY_HH_MM_SS = "ddMMyyyyHHmmss";

    public static final String FORMAT_DD_MM_YYYY= "dd-MM-yyyy";
    public static final String FORMAT_YYYY_MM_DD= "yyyy-MM-dd";
    public static final String SEARCH_START_DATE_SUFFIX = " 00:00:00";
    public static final String SEARCH_END_DATE_SUFFIX= " 23:59:59";

    public static final String SEARCH_LAST_YEAR_DAY_SLASH_PREFIX = "31/12/";

    public static final String SEARCH_FIRST_YEAR_DAY_SLASH_PREFIX = "01/01/";
    public static final int DATE_OF_WEEK = 7;
    public static final String FORMAT_DATE_MM_YYYY= "MM/uuuu";
    public static final String FORMAT_DATE_YYYY_MM_DD_2= "YYYY/MM/dd";

    /**
     * Get current time with time zone
     * @return : current date
     */
    public static Date getCurrentDateTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return calendar.getTime();
    }


    /**
     * Calculate a time interval type : "NOW", "YEAR", "MONTH", "DATE" - interval is negative if minus
     */
    public static Date calculateDateByInterval(Date sourceDate, Integer interval, String intervalType){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sourceDate);
        if (interval == 0 || Objects.equals(intervalType, "NOW")) {
            return calendar.getTime();
        }
        switch (intervalType) {
            case "SECOND":
                calendar.add(Calendar.SECOND, interval);
                break;
            case "MINUTE":
                calendar.add(Calendar.MINUTE, interval);
                break;
            case "HOUR":
                calendar.add(Calendar.HOUR, interval);
                break;
            case "MONTH":
                calendar.add(Calendar.MONTH, interval);
                break;
            case "DATE":
                calendar.add(Calendar.DATE, interval);
                break;
            case "YEAR":
                calendar.add(Calendar.YEAR, interval);
                break;
            default:
                break;
        }
        return calendar.getTime();
    }

    /**
     * Convert LocalDate to Date
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.of(TIME_ZONE)).toInstant());
    }

    /**
     * Convert LocalDateTime to Date
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of(TIME_ZONE)).toInstant());
    }

    /**
     * Convert Date to LocalDate
     */
    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of(TIME_ZONE)).toLocalDate();
    }

    /**
     * Convert Date to LocalDateTime
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.of(TIME_ZONE)).toLocalDateTime();
    }

    /**
     * Parse a date string with a specified pattern and default value
     */
    public static Date parseWithDefault(String targetDate, String pattern, Date defaultDate){
        if(Objects.isNull(targetDate) || Objects.isNull(pattern))
            return defaultDate;
        try{
            return new SimpleDateFormat(pattern).parse(targetDate);
        } catch (ParseException parseException) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, parseException.getMessage(), parseException.getCause());
        }
        return null;
    }

    /**
     * Format a date to a string with a specified pattern
     */
    public static String formatDateWithPattern(Date date, String pattern){
        if(Objects.isNull(date)) return CharacterConstants.BLANK;
        return new SimpleDateFormat(pattern).format(date);
    }


}
