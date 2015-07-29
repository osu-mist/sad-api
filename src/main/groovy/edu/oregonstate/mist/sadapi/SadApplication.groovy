package edu.oregonstate.mist.sadapi

import edu.oregonstate.mist.sadapi.db.SadDAO
import edu.oregonstate.mist.sadapi.resources.SadResource
import io.dropwizard.Application
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class SadApplication extends Application<SadApplicationConfiguration>{
    @Override
    public void initialize(Bootstrap<SadApplicationConfiguration> bootstrap) {}

    @Override
    public void run(SadApplicationConfiguration configuration, Environment environment) {
        SadDAO sadDAO = new SadDAO(configuration, environment)
        environment.lifecycle().manage(sadDAO)
        environment.jersey().register((Object)new SadResource(sadDAO))
    }

    public static void main(String[] arguments) throws Exception {
        new SadApplication().run(arguments)
    }
}
