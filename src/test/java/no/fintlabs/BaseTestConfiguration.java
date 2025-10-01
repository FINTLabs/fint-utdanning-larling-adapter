package no.fintlabs;

import no.fintlabs.adapter.AdapterRegisterService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class BaseTestConfiguration {

    // Mock the AdapterRegisterService to prevent actual registration during tests
    @MockBean
    private AdapterRegisterService adapterRegisterService;
}
