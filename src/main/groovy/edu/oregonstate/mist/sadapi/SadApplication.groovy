package edu.oregonstate.mist.sadapi

import edu.oregonstate.mist.sadapi.db.SadDAO
import edu.oregonstate.mist.sadapi.resources.SadResource
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

/**
 * Main application class.
 */
class SadApplication extends Application<SadApplicationConfiguration> {
    /**
     * Initializes the application bootstrap.
     *
     * @param bootstrap
     */
    @Override
    public void initialize(Bootstrap<SadApplicationConfiguration> bootstrap) {}

    /**
     * Parses command-line arguments and runs the application.
     *
     * @param configuration
     * @param environment
     */
    @Override
    public void run(SadApplicationConfiguration configuration, Environment environment) {
        SadDAO sadDAO = new SadDAO(configuration, environment)
        environment.lifecycle().manage(sadDAO)
        environment.jersey().register((Object)new SadResource(sadDAO))
    }

    /**
     * Instantiates the application class with command-line arguments.
     *
     * @param arguments
     * @throws Exception
     */
    public static void main(String[] arguments) throws Exception {
        new SadApplication().run(arguments)
    }
}
