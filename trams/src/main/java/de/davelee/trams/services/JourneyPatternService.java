package de.davelee.trams.services;

import java.util.List;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.repository.JourneyPatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyPatternService {

    @Autowired
    private JourneyPatternRepository journeyPatternRepository;
	
	public JourneyPatternService() {
		
	}

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
        JourneyPatternModel journeyPatternModel = new JourneyPatternModel();
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

}
