package org.matsim.example;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

public class SimpleAnalysis {

    public static void main(String[] args) {
        // Load the config file
        Config config;

        if ( args==null || args.length==0 || args[0]==null ) {
            // No arguments provided, default to "config.xml"
            System.out.println("No arguments provided. Loading default config: 'config.xml'");
            config = ConfigUtils.loadConfig("config.xml");
        } else {
            // Argument provided, use the path from args[0]
            System.out.println("Loading config from provided argument: ");
            config = ConfigUtils.loadConfig(args);
        }

        // Step 3: Overwrite any existing output directory to prevent conflicts
        config.controller().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);

        // Load the scenario from the validated config
        Scenario scenario = ScenarioUtils.loadScenario(config);

        // Initialize the MATSim controller
        Controler controler = new Controler(scenario);

        // Run the simulation
        System.out.println("Running simulation...");
        controler.run();

        // Optional: Confirmation after simulation ends
        System.out.println("Simulation completed successfully.");
    }
}
