package com.demo.user_app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import com.demo.user_app.dto.CustDtoConverter;
import com.demo.user_app.dto.CustomerDto;
import com.demo.user_app.dto.PageDto;
import com.demo.user_app.error.CustServiceException;
import com.demo.user_app.model.Customer;
import com.demo.user_app.repository.CustomerRepository;
import com.demo.user_app.util.EmailUtil;
import com.google.gson.Gson;

import io.micrometer.common.util.StringUtils;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {
        validateCreateCustomerDTO(customerDto);
        // Check if email already exists
        if (customerRepository.existsByEmailAddress(customerDto.getEmailAddress())) {
            throw new CustServiceException("Email address already exists");
        }
        Customer customer = CustDtoConverter.toEntity(customerDto);
        Customer savedCustomer  = customerRepository.save(customer);
        return CustDtoConverter.toDTO(savedCustomer);
    }

    private void validateCreateCustomerDTO(CustomerDto customerDto) {
        if (StringUtils.isEmpty(customerDto.getFirstName())) {
            throw new CustServiceException("First name missing");
        }
        if (StringUtils.isEmpty(customerDto.getLastName())) {
            throw new CustServiceException("Last name missing");
        }
        if (StringUtils.isEmpty(customerDto.getEmailAddress())) {
            throw new CustServiceException("Email address missing");
        }
        if (StringUtils.isEmpty(customerDto.getPhoneNumber())) {
            throw new CustServiceException("Phone number missing");
        }
        if (!EmailUtil.isValidEmailFormat(customerDto.getEmailAddress())) {
            throw new CustServiceException("Invalid email format");
        }
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        Optional<CustomerDto> result = customerRepository.findById(id).map(CustDtoConverter::toDTO);
        return result;
    }

    @Override
    public PageDto<CustomerDto> getCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage = customerRepository.findAll(pageable);
        PageDto<CustomerDto> customerDTOPage = new PageDto<>(
                customerPage.getContent().stream()
                        .map(CustDtoConverter::toDTO)
                        .toList(),
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements(),
                customerPage.getTotalPages()
        );
        return customerDTOPage;
    }

    // Check if this email address been used by other customers in db
    private boolean checkEmailConflict(String email, UUID id) {
        Optional<Customer> existingCustomer = customerRepository.findByEmailAddress(email);
        return existingCustomer.isPresent() && !existingCustomer.get().getId().equals(id);
    }

    private boolean checkTrim(final String value) {
        return (value != null && value.trim().isEmpty());
    }

    private void validateUpdateCustomerDto(CustomerDto customerDto) {
        if (customerDto.getId() == null) {
            throw new CustServiceException("Id can not be empty");
        }
        if (checkTrim(customerDto.getFirstName())) {
            throw new CustServiceException("First name can not be empty");
        }
        if (checkTrim(customerDto.getLastName())) {
            throw new CustServiceException("Last name can not be empty");
        }
        if (checkTrim(customerDto.getPhoneNumber())) {
            throw new CustServiceException("Phone number can not be empty");
        }
        if (checkTrim(customerDto.getEmailAddress())) {
            throw new CustServiceException("Email address can not be empty");
        }

        Optional<Customer> customer = customerRepository.findById(customerDto.getId());
        if (customer.isEmpty()) {
            throw new CustServiceException("Customer not found");
        }

        if (StringUtils.isNotBlank(customerDto.getEmailAddress())) {
            if (!EmailUtil.isValidEmailFormat(customerDto.getEmailAddress()))
                throw new CustServiceException("Invalid email format");
            if (checkEmailConflict(customerDto.getEmailAddress(), customerDto.getId()))
                throw new CustServiceException("Email address already exists");
        }
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        validateUpdateCustomerDto(customerDto);
        int result = customerRepository.updateCustomerIfChanged(
                customerDto.getId(),
                customerDto.getFirstName(),
                customerDto.getMiddleName(),
                customerDto.getLastName(),
                customerDto.getEmailAddress(),
                customerDto.getPhoneNumber()
        );
        return result > 0;
    }

    @Override
    public boolean deleteCustomerById(UUID id) {
        return customerRepository.deleteCustomerById(id) > 0;
    }
}
