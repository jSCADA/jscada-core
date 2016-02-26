package ch.hevs.jscada.config;

import java.io.IOException;

public class ScadaSystemFactoryWithoutUsageHelper extends ScadaSystemFactory {
    @Override
    protected void loadImplementation(ConfigurationDictionary configuration) throws Exception {
        throw new IOException("Not implemented");
    }
}
