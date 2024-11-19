package com.demo.user_app.controller;

import com.demo.user_app.util.UUIDUtil;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.demo.user_app.dto.CustomerDto;
import com.demo.user_app.dto.PageDto;
import com.demo.user_app.dto.MessageResponse;
import com.demo.user_app.error.CustServiceException;
import com.demo.user_app.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Create a new customer (ID is ignored)")
    @PostMapping
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerDto customerDTO) {
        try {
            CustomerDto savedCustomerDto = customerService.createCustomer(customerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDto);
        } catch (CustServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get a list of customers with pagination support")
    @GetMapping("list")
    public ResponseEntity<PageDto<CustomerDto>> getCustomers(
            @Valid @RequestParam(defaultValue = "0") @Min(0) int page,
            @Valid @RequestParam(defaultValue = "50") @Min(10) @Max(1000) int size)
    {
        PageDto<CustomerDto> customerDTOPage = customerService.getCustomers(page, size);
        return ResponseEntity.ok(customerDTOPage);
    }

    @Operation(summary = "Get the customer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@Valid @PathVariable UUID id) {
        logger.info("getCustomerById() [{}]", id);
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Operation(summary = "Update the customer by a Customer JSON object")
    @PutMapping
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerDto customerDTO) {
        try {
            boolean result = customerService.updateCustomer(customerDTO);
            if (result) {
                return ResponseEntity.ok(new MessageResponse("Update successful"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Customer not found"));
            }
        } catch (CustServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Delete the customer by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@Valid @PathVariable UUID id) {
        boolean result = customerService.deleteCustomerById(id);
        if (result) {
            return ResponseEntity.ok(new MessageResponse("Delete successful"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Customer not found"));
        }
    }
}
