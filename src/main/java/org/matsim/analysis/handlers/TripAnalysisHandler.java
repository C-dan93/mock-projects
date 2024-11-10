package org.matsim.analysis.handlers;

import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import java.util.HashMap;
import java.util.Map;

public class TripAnalysisHandler implements PersonDepartureEventHandler, PersonArrivalEventHandler {
    private final Map<String, Double> tripStartTimes = new HashMap<>();
    private final Map<String, Double> tripDurations = new HashMap<>();

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        String personId = event.getPersonId().toString();
        tripStartTimes.put(personId, event.getTime());
    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {
        String personId = event.getPersonId().toString();
        Double startTime = tripStartTimes.get(personId);

        if (startTime != null) {
            double duration = event.getTime() - startTime;
            tripDurations.put(personId, duration);
        }
    }

    @Override
    public void reset(int iteration) {
        tripStartTimes.clear();
        tripDurations.clear();
    }

    public Map<String, Double> getTripDurations() {
        return tripDurations;
    }
}