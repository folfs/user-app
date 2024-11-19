package com.demo.user_app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.demo.user_app.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Customer c WHERE c.id = :id")
    int deleteCustomerById(UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET " +
            "c.firstName = COALESCE(:firstName, c.firstName), " +
            "c.middleName = COALESCE(:middleName, c.middleName), " +
            "c.lastName = COALESCE(:lastName, c.lastName), " +
            "c.emailAddress = COALESCE(:emailAddress, c.emailAddress), " +
            "c.phoneNumber = COALESCE(:phoneNumber, c.phoneNumber) " +
            "WHERE c.id = :id")
    int updateCustomerIfChanged(@Param("id") UUID id,
                                @Param("firstName") String firstName,
                                @Param("middleName") String middleName,
                                @Param("lastName") String lastName,
                                @Param("emailAddress") String emailAddress,
                                @Param("phoneNumber") String phoneNumber);

    boolean existsByEmailAddress(String emailAddress);

    Optional<Customer> findByEmailAddress(String emailAddress);
}
