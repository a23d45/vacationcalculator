package org.example.controller;

import org.example.exception.BadRequestException;
import org.example.holidays.HolidayManager;
import org.example.model.VacationResponse;
import org.example.service.VacationCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
public class VacationCalculatorController {

    private final VacationCalculatorService vacationCalculatorService;
    @Autowired
    private HolidayManager holidayManager;

    public VacationCalculatorController(VacationCalculatorService vacationCalculatorService) {
        this.vacationCalculatorService = vacationCalculatorService;
    }

    @GetMapping("/calculate")
    public VacationResponse calculateVacationPay(
            @RequestParam Double averageSalary,
            @RequestParam Integer vacationDays,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        // Проверка на позитивные числовые значения
        if (averageSalary <= 0) {
            throw new BadRequestException("Average salary must be a positive number.");
        }
        if (vacationDays <= 0) {
            throw new BadRequestException("Vacation days must be a positive integer.");
        }

        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);

        double vacationPay;
        if (start == null || end == null) {
            vacationPay = vacationCalculatorService.calculateVacationPay(averageSalary, vacationDays);
            return new VacationResponse(vacationPay);
        }

        if (start.getYear() != 2025 || end.getYear() != 2025) {
            throw new BadRequestException("Year must be 2025.");
        }

        if (start.isAfter(end)) {
            throw new BadRequestException("Start date cannot be after end date.");
        }

        vacationPay = vacationCalculatorService.calculateVacationPay(averageSalary, start, end);
        return new VacationResponse(vacationPay);
    }

    private LocalDate parseDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: " + date);
        }
    }
}