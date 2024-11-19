package com.demo.user_app.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class CustomerDto {
    private UUID id;
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;
    @Size(max = 50, message = "Middle must not exceed 50 characters")
    private String middleName;
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;
    @Size(max = 120, message = "Email must not exceed 120 characters")
    private String emailAddress;
    @Size(max = 30, message = "Phone number must not exceed 30 characters")
    private String phoneNumber;

    public CustomerDto(UUID id, String firstName, String middleName, String lastName, String emailAddress, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }
}
