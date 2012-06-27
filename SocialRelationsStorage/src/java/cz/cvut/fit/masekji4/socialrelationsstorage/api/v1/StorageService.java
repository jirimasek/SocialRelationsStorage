package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.filters.SourceFilter;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
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
 * <code>StorageService</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface StorageService
{
    
    // <editor-fold defaultstate="collapsed" desc="Persons">
    public Integer createPerson(Person person) throws InvalidPersonException, InvalidProfileException, PersonAlreadyExistsException;
    
    public Person retrievePerson(Integer id) throws PersonNotFoundException;
    
    public Person retrievePerson(String prefix, String username) throws PersonNotFoundException;
    
    public List<Person> retrievePersons();
    
    public List<Person> retrievePersons(String source);
    
    public List<Person> retrieveAlterEgos(Integer id, SourceFilter filter) throws PersonNotFoundException;
    
    public Integer updatePerson(Person person) throws InvalidPersonException, InvalidProfileException;
    
    public boolean deletePerson(Integer id);// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Sameness">
    public boolean declareSameness(Integer person, Integer alterEgo, List<URI> sorces)
            throws PersonNotFoundException;
    
    public boolean refuseSameness(Integer person, Integer alterEgo)
            throws PersonNotFoundException;// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Relations">
    public Integer createRelation(Relation relation) throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException;

    public Relation retrieveRelation(Integer id) throws RelationNotFoundException, IllegalAccessException;
    
    public List<Relation> retrieveRelations(Integer id, SourceFilter filter) throws PersonNotFoundException;
    
    public List<Relation> retrieveRelations(Integer id, String type, SourceFilter filter) throws PersonNotFoundException;
    
    public List<Relation> retrieveRelations(Integer object, Integer subject, String type, SourceFilter filter) throws PersonNotFoundException;

    public Integer updateRelation(Relation relation) throws PersonNotFoundException, IllegalAccessException;

    public boolean deleteRelation(Integer id) throws IllegalAccessException;

    public List<URI> retrieveSources();// </editor-fold>
}
