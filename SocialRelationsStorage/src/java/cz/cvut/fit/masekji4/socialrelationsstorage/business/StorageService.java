package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
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
    
    public Person retrievePerson(Integer id) throws PersonNotFoundException;
    
    public Person retrievePerson(String prefix, String username) throws PersonNotFoundException;
    
    public List<Person> retrievePersons(String source);
    
    public List<Person> retrieveAlterEgos(Integer id) throws PersonNotFoundException;
    
    public List<Person> retrieveAlterEgos(String prefix, String username) throws PersonNotFoundException;
    
    public boolean deletePerson(Integer id);
    
    public boolean deletePerson(String prefix, String username);

    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */

    public Relation retrieveRelation(Integer id) throws RelationNotFoundException, IllegalAccessException;
    
    public List<Relation> retrieveRelations(Integer id) throws PersonNotFoundException;
    
    public List<Relation> retrieveRelations(String prefix, String username) throws PersonNotFoundException;

    public boolean deleteRelation(Integer id) throws IllegalAccessException;
}
