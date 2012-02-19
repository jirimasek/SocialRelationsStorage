package cz.cvut.fit.masekji4.socialrelationsstorage.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Třída <code>ConfigurationFactory</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class ConfigurationFactory
{

    private volatile static Properties configProperties;
    public static final String propertiesFilePath = ConfigurationFactory.class.
            getResource("config.properties").getPath();

    public synchronized static Properties getProperties()
    {
        if (configProperties == null)
        {
            configProperties = new Properties();

            try
            {
                configProperties.load(new FileInputStream(propertiesFilePath));
            }
            catch (IOException ex)
            {
                Logger.getLogger(ConfigurationFactory.class.getName()).log(
                        Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }

        return configProperties;
    }

    public @Produces
    @Config
    String getConfiguration(InjectionPoint p)
    {
        String configKey = p.getMember().getDeclaringClass().getName() + "." + p.
                getMember().getName();

        Properties config = getProperties();

        if (config.getProperty(configKey) == null)
        {
            configKey = p.getMember().getDeclaringClass().getSimpleName() + "." + p.
                    getMember().getName();

            if (config.getProperty(configKey) == null)
            {
                configKey = p.getMember().getName();
            }
        }

        System.err.println("Config key = " + configKey + " value = " + config.
                getProperty(configKey));

        return config.getProperty(configKey);
    }

    public @Produces
    @Config
    Double getConfigurationDouble(InjectionPoint p)
    {
        String value = getConfiguration(p);

        return Double.parseDouble(value);
    }
}
