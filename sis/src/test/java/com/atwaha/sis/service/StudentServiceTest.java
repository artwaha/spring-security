package com.atwaha.sis.service;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StudentServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceTest.class);

    @AfterAll
    static void afterAll() {
        logger.info("afterAll");
    }

    @BeforeAll
    static void beforeAll() {
        logger.info("beforeAll");
    }

    @BeforeEach
    void setUp() {
        logger.info("setUp");
    }

    @AfterEach
    void tearDown() {
        logger.info("tearDown");
    }

    @Test
    void getAllStudents() {
        logger.info("getAllStudents");
    }

    @Test
    void addStudent() {
        logger.info("addStudent");
    }
}