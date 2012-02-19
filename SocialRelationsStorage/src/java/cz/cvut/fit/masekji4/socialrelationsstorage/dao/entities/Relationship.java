package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Relationship
{
    private Person object;
    private Person subject;
    private String relationship;
    private String source;

    public Relationship()
    {
        this.object = null;
        this.subject = null;
        this.relationship = null;
        this.source = null;
    }

    public Person getObject()
    {
        return object;
    }

    public void setObject(Person object)
    {
        this.object = object;
    }
    

    public String getRelationship()
    {
        return relationship;
    }

    public void setRelationship(String relationship)
    {
        this.relationship = relationship;
    }

    public Person getSubject()
    {
        return subject;
    }

    public void setSubject(Person subject)
    {
        this.subject = subject;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }
    
    public boolean isComplete()
    {
        if (object == null)
        {
            return false;
        }
        
        if (relationship == null || relationship.isEmpty())
        {
            return false;
        }
        
        if (subject == null)
        {
            return false;
        }
        
        if (source == null || source.isEmpty())
        {
            return false;
        }
        
        return true;
    }
    
    public void build(JSONObject data) throws JSONException
    {
        if (data.has("object") && !data.getString("object").isEmpty())
        {
            object = new Person(data.getString("object"));
        }
        
        if (data.has("relationship") && !data.getString("relationship").isEmpty())
        {
            relationship = data.getString("relationship");
        }
        
        if (data.has("subject") && !data.getString("subject").isEmpty())
        {
            subject = new Person(data.getString("subject"));
        }
        
        if (data.has("source") && !data.getString("source").isEmpty())
        {
            source = data.getString("source");
        }
    }
}
