package com.demo.user_app;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.demo.user_app.dto.CustDtoConverter;
import com.demo.user_app.model.Customer;
import com.demo.user_app.util.RandomStringUtil;
import com.google.gson.Gson;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerIntegrationTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String CUSTOMER_API = "/api/customer";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createCustomerSucc() throws Exception {
        String first = RandomStringUtil.randomStr(10);
        String last = RandomStringUtil.randomStr(10);
        String email = String.format("%s@%s.com", first, last);
        String phone = RandomStringUtil.randomNumStr(10);
        Customer customer = new Customer(first, null, last, email, phone);
        mockMvc.perform(post(CUSTOMER_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(CustDtoConverter.toDTO(customer))))
                .andExpect(status().isCreated());
    }

    @Test
    public void createCustomerFail() throws Exception {
        String first = RandomStringUtil.randomStr(10);
        String last = RandomStringUtil.randomStr(10);
        Customer customer = new Customer(first, null, last, null, null);
        mockMvc.perform(post(CUSTOMER_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(CustDtoConverter.toDTO(customer))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void findCustomerById() throws Exception {
        mockMvc.perform(get(CUSTOMER_API + "/c40b548a-1111-4273-b7fd-ce251101c921"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("User1"));
    }

    @Test
    public void updateCustomerById() throws Exception {
        String firstName = RandomStringUtil.randomStr(10);
        String lastName = RandomStringUtil.randomStr(10);
        String email = firstName + "@" + lastName + ".com";
        String phone = RandomStringUtil.randomNumStr(10);
        Customer customer = new Customer(firstName, null, lastName, email, phone);
        customer.setId(UUID.fromString("c40b548a-3333-4273-b7fd-ce251101c923")); //User3
        mockMvc.perform(put("/api/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(CustDtoConverter.toDTO(customer))))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCustomerById() throws Exception {
        mockMvc.perform(delete(CUSTOMER_API + "/c40b548a-6666-4273-b7fd-ce251101c926")) //User6
                .andExpect(status().isOk());
    }
}
