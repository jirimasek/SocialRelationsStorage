package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class DataProperty
{
    private String name;
    private String datatype;
    private String value;

    public DataProperty()
    {
        this.name = null;
        this.datatype = null;
        this.value = null;
    }

    public DataProperty(String name, String datatype, String value)
    {
        this.name = name;
        this.datatype = datatype;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDatatype()
    {
        return datatype;
    }

    public void setDatatype(String datatype)
    {
        this.datatype = datatype;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
