package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the constructor, getter and setter methods of the <code>UserResponse</code> class.
 * Created by davelee on 08.02.17.
 */
public class UserResponseTest {

    /**
     * Test the constructor and ensure variables are set together using the getter methods.
     */
    @Test
    public void testConstructor() {
        UserResponse userResponse = new UserResponse("David", "Lee", "david.lee", "MyCompany", 25, "Monday,Tuesday",
                "Tester", "08-02-2017", null, "Admin", "01-01-1991", 12.0, 40, List.of("Certified Tester"),
                List.of(UserHistoryResponse.builder()
                        .userHistoryReason("Joined")
                        .date("08-02-2017")
                        .comment("Welcome to the company!")
                        .build()));
        assertEquals("david.lee", userResponse.getUsername());
        assertEquals("MyCompany", userResponse.getCompany());
        assertEquals("08-02-2017", userResponse.getStartDate());
        assertNull(userResponse.getEndDate());
        assertEquals("David", userResponse.getFirstName());
        assertEquals("Lee", userResponse.getSurname());
        assertEquals(25, userResponse.getLeaveEntitlementPerYear());
        assertEquals("Tester", userResponse.getPosition());
        assertEquals("Monday,Tuesday", userResponse.getWorkingDays());
        assertEquals("Admin", userResponse.getRole());
        assertEquals("01-01-1991", userResponse.getDateOfBirth());
        assertEquals("Joined", userResponse.getUserHistory().get(0).getUserHistoryReason());
        assertEquals("08-02-2017", userResponse.getUserHistory().get(0).getDate());
        assertEquals("Welcome to the company!", userResponse.getUserHistory().get(0).getComment());
        assertEquals("UserResponse(firstName=David, surname=Lee, username=david.lee, company=MyCompany, leaveEntitlementPerYear=25, workingDays=Monday,Tuesday, position=Tester, startDate=08-02-2017, endDate=null, role=Admin, dateOfBirth=01-01-1991, hourlyWage=12.0, contractedHoursPerWeek=40, trainings=[Certified Tester], userHistory=[UserHistoryResponse(date=08-02-2017, userHistoryReason=Joined, comment=Welcome to the company!)])", userResponse.toString());
    }

    /**
     * Test the builder and ensure variables are set together using the getter methods.
     */
    @Test
    public void testBuilder() {
        UserResponse userResponse = UserResponse.builder()
                .username("david.lee")
                .company("MyCompany")
                .startDate("08-02-2017")
                .firstName("David")
                .surname("Lee")
                .leaveEntitlementPerYear(25)
                .position("Tester")
                .workingDays("Monday,Tuesday")
                .role("Admin")
                .dateOfBirth("01-01-1991")
                .hourlyWage(12.0)
                .contractedHoursPerWeek(40)
                .trainings(List.of("Certified Tester"))
                .userHistory(List.of(UserHistoryResponse.builder()
                        .comment("Welcome to the company!")
                        .date("08-02-2017")
                        .userHistoryReason("Joined")
                        .build()))
                .build();
        assertEquals("david.lee", userResponse.getUsername());
        assertEquals("MyCompany", userResponse.getCompany());
        assertEquals("08-02-2017", userResponse.getStartDate());
        assertEquals("David", userResponse.getFirstName());
        assertEquals("Lee", userResponse.getSurname());
        assertEquals(25, userResponse.getLeaveEntitlementPerYear());
        assertEquals("Tester", userResponse.getPosition());
        assertEquals("Monday,Tuesday", userResponse.getWorkingDays());
        assertEquals("Admin", userResponse.getRole());
        assertEquals("01-01-1991", userResponse.getDateOfBirth());
        assertEquals("UserResponse(firstName=David, surname=Lee, username=david.lee, company=MyCompany, leaveEntitlementPerYear=25, workingDays=Monday,Tuesday, position=Tester, startDate=08-02-2017, endDate=null, role=Admin, dateOfBirth=01-01-1991, hourlyWage=12.0, contractedHoursPerWeek=40, trainings=[Certified Tester], userHistory=[UserHistoryResponse(date=08-02-2017, userHistoryReason=Joined, comment=Welcome to the company!)])", userResponse.toString());
    }

    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    @Test
    public void testGettersAndSetters() {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("david.lee");
        assertEquals("david.lee", userResponse.getUsername());
        userResponse.setCompany("MyCompany");
        assertEquals("MyCompany", userResponse.getCompany());
        userResponse.setStartDate("08-02-2017");
        assertEquals("08-02-2017", userResponse.getStartDate());
        userResponse.setFirstName("David");
        assertEquals("David", userResponse.getFirstName());
        userResponse.setSurname("Lee");
        assertEquals("Lee", userResponse.getSurname());
        userResponse.setLeaveEntitlementPerYear(25);
        assertEquals(25, userResponse.getLeaveEntitlementPerYear());
        userResponse.setPosition("Tester");
        assertEquals("Tester", userResponse.getPosition());
        userResponse.setWorkingDays("Monday,Tuesday");
        assertEquals("Monday,Tuesday", userResponse.getWorkingDays());
        userResponse.setRole("Employee");
        assertEquals("Employee", userResponse.getRole());
        userResponse.setDateOfBirth("31-12-1990");
        assertEquals("31-12-1990", userResponse.getDateOfBirth());
    }

}
