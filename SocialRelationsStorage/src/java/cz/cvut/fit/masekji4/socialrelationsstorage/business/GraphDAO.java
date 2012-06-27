package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import java.net.URI;
import java.util.List;

/**
 * Rozhraní <code>GraphDAO</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface GraphDAO
{
    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */

    /**
     * 
     * @param person
     * @return
     * @throws InvalidPersonException
     * @throws InvalidProfileException
     * @throws PersonAlreadyExistsException 
     */
    public Integer createPerson(Person person) throws InvalidPersonException, InvalidProfileException, PersonAlreadyExistsException;

    /**
     * 
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    public Person retrievePerson(Integer id) throws PersonNotFoundException;

    /**
     * 
     * @param key
     * @return
     * @throws PersonNotFoundException 
     */
    public Person retrievePerson(Key key) throws PersonNotFoundException;

    /**
     * 
     * @param source
     * @return 
     */
    public List<Person> retrievePersons(URI source);

    /**
     * 
     * @return 
     */
    public List<Person> retrievePersons();

    /**
     * 
     * @param person
     * @return
     * @throws InvalidPersonException
     * @throws InvalidProfileException 
     */
    public Integer updatePerson(Person person) throws InvalidPersonException, InvalidProfileException;

    /**
     * 
     * @param id
     * @return 
     */
    public boolean deletePerson(Integer id);

    /**
     * 
     * @param key
     * @return 
     */
    public boolean deletePerson(Key key);

    /* ********************************************************************** *
     *                                SAMENESS                                *
     * ********************************************************************** */
    /**
     * 
     * @param person
     * @param alterEgo
     * @param sources
     * @return
     * @throws PersonNotFoundException 
     */
    public boolean declareSameness(Integer person, Integer alterEgo,
            List<URI> sources)
            throws PersonNotFoundException;

    /**
     * 
     * @param person
     * @param alterEgo
     * @param sources
     * @return
     * @throws PersonNotFoundException 
     */
    public boolean declareSameness(Key person, Key alterEgo, List<URI> sources)
            throws PersonNotFoundException;

    /**
     * 
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    public Path retrieveAlterEgos(Integer id) throws PersonNotFoundException;

    /**
     * 
     * @param key
     * @return
     * @throws PersonNotFoundException 
     */
    public Path retrieveAlterEgos(Key key) throws PersonNotFoundException;

    /**
     * 
     * @param person
     * @param alterEgo
     * @return
     * @throws PersonNotFoundException 
     */
    public boolean refuseSameness(Integer person, Integer alterEgo) throws PersonNotFoundException;

    /**
     * 
     * @param person
     * @param alterEgo
     * @return
     * @throws PersonNotFoundException 
     */
    public boolean refuseSameness(Key person, Key alterEgo) throws PersonNotFoundException;

    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */
    
    /**
     * 
     * @param relation
     * @return
     * @throws PersonNotFoundException
     * @throws RelationAlreadyExistsException
     * @throws IllegalAccessException
     * @throws InvalidRelationshipException 
     */
    public Integer createRelation(Relation relation)
            throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException;

    /**
     * 
     * @param id
     * @return
     * @throws RelationNotFoundException
     * @throws IllegalAccessException 
     */
    public Relation retrieveRelation(Integer id)
            throws RelationNotFoundException, IllegalAccessException;

    /**
     * 
     * @param person
     * @param direction
     * @return
     * @throws PersonNotFoundException 
     */
    public List<Relation> retrieveRelations(Integer person,
            DirectionEnum direction)
            throws PersonNotFoundException;

    /**
     * 
     * @param key
     * @param direction
     * @return
     * @throws PersonNotFoundException 
     */
    public List<Relation> retrieveRelations(Key key, DirectionEnum direction)
            throws PersonNotFoundException;

    /**
     * 
     * @param person
     * @param direction
     * @param type
     * @return
     * @throws PersonNotFoundException 
     */
    public List<Relation> retrieveRelations(Integer person,
            DirectionEnum direction, String type)
            throws PersonNotFoundException;

    /**
     * 
     * @param key
     * @param direction
     * @param type
     * @return
     * @throws PersonNotFoundException 
     */
    public List<Relation> retrieveRelations(Key key, DirectionEnum direction,
            String type)
            throws PersonNotFoundException;

    /**
     * 
     * @param relation
     * @return
     * @throws PersonNotFoundException
     * @throws IllegalAccessException 
     */
    public Integer updateRelation(Relation relation)
            throws PersonNotFoundException, IllegalAccessException;

    /**
     * 
     * @param id
     * @return
     * @throws IllegalAccessException 
     */
    public boolean deleteRelation(Integer id) throws IllegalAccessException;
}