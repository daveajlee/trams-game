package de.davelee.trams.services;

import java.time.LocalDate;

import de.davelee.trams.api.request.UserRequest;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.api.response.UsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class DriverService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${server.personalman.url}")
	private String personalManServerUrl;

	/**
     * Check if the driver has started work or not.
	 * @param startDate a <code>LocalDate</code> object with the start date.
     * @param currentDate a <code>LocalDate</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver has started work.
     */
    public boolean hasStartedWork (final LocalDate startDate, final LocalDate currentDate ) {
    	return currentDate.isAfter(startDate) || currentDate.isEqual(startDate);
    }

	public UserResponse getDriverByName(final String name, final String company, final String token) {
		return restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&username=" + name + "&token=" + token, UserResponse.class);
	}

	public UserResponse[] getAllDrivers (final String company, final String token) {
		try {
			UsersResponse usersResponse = restTemplate.getForObject(personalManServerUrl + "user/?company=" + company + "&username=mmustermann&token=" + token, UsersResponse.class);
			if (usersResponse != null && usersResponse.getUserResponses() != null) {
				return usersResponse.getUserResponses();
			}
			return null;
		} catch ( HttpClientErrorException exception ) {
			//If forbidden then return null.
			return null;
		}
	}

	public void saveDriver ( final UserRequest userRequest ) {
		restTemplate.postForObject(personalManServerUrl + "user/", userRequest, Void.class);
	}

	public void removeDriver ( final String company, final String username, final String token ) {
		restTemplate.delete(personalManServerUrl + "user/?company=" + company + "&username=" + username + "&token=" + token);
	}

	/**
	 * Delete all stored drivers (primarily used for loading a game)
	 * @param company a <code>String</code> containing the name of the company to delete drivers for.
	 * @param token a <code>String</code> containing the authentification token.
	 */
	public void removeAllDrivers ( final String company, final String token ) {
		restTemplate.delete(personalManServerUrl + "api/company/?name=" + company + "&token=" + token);
	}
    
}
