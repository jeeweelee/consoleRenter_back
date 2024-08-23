package com.jia.consolerenter.repository;

import com.jia.consolerenter.model.RentedConsole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentingRepository extends JpaRepository<RentedConsole, Long> {
    List<RentedConsole> findByConsoleId(Long consoleId);

    RentedConsole findByRentingConfirmationCode(String confirmationCode);
}
