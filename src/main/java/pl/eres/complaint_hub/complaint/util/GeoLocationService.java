package pl.eres.complaint_hub.complaint.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoLocationService {

    private final RestTemplate restTemplate;

    @Value("${geo.api.url}")
    private String geoApiUrl;

    public String getCountryByIp(String ip) {
        String url = String.format(geoApiUrl, ip);
        var response = restTemplate.getForObject(url, Map.class);
        return MapUtils.isNotEmpty(response) ? (String) response.getOrDefault("country", "Unknown") : "Unknown";
    }
}
