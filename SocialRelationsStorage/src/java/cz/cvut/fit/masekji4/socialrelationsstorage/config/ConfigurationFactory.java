package cz.cvut.fit.masekji4.socialrelationsstorage.config;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Namespace;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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

        return config.getProperty(configKey);
    }

    public @Produces
    @Config
    Double getConfigurationDouble(InjectionPoint p)
    {
        String value = getConfiguration(p);

        return Double.parseDouble(value);
    }
    
    public @Produces
    @Config
    int getConfigurationInt(InjectionPoint p)
    {
        String value = getConfiguration(p);

        return Integer.parseInt(value);
    }
    

    public @Produces
    @Config
    Map<String, Namespace> getNamespaces(InjectionPoint p)
    {
        String value = getConfiguration(p);
        
        Map<String, Namespace> namespaces = new HashMap<String, Namespace>();
        
        try
        {
            JSONObject obj = new JSONObject(value);
            
            for (Iterator it = obj.keys() ; it.hasNext() ;)
            {
                String namespace = (String) it.next();
                
                JSONArray arr = obj.getJSONArray(namespace);
                
                namespaces.put(namespace, new Namespace(namespace, arr.getString(0), arr.getString(1), arr.getInt(2)));
            }
        }
        catch (JSONException ex)
        {
        }

        return namespaces;
    }
}
