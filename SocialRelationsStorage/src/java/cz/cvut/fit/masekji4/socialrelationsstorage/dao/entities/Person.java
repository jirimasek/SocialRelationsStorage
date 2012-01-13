package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Person
{

    private String about;
    private List<DataProperty> dataProperties;
    private List<ObjectProperty> objectProperties;

    public Person()
    {
        this(null);
    }

    public Person(String about)
    {
        this.about = about;
        this.dataProperties = null;
        this.objectProperties = null;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
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
}
