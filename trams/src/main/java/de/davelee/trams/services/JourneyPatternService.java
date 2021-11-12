package de.davelee.trams.services;

import java.util.List;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.model.JourneyPatternModel;
import org.springframework.stereotype.Service;

@Service
public class JourneyPatternService {

    public void saveJourneyPattern ( final JourneyPatternModel journeyPatternModel ) {
        journeyPatternRepository.saveAndFlush(convertToJourneyPattern(journeyPatternModel));
    }

    private JourneyPattern convertToJourneyPattern ( final JourneyPatternModel journeyPatternModel ) {
    	JourneyPattern journeyPattern = new JourneyPattern();
        journeyPattern.setDaysOfOperation(journeyPatternModel.getDaysOfOperation());
        journeyPattern.setEndTime(journeyPatternModel.getEndTime());
        journeyPattern.setFrequency(journeyPatternModel.getFrequency());
        journeyPattern.setName(journeyPatternModel.getName());
        journeyPattern.setOutgoingTerminus(journeyPatternModel.getOutgoingTerminus());
        journeyPattern.setReturnTerminus(journeyPatternModel.getReturnTerminus());
        journeyPattern.setRouteDuration(journeyPatternModel.getDuration());
        journeyPattern.setRouteNumber(journeyPatternModel.getRouteNumber());
        journeyPattern.setStartTime(journeyPatternModel.getStartTime());
        journeyPattern.setTimetableName(journeyPatternModel.getTimetableName());
        return journeyPattern;
    }

    private JourneyPatternModel convertToJourneyPatternModel ( final JourneyPattern journeyPattern ) {
        JourneyPatternModel journeyPatternModel = JourneyPatternModel.builder().build();
        journeyPatternModel.setDaysOfOperation(journeyPattern.getDaysOfOperation());
        journeyPatternModel.setEndTime(journeyPattern.getEndTime());
        journeyPatternModel.setFrequency(journeyPattern.getFrequency());
        journeyPatternModel.setName(journeyPattern.getName());
        journeyPatternModel.setOutgoingTerminus(journeyPattern.getOutgoingTerminus());
        journeyPatternModel.setReturnTerminus(journeyPattern.getReturnTerminus());
        journeyPatternModel.setDuration(journeyPattern.getRouteDuration());
        journeyPatternModel.setRouteNumber(journeyPattern.getRouteNumber());
        journeyPatternModel.setStartTime(journeyPattern.getStartTime());
        journeyPatternModel.setTimetableName(journeyPattern.getTimetableName());
        return journeyPatternModel;
    }

    public JourneyPatternModel[] getAllJourneyPatterns() {
        List<JourneyPattern> journeyPatterns = journeyPatternRepository.findAll();
        JourneyPatternModel[] journeyPatternModels = new JourneyPatternModel[journeyPatterns.size()];
        for ( int i = 0; i < journeyPatternModels.length; i++ ) {
            journeyPatternModels[i] = convertToJourneyPatternModel(journeyPatterns.get(i));
        }
        return journeyPatternModels;
    }

    public JourneyPatternModel[] getJourneyPatternsByTimetableNameAndRouteNumber ( final String timetableName, final String routeNumber ) {
        List<JourneyPattern> journeyPatterns = journeyPatternRepository.findByTimetableNameAndRouteNumber(timetableName, routeNumber);
        JourneyPatternModel[] journeyPatternModels = new JourneyPatternModel[journeyPatterns.size()];
        for ( int i = 0; i < journeyPatternModels.length; i++ ) {
            journeyPatternModels[i] = convertToJourneyPatternModel(journeyPatterns.get(i));
        }
        return journeyPatternModels;
    }

    /**
     * Delete a single journey pattern based on its name, timetable name and route number.
     * @param name a <code>String</code> containing the name of the journey pattern.
     * @param timetableName a <code>String</code> containing the timetable name that this journey pattern has.
     * @param routeNumber a <code>String</code> containing the route number that this journey pattern has.
     */
    public void deleteJourneyPattern ( final String name, final String timetableName, final String routeNumber ) {
	    JourneyPattern journeyPatternToDelete = journeyPatternRepository.findByNameAndTimetableNameAndRouteNumber(name, timetableName, routeNumber);
	    journeyPatternRepository.delete(journeyPatternToDelete);
    }

    /**
     * Retrieve a single journey pattern based on its name, timetable name and route number.
     * @param name a <code>String</code> containing the name of the journey pattern.
     * @param timetableName a <code>String</code> containing the timetable name that this journey pattern has.
     * @param routeNumber a <code>String</code> containing the route number that this journey pattern has.
     * @return a <code>JourneyPatternModel</code> containing the matching journey pattern.
     */
    public JourneyPatternModel getJourneyPattern ( final String name, final String timetableName, final String routeNumber ) {
        return convertToJourneyPatternModel(journeyPatternRepository.findByNameAndTimetableNameAndRouteNumber(name, timetableName, routeNumber));
    }

    /**
     * Delete all journey patterns (only used for load function)
     */
    public void deleteAllJourneyPatterns() {
        journeyPatternRepository.deleteAll();
    }

}
