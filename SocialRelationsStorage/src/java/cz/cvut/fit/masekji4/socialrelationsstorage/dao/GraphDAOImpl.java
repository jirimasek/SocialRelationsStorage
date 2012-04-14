package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.DirectionEnum.ALL;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.DirectionEnum.OUT;

import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.TypeEnum.FULLPATH;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.KeyFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.Neo4j;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidMetadataException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeIndexNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Třída <code>GraphDAOImpl</code> implementuje ... a zajišťuje ...
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class GraphDAOImpl implements GraphDAO
{
    
    
    private String usrIndexKey = "username";
    private String srcIndexName = "sources";
    private String srcIndexKey = "source";
    private String owlSameAs = "owl:sameAs";
    private String foafHomepage = "profile";
    private String siocNote = "sources";
    private String globalIndexName = "global";
    private String globalIndexKey = "all";
    private String globalIndexValue = "persons";
    
    @Inject
    @Config
    private int ALTEREGO_TRAVERSATION_MAX_DEPTH;

    @Inject
    @Neo4j
    private PersistenceManager pm;
    @Inject
    private KeyFactory keyFactory;

    public GraphDAOImpl()
    {
    }

    protected GraphDAOImpl(PersistenceManager pm, KeyFactory keyFactory, int ALTEREGO_TRAVERSATION_MAX_DEPTH)
    {
        this.pm = pm;
        this.keyFactory = keyFactory;
        this.ALTEREGO_TRAVERSATION_MAX_DEPTH = ALTEREGO_TRAVERSATION_MAX_DEPTH;
    }

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
    @Override
    public Integer createPerson(Person person) throws InvalidPersonException, InvalidProfileException, PersonAlreadyExistsException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person object is null.");
        }

        if (!person.isValid())
        {
            throw new InvalidPersonException(
                    "Person is not declarated properly. Person's profile is not referred correctly or information source of person is missing.");
        }

        try
        {
            Key key = keyFactory.createKey(person.getProfile());

            if (isPersonAlreadyCreated(key))
            {
                throw new PersonAlreadyExistsException();
            }

            Integer id = pm.createNode(toProperties(person));

            pm.addNodeToIndex(key.getPrefix(), usrIndexKey, key.getUsername(), id);
            pm.addNodeToIndex(globalIndexName, globalIndexKey, globalIndexValue, id, false);

            indexSources(id, person.getSources());

            return id;
        }
        catch (InvalidPropertiesException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public Person retrievePerson(Integer id) throws PersonNotFoundException
    {

        if (id == null)
        {
            throw new IllegalArgumentException("Person ID is null.");
        }

        try
        {
            JSONObject node = pm.retrieveNode(id);

            if (node == null)
            {
                throw new PersonNotFoundException();
            }

            return toPerson(node);
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
    }

    /**
     * 
     * @param key
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public Person retrievePerson(Key key) throws PersonNotFoundException
    {
        if (key == null)
        {
            throw new IllegalArgumentException("Person key is null.");
        }

        try
        {
            JSONObject node = pm.retrieveNodeFromIndex(key.getPrefix(),
                    usrIndexKey, key.getUsername());

            if (node == null)
            {
                throw new PersonNotFoundException();
            }

            return toPerson(node);
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public List<Person> retrievePersons()
    {
        List<Person> persons = new ArrayList<Person>();

        try
        {
            JSONArray nodes = pm.retrieveNodesFromIndex(globalIndexName,
                    globalIndexKey, globalIndexValue);

            for (int i = 0 ; i < nodes.length() ; i++)
            {
                persons.add(toPerson(nodes.getJSONObject(i)));
            }

            return persons;
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            return persons;
        }
    }

    /**
     * 
     * @param source
     * @return 
     */
    @Override
    public List<Person> retrievePersons(URI source)
    {
        List<Person> persons = new ArrayList<Person>();

        try
        {
            JSONArray nodes = pm.retrieveNodesFromIndex(srcIndexName,
                    srcIndexKey,
                    source.getHost());

            for (int i = 0 ; i < nodes.length() ; i++)
            {
                persons.add(toPerson(nodes.getJSONObject(i)));
            }

            return persons;
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            return persons;
        }
    }

    /**
     * 
     * @param person
     * @return
     * @throws InvalidPersonException
     * @throws InvalidProfileException 
     */
    @Override
    public Integer updatePerson(Person person) throws InvalidPersonException, InvalidProfileException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person object is null.");
        }

        if (!person.isValid())
        {
            throw new InvalidPersonException(
                    "Person is not declarated properly. Person's profile are not referred correctly or information source of person is missing.");
        }

        try
        {
            Key key = keyFactory.createKey(person.getProfile());

            Person p = retrievePerson(key);

            pm.addProperties(p.getId(), toProperties(person));
            
            reindexSources(p.getId(), p.getSources(), person.getSources());

            return p.getId();
        }
        catch (PersonNotFoundException ex)
        {
            try
            {
                return createPerson(person);
            }
            catch (PersonAlreadyExistsException e)
            {
                throw new RuntimeException(e);
            }
        }
        catch (InvalidPropertiesException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param id
     * @return 
     */
    @Override
    public boolean deletePerson(Integer id)
    {

        if (id == null)
        {
            throw new IllegalArgumentException("Person ID is null.");
        }

        try
        {
            JSONArray relationships = pm.retrieveRelationships(id, ALL);

            for (int i = 0 ; i < relationships.length() ; i++)
            {
                JSONObject relationship = relationships.getJSONObject(i);

                String relURI = relationship.getString("self");

                int slash = relURI.lastIndexOf("/");
                Integer relId = Integer.valueOf(relURI.substring(slash + 1));

                pm.deleteRelationship(relId);
            }

            return pm.deleteNode(id);
        }
        catch (NodeNotFoundException ex)
        {
            return false;
        }
        catch (CannotDeleteNodeException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param key
     * @return 
     */
    @Override
    public boolean deletePerson(Key key)
    {

        if (key == null)
        {
            throw new IllegalArgumentException("Person key is null.");
        }

        try
        {
            JSONObject node = pm.retrieveNodeFromIndex(key.getPrefix(),
                    usrIndexKey, key.getUsername());

            if (node == null)
            {
                return false;
            }

            String relURI = node.getString("self");

            int slash = relURI.lastIndexOf("/");
            Integer nodeId = Integer.valueOf(relURI.substring(slash + 1));

            return deletePerson(nodeId);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            return false;
        }
    }

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
    @Override
    public boolean declareSameness(Integer person, Integer alterEgo,
            List<URI> sources) throws PersonNotFoundException
    {
        Relation sameness = new Relation();

        sameness.setObject(person);
        sameness.setSubject(alterEgo);
        sameness.setType(owlSameAs);
        sameness.setSources(sources);

        if (!sameness.isValid())
        {
            throw new IllegalArgumentException(
                    "Sameness is not declarated properly. Persons are not referred correctly or information source of sameness is missing.");
        }

        try
        {
            Integer in = null;
            Integer out = null;

            List<Relation> relations = retrieveSameness(sameness.getObject());

            for (Relation relation : relations)
            {
                if (relation.getObject().equals(sameness.getObject())
                        && relation.getSubject().equals(sameness.getSubject()))
                {
                    out = relation.getId();
                }
                else if (relation.getObject().equals(sameness.getSubject())
                        && relation.getSubject().equals(sameness.getObject()))
                {
                    in = relation.getId();
                }
            }

            if (in != null)
            {
                pm.addMetadataToRelationship(in, toProperties(sameness));
            }
            else
            {
                pm.createRelationship(sameness.getSubject(),
                        sameness.getObject(),
                        sameness.getType(), toProperties(sameness));
            }

            if (out != null)
            {
                pm.addMetadataToRelationship(out, toProperties(sameness));
            }
            else
            {
                pm.createRelationship(sameness.getObject(),
                        sameness.getSubject(),
                        sameness.getType(), toProperties(sameness));
            }

            return (in == null) && (out == null);
        }
        catch (InvalidRelationshipException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (NodeNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (RelationshipNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (InvalidMetadataException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param person
     * @param alterEgo
     * @param sources
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public boolean declareSameness(Key person, Key alterEgo, List<URI> sources)
            throws PersonNotFoundException
    {

        if (person == null || alterEgo == null)
        {
            throw new IllegalArgumentException(
                    "Persons are not referred correctly. One or both keys are null.");
        }

        try
        {
            JSONObject n1 = pm.retrieveNodeFromIndex(person.getPrefix(),
                    usrIndexKey,
                    person.getUsername());

            if (n1 == null)
            {
                throw new PersonNotFoundException();
            }

            JSONObject n2 = pm.retrieveNodeFromIndex(alterEgo.getPrefix(),
                    usrIndexKey,
                    alterEgo.getUsername());

            if (n2 == null)
            {
                throw new PersonNotFoundException();
            }

            return declareSameness(toPerson(n1).getId(), toPerson(n2).getId(),
                    sources);
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public Path retrieveAlterEgos(Integer id) throws PersonNotFoundException
    {
        TraversalDescription t = new TraversalDescription();

        t.addRelationship(owlSameAs, OUT);
        t.setMaxDepth(ALTEREGO_TRAVERSATION_MAX_DEPTH);

        try
        {
            JSONArray paths = pm.traverse(id, t, FULLPATH);

            // Create start of path

            Path start = new Path();

            start.setPerson(retrievePerson(id));

            // Create map of path parts

            Map<Integer, Path> parts = new HashMap<Integer, Path>();

            parts.put(id, start);

            // Analyze returned paths

            for (int i = 0 ; i < paths.length() ; i++)
            {
                JSONObject path = paths.getJSONObject(i);

                JSONArray nodes = path.getJSONArray("nodes");

                Map<Integer, Person> persons = new HashMap<Integer, Person>();

                for (int j = 0 ; j < nodes.length() ; j++)
                {
                    Person person = toPerson(nodes.getJSONObject(j));

                    persons.put(person.getId(), person);
                }

                JSONArray relationships = path.getJSONArray("relationships");

                List<Relation> relations = new ArrayList<Relation>();

                for (int j = 0 ; j < relationships.length() ; j++)
                {
                    Relation relation = toRelation(
                            relationships.getJSONObject(j));

                    relations.add(relation);
                }

                for (Relation relation : relations)
                {
                    if (relation.getType().equals(owlSameAs))
                    {
                        if (relation.getObject().equals(relation.getSubject()))
                        {
                            continue;
                        }

                        Path p1;

                        if (parts.containsKey(relation.getObject()))
                        {
                            p1 = parts.get(relation.getObject());
                        }
                        else
                        {
                            p1 = new Path();

                            p1.setPerson(persons.get(relation.getObject()));

                            parts.put(relation.getObject(), p1);
                        }

                        Path p2;

                        if (parts.containsKey(relation.getSubject()))
                        {
                            p2 = parts.get(relation.getSubject());
                        }
                        else
                        {
                            p2 = new Path();

                            p2.setPerson(persons.get(relation.getSubject()));

                            parts.put(relation.getSubject(), p2);
                        }

                        for (URI source : relation.getSources())
                        {
                            p2.addSource(source);
                        }

                        p1.addPerson(p2);
                    }
                }
            }

            return start;
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
    }

    /**
     * 
     * @param key
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public Path retrieveAlterEgos(Key key) throws PersonNotFoundException
    {
        if (key == null)
        {
            throw new IllegalArgumentException("Person key is null.");
        }

        try
        {
            JSONObject node = pm.retrieveNodeFromIndex(key.getPrefix(),
                    usrIndexKey, key.getUsername());

            if (node == null)
            {
                throw new PersonNotFoundException();
            }

            String relURI = node.getString("self");

            int slash = relURI.lastIndexOf("/");
            Integer nodeId = Integer.valueOf(relURI.substring(slash + 1));

            return retrieveAlterEgos(nodeId);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
    }

    /**
     * 
     * @param person
     * @param alterEgo
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public boolean refuseSameness(Integer person, Integer alterEgo)
            throws PersonNotFoundException
    {

        if (person == null || alterEgo == null)
        {
            throw new IllegalArgumentException(
                    "Persons are not referred correctly. One or both IDs are null.");
        }

        List<Relation> relations = retrieveSameness(person);

        int deleted = 0;

        for (Relation relation : relations)
        {
            if ((relation.getObject().equals(person)
                    && relation.getSubject().equals(alterEgo))
                    || (relation.getObject().equals(alterEgo)
                    && relation.getSubject().equals(person)))
            {
                pm.deleteRelationship(relation.getId());

                deleted++;
            }
        }

        return deleted > 0;
    }

    /**
     * 
     * @param person
     * @param alterEgo
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public boolean refuseSameness(Key person, Key alterEgo) throws PersonNotFoundException
    {

        if (person == null || alterEgo == null)
        {
            throw new IllegalArgumentException(
                    "Persons are not referred correctly. One or both keys are null.");
        }

        try
        {
            JSONObject n1 = pm.retrieveNodeFromIndex(person.getPrefix(),
                    usrIndexKey, person.getUsername());

            if (n1 == null)
            {
                throw new PersonNotFoundException();
            }

            JSONObject n2 = pm.retrieveNodeFromIndex(alterEgo.getPrefix(),
                    usrIndexKey, alterEgo.getUsername());

            if (n2 == null)
            {
                throw new PersonNotFoundException();
            }

            return refuseSameness(toPerson(n1).getId(), toPerson(n2).getId());
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
    }

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
    @Override
    public Integer createRelation(Relation relation) throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException
    {
        if (relation == null)
        {
            throw new IllegalArgumentException(
                    "Relation object is null.");
        }

        if (!relation.isValid())
        {
            throw new InvalidRelationshipException(
                    "Relation is not declarated properly. Persons are not referred correctly or information source of relation is missing.");
        }

        if (relation.getType().equals(owlSameAs))
        {
            throw new IllegalAccessException(String.format(
                    "Relation type cannot be %s.", owlSameAs));
        }

        try
        {
            if (isRelationAlreadyCreated(relation.getObject(), relation.
                    getSubject(), relation.getType()))
            {
                throw new RelationAlreadyExistsException();
            }

            Integer id = pm.createRelationship(relation.getObject(),
                    relation.getSubject(), relation.getType(), toProperties(
                    relation));

            return id;
        }
        catch (NodeNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (InvalidMetadataException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param id
     * @return
     * @throws RelationNotFoundException
     * @throws IllegalAccessException 
     */
    @Override
    public Relation retrieveRelation(Integer id)
            throws RelationNotFoundException, IllegalAccessException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Relation ID is null.");
        }

        try
        {
            JSONObject relationship = pm.retrieveRelationship(id);

            Relation relation = toRelation(relationship);

            if (relation.getType().equals(owlSameAs))
            {
                // TODO - Add exception message.
                throw new IllegalAccessException();
            }

            return relation;
        }
        catch (RelationshipNotFoundException ex)
        {
            throw new RelationNotFoundException();
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param person
     * @param direction
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(Integer person,
            DirectionEnum direction) throws PersonNotFoundException
    {
        return retrieveRelations(person, direction, null);
    }

    /**
     * 
     * @param key
     * @param direction
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(Key key, DirectionEnum direction)
            throws PersonNotFoundException
    {
        return retrieveRelations(key, direction, null);
    }

    /**
     * 
     * @param person
     * @param direction
     * @param type
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(Integer person,
            DirectionEnum direction,
            String type) throws PersonNotFoundException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person ID is null.");
        }

        try
        {
            JSONArray relationships = pm.retrieveRelationships(person, direction,
                    type);

            List<Relation> relations = new ArrayList<Relation>();

            for (int i = 0 ; i < relationships.length() ; i++)
            {
                Relation relation = toRelation(relationships.getJSONObject(i));

                if (!relation.getType().equals(owlSameAs))
                {
                    relations.add(relation);
                }
            }

            return relations;
        }
        catch (NodeNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param key
     * @param direction
     * @param type
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(Key key, DirectionEnum direction,
            String type) throws PersonNotFoundException
    {
        if (key == null)
        {
            throw new IllegalArgumentException("Person key is null.");
        }

        try
        {
            JSONObject node = pm.retrieveNodeFromIndex(key.getPrefix(),
                    usrIndexKey,
                    key.getUsername());

            if (node == null)
            {
                throw new PersonNotFoundException();
            }

            Person person = toPerson(node);

            return retrieveRelations(person.getId(), direction, type);
        }
        catch (InvalidProfileException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (NodeIndexNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param relation
     * @return
     * @throws PersonNotFoundException
     * @throws IllegalAccessException 
     */
    @Override
    public Integer updateRelation(Relation relation)
            throws PersonNotFoundException, IllegalAccessException
    {
        if (relation == null)
        {
            throw new IllegalArgumentException(
                    "Relation object is null.");
        }

        if (!relation.isValid())
        {
            throw new IllegalArgumentException(
                    "Relation is not declarated properly. Persons are not referred correctly or information source of relation is missing.");
        }

        if (relation.getType().equals(owlSameAs))
        {
            // TODO - Add exception message.
            throw new IllegalAccessException();
        }

        try
        {
            List<Relation> relations = retrieveRelations(relation.getObject(),
                    OUT, relation.getType());

            for (Relation rel : relations)
            {
                if (rel.getSubject().equals(relation.getSubject()))
                {
                    pm.addMetadataToRelationship(rel.getId(),
                            toProperties(relation));

                    return rel.getId();
                }
            }

            Integer id = pm.createRelationship(relation.getObject(),
                    relation.getSubject(), relation.getType(),
                    toProperties(relation));

            return id;
        }
        catch (InvalidRelationshipException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (NodeNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (RelationshipNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (InvalidMetadataException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param id
     * @return
     * @throws IllegalAccessException 
     */
    @Override
    public boolean deleteRelation(Integer id) throws IllegalAccessException
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Relation ID is null.");
        }

        try
        {
            Relation relation = retrieveRelation(id);

            if (relation.getType().equals(owlSameAs))
            {
                // TODO - Add exception message.
                throw new IllegalAccessException();
            }
        }
        catch (RelationNotFoundException ex)
        {
            return false;
        }

        return pm.deleteRelationship(id);
    }

    /**
     * 
     * @param person
     * @return
     * @throws PersonNotFoundException 
     */
    private List<Relation> retrieveSameness(Integer person) throws PersonNotFoundException
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person ID is null.");
        }

        try
        {
            JSONArray relationships = pm.retrieveRelationships(person, ALL,
                    owlSameAs);

            List<Relation> relations = new ArrayList<Relation>();

            for (int i = 0 ; i < relationships.length() ; i++)
            {
                Relation relation = toRelation(relationships.getJSONObject(i));

                relations.add(relation);
            }

            return relations;
        }
        catch (NodeNotFoundException ex)
        {
            throw new PersonNotFoundException();
        }
        catch (JSONException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (URISyntaxException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    /* ********************************************************************** *
     *                            Accessor Methods                            *
     * ********************************************************************** */
    /**
     * 
     * @param key
     * @return
     * @throws JSONException 
     */
    private boolean isPersonAlreadyCreated(Key key) throws JSONException
    {
        JSONObject node;

        try
        {
            node = pm.retrieveNodeFromIndex(key.getPrefix(), usrIndexKey, key.
                    getUsername());

            if (node != null)
            {
                return true;
            }
        }
        catch (NodeIndexNotFoundException ex)
        {
        }

        return false;
    }

    /**
     * 
     * @param object
     * @param subject
     * @param type
     * @return
     * @throws PersonNotFoundException 
     */
    private boolean isRelationAlreadyCreated(Integer object, Integer subject,
            String type) throws PersonNotFoundException
    {
        List<Relation> relations = retrieveRelations(object, OUT, type);

        for (Relation relation : relations)
        {
            if (relation.getSubject().equals(subject))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param person
     * @return
     * @throws JSONException 
     */
    private JSONObject toProperties(Person person) throws JSONException
    {
        JSONObject data = new JSONObject();

        data.put(foafHomepage, person.getProfile().toString());

        List<String> sources = new ArrayList<String>();

        for (URI source : person.getSources())
        {
            sources.add(source.getHost());
        }

        data.put(siocNote, new JSONArray(sources));

        if (person.getProperties() != null)
        {
            for (String property : person.getProperties().keySet())
            {
                data.put(property, person.getProperties().get(property));
            }
        }

        return data;
    }

    /**
     * 
     * @param relation
     * @return
     * @throws JSONException 
     */
    private JSONObject toProperties(Relation relation) throws JSONException
    {
        JSONObject data = new JSONObject();

        List<String> sources = new ArrayList<String>();

        for (URI source : relation.getSources())
        {
            sources.add(source.getHost());
        }

        data.put(siocNote, new JSONArray(sources));

        if (relation.getProperties() != null)
        {
            for (String property : relation.getProperties().keySet())
            {
                data.put(property, relation.getProperties().get(property));
            }
        }

        return data;
    }

    /**
     * 
     * @param node
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    private Person toPerson(JSONObject node) throws JSONException, URISyntaxException, InvalidProfileException
    {
        Person person = new Person();

        // Retrieve ID of the person

        String relURI = node.getString("self");

        int slash = relURI.lastIndexOf("/");
        Integer nodeId = Integer.valueOf(relURI.substring(slash + 1));

        person.setId(nodeId);

        // Retrieve data object from the node

        JSONObject data = node.getJSONObject("data");

        // Retrieve profile URI

        if (data.has(foafHomepage))
        {
            URI profile = new URI(data.getString(foafHomepage));

            person.setProfile(profile);
            person.setKey(keyFactory.createKey(profile));

            data.remove(foafHomepage);
        }

        // Retrieve sources of the node information

        if (data.has(siocNote))
        {
            JSONArray array = data.getJSONArray(siocNote);

            List<URI> sources = new ArrayList<URI>();

            for (int i = 0 ; i < array.length() ; i++)
            {
                URI source = new URI("http://" + array.getString(i));

                sources.add(source);
            }

            person.setSources(sources);

            data.remove(siocNote);
        }

        // Retrieve other information about person

        if (data.length() > 0)
        {
            Map<String, String> properties = new HashMap<String, String>();

            for (Iterator it = data.keys() ; it.hasNext() ;)
            {
                String key = (String) it.next();

                properties.put(key, data.getString(key));
            }

            person.setProperties(properties);
        }

        return person;
    }

    /**
     * 
     * @param node
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    private Relation toRelation(JSONObject node) throws JSONException, URISyntaxException
    {
        Relation relation = new Relation();

        int slash;
        Integer nodeId;

        // Retrieve ID of the relation

        String relURI = node.getString("self");

        slash = relURI.lastIndexOf("/");
        nodeId = Integer.valueOf(relURI.substring(slash + 1));

        relation.setId(nodeId);

        // Retrieve ID of the object

        String objectURI = node.getString("start");

        slash = objectURI.lastIndexOf("/");
        nodeId = Integer.valueOf(objectURI.substring(slash + 1));

        relation.setObject(nodeId);

        // Retrieve ID of the subject

        String subjectURI = node.getString("end");

        slash = subjectURI.lastIndexOf("/");
        nodeId = Integer.valueOf(subjectURI.substring(slash + 1));

        relation.setSubject(nodeId);

        // Retrieve type of the relation

        relation.setType(node.getString("type"));

        // Retrieve data object from the node

        JSONObject data = node.getJSONObject("data");

        // Retrieve sources of the node information

        if (data.has(siocNote))
        {
            JSONArray array = data.getJSONArray(siocNote);

            List<URI> sources = new ArrayList<URI>();

            for (int i = 0 ; i < array.length() ; i++)
            {
                URI source = new URI("http://" + array.getString(i));

                sources.add(source);
            }

            relation.setSources(sources);

            data.remove(siocNote);
        }

        // Retrieve other information about person

        if (data.length() > 0)
        {
            Map<String, String> properties = new HashMap<String, String>();

            for (Iterator it = data.keys() ; it.hasNext() ;)
            {
                String key = (String) it.next();

                properties.put(key, data.getString(key));
            }

            relation.setProperties(properties);
        }

        return relation;
    }

    /**
     * 
     * @param id
     * @param sources
     * @throws JSONException 
     */
    private void indexSources(Integer id, List<URI> sources) throws JSONException
    {   
        try
        {
            for (URI source : sources)
            {
                pm.addNodeToIndex(srcIndexName, srcIndexKey, source.getHost(),
                        id, false);
            }
        }
        catch (NodeIndexNotFoundException ex)
        {
            pm.createNodeIndex(srcIndexName);

            indexSources(id, sources);
        }
    }

    /**
     * 
     * @param id
     * @param sources
     * @throws JSONException 
     */
    private void reindexSources(Integer id, List<URI> oldSources, List<URI> newSources) throws JSONException
    {   
        for (URI source : oldSources)
        {
            try
            {
                pm.deleteNodeFromIndex(srcIndexName, srcIndexKey, source.getHost(), id);
            }
            catch (NodeIndexNotFoundException ex)
            {
                // TODO - Nothing to do.
            }
        }
        
        indexSources(id, newSources);
    }// </editor-fold>
}
