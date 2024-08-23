package com.jia.consolerenter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentingResponse {
    private Long rentingId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String customerFullName;
    private String customerEmail;
    private String rentingConfirmationCode;
    private int numOfDays;
    private ConsoleResponse console;
}
