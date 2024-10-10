package org.matsim.analysis;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

public class SimpleAnalysis {
    public static void main(String[] args) {
        try {
            // Load the MATSim configuration file
            Config config = ConfigUtils.loadConfig("scenario/config.xml");

            // Overwrite any existing output directory to prevent conflicts
            config.controller().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);

            // Load the scenario
            Scenario scenario = ScenarioUtils.loadScenario(config);

            // Initialize the controller
            Controler controler = new Controler(scenario);

            // Run the simulation
            controler.run();
        } catch (RuntimeException e) {
            System.err.println("Runtime error during simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
