package com.demo.user_app.dto;
import com.demo.user_app.model.Customer;

import java.util.UUID;

public class CustDtoConverter {
    public static CustomerDto toDTO(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getMiddleName(),
                customer.getLastName(),
                customer.getEmailAddress(),
                customer.getPhoneNumber()
        );
    }

    public static Customer toEntity(CustomerDto customerDTO) {
        return new Customer(
                customerDTO.getFirstName(),
                customerDTO.getMiddleName(),
                customerDTO.getLastName(),
                customerDTO.getEmailAddress(),
                customerDTO.getPhoneNumber()
        );
    }
}
