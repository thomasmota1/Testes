package com.tms.hello;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloJUnitTest {
    @Test
    void testSayHello() {
        assertEquals("Hello, World of Tests!", new HelloJUnit().sayHello());
    }
}
