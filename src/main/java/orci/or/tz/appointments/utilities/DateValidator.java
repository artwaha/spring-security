package orci.or.tz.appointments.utilities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateValidator {

     // Define common holidays
    private static final Map<String, String> commonHolidays = new HashMap<>();

    static {
        commonHolidays.put("01-01", "NEW YEAR DAY");
        commonHolidays.put("12-01", "ZANZIBAR REVOLUTION DAY");
        commonHolidays.put("07-04", "KARUME DAY");
        commonHolidays.put("26-04", "UNION DAY");
        commonHolidays.put("01-05", "LABOUR DAY");
        commonHolidays.put("07-07", "SABA SABA DAY");
        commonHolidays.put("08-08", "NANE DAY");
        commonHolidays.put("14-10", "NYERERE DAY");
        commonHolidays.put("09-12", "INDEPENDENCE DAY");
        commonHolidays.put("25-12", "CHRISTMAS DAY");
        commonHolidays.put("26-12", "BOXING DAY");
    }

    // check if the provided Date folows on a Weekend
     public boolean isHoliday(LocalDate date) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM");
        String formattedDate = date.format(myFormatObj);

        return commonHolidays.containsKey(formattedDate);
    }

    // Get the Name of the Holiday
    public String getTheHoliDayName(LocalDate date) {
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM");
        String formattedDate = date.format(myFormatObj);

        String holidayName = commonHolidays.get(formattedDate);
        return holidayName;
    }


    // check if the the given Date follows on a Weekend
    public boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    // know the name of the day of the provided LocalDate
    public static String getDayOfWeek(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
}

