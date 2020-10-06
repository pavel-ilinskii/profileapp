package com.ilinskii.profileapp;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;

@ContextConfiguration(initializers = AbstractTest.Initializer.class)
public abstract class AbstractTest {

    private static final PostgreSQLContainer dbContainer = new PostgreSQLContainer();

    private static final String NETWORK_ALIAS = "postgres";

    static {
        dbContainer.withNetwork(Network.SHARED)
                .withNetworkAliases(NETWORK_ALIAS)
                .start();
    }

    private static final String JDBC_URL = dbContainer.getJdbcUrl();
    private static final String USERNAME = dbContainer.getUsername();
    private static final String PASSWORD = dbContainer.getPassword();


    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + JDBC_URL,
                    "spring.datasource.username=" + USERNAME,
                    "spring.datasource.password=" + PASSWORD
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}

