package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.DirectionEnum;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Třída <code>Relationship</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Relationship
{

    private String type;
    private DirectionEnum direction;

    /**
     * 
     */
    public Relationship()
    {
        this(null, null);
    }
    
    /**
     * 
     * @param type
     * @param direction 
     */
    public Relationship(String type, DirectionEnum direction)
    {
        this.type = type;
        this.direction = direction;
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
    public DirectionEnum getDirection()
    {
        return direction;
    }

    /**
     * 
     * @param direction 
     */
    public void setDirection(DirectionEnum direction)
    {
        this.direction = direction;
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
