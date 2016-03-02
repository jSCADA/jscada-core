package ch.hevs.jscada.factory;

import ch.hevs.jscada.config.ConfigurationDictionary;

import java.io.IOException;

public class ScadaSystemFactoryWithoutUsageHelper extends ScadaSystemFactory {
    @Override
    protected void loadImplementation(ConfigurationDictionary configuration) throws Exception {
        throw new IOException("Not implemented");
    }
}
