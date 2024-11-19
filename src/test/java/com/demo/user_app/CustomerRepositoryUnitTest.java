package com.demo.user_app;
import com.demo.user_app.model.Customer;
import com.demo.user_app.repository.CustomerRepository;
import com.demo.user_app.util.RandomStringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class CustomerRepositoryUnitTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void createCustomer() {
        String firstName = RandomStringUtil.randomStr(10);
        String lastName = RandomStringUtil.randomStr(10);
        String email = String.format("%s@%s.com", firstName, lastName);
        String phone = RandomStringUtil.randomNumStr(10);
        Customer newCustomer = new Customer(firstName, null, lastName, email, phone);
        Customer savedCustomer = customerRepository.save(newCustomer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getFirstName()).isEqualTo(firstName);
    }

    @Test
    public void findCustomerById() {
        Optional<Customer> foundCustomer = customerRepository.findById(UUID.fromString("fb3f593a-4444-4350-afce-e27259e1dba4"));
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getFirstName()).isEqualTo("User4");
        assertThat(foundCustomer.get().getLastName()).isEqualTo("Last4");
    }

    @Test
    public void updateCustomerById() {
        String lastName = RandomStringUtil.randomStr(10);
        String phone = RandomStringUtil.randomNumStr(10);
        Customer customer = new Customer();
        customer.setId(UUID.fromString("c40b548a-5555-4273-b7fd-ce251101c925"));
        customer.setLastName(lastName);
        customer.setPhoneNumber(phone);

        Customer updatedCustomer = customerRepository.save(customer);
        assertThat(updatedCustomer.getPhoneNumber()).isEqualTo(phone);
        assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void deleteCustomerById() {
        Customer customer = new Customer();
        customer.setId(UUID.fromString("c40b548a-2222-4273-b7fd-ce251101c922"));
        customerRepository.delete(customer);

        Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());
        assertThat(deletedCustomer).isNotPresent();
    }
}
