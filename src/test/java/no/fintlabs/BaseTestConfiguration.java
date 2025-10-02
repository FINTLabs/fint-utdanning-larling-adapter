package no.fintlabs;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.WireMockServer;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import no.fintlabs.adapter.AdapterRegisterService;
import no.fintlabs.adapter.datasync.ResourceSubscriber;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@TestConfiguration
public class BaseTestConfiguration {

    // Mock the AdapterRegisterService to prevent actual registration during tests
    @MockBean
    private AdapterRegisterService adapterRegisterService;

//    @MockBean
//    private ResourceSubscriber resourceSubscriber;

}
