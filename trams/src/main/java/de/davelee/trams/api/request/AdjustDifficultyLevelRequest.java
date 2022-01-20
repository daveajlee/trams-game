package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to adjust the difficulty level
 * for a particular company.
 * @author Dave Lee
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdjustDifficultyLevelRequest {

    /**
     * The name of the company to adjust the difficulty level for.
     */
    private String company;

    /**
     * The new difficulty level which should be used for this company (can be EASY, MEDIUM or HARD).
     */
    private String difficultyLevel;

}
