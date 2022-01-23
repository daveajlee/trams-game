package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the constructor, getter and setter methods of the <code>UsersResponse</code> class.
 * Created by davelee on 08.02.17.
 */
public class UsersResponseTest {

    /**
     * Test the builder method and ensure variables are set together using the getter methods.
     */
    @Test
    public void testBuilder() {
        UserResponse[] userResponses = new UserResponse[1];
        userResponses[0] =  new UserResponse("David", "Lee", "david.lee", "MyCompany", 25, "Monday,Tuesday", "Tester",
                "08-02-2017", null, "Admin", "31-12-1990", 12.0, 40, List.of("Certified Tester"),
                List.of(UserHistoryResponse.builder()
                        .userHistoryReason("Joined")
                        .date("08-02-2017")
                        .comment("Welcome to the company!")
                        .build()));
        UsersResponse usersResponse = UsersResponse.builder()
                .count(1L)
                .userResponses(userResponses).build();
        assertEquals(1L,usersResponse.getCount());
        assertEquals(1, usersResponse.getUserResponses().length);
        assertEquals("MyCompany", usersResponse.getUserResponses()[0].getCompany());
    }

    /**
     * Test the setter methods and ensure variables are set together using the getter methods.
     */
    @Test
    public void testGettersAndSetters() {
        UserResponse[] userResponses = new UserResponse[1];
        UserHistoryResponse userHistoryResponse = new UserHistoryResponse();
        userHistoryResponse.setUserHistoryReason("Joined");
        userHistoryResponse.setComment("Welcome to the company!");
        userHistoryResponse.setDate("08-02-2017");
        userResponses[0] =  new UserResponse("David", "Lee", "david.lee", "MyCompany", 25, "Monday,Tuesday", "Tester",
                "08-02-2017", null, "Admin", "31-12-1990", 12.0, 40, List.of("Certified Tester"),
                List.of(userHistoryResponse));
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setCount(1L);
        usersResponse.setUserResponses(userResponses);
        assertEquals(1L,usersResponse.getCount());
        assertEquals(1, usersResponse.getUserResponses().length);
        assertEquals("MyCompany", usersResponse.getUserResponses()[0].getCompany());
    }

}
