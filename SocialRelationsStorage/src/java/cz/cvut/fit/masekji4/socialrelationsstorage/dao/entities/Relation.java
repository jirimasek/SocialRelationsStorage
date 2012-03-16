package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities;

import java.util.List;

/**
 * Třída <code>Relation</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Relation
{
    private int id;
    private Person start;
    private Person end;
    private List<String> source;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Person getStart()
    {
        return start;
    }

    public void setStart(Person start)
    {
        this.start = start;
    }

    public Person getEnd()
    {
        return end;
    }

    public void setEnd(Person end)
    {
        this.end = end;
    }

    public List<String> getSource()
    {
        return source;
    }

    public void setSource(List<String> source)
    {
        this.source = source;
    }
}
