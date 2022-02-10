package de.davelee.trams.controllers;

import de.davelee.trams.api.response.ExportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

/**
 * This class enables access to route and vehicle exports via REST endpoints to the TraMS Operations microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
public class ExportController {

    private RestTemplate restTemplate;

    @Value("${server.operations.url}")
    private String operationsServerUrl;

    /**
     * Get the export of routes and vehicles for the supplied company.
     * @param company a <code>String</code> with the name of the company to return export for.
     * @return a <code>ExportResponse</code> object containing the exported data.
     */
    public ExportResponse getExport (final String company ) {
        return restTemplate.getForObject(operationsServerUrl + "export/?company=" + company, ExportResponse.class);
    }

    /**
     * Set the rest template object via Spring.
     * @param restTemplate a <code>RestTemplate</code> object.
     */
    @Autowired
    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
