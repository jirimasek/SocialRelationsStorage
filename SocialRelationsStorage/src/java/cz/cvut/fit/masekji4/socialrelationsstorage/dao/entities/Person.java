package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Třída <code>Person</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Person
{

    private int id;
    private Key key;
    private URI profile;
    private List<URI> sources;
    private Map<String, String> properties;

    /**
     * 
     * @return 
     */
    public int getId()
    {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * 
     * @return 
     */
    public Key getKey()
    {
        return key;
    }

    /**
     * 
     * @param key 
     */
    public void setKey(Key key)
    {
        this.key = key;
    }

    /**
     * 
     * @return 
     */
    public URI getProfile()
    {
        return profile;
    }

    /**
     * 
     * @param profile 
     */
    public void setProfile(URI profile)
    {
        this.profile = profile;
    }

    /**
     * 
     * @return 
     */
    public List<URI> getSources()
    {
        return sources;
    }

    /**
     * 
     * @param sources 
     */
    public void setSources(List<URI> sources)
    {
        this.sources = sources;
    }

    /**
     * 
     * @param uri 
     */
    public void addSource(URI uri)
    {
        if (sources == null)
        {
            sources = new ArrayList<URI>();
        }

        sources.add(uri);
    }

    /**
     * 
     * @return 
     */
    public Map<String, String> getProperties()
    {
        return properties;
    }

    /**
     * 
     * @param properties 
     */
    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    /**
     * 
     * @param property
     * @param value 
     */
    public void addProperty(String property, String value)
    {
        if (properties == null)
        {
            properties = new HashMap<String, String>();
        }

        properties.put(property, value);
    }

    /**
     * 
     * @return 
     */
    public boolean isValid()
    {
        if (profile == null)
        {
            return false;
        }

        if (sources == null || sources.isEmpty())
        {
            return false;
        }

        return true;
    }

    /**
     * 
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Person)
        {
            Person person = (Person) o;
            
            if (profile == null && person.getProfile() != null)
            {
                return false;
            }
            
            if (profile != null && !profile.equals(person.getProfile()))
            {
                return false;
            }
            
            if (sources == null && person.getSources() != null)
            {
                return false;
            }
            
            if (sources != null && !sources.equals(person.getSources()))
            {
                return false;
            }
            
            if (properties == null && person.getProperties() != null)
            {
                return false;
            }
            
            if (properties != null && !properties.equals(person.getProperties()))
            {
                return false;
            }
            
            return true;
            
        }
        
        return false;
    }
}
