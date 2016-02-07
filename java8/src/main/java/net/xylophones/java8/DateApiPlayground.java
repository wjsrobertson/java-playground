package net.xylophones.java8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class DateApiPlayground {

    public static void main(String[] args) {

    }

    private static void playWithTime() {
        LocalTime time = LocalTime.of(12, 30);
        time = LocalTime.MIN;
        time = LocalTime.MIDNIGHT;
    }

    private static void playWithDuration() {
        Duration.between(LocalDateTime.of(2010, 1, 1, 12, 30, 0), LocalDateTime.of(2011, 1, 10, 12, 30, 0));
    }

    private static void playWithPeriod() {
        Period period = Period.ofDays(2);
        period = Period.between(LocalDate.of(2010, 1, 1), LocalDate.of(2011, 1, 10));

        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime plus = dateTime.plus(period);

        System.out.println(plus);
    }

    private static void playWithZonedDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        zonedDateTime = ZonedDateTime.parse("2014-01-01T12:30:00+01:00");

        ZoneId zoneId = zonedDateTime.getZone();

        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
        availableZoneIds.forEach(
                (zone) -> System.out.println(zone)
        );
    }

    private static void playWithLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = LocalDateTime.of(2014, 1, 1, 12, 30);
        dateTime = LocalDateTime.of(2014, Month.JANUARY, 1, 12, 30);
        dateTime = LocalDateTime.parse("2014-01-01T12:30:00");
        dateTime = LocalDateTime.parse("2014-01-01T12:30:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private static void playWithLocalDate() {
        LocalDate localDate = LocalDate.now();
        localDate = LocalDate.of(2014, 1, 1);
        localDate = LocalDate.of(2014, Month.JANUARY, 1);
        localDate = LocalDate.parse("2014-01-01");
        localDate = LocalDate.parse("2014-01-01", DateTimeFormatter.ISO_DATE);
        localDate = LocalDate.parse("2014-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
