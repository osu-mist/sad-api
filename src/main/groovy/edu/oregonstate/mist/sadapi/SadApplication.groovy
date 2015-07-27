package edu.oregonstate.mist.sadapi

import edu.oregonstate.mist.sadapi.db.SadDAO
import edu.oregonstate.mist.sadapi.resources.SadResource
import io.dropwizard.Application
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI

class SadApplication extends Application<SadApplicationConfiguration>{
    @Override
    public void initialize(Bootstrap<SadApplicationConfiguration> bootstrap) {}

    @Override
    public void run(SadApplicationConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory()
        final DBI jdbi = factory.build(environment, configuration.getDatabase(), 'oracle')
        final SadDAO sadDAO = jdbi.onDemand(SadDAO.class)
        environment.jersey().register(new SadResource(sadDAO))
    }

    public static void main(String[] arguments) throws Exception {
        new SadApplication().run(arguments)
    }
}
