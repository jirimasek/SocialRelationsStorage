package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Třída <code>Path</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Path
{
    private Person person;
    private Set<URI> sources;
    private Set<Path> persons;

    /**
     * 
     * @return 
     */
    public Person getPerson()
    {
        return person;
    }

    /**
     * 
     * @param person 
     */
    public void setPerson(Person person)
    {
        this.person = person;
    }

    /**
     * 
     * @return 
     */
    public Set<URI> getSources()
    {
        return sources;
    }

    /**
     * 
     * @param sources 
     */
    public void setSources(Set<URI> sources)
    {
        this.sources = sources;
    }
    
    /**
     * 
     * @param source 
     */
    public void addSource(URI source)
    {
        if (sources == null)
        {
            sources = new HashSet<URI>();
        }
        
        sources.add(source);
    }

    /**
     * 
     * @return 
     */
    public Set<Path> getPersons()
    {
        return persons;
    }

    /**
     * 
     * @param persons 
     */
    public void setPersons(Set<Path> persons)
    {
        this.persons = persons;
    }
    
    /**
     * 
     * @param person 
     */
    public void addPerson(Path person)
    {
        if (persons == null)
        {
            persons = new HashSet<Path>();
        }
        
        persons.add(person);
    }
}
