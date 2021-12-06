package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to add the following user to the server
 * containing first name, surname, username, company they work for, how much leave they are entitled to per year,
 * which days they work, their position and their start date.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRequest {

    // first name of the user
    private String firstName;

    // surname of the user
    private String surname;

    // username
    private String username;

    //password
    private String password;

    // company associated with
    private String company;

    // leave entitlement for this user (in days per year)
    private int leaveEntitlementPerYear;

    // which days of the week that the users works comma-separated (e.g. Monday,Tuesday,Wednesday,Thursday)
    private String workingDays;

    // the position of this user
    private String position;

    // start date for the user in format dd-MM-yyyy
    private String startDate;

    //The role which the user would like
    private String role;

    // date of birth for the user in format dd-MM-yyyy
    private String dateOfBirth;

}