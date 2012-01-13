package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class ObjectProperty
{
    private String name;
    private String resource;
    private Map<String, String> properties;

    public ObjectProperty(String name, String resource)
    {
        this.name = name;
        this.resource = resource;
        this.properties = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource)
    {
        this.resource = resource;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }
    
    public void addProperty(String name, String value)
    {
        if (this.properties == null)
        {
            this.properties = new HashMap<String, String>();
        }
        
        this.properties.put(name, value);
    }
}
