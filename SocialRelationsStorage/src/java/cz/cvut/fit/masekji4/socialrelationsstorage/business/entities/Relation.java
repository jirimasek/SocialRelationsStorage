package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>Relation</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Relation
{
    private Integer id;
    private Integer object;
    private Integer subject;
    private String type;
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

    public Integer getObject()
    {
        return object;
    }

    public void setObject(Integer object)
    {
        this.object = object;
    }

    public Integer getSubject()
    {
        return subject;
    }

    public void setSubject(Integer subject)
    {
        this.subject = subject;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
        if (object == null)
        {
            return false;
        }
        
        if (subject == null)
        {
            return false;
        }
        
        if (type == null)
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
        
        final Relation other = (Relation) obj;
        
        if (this.object != other.object)
        {
            return false;
        }
        
        if (this.subject != other.subject)
        {
            return false;
        }
        
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type))
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
