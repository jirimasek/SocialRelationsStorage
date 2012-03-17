package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Třída <code>Relation</code>
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
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * 
     * @return 
     */
    public Integer getObject()
    {
        return object;
    }

    /**
     * 
     * @param object 
     */
    public void setObject(Integer object)
    {
        this.object = object;
    }

    /**
     * 
     * @return 
     */
    public Integer getSubject()
    {
        return subject;
    }

    /**
     * 
     * @param subject 
     */
    public void setSubject(Integer subject)
    {
        this.subject = subject;
    }

    /**
     * 
     * @return 
     */
    public String getType()
    {
        return type;
    }

    /**
     * 
     * @param type 
     */
    public void setType(String type)
    {
        this.type = type;
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
