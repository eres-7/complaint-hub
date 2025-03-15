package pl.eres.complaint_hub.complaint.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoLocationServiceTest {

    private static final String GEO_API_URL = "http://ip-api.com/json/%s";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeoLocationService geoLocationService;

    @BeforeEach
    void setUp() {
        geoLocationService = new GeoLocationService(restTemplate);
        ReflectionTestUtils.setField(geoLocationService, "geoApiUrl", GEO_API_URL);
    }

    @Test
    void shouldReturnCountryWhenApiReturnsValidResponse() {
        // Given
        String ip = "8.8.8.8";
        String expectedCountry = "United States";
        String url = String.format(GEO_API_URL, ip);
        Map<String, Object> mockResponse = Map.of("country", expectedCountry);
        when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        // When
        String actualCountry = geoLocationService.getCountryByIp(ip);

        // Then
        assertEquals(expectedCountry, actualCountry);
    }

    @Test
    void shouldReturnUnknownWhenApiResponseIsNull() {
        // Given
        String ip = "8.8.8.8";
        String url = String.format(GEO_API_URL, ip);
        when(restTemplate.getForObject(url, Map.class)).thenReturn(null);

        // When
        String actualCountry = geoLocationService.getCountryByIp(ip);

        // Then
        assertEquals("Unknown", actualCountry);
    }

    @Test
    void shouldReturnUnknownWhenApiDoesNotContainCountryKey() {
        // Given
        String ip = "8.8.8.8";
        String url = String.format(GEO_API_URL, ip);
        Map<String, Object> mockResponse = Map.of("status", "success");
        when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        // When
        String actualCountry = geoLocationService.getCountryByIp(ip);

        // Then
        assertEquals("Unknown", actualCountry);
    }
}