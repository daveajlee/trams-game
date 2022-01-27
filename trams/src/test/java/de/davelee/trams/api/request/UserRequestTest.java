package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the UserRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class UserRequestTest {

    /**
     * Ensure that a UserRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setCompany("Lee Buses");
        userRequest.setDateOfBirth("01-01-1950");
        userRequest.setFirstName("Dave");
        userRequest.setSurname("Lee");
        userRequest.setPassword("test");
        userRequest.setUsername("dave.lee");
        userRequest.setPosition("Tester");
        userRequest.setLeaveEntitlementPerYear(20);
        userRequest.setStartDate("01-01-2020");
        userRequest.setWorkingDays("Saturday,Sunday");
        assertEquals("Lee Buses", userRequest.getCompany());
        assertEquals("01-01-1950", userRequest.getDateOfBirth());
        assertEquals("Dave", userRequest.getFirstName());
        assertEquals("Lee", userRequest.getSurname());
        assertEquals("test", userRequest.getPassword());
        assertEquals("dave.lee", userRequest.getUsername());
        assertEquals("Tester", userRequest.getPosition());
        assertEquals(20, userRequest.getLeaveEntitlementPerYear());
        assertEquals("01-01-2020", userRequest.getStartDate());
        assertEquals("Saturday,Sunday", userRequest.getWorkingDays());
        assertEquals("UserRequest(firstName=Dave, surname=Lee, username=dave.lee, password=test, company=Lee Buses, leaveEntitlementPerYear=20, workingDays=Saturday,Sunday, position=Tester, startDate=01-01-2020, role=null, dateOfBirth=01-01-1950)", userRequest.toString());
    }

}
