package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import java.util.List;

/**
 * Rozhraní <code>StorageService</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface StorageService
{
    public Person retrievePerson(Integer id) throws PersonNotFoundException;
    
    public Person retrievePerson(String prefix, String username) throws PersonNotFoundException;
    
    public List<Person> retrievePersons(String source);
    
    public List<Person> retrieveAlterEgos(Integer id) throws PersonNotFoundException;
    
    public List<Person> retrieveAlterEgos(String prefix, String username) throws PersonNotFoundException;
}
