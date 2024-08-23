package com.jia.consolerenter.service;

import com.jia.consolerenter.exception.InternalServiceException;
import com.jia.consolerenter.exception.ResourceNotFoundException;
import com.jia.consolerenter.model.Console;
import com.jia.consolerenter.repository.ConsoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsoleService implements IConsoleService {
    private final ConsoleRepository consoleRepository;

    @Override
    public Console addNewConsole(MultipartFile file,String consoleName, String consoleType, BigDecimal consolePrice) throws SQLException, IOException {
        Console console = new Console();
        console.setConsoleName(consoleName);
        console.setConsoleType(consoleType);
        console.setConsolePrice(consolePrice);
        if (!file.isEmpty()) {
            byte[] photoBytes = file.getBytes();
            Blob photBlob = new SerialBlob(photoBytes);
            console.setPhoto(photBlob);
        }
        return consoleRepository.save(console);
    }

    @Override
    public List<String> getAllConsoleTypes() {
        return consoleRepository.findDistinctConsoleTypes();
    }

    @Override
    public List<Console> getAllConsoles() {
        return consoleRepository.findAll();
    }

    @Override
    public byte[] getConsolePhotoByConsoleId(Long consoleId) throws SQLException {
        Optional<Console> theConsole = consoleRepository.findById(consoleId);
        if (theConsole.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, Room not found!");
        }
        Blob photoBlob = theConsole.get().getPhoto();
        
        if(photoBlob != null){
            return photoBlob.getBytes(1,(int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteConsole(Long consoleId) {
        Optional<Console> theConsole = consoleRepository.findById(consoleId);
        if(theConsole.isPresent()){
            consoleRepository.deleteById(consoleId);
        }
    }

    @Override
    public Console updateConsole(Long consoleId,String consoleName, String consoleType, BigDecimal consolePrice, byte[] photoBytes) {
        Console console = consoleRepository.findById(consoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Console not found"));
        if(consoleName != null){
            console.setConsoleName(consoleName);
        }
        if (consoleType != null){
            console.setConsoleType(consoleType);
        }
        if (consolePrice != null) {
            console.setConsolePrice(consolePrice);
        }
        if(photoBytes != null && photoBytes.length>0){
            try{
                console.setPhoto(new SerialBlob(photoBytes));

            }catch(SQLException ex){
                throw new InternalServiceException("Error updating console");

            }

        }
        return consoleRepository.save(console);
    }

    @Override
    public Optional<Console> getConsoleById(Long consoleId) {
        return Optional.of(consoleRepository.findById(consoleId).get());
    }


}