package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Třída <code>SameAs</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@XmlRootElement
public class SameAs
{
    private List<String> sources;
    private List<Person> persons;

    public List<String> getSources()
    {
        return sources;
    }

    public void setSources(List<String> sources)
    {
        this.sources = sources;
    }

    public List<Person> getPersons()
    {
        return persons;
    }

    public void setPersons(List<Person> persons)
    {
        this.persons = persons;
    }
}
