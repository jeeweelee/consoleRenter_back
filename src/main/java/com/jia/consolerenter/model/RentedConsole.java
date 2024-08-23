package com.jia.consolerenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rented")
public class RentedConsole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentingId;

    @Column(name = "check_In")
    private LocalDate checkInDate;

    @Column(name = "check_Out")
    private LocalDate checkOutDate;

    @Column(name = "customer_FullName")
    private String customerFullName;

    @Column(name = "customer_Email")
    private String customerEmail;

    @Column(name = "confirmation_code")
    private String rentingConfirmationCode;

    @Column(name = "numOfDays")
    private int numOfDays;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "console_id")
    private Console console;
}
