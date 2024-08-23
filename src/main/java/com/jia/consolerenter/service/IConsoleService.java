package com.jia.consolerenter.service;

import com.jia.consolerenter.model.Console;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IConsoleService {


    Console addNewConsole(MultipartFile file, String consoleName, String consoleType, BigDecimal consolePrice) throws SQLException, IOException;

    List<String> getAllConsoleTypes();

    List<Console> getAllConsoles();

    byte[] getConsolePhotoByConsoleId(Long consoleId) throws SQLException;

    void deleteConsole(Long consoleId);



    Console updateConsole(Long consoleId, String consoleName, String consoleType, BigDecimal consolePrice, byte[] photoBytes);

    Optional<Console> getConsoleById(Long consoleId);


}
