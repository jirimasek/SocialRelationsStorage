package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum;
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
     * @throws PersonAlreadyExistsException 
     */
    public Integer createPerson(Person person) throws PersonAlreadyExistsException;
    
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
    
    public List<Person> retrievePersons();
    
    public void updatePerson(Person person);
    
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
    
    public void declareSameness(Integer person, Integer alterEgo, List<URI> sources)
            throws PersonNotFoundException;
    
    public void declareSameness(Key person, Key alterEgo, List<URI> sources)
            throws PersonNotFoundException;
    
    public Path retrieveAlterEgos(Person person) throws PersonNotFoundException;
    
    public Path retrieveAlterEgos(Key key) throws PersonNotFoundException;
    
    public boolean refuseSameness(Integer person, Integer alterEgo);
    
    public boolean refuseSameness(Key person, Key alterEgo) throws PersonNotFoundException;
    
    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */
    
    public Integer createRelation(Relation relation)
            throws PersonNotFoundException, RelationAlreadyExistsException;
    
    public Relation retrieveRelation(Integer id) throws RelationNotFoundException;
    
    public List<Relation> retrieveRelations(Integer person, DirectionEnum direction)
            throws PersonNotFoundException;
    
    public List<Relation> retrieveRelations(Key key, DirectionEnum direction)
            throws PersonNotFoundException;
    
    public List<Relation> retrieveRelations(Integer person, DirectionEnum direction, String type)
            throws PersonNotFoundException;
    
    public List<Relation> retrieveRelations(Key key, DirectionEnum direction, String type)
            throws PersonNotFoundException;
    
    public void updateRelation(Relation relation);
    
    public boolean deleteRelation(Integer id);
}
