package com.jia.consolerenter.controller;

import com.jia.consolerenter.exception.PhotoRetrievalException;
import com.jia.consolerenter.exception.ResourceNotFoundException;
import com.jia.consolerenter.model.Console;
import com.jia.consolerenter.model.RentedConsole;
import com.jia.consolerenter.response.ConsoleResponse;
import com.jia.consolerenter.service.IConsoleService;
import com.jia.consolerenter.service.RentingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consoles")
public class ConsoleController {
    private final IConsoleService consoleService;


    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ConsoleResponse> addNewConsole(@RequestParam("photo") MultipartFile photo,
                                                         @RequestParam("consoleName") String consoleName,
                                                         @RequestParam("consoleType") String consoleType,
                                                         @RequestParam("consolePrice") BigDecimal consolePrice) throws SQLException, IOException {
        Console savedConsole = consoleService.addNewConsole(photo, consoleName, consoleType, consolePrice);
        ConsoleResponse response = new ConsoleResponse(savedConsole.getId(),savedConsole.getConsoleName(), savedConsole.getConsoleType(), savedConsole.getConsolePrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/console/types")
    public List<String> getConsoleTypes(){

        return consoleService.getAllConsoleTypes();
    }
    @GetMapping("/all-consoles")
    public ResponseEntity<List<ConsoleResponse>> getAllConsoles() throws SQLException {
        List<Console> consoles = consoleService.getAllConsoles();
        List<ConsoleResponse> consoleResponses = new ArrayList<>();
        for(Console console : consoles){
            byte[] photoBytes = consoleService.getConsolePhotoByConsoleId(console.getId());
            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                ConsoleResponse consoleResponse = getConsoleResponse(console);
                consoleResponse.setPhoto(base64Photo);
                consoleResponses.add(consoleResponse);
            }
        }
        return ResponseEntity.ok(consoleResponses);
    }

    @DeleteMapping("/delete/console/{consoleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteConsole(@PathVariable Long consoleId){
        consoleService.deleteConsole(consoleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/update/{consoleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ConsoleResponse> updateConsole(
            @PathVariable Long consoleId,
            @RequestParam(required = false) String consoleName,
            @RequestParam(required = false) String consoleType,
            @RequestParam(required = false) BigDecimal consolePrice,
            @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {

        byte[] photoBytes = null;
        Blob photoBlob = null;

        if (photo != null && !photo.isEmpty()) {
            photoBytes = IOUtils.toByteArray(photo.getInputStream());
            photoBlob = new SerialBlob(photoBytes);
        } else {
            photoBytes = consoleService.getConsolePhotoByConsoleId(consoleId);
            photoBlob = new SerialBlob(photoBytes);
        }

        Console updatedConsole = consoleService.updateConsole(consoleId,consoleName, consoleType, consolePrice, photoBytes);
        updatedConsole.setPhoto(photoBlob);

        ConsoleResponse consoleResponse = getConsoleResponse(updatedConsole);

        return ResponseEntity.ok(consoleResponse);
    }

    @GetMapping("/console/{consoleId}")
    public ResponseEntity<Optional<ConsoleResponse>> getRoomById(@PathVariable Long consoleId){
        Optional<Console> theConsole = consoleService.getConsoleById(consoleId);
        return theConsole.map(console -> {
            ConsoleResponse consoleResponse = getConsoleResponse(console);
            return  ResponseEntity.ok(Optional.of(consoleResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Console not found"));
    }

    private ConsoleResponse getConsoleResponse(Console console) {
        List<RentedConsole> rentings = getAllRentingsByConsoleId(console.getId());

        byte[] photoBytes = null;
        Blob photoBlob = console.getPhoto();
        if(photoBlob != null){
            try{
                photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
            } catch(SQLException e){
                throw new PhotoRetrievalException("Error retrieving photo");

            }
        }
        return new ConsoleResponse(console.getId(),console.getConsoleName(),
                console.getConsoleType(), console.getConsolePrice(),
                console.isRented(), photoBytes);
    }

    private List<RentedConsole> getAllRentingsByConsoleId(Long consoleId) {
        return null;

    }
}
