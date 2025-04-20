package org.example.service;

import org.example.holidays.HolidayManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class VacationCalculatorService {

    @Autowired
    private HolidayManager holidayManager;

    public double calculateVacationPay(double averageSalary, int vacationDays) {
        return averageSalary * vacationDays;
    }

    public double calculateVacationPay(double averageSalary, LocalDate start, LocalDate end) {
        long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;

        int dayOffCount = holidayManager.getNumberDaysOffBetweenDates(start, end);
        int workingDays = (int) daysBetween - dayOffCount;
        return workingDays * averageSalary;
    }
}