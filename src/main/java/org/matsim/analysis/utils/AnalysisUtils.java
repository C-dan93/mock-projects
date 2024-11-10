package org.matsim.analysis.utils;

import org.matsim.core.utils.charts.XYLineChart;
import java.util.Map;

public class AnalysisUtils {
    public static void createTimeSeriesChart(
            String filename,
            String title,
            String xLabel,
            String yLabel,
            String seriesName,
            Map<Integer, Double> data) {

        XYLineChart chart = new XYLineChart(title, xLabel, yLabel);

        double[] iterations = data.keySet().stream()
                .mapToDouble(Integer::doubleValue)
                .toArray();
        double[] values = data.values().stream()
                .mapToDouble(Double::doubleValue)
                .toArray();

        chart.addSeries(seriesName, iterations, values);
        chart.saveAsPng(filename, 800, 600);
    }

    public static double calculateAverage(Map<String, Double> values) {
        if (values.isEmpty()) {
            return 0.0;
        }
        return values.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}