package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * <code>Relationship</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Relationship
{

    private String type;
    private DirectionEnum direction;

    public Relationship()
    {
        this(null, null);
    }
    
    public Relationship(String type, DirectionEnum direction)
    {
        this.type = type;
        this.direction = direction;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public DirectionEnum getDirection()
    {
        return direction;
    }

    public void setDirection(DirectionEnum direction)
    {
        this.direction = direction;
    }

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
