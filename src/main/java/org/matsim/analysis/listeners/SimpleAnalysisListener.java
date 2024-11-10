package org.matsim.analysis.listeners;

import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.analysis.handlers.DepartureEventHandler;
import org.matsim.analysis.handlers.LinkVolumeHandler;
import org.matsim.analysis.handlers.TripAnalysisHandler;
import org.matsim.analysis.handlers.ModeChoiceHandler;
import org.matsim.analysis.utils.AnalysisUtils;
import java.util.HashMap;
import java.util.Map;

public class SimpleAnalysisListener implements IterationEndsListener {
    private final Map<Integer, Double> avgScores = new HashMap<>();
    private final Map<Integer, Double> avgTripDurations = new HashMap<>();
    private final DepartureEventHandler departureHandler;
    private final LinkVolumeHandler volumeHandler;
    private final TripAnalysisHandler tripHandler;
    private final ModeChoiceHandler modeHandler;

    public SimpleAnalysisListener(
            DepartureEventHandler departureHandler,
            LinkVolumeHandler volumeHandler,
            TripAnalysisHandler tripHandler,
            ModeChoiceHandler modeHandler) {
        this.departureHandler = departureHandler;
        this.volumeHandler = volumeHandler;
        this.tripHandler = tripHandler;
        this.modeHandler = modeHandler;
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        int iteration = event.getIteration();

        // Calculate average score using AnalysisUtils
        double avgScore = AnalysisUtils.calculateAverage(
                collectPersonScores(event)
        );
        avgScores.put(iteration, avgScore);

        // Calculate average trip duration using AnalysisUtils
        double avgDuration = AnalysisUtils.calculateAverage(
                tripHandler.getTripDurations()
        );
        avgTripDurations.put(iteration, avgDuration);

        // Print all analyses
        printIterationAnalysis(iteration, avgScore, avgDuration);

        // Create charts at the last iteration
        if (iteration == event.getServices().getConfig().controller().getLastIteration()) {
            createCharts(event);
        }
    }

    private void printIterationAnalysis(int iteration, double avgScore, double avgDuration) {
        // Basic statistics
        System.out.println("Iteration " + iteration + ":");
        System.out.println("  Average Score: " + avgScore);
        System.out.println("  Average Trip Duration: " + avgDuration);

        // Departure analysis
        System.out.println("\nDeparture Analysis:");
        System.out.println("  Total Departures: " + departureHandler.getTotalDepartures());
        System.out.println("  Early Departures: " + departureHandler.getEarlyDepartures());
        System.out.println("  Late Departures: " + departureHandler.getLateDepartures());

        // Calculate earliest and latest departures
        double earliestDeparture = departureHandler.getDepartureTimes().values().stream()
                .min(Double::compareTo)
                .orElse(0.0);
        double latestDeparture = departureHandler.getDepartureTimes().values().stream()
                .max(Double::compareTo)
                .orElse(0.0);

        System.out.println("  Average Departure Time: " +
                String.format("%.2f", departureHandler.getAverageDepartureTime() / 3600) + " hours");
        System.out.println("  Earliest Departure: " +
                String.format("%.2f", earliestDeparture / 3600) + " hours");
        System.out.println("  Latest Departure: " +
                String.format("%.2f", latestDeparture / 3600) + " hours");

        // Volume analysis
        Map<String, Integer> volumes = volumeHandler.getLinkVolumes();
        String mostUsedLink = volumes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("none");
        int maxVolume = volumes.getOrDefault(mostUsedLink, 0);

        System.out.println("\nLink Volume Analysis:");
        System.out.println("  Total links used: " + volumes.size());
        System.out.println("  Most used link: " + mostUsedLink + " (volume: " + maxVolume + ")");

        // Mode choice analysis
        System.out.println("\nMode Choice Analysis:");
        modeHandler.getModeFrequency().forEach((mode, modeCount) ->
                System.out.println("  " + mode + ": " + modeCount + " trips"));

        System.out.println("\nHourly Mode Distribution:");
        modeHandler.getModeByHour().forEach((mode, hourData) -> {
            System.out.println("  " + mode + ":");
            hourData.forEach((hour, hourCount) ->
                    System.out.println("    Hour " + hour + ": " + hourCount + " trips"));
        });
    }

    private Map<String, Double> collectPersonScores(IterationEndsEvent event) {
        Map<String, Double> scores = new HashMap<>();
        event.getServices().getScenario().getPopulation().getPersons().forEach((id, person) -> {
            if (person.getSelectedPlan() != null && person.getSelectedPlan().getScore() != null) {
                scores.put(id.toString(), person.getSelectedPlan().getScore());
            }
        });
        return scores;
    }

    private void createCharts(IterationEndsEvent event) {
        String outputDir = event.getServices().getControlerIO().getOutputPath();

        AnalysisUtils.createTimeSeriesChart(
                outputDir + "/scores.png",
                "Average Scores",
                "Iteration",
                "Score",
                "Average Score",
                avgScores
        );

        AnalysisUtils.createTimeSeriesChart(
                outputDir + "/tripDurations.png",
                "Average Trip Durations",
                "Iteration",
                "Duration (s)",
                "Average Duration",
                avgTripDurations
        );
    }
}