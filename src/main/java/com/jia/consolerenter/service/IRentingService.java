package com.jia.consolerenter.service;

import com.jia.consolerenter.model.Console;
import com.jia.consolerenter.model.RentedConsole;

import java.util.List;

public interface IRentingService {

    List<RentedConsole> getAllRentingsByConsoleId(Long consoleId);

    void cancelRenting(Long rentingId);

    String saveRenting(Long consoleId, RentedConsole rentingRequest);

    RentedConsole findByRentingConfirmationCode(String confirmationCode);



    List<RentedConsole> getAllRentings();
}


