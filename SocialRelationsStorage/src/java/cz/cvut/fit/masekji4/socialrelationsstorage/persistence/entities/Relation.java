package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum;
import java.net.URI;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Třída <code>Relation</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Relation implements GraphElement
{

    private URI self;
    private String type;
    private URI start;
    private URI end;
    private DirectionEnum direction;
    private Map<String, String> properties;

    public Relation()
    {
        this(null, null);
    }
    
    public Relation(String type, DirectionEnum direction)
    {
        this.type = type;
        this.direction = direction;
    }

    public URI getSelf()
    {
        return self;
    }

    public void setSelf(URI self)
    {
        this.self = self;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public URI getStart()
    {
        return start;
    }

    public void setStart(URI start)
    {
        this.start = start;
    }

    public URI getEnd()
    {
        return end;
    }

    public void setEnd(URI end)
    {
        this.end = end;
    }

    public DirectionEnum getDirection()
    {
        return direction;
    }

    public void setDirection(DirectionEnum direction)
    {
        this.direction = direction;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }

    /**
     * Serializuje entitu do formátu JSON.
     * 
     * @return      JSON
     */
    public JSONObject toJson() throws JSONException
    {
        JSONObject json = new JSONObject();
        
        json.put("type", type);

        if (direction != null)
        {
            json.put("direction", direction.name().toLowerCase());
        }

        return json;
    }
}
