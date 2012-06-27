package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * <code>Path</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Path
{
    private Person person;
    private Set<URI> sources;
    private Set<Path> persons;

    public Person getPerson()
    {
        return person;
    }

    public void setPerson(Person person)
    {
        this.person = person;
    }

    public Set<URI> getSources()
    {
        return sources;
    }

    public void setSources(Set<URI> sources)
    {
        this.sources = sources;
    }
    
    public void addSource(URI source)
    {
        if (sources == null)
        {
            sources = new HashSet<URI>();
        }
        
        sources.add(source);
    }

    public Set<Path> getPersons()
    {
        return persons;
    }

    public void setPersons(Set<Path> persons)
    {
        this.persons = persons;
    }
    
    public void addPerson(Path person)
    {
        if (persons == null)
        {
            persons = new HashSet<Path>();
        }
        
        persons.add(person);
    }
}
