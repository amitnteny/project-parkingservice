package com.parkingservice.project.controller;

import com.parkingservice.project.dto.BookingRequest;
import com.parkingservice.project.dto.TicketDetails;
import com.parkingservice.project.exception.BadRequestException;
import com.parkingservice.project.exception.ErrorResponse;
import com.parkingservice.project.exception.SlotUnavailableException;
import com.parkingservice.project.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping(path = "/park", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TicketDetails> bookSlot(@RequestBody BookingRequest bookingRequest) {
        TicketDetails ticketDetails = bookingService.bookSlot(bookingRequest);
        return ResponseEntity.ok().body(ticketDetails);
    }

    @GetMapping(path = "/unpark", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity unPark(@RequestParam("ticket_id") Long ticketId) throws BadRequestException {
        bookingService.freeSlot(ticketId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleSlotUnavailableException(SlotUnavailableException exception) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setErrorMessage(exception.getMessage());
        error.setTimestamp(Instant.now().getEpochSecond());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST);
        error.setErrorMessage(exception.getMessage());
        error.setTimestamp(Instant.now().getEpochSecond());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception exception) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        error.setErrorMessage(exception.getMessage());
        error.setTimestamp(Instant.now().getEpochSecond());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
