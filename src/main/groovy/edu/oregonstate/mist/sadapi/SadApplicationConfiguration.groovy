package edu.oregonstate.mist.sadapi

import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

/**
 * Application configuration class.
 */
class SadApplicationConfiguration extends Configuration {
    DataSourceFactory database = new DataSourceFactory()
}
