package com.mauntung.mauntung.domain.model.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    static ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void whenWriteValueAsString_genderEnum_shouldReturnToString() throws JsonProcessingException {
        assertEquals(String.format("\"%s\"", Customer.Gender.FEMALE), jsonMapper.writeValueAsString(Customer.Gender.FEMALE));
        assertEquals(String.format("\"%s\"", Customer.Gender.MALE), jsonMapper.writeValueAsString(Customer.Gender.MALE));
    }
}