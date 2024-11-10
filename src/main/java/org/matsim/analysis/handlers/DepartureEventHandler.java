package org.matsim.analysis.handlers;

import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import java.util.HashMap;
import java.util.Map;

public class DepartureEventHandler implements PersonDepartureEventHandler {
    private static final double EARLY_DEPARTURE_THRESHOLD = 6 * 3600;  // 6:00 AM in seconds
    private static final double LATE_DEPARTURE_THRESHOLD = 9 * 3600;   // 9:00 AM in seconds

    private final Map<String, Double> departureTimes = new HashMap<>();
    private int earlyDepartures = 0;
    private int lateDepartures = 0;

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        String personId = event.getPersonId().toString();
        double time = event.getTime();

        // Store departure time
        departureTimes.put(personId, time);

        // Categorize departure
        if (time < EARLY_DEPARTURE_THRESHOLD) {
            earlyDepartures++;
        } else if (time > LATE_DEPARTURE_THRESHOLD) {
            lateDepartures++;
        }
    }

    @Override
    public void reset(int iteration) {
        departureTimes.clear();
        earlyDepartures = 0;
        lateDepartures = 0;
    }

    public int getEarlyDepartures() {
        return earlyDepartures;
    }

    public int getLateDepartures() {
        return lateDepartures;
    }

    public Map<String, Double> getDepartureTimes() {
        return departureTimes;
    }

    public int getTotalDepartures() {
        return departureTimes.size();
    }

    public double getAverageDepartureTime() {
        if (departureTimes.isEmpty()) {
            return 0.0;
        }
        return departureTimes.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}