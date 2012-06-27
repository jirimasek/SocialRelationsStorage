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
 * <code>GraphDAO</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface GraphDAO
{
    // <editor-fold defaultstate="collapsed" desc="Persons">
    public Integer createPerson(Person person) throws InvalidPersonException, InvalidProfileException, PersonAlreadyExistsException;

    public Person retrievePerson(Integer id) throws PersonNotFoundException;

    public Person retrievePerson(Key key) throws PersonNotFoundException;

    public List<Person> retrievePersons(URI source);

    public List<Person> retrievePersons();

    public Integer updatePerson(Person person) throws InvalidPersonException, InvalidProfileException;

    public boolean deletePerson(Integer id);

    public boolean deletePerson(Key key);// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Sameness">
    public boolean declareSameness(Integer person, Integer alterEgo,
            List<URI> sources)
            throws PersonNotFoundException;

    public boolean declareSameness(Key person, Key alterEgo, List<URI> sources)
            throws PersonNotFoundException;

    public Path retrieveAlterEgos(Integer id) throws PersonNotFoundException;

    public Path retrieveAlterEgos(Key key) throws PersonNotFoundException;

    public boolean refuseSameness(Integer person, Integer alterEgo) throws PersonNotFoundException;

    public boolean refuseSameness(Key person, Key alterEgo) throws PersonNotFoundException;// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Relations">
    public Integer createRelation(Relation relation)
            throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException;

    public Relation retrieveRelation(Integer id)
            throws RelationNotFoundException, IllegalAccessException;

    public List<Relation> retrieveRelations(Integer person,
            DirectionEnum direction)
            throws PersonNotFoundException;

    public List<Relation> retrieveRelations(Key key, DirectionEnum direction)
            throws PersonNotFoundException;

    public List<Relation> retrieveRelations(Integer person,
            DirectionEnum direction, String type)
            throws PersonNotFoundException;

    public List<Relation> retrieveRelations(Key key, DirectionEnum direction,
            String type)
            throws PersonNotFoundException;

    public Integer updateRelation(Relation relation)
            throws PersonNotFoundException, IllegalAccessException;

    public boolean deleteRelation(Integer id) throws IllegalAccessException;// </editor-fold>
}
