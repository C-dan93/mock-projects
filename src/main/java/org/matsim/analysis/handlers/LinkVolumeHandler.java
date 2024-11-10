package org.matsim.analysis.handlers;

import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import java.util.HashMap;
import java.util.Map;

public class LinkVolumeHandler implements LinkEnterEventHandler {
    private final Map<String, Integer> linkVolumes = new HashMap<>();

    @Override
    public void handleEvent(LinkEnterEvent event) {
        String linkId = event.getLinkId().toString();
        linkVolumes.merge(linkId, 1, Integer::sum);
    }

    @Override
    public void reset(int iteration) {
        linkVolumes.clear();
    }

    public Map<String, Integer> getLinkVolumes() {
        return linkVolumes;
    }
}