package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Rozhraní <code>StorageService</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface StorageService
{
    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
    
    public Integer createPerson(Person person) throws InvalidPersonException, InvalidProfileException, PersonAlreadyExistsException;
    
    public Person retrievePerson(Integer id) throws PersonNotFoundException;
    
    public Person retrievePerson(String prefix, String username) throws PersonNotFoundException;
    
    public List<Person> retrievePersons();
    
    public List<Person> retrievePersons(String source);
    
    public List<Person> retrieveAlterEgos(Integer id) throws PersonNotFoundException;
    
    public List<Person> retrieveAlterEgos(String prefix, String username) throws PersonNotFoundException;
    
    public Integer updatePerson(Person person) throws InvalidPersonException, InvalidProfileException;
    
    public boolean deletePerson(Integer id);
    
    public boolean deletePerson(String prefix, String username);
    
    /* ********************************************************************** *
     *                                SAMENESS                                *
     * ********************************************************************** */
    
    public boolean declareSameness(Integer person, Integer alterEgo, List<URI> sorces)
            throws PersonNotFoundException;
    
    public boolean refuseSameness(Integer person, Integer alterEgo)
            throws PersonNotFoundException;

    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */

    public Integer createRelation(Relation relation) throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException;

    public Relation retrieveRelation(Integer id) throws RelationNotFoundException, IllegalAccessException;
    
    public List<Relation> retrieveRelations(Integer id) throws PersonNotFoundException;
    
    public List<Relation> retrieveRelations(String prefix, String username) throws PersonNotFoundException;

    public boolean deleteRelation(Integer id) throws IllegalAccessException;
}
