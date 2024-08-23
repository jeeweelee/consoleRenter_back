package com.jia.consolerenter.response;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;

import java.util.List;

@Data
@NoArgsConstructor
public class ConsoleResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String consoleName;
    private String consoleType;
    private BigDecimal consolePrice;
    private boolean isRented;


    private String photo;

    private List<RentingResponse> rentings;

    public ConsoleResponse(Long id,String consoleName, String consoleType, BigDecimal consolePrice) {
        this.id = id;
        this.consoleName = consoleName;
        this.consoleType = consoleType;
        this.consolePrice = consolePrice;
    }

    public ConsoleResponse(Long id,String consoleName, String consoleType, BigDecimal consolePrice, boolean isRented, byte[] photoBytes) {
        this.id = id;
        this.consoleName = consoleName;
        this.consoleType = consoleType;
        this.consolePrice = consolePrice;
        this.isRented = isRented;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes):null;

    }
}
