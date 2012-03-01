package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Třída <code>Person</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@XmlRootElement
public class Person
{
    
    private String prefix;
    private String username;
    private Integer uin;
    private List<DataProperty> dataProperties;
    private List<ObjectProperty> objectProperties;
    private List<SameAs> sameAs;

    public Person()
    {   
        this.prefix = null;
        this.username = null;
        this.uin = null;
        this.dataProperties = null;
        this.objectProperties = null;
    }

    public Person(String uid)
    {
        String[] s = uid.split(":");
        
        this.prefix = s[0];
        this.username = s[1];
        
        this.uin = null;
        this.dataProperties = null;
        this.objectProperties = null;
    }
    
    public String getUid()
    {
        return prefix + ":" + username;
    }
    
    public void setUid(String uid)
    {
        String[] s = uid.split(":");
        
        this.prefix = s[0];
        this.username = s[1];
    }

    public Integer getUin()
    {
        return uin;
    }

    public void setUin(Integer uin)
    {
        this.uin = uin;
    }

    public List<DataProperty> getDataProperties()
    {
        return dataProperties;
    }

    public void setDataProperties(List<DataProperty> dataProperties)
    {
        this.dataProperties = dataProperties;
    }

    public void addDataProperty(DataProperty dataProperty)
    {

        if (this.dataProperties == null)
        {
            this.dataProperties = new ArrayList<DataProperty>();
        }

        this.dataProperties.add(dataProperty);
    }

    public List<ObjectProperty> getObjectProperties()
    {
        return objectProperties;
    }

    public void setObjectProperties(List<ObjectProperty> objectProperties)
    {
        this.objectProperties = objectProperties;
    }

    public void addObjectProperty(ObjectProperty objectProperty)
    {

        if (this.objectProperties == null)
        {
            this.objectProperties = new ArrayList<ObjectProperty>();
        }

        this.objectProperties.add(objectProperty);
    }

    public List<SameAs> getSameAs()
    {
        return sameAs;
    }

    public void setSameAs(List<SameAs> sameAs)
    {
        this.sameAs = sameAs;
    }
}
