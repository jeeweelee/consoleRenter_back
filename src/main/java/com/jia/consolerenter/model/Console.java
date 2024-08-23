package com.jia.consolerenter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Console {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String consoleName;
    private String consoleType;
    private BigDecimal consolePrice;
    private boolean isRented = false;

    @Lob
    private Blob photo;

    @OneToMany(mappedBy="console", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RentedConsole> rentings;

    public Console() {
        this.rentings = new ArrayList<>();
    }


}
