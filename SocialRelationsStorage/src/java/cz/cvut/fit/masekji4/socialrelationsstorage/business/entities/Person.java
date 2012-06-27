package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Key;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>Person</code>
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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Key getKey()
    {
        return key;
    }

    public void setKey(Key key)
    {
        this.key = key;
    }

    public URI getProfile()
    {
        return profile;
    }

    public void setProfile(URI profile)
    {
        this.profile = profile;
    }

    public List<URI> getSources()
    {
        return sources;
    }

    public void setSources(List<URI> sources)
    {
        this.sources = sources;
    }

    public void addSource(URI uri)
    {
        if (sources == null)
        {
            sources = new ArrayList<URI>();
        }

        sources.add(uri);
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    public void addProperty(String property, String value)
    {
        if (properties == null)
        {
            properties = new HashMap<String, String>();
        }

        properties.put(property, value);
    }

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

    @Override
    public int hashCode()
    {
        return id;
    }
}
