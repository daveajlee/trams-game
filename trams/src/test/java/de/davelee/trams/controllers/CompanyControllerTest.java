package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.SatisfactionRateResponse;
import de.davelee.trams.api.response.TimeResponse;
import de.davelee.trams.api.response.VehicleResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class CompanyControllerTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private VehicleController vehicleController;

	@InjectMocks
	private CompanyController companyController;
	
	@Test
	public void testIncrement() {
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
		companyController.createCompany("Dave A J Lee", "Landuff Transport Company", "Mustermann GmbH");
		Mockito.when(restTemplate.getForObject(anyString(), eq(CompanyResponse.class))).thenReturn(null);
		Assertions.assertNull(companyController.getCompany("Mustermann GmbH", "My First Name"));
		Mockito.when(restTemplate.getForObject(anyString(), eq(CompanyResponse.class))).thenReturn(
				CompanyResponse.builder().name("Mustermann GmbH").playerName("Dave A J Lee").difficultyLevel("EASY")
						.time("24-12-2020 11:58").build()
		);
		Assertions.assertNotNull(companyController.getCompany("Mustermann GmbH", "Dave A J Lee"));
		CompanyResponse companyResponse = companyController.getCompany("Mustermann GmbH", "Dave A J Lee");
		assertEquals(companyResponse.getTime(), "24-12-2020 11:58");
		Mockito.when(restTemplate.patchForObject(anyString(), any(), eq(TimeResponse.class))).thenReturn(
				TimeResponse.builder()
						.company("Mustermann GmbH")
						.time("24-12-2020 12:13")
						.build()
		);
		String time = companyController.incrementTime("Mustermann GmbH");
		assertEquals("24-12-2020 12:13", time);
		companyController.withdrawOrCreditBalance(-100.0, "Dave A J Lee");
		companyController.withdrawOrCreditBalance(10.0, "Dave A J Lee");
		Mockito.when(restTemplate.patchForObject(anyString(), any(), eq(SatisfactionRateResponse.class))).thenReturn(
				SatisfactionRateResponse.builder()
						.company("Mustermann GmbH")
						.satisfactionRate(91)
						.build()
		);
		Mockito.when(vehicleController.getVehicleModels("Mustermann GmbH")).thenReturn(
				new VehicleResponse[0]
		);
		assertEquals(companyController.computeAndReturnPassengerSatisfaction("Mustermann GmbH", "EASY"), 91);
	}

}
