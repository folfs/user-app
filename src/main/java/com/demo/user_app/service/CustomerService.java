package com.demo.user_app.service;

import java.util.Optional;
import java.util.UUID;

import com.demo.user_app.dto.CustomerDto;
import com.demo.user_app.dto.PageDto;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto customer);

    Optional<CustomerDto> getCustomerById(UUID id);

    PageDto<CustomerDto> getCustomers(int page, int size);

    boolean updateCustomer(CustomerDto customer);

    boolean deleteCustomerById(UUID id);
}
