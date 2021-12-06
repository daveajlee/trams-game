package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response from the server containing details
 * of all matched user according to specified criteria. As well as containing details about the users in form of
 * an array of <code>UserResponse</code> objects, the object also contains a simple count of the users.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsersResponse {

    //a count of the number of users which were found by the server.
    private Long count;

    //an array of all users found by the server.
    private UserResponse[] userResponses;

}