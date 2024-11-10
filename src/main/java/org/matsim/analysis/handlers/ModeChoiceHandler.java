package org.matsim.analysis.handlers;

import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import java.util.HashMap;
import java.util.Map;

public class ModeChoiceHandler implements PersonDepartureEventHandler {
    private final Map<String, Integer> modeFrequency = new HashMap<>();
    private final Map<String, Map<Integer, Integer>> modeByHour = new HashMap<>(); // hour -> count

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        // Count overall mode frequency
        String mode = event.getLegMode();
        modeFrequency.merge(mode, 1, Integer::sum);

        // Count modes by hour
        int hour = (int) (event.getTime() / 3600);
        modeByHour.computeIfAbsent(mode, k -> new HashMap<>())
                .merge(hour, 1, Integer::sum);
    }

    @Override
    public void reset(int iteration) {
        modeFrequency.clear();
        modeByHour.clear();
    }

    public Map<String, Integer> getModeFrequency() {
        return modeFrequency;
    }

    public Map<String, Map<Integer, Integer>> getModeByHour() {
        return modeByHour;
    }
}