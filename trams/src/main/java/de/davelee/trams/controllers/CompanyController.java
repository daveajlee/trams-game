package de.davelee.trams.controllers;

import de.davelee.trams.api.request.*;
import de.davelee.trams.api.response.*;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

/**
 * This class enables access to Company data via REST endpoints to the TraMS Business microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
public class CompanyController {

	private RestTemplate restTemplate;

	@Value("${server.business.url}")
	private String businessServerUrl;

	private VehicleController vehicleController;

	/**
	 * Withdraw or credit the specified amount from/to the balance of the current company.
	 * @param amount a <code>double</code> with the amount to withdraw from the balance if negative or credit if positive.
	 * @param company a <code>String</code> with the name of the company.
	 */
	public void withdrawOrCreditBalance ( final double amount, final String company ) {
		restTemplate.patchForObject(businessServerUrl + "company/balance",
				AdjustBalanceRequest.builder()
						.company(company)
						.value(amount).build(),
				BalanceResponse.class);
	}

	/**
	 * Create a new company for the specified player running the specified scenario.
	 * @param playerName a <code>String</code> with the player name for this new company.
	 * @param scenarioName a <code>String</code> with the scenario to use for this new company.
	 * @param company a <code>String</code> with the name of the company to use.
	 * @return a <code>CompanyResponse</code> representing the new company which was created.
	 */
	public CompanyResponse createCompany ( final String playerName, final String scenarioName, final String company ) {
		//Save game to db and return it.
		restTemplate.postForObject(businessServerUrl + "company/", CompanyRequest.builder()
				.name(company)
				.startingBalance(80000.00)
				.startingTime("01-03-2017 04:00")
				.playerName(playerName)
				.difficultyLevel(DifficultyLevel.EASY.name())
				.scenarioName(scenarioName)
				.build(), Void.class);
		return restTemplate.getForObject(businessServerUrl + "company/?name=" + company + "&playerName=" + playerName, CompanyResponse.class);
	}

	/**
	 * This method loads a company from a saved file.
	 * @param companyResponse a <code>CompanyResponse</code> containing the company to load.
	 */
	public void loadCompany ( final CompanyResponse companyResponse ) {
		//Save game to db.
		restTemplate.postForObject(businessServerUrl + "company/", CompanyRequest.builder()
				.name(companyResponse.getName())
				.startingBalance(companyResponse.getBalance())
				.startingTime(companyResponse.getTime())
				.playerName(companyResponse.getPlayerName())
				.difficultyLevel(companyResponse.getDifficultyLevel())
				.scenarioName(companyResponse.getScenarioName())
				.build(), Void.class);
	}

	/**
	 * This method returns the current game model from the cache.
	 * @param company a <code>String</code> with the name of the company.
	 * @param playerName a <code>String</code> with the player name for this game.
	 * @return a <code>CompanyResponse</code> representing the current game being played.
	 */
	public CompanyResponse getCompany (final String company, final String playerName ) {
		return restTemplate.getForObject(businessServerUrl + "company/?name=" + company + "&playerName=" + playerName, CompanyResponse.class);
	}

	/**
	 * Increment the time for the currently running simulation.
	 * @param company a <code>String</code> with the name of the company.
	 * @return a <code>String</code> with the new time after incrementing it.
	 */
	public String incrementTime ( final String company ) {
		//Get data from API.
		TimeResponse timeResponse = restTemplate.patchForObject(businessServerUrl + "company/time",
				AddTimeRequest.builder()
						.company(company)
						.minutes(15).build(),
				TimeResponse.class);
		return timeResponse != null ? timeResponse.getTime() : null;
	}

	/**
	 * Set a new difficulty level for this company.
	 * @param company a <code>String</code> with the name of the company.
	 * @param difficultyLevel a <code>String</code> containing the difficulty level that should be set for this company.
	 * @return a <code>String</code> with the new difficulty level.
	 */
	public String setDifficultyLevel ( final String company, final String difficultyLevel ) {
		//Get data from API.
		DifficultyLevelResponse difficultyLevelResponse = restTemplate.patchForObject(businessServerUrl + "company/difficultyLevel",
				AdjustDifficultyLevelRequest.builder()
						.company(company)
						.difficultyLevel(difficultyLevel).build(),
				DifficultyLevelResponse.class);
		//Return response from API.
		return difficultyLevelResponse.getDifficultyLevel();
	}

	/**
	 * Compute and return the passenger satisfaction for the current game.
	 * @param company a <code>String</code> with the name of the company.
	 * @param difficultyLevel a <code>String</code> with the difficulty level to use for calculation e.g. EASY, MEDIUM, HARD.
	 * @return a <code>int</code> with the current level of passenger satisfaction between 0 and 100.
	 */
	public double computeAndReturnPassengerSatisfaction ( final String company, final String difficultyLevel ) {
		//Essentially satisfaction is determined by the route schedules that are running on time.
		//Now count number of route schedules into three groups: 1 - 5 minutes late, 6 - 15 minutes late, 16+ minutes late.
		int numSmallLateSchedules = 0; int numMediumLateSchedules = 0; int numLargeLateSchedules = 0;
		//Now go through all vehicles.
		for ( VehicleResponse vehicleModel : vehicleController.getVehicles(company).getVehicleResponses() ) {
			//Running... 1 - 5 minutes late.
			if ( vehicleModel.getDelayInMinutes() > 0 && vehicleModel.getDelayInMinutes() < 6 ) {
				numSmallLateSchedules++;
			}
			//Running... 6 - 15 minutes late.
			else if ( vehicleModel.getDelayInMinutes() > 5 && vehicleModel.getDelayInMinutes() < 16 ) {
				numMediumLateSchedules++;
			}
			//Running... 16+ minutes late.
			else if ( vehicleModel.getDelayInMinutes() > 15 ) {
				numLargeLateSchedules++;
			}
		}
		int totalSubtract = 0;

		//Easy: numSmallLateSchedules / 2 and numMediumLateSchedules and numLargeLateSchedules*2.
		DifficultyLevel myDifficultyLevel = DifficultyLevel.valueOf(difficultyLevel);
		if ( myDifficultyLevel == DifficultyLevel.EASY ) {
			totalSubtract = (numSmallLateSchedules/2) + numMediumLateSchedules + (numLargeLateSchedules*2);
		}
		else if ( myDifficultyLevel == DifficultyLevel.INTERMEDIATE ) {
			totalSubtract = (numSmallLateSchedules) + (numMediumLateSchedules*2) + (numLargeLateSchedules*3);
		}
		else if ( myDifficultyLevel == DifficultyLevel.MEDIUM ) {
			totalSubtract = (numSmallLateSchedules*2) + (numMediumLateSchedules*3) + (numLargeLateSchedules*4);
		}
		else if ( myDifficultyLevel == DifficultyLevel.HARD ) {
			totalSubtract = (numSmallLateSchedules*3) + (numMediumLateSchedules*4) + (numLargeLateSchedules*5);
		}
		//Enable patch method for this rest template.
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(5000);
		requestFactory.setReadTimeout(5000);
		restTemplate.setRequestFactory(requestFactory);

		//Subtract from passenger satisfaction.
		SatisfactionRateResponse satisfactionRateResponse = restTemplate.patchForObject(businessServerUrl + "company/satisfaction",
				AdjustSatisfactionRequest.builder()
						.company(company)
						.satisfactionRate(totalSubtract).build(),
				SatisfactionRateResponse.class);
		return satisfactionRateResponse != null ? satisfactionRateResponse.getSatisfactionRate() : -1.0;
	}

	/**
	 * Export all the company information for the supplied company and player name. The other supplied data
	 * should be integrated into the export.
	 * @param company a <code>String</code> containing the name of the company to export.
	 * @param playerName a <code>String</code> containing the name of the player who's company should be exported.
	 * @param drivers a <code>String</code> containing the driver information that exist for this company.
	 * @param messages a <code>String</code> containing the message information that exist for this company.
	 * @param routes a <code>String</code> containing the route information that exist for this company.
	 * @param vehicles a <code>String</code> containing the vehicle information that exist for this company.
	 * @return a <code>ExportCompanyResponse</code> object containing the exported data.
	 */
	public ExportCompanyResponse exportCompany(final String company, final String playerName, final String drivers,
											   final String messages, final String routes, final String vehicles) {
		return restTemplate.postForObject(businessServerUrl + "company/export",
				ExportCompanyRequest.builder()
						.company(company)
						.playerName(playerName)
						.drivers(drivers)
						.messages(messages)
						.routes(routes)
						.vehicles(vehicles)
						.build(),
				ExportCompanyResponse.class);
	}

	/**
	 * Set the rest template object via Spring.
	 * @param restTemplate a <code>RestTemplate</code> object.
	 */
	@Autowired
	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Set the vehicle controller object via Spring.
	 * @param vehicleController a <code>VehicleController</code> object.
	 */
	@Autowired
	public void setVehicleController(final VehicleController vehicleController) {
		this.vehicleController = vehicleController;
	}
}
