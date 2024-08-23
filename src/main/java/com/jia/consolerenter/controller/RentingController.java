package com.jia.consolerenter.controller;

import com.jia.consolerenter.exception.InvalidRentingRequestException;
import com.jia.consolerenter.exception.ResourceNotFoundException;
import com.jia.consolerenter.model.Console;
import com.jia.consolerenter.model.RentedConsole;
import com.jia.consolerenter.response.ConsoleResponse;
import com.jia.consolerenter.response.RentingResponse;
import com.jia.consolerenter.service.IConsoleService;
import com.jia.consolerenter.service.IRentingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rentings")
public class RentingController {
    private final IRentingService rentingService;
    private final IConsoleService consoleService;

    @GetMapping("/all-rentings")
    public ResponseEntity<List<RentingResponse>> getAllRentings() {
        List<RentedConsole> rentings = rentingService.getAllRentings();
        List<RentingResponse> rentingResponses = new ArrayList<>();
        for (RentedConsole renting : rentings){
            RentingResponse rentingResponse = getRentingResponse(renting);
            rentingResponses.add(rentingResponse);
        }
        return ResponseEntity.ok(rentingResponses);
    }


    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getRentingByConfirmationCode(@PathVariable String confirmationCode){
        try{
            RentedConsole renting = rentingService.findByRentingConfirmationCode(confirmationCode);
            RentingResponse rentingResponse = getRentingResponse(renting);
            return ResponseEntity.ok(rentingResponse);
        }catch(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/console/{consoleId}/renting")
    public ResponseEntity<?> saveRenting(@PathVariable Long consoleId, @RequestBody RentedConsole rentingRequest){
        try{
            String confirmationCode = rentingService.saveRenting(consoleId, rentingRequest);
            return ResponseEntity.ok().body(Map.of("confirmationCode", confirmationCode));
        }catch(InvalidRentingRequestException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/renting/{rentingId}/delete")
    public void cancelRenting(@PathVariable Long rentingId){
        rentingService.cancelRenting(rentingId);
    }

    private RentingResponse getRentingResponse(RentedConsole renting) {
        Console theConsole = consoleService.getConsoleById(renting.getConsole().getId()).orElseThrow(() -> new ResourceNotFoundException("Console not found"));
        ConsoleResponse console = new ConsoleResponse(
                theConsole.getId(),
                theConsole.getConsoleName(),
                theConsole.getConsoleType(),
                theConsole.getConsolePrice());

        return new RentingResponse(
                renting.getRentingId(),
                renting.getCheckInDate(),
                renting.getCheckOutDate(),
                renting.getCustomerFullName(),
                renting.getCustomerEmail(),
                renting.getRentingConfirmationCode(),
                renting.getNumOfDays(),
                console);
    }
}
