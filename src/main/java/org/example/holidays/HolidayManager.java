package org.example.holidays;

import lombok.Getter;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
public class HolidayManager {
    @Getter
    private Set<LocalDate> weekendDays = new HashSet<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private String path = "holidays.txt";

    public HolidayManager() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LocalDate date = LocalDate.parse(line + "-2025", formatter);
                weekendDays.add(date);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberDaysOffBetweenDates(LocalDate startDate, LocalDate endDate) {
        int result = 0;

        LocalDate currentDate = startDate;
        while (!currentDate.equals(endDate)) {
            if (weekendDays.contains(currentDate)) {
                result++;
            }
            currentDate = currentDate.plusDays(1);

        }
        if (weekendDays.contains(endDate)) {
            result++;
        }

        return result;
    }


}