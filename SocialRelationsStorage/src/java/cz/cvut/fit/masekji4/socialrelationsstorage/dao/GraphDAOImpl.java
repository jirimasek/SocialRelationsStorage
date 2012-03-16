package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.ALL;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.KeyFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.SamenessAlreadySetException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.Neo4j;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeIndexNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
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

    @Inject
    @Neo4j
    private PersistenceManager pm;
    @Inject
    private KeyFactory keyFactory;
    private String index = "username";

    public GraphDAOImpl()
    {
    }

    protected GraphDAOImpl(PersistenceManager pm, KeyFactory keyFactory)
    {
        this.pm = pm;
        this.keyFactory = keyFactory;
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
            node = pm.retrieveNodeFromIndex(key.getPrefix(), index, key.
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
     * @param person
     * @return
     * @throws JSONException 
     */
    private JSONObject toProperties(Person person) throws JSONException
    {
        JSONObject node = new JSONObject();

        node.put("foaf:homepage", person.getProfile().toString());

        for (URI uri : person.getSources())
        {
            node.append("sioc:note", uri.toString());
        }

        if (person.getProperties() != null)
        {
            for (String property : person.getProperties().keySet())
            {
                node.put(property, person.getProperties().get(property));
            }
        }

        return node;
    }

    private Person toPerson(JSONObject node) throws JSONException, URISyntaxException
    {
        Person person = new Person();
        
        // Retrieve ID of the person

        String relURI = node.getString("self");

        int slash = relURI.lastIndexOf("/");
        int nodeId = Integer.parseInt(relURI.substring(slash + 1));

        person.setId(nodeId);
        
        // Retrieve data object from the node

        JSONObject data = node.getJSONObject("data");
        
        // Retrieve profile URI

        if (data.has("foaf:homepage"))
        {
            URI profile = new URI(data.getString("foaf:homepage"));

            person.setProfile(profile);
            person.setKey(keyFactory.createKey(profile));

            data.remove("foaf:homepage");
        }
        
        // Retrieve sources of the node information

        if (data.has("sioc:note"))
        {
            JSONArray array = data.getJSONArray("sioc:note");

            List<URI> sources = new ArrayList<URI>();

            for (int i = 0 ; i < array.length() ; i++)
            {
                URI source = new URI(array.getString(i));

                sources.add(source);
            }

            person.setSources(sources);

            data.remove("sioc:note");
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
    }// </editor-fold>

    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
    @Override
    public Person createPerson(Person person) throws PersonAlreadyExistsException
    {
        if (person == null || !person.isValid())
        {
            throw new IllegalArgumentException(
                    "Person object is null or not valid.");
        }

        try
        {
            Key key = keyFactory.createKey(person.getProfile());

            if (isPersonAlreadyCreated(key))
            {
                throw new PersonAlreadyExistsException();
            }

            int id = pm.createNode(toProperties(person));

            person.setId(id);
            person.setKey(key);

            pm.addNodeToIndex(key.getPrefix(), index, key.getUsername(), id);

            return person;
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
        try
        {
            JSONObject node = pm.retrieveNode(id);

            if (node == null)
            {
                throw new PersonNotFoundException();
            }

            return toPerson(node);
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
        try
        {
            JSONObject node = pm.retrieveNodeFromIndex(key.getPrefix(), index,
                    key.getUsername());

            if (node == null)
            {
                throw new PersonNotFoundException();
            }

            return toPerson(node);
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

    @Override
    public List<Person> retrievePersons()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updatePerson(Person person)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param id
     * @return 
     */
    @Override
    public boolean deletePerson(Integer id)
    {
        try
        {
            JSONArray relationships = pm.retrieveRelationships(id, ALL);

            for (int i = 0 ; i < relationships.length() ; i++)
            {
                JSONObject relationship = relationships.getJSONObject(i);

                String relURI = relationship.getString("self");

                int slash = relURI.lastIndexOf("/");
                int relId = Integer.parseInt(relURI.substring(slash + 1));

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
        try
        {
            JSONObject node = pm.retrieveNodeFromIndex(key.getPrefix(), index,
                    key.getUsername());

            if (node == null)
            {
                return false;
            }

            String relURI = node.getString("self");

            int slash = relURI.lastIndexOf("/");
            int nodeId = Integer.parseInt(relURI.substring(slash + 1));

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
    @Override
    public void declareSameness(Person person, Person alterEgo) throws SamenessAlreadySetException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void declareSameness(Key key, Key alterEgo) throws PersonNotFoundException, SamenessAlreadySetException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Path retrieveAlterEgos(Person person) throws PersonNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Path retrieveAlterEgos(Key key) throws PersonNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean refuseSameness(Person person, Person alterEgo)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean refuseSameness(Key key, Key alterEgo) throws PersonNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */
    @Override
    public void createRelation(Relation relation) throws RelationAlreadyExistsException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Relation retrieveRelation() throws RelationNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Relation> retrieveRelations()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateRelation(Relation relation)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteRelation(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
