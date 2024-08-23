package com.jia.consolerenter.service;

import com.jia.consolerenter.exception.InvalidRentingRequestException;
import com.jia.consolerenter.exception.ResourceNotFoundException;
import com.jia.consolerenter.model.Console;
import com.jia.consolerenter.model.RentedConsole;
import com.jia.consolerenter.repository.RentingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;


import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentingService implements IRentingService {
    private final RentingRepository rentingRepository;
    private final IConsoleService consoleService;



    @Override
    public List<RentedConsole> getAllRentings() {
        return rentingRepository.findAll();
    }

    @Override
    public void cancelRenting(Long rentingId) {
        rentingRepository.deleteById(rentingId);
    }

    @Override
    public List<RentedConsole> getAllRentingsByConsoleId(Long consoleId) {
        return rentingRepository.findByConsoleId(consoleId);
    }

    @Override
    public String saveRenting(Long consoleId, RentedConsole rentingRequest) {
        log.debug("Saving renting request: {}", rentingRequest);

        if (rentingRequest.getCheckOutDate().isBefore(rentingRequest.getCheckInDate())) {
            throw new InvalidRentingRequestException("Check-in date must come before checkout date");
        }

        Console console = consoleService.getConsoleById(consoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Console not found"));

        rentingRequest.setConsole(console);
        rentingRequest.setRentingConfirmationCode(generateConfirmationCode());

        log.debug("Renting request after setting console and confirmation code: {}", rentingRequest);

        rentingRepository.save(rentingRequest);
        log.info("Renting request saved successfully with confirmation code: {}", rentingRequest.getRentingConfirmationCode());

        return rentingRequest.getRentingConfirmationCode();
    }

    @Override
    public RentedConsole findByRentingConfirmationCode(String confirmationCode) {
        return rentingRepository.findByRentingConfirmationCode(confirmationCode);
    }

    private String generateConfirmationCode() {

        Random random = new Random();
        long randomNumber = 1000000000L + (long) (random.nextDouble() * 9000000000L);
        return String.valueOf(randomNumber);
    }
}
