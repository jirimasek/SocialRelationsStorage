package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Key;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Třída <code>Person</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Person
{

    private Integer id;
    private Key key;
    private URI profile;
    private List<URI> sources;
    private Map<String, String> properties;

    /**
     * 
     * @return 
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id)
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
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        
        if (getClass() != obj.getClass())
        {
            return false;
        }
        
        final Person other = (Person) obj;
        
        if (this.profile != other.profile && (this.profile == null || !this.profile.equals(other.profile)))
        {
            return false;
        }
        
        if (this.sources != other.sources && (this.sources == null || !this.sources.equals(other.sources)))
        {
            return false;
        }
        
        if (this.properties != other.properties && (this.properties == null || !this.properties.equals(other.properties)))
        {
            return false;
        }
        
        return true;
    }

    /**
     * 
     * @return 
     */
    @Override
    public int hashCode()
    {
        return id;
    }
}
