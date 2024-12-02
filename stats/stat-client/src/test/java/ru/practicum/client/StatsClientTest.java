package ru.practicum.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;


@SpringBootTest
@ComponentScan(basePackages = {"ru.practicum.client", "ru.practicum.server", "ru.practicum.dto"})
public class StatsClientTest {

    @Autowired
    StatsClient statsClient;

    @Test
    void testClient() {

    }

}