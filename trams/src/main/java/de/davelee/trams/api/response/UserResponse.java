package de.davelee.trams.api.response;

import lombok.*;

import java.util.List;

/**
 * This class represents a response from the server for a particular user
 * containing first name, surname, username, company they work for, how much leave they are entitled to per year,
 * which days they work, their position and their start date plus other important user information.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserResponse {

    // first name of the user
    private String firstName;

    // surname of the user
    private String surname;

    // username
    private String username;

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

    // end date for the user in format dd-MM-yyyy
    private String endDate;

    //The role that this user has
    private String role;

    //The date of birth for this user
    private String dateOfBirth;

    //The salary of this user
    private double hourlyWage;

    //The number of hours that this user works
    private int contractedHoursPerWeek;

    //list of trainings and qualifications that user has
    private List<String> trainings;

    //list of entries in the log history of this user
    private List<UserHistoryResponse> userHistory;

}