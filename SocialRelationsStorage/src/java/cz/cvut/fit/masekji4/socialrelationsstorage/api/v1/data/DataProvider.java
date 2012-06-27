package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.filters.SourceFilter;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.filters.SourceFilterImpl;
import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.NumberUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.KeyFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidRelationException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidSamenessException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * <code>DataProvider</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class DataProvider
{

    @Inject
    @Config
    private String FOAF_PERSON;
    @Inject
    @Config
    private String REL_RELATIONSHIP;
    @Inject
    @Config
    private String OWL_SAME_AS;
    @Inject
    @Config
    private String SIOC_NOTE;
    @Inject
    @Config
    private String JSON_LD_ID;
    @Inject
    @Config
    private String JSON_LD_TYPE;
    @Inject
    private StorageService storageService;
    @Inject
    private KeyFactory keyFactory;
    @Inject
    private JsonBuilder jsonBuilder;
    @Inject
    private ObjectBuilder objectBuilder;
    @Inject
    private UriBuilder uriBuilder;

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
     * @throws PersonNotFoundException
     * @throws JSONException
     * @throws URISyntaxException 
     */
    public JSONObject createPerson(JSONObject person) throws InvalidPersonException,
            InvalidProfileException, PersonAlreadyExistsException,
            PersonNotFoundException, JSONException, URISyntaxException
    {
        Person obj = objectBuilder.getPerson(person);
        
        Integer id = storageService.createPerson(obj);

        return retrievePerson(id.toString(), null);
    }

    /**
     * 
     * @param uid
     * @param filter
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrievePerson(String uid, SourceFilter filter)
            throws PersonNotFoundException, JSONException
    {
        Integer id = getPersonId(uid);
        
        Person obj = storageService.retrievePerson(id);
        List<Person> list = storageService.retrieveAlterEgos(id, filter);

        Collection<String> sameAs = new LinkedList<String>();

        for (Person person : list)
        {
            String alterego;
            
            if (NumberUtils.isInt(uid))
            {
                alterego = String.format("%s:%s", person.getKey().getPrefix(), person.getKey().getUsername());
            }
            else
            {
                alterego = person.getId().toString();
            }
                    
            sameAs.add(alterego);
        }

        JSONObject person = jsonBuilder.getPerson(obj, uid, sameAs);

        return person;

    }

    /**
     * 
     * @return
     * @throws JSONException 
     */
    public JSONObject retrievePersons() throws JSONException
    {
        List<Person> list = storageService.retrievePersons();

        JSONObject persons = jsonBuilder.getPersons(list);

        return persons;
    }

    /**
     * 
     * @param source
     * @return
     * @throws JSONException 
     */
    public JSONObject retrievePersons(String source) throws JSONException
    {
        List<Person> list = storageService.retrievePersons("http://" + source);

        JSONObject persons = jsonBuilder.getPersons(list, source);

        return persons;
    }

    /**
     * 
     * @param uid
     * @param person
     * @return
     * @throws JSONException
     * @throws URISyntaxException
     * @throws InvalidPersonException
     * @throws InvalidProfileException
     * @throws PersonNotFoundException 
     */
    public JSONObject updatePerson(String uid, JSONObject person)
            throws JSONException, URISyntaxException, InvalidPersonException,
            InvalidProfileException, PersonNotFoundException
    {
        Integer id = getPersonId(uid);

        Person e = storageService.retrievePerson(id);

        // Check if @id is set and if it matches ID in URI

        if (person.has(JSON_LD_ID))
        {
            String juri = person.getString(JSON_LD_ID);
            String juid = juri.substring(juri.lastIndexOf("/") + 1);

            if (NumberUtils.isInt(juid))
            {
                if (!e.getId().equals(new Integer(juid)))
                {
                    throw new InvalidPersonException(
                            String.format(
                            "ID in attribute %s is not corresponding to ID of this resource.",
                            JSON_LD_ID));
                }
            }
            else
            {
                String[] key = uid.split(":");

                if (key.length != 2)
                {
                    throw new InvalidPersonException(
                            String.format("ID in attribute %s is not valid.",
                            JSON_LD_ID));
                }

                if (!e.getKey().getPrefix().equals(key[0])
                        || !e.getKey().getUsername().equals(key[1]))
                {
                    throw new InvalidPersonException(
                            String.format(
                            "ID in attribute %s is not corresponding to ID of this resource.",
                            JSON_LD_ID));
                }
            }
        }

        // Check if @type is set and if it matches resource type

        if (person.has(JSON_LD_TYPE))
        {
            String type = person.getString(JSON_LD_TYPE);

            if (!type.equals(FOAF_PERSON))
            {
                throw new InvalidPersonException(
                        String.format(
                        "Type in attribute %s is not corresponding to type of this resource.",
                        JSON_LD_TYPE));
            }
        }

        // Build Person object

        Person obj = objectBuilder.getPerson(person);

        if (obj.getProfile() != null)
        {
            Key key = keyFactory.createKey(obj.getProfile());

            if (!e.getKey().equals(key))
            {
                throw new InvalidProfileException(
                        "Profile URI cannot be changed.");
            }
        }
        else
        {
            obj.setProfile(e.getProfile());
        }

        if (obj.getSources() == null || obj.getSources().isEmpty())
        {
            obj.setSources(e.getSources());
        }

        String nuid = storageService.updatePerson(obj).toString();

        return retrievePerson(nuid, SourceFilterImpl.buildFilter(null));
    }

    /**
     * 
     * @param uid
     * @return 
     */
    public boolean deletePerson(String uid)
    {
        try
        {
            Integer id = getPersonId(uid);
            
            return storageService.deletePerson(id);
        }
        catch (PersonNotFoundException ex)
        {
            return false;
        }
    }

    /* ********************************************************************** *
     *                                SAMENESS                                *
     * ********************************************************************** */
    /**
     * 
     * @param id
     * @param sameness
     * @throws JSONException
     * @throws URISyntaxException
     * @throws InvalidSamenessException
     * @throws PersonNotFoundException 
     */
    public URI declareSameness(String uid, JSONObject sameness)
            throws JSONException, URISyntaxException, InvalidSamenessException, PersonNotFoundException
    {
        Integer id = getPersonId(uid);
        
        if (sameness.has(OWL_SAME_AS) && sameness.has(SIOC_NOTE))
        {
            List<URI> sources = getSources(sameness);
            
            String uri2 = sameness.getString(OWL_SAME_AS);
            String uid2 = uri2.substring(uri2.lastIndexOf("/") + 1);

            Integer alterego = getPersonId(uid2);

            if (storageService.declareSameness(id, alterego, sources))
            {
                URI uri = new URI(uriBuilder.getSamenessURI(uid, uid2));
                
                return uri;
            }
            else
            {
                return null;
            }
        }

        // TODO - Add exception message.
        throw new InvalidSamenessException();
    }

    /**
     * 
     * @param person
     * @param alterEgo
     * @return
     * @throws PersonNotFoundException 
     */
    public boolean refuseSameness(String person, String alterEgo)
            throws PersonNotFoundException
    {
        Integer uid1 = getPersonId(person);
        Integer uid2 = getPersonId(alterEgo);
        
        return storageService.refuseSameness(uid1, uid2);
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
     * @throws JSONException
     * @throws RelationNotFoundException
     * @throws URISyntaxException 
     */
    public JSONObject createRelation(JSONObject relation) throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException, JSONException, RelationNotFoundException, URISyntaxException
    {
        Relation obj = objectBuilder.getRelation(relation);

        Integer id = storageService.createRelation(obj);

        return retrieveRelation(id);
    }

    /**
     * 
     * @param id
     * @return
     * @throws RelationNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrieveRelation(Integer id)
            throws RelationNotFoundException, JSONException, IllegalAccessException
    {
        Relation rel = storageService.retrieveRelation(id);

        JSONObject relation = jsonBuilder.getRelation(rel);

        return relation;
    }

    /**
     * 
     * @param uid
     * @param filter
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrieveRelations(String uid, SourceFilter filter)
            throws PersonNotFoundException, JSONException
    {
        Integer id = getPersonId(uid);
        
        List<Relation> list = storageService.retrieveRelations(id, filter);

        JSONObject relations = jsonBuilder.getRelations(list, uid);

        return relations;
    }

    /**
     * 
     * @param uid
     * @param type
     * @param filter
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrieveRelations(String uid, String type,
            SourceFilter filter) throws PersonNotFoundException, JSONException
    {
        Integer id = getPersonId(uid);
        
        List<Relation> list = storageService.retrieveRelations(id, type, filter);

        JSONObject relations = jsonBuilder.getRelations(list, uid, type);

        return relations;
    }

    /**
     * 
     * @param object
     * @param subject
     * @param type
     * @param filter
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrieveRelations(String object, String subject,
            String type, SourceFilter filter) throws PersonNotFoundException,
            JSONException
    {
        Integer obj = getPersonId(object);
        Integer sub = getPersonId(subject);
        
        List<Relation> list = storageService.retrieveRelations(obj, sub,
                type, filter);

        JSONObject relations = jsonBuilder.getRelations(list, object, type, subject);

        return relations;
    }

    /**
     * 
     * @param id
     * @param relation
     * @return
     * @throws RelationNotFoundException
     * @throws IllegalAccessException
     * @throws JSONException
     * @throws PersonNotFoundException
     * @throws InvalidRelationshipException
     * @throws URISyntaxException
     * @throws InvalidRelationException 
     */
    public JSONObject updateRelation(Integer id, JSONObject relation)
            throws RelationNotFoundException, IllegalAccessException,
            JSONException, PersonNotFoundException, InvalidRelationshipException,
            URISyntaxException, InvalidRelationException
    {

        Relation e = storageService.retrieveRelation(id);

        // Check if @id is set and if it matches ID in URI

        if (relation.has(JSON_LD_ID))
        {
            String uri = relation.getString(JSON_LD_ID);
            String rid = uri.substring(uri.lastIndexOf("/") + 1);

            if (!NumberUtils.isInt(rid) || !e.getId().equals(new Integer(rid)))
            {
                throw new InvalidRelationException(
                        String.format(
                        "ID in attribute %s is not corresponding to ID of this resource.",
                        JSON_LD_ID));
            }
        }

        // Check if @type is set and if it matches resource type

        if (relation.has(JSON_LD_TYPE))
        {
            String type = relation.getString(JSON_LD_TYPE);

            if (!type.equals(REL_RELATIONSHIP))
            {
                throw new InvalidRelationException(
                        String.format(
                        "Type in attribute %s is not corresponding to type of this resource.",
                        JSON_LD_TYPE));
            }
        }

        // Build Relation object

        Relation obj = objectBuilder.getRelation(relation);

        if (obj.getObject() != null && !e.getObject().equals(obj.getObject()))
        {
            throw new InvalidRelationshipException(
                    "Participants of the relation cannot be changed.");
        }
        else
        {
            obj.setObject(e.getObject());
        }

        if (obj.getType() != null && !e.getType().equals(obj.getType()))
        {
            throw new InvalidRelationshipException(
                    "Relation type cannot be changed.");
        }
        else
        {
            obj.setType(e.getType());
        }

        if (obj.getSubject() != null && !e.getSubject().equals(obj.getSubject()))
        {
            throw new InvalidRelationshipException(
                    "Participants of the relation cannot be changed.");
        }
        else
        {
            obj.setSubject(e.getSubject());
        }

        if (obj.getSources() == null || obj.getSources().isEmpty())
        {
            obj.setSources(e.getSources());
        }

        id = storageService.updateRelation(obj);

        return retrieveRelation(id);
    }

    /**
     * 
     * @param id
     * @return
     * @throws IllegalAccessException 
     */
    public boolean deleteRelation(Integer id) throws IllegalAccessException
    {
        return storageService.deleteRelation(id);
    }

    /* ********************************************************************** *
     *                                SOURCES                                 *
     * ********************************************************************** */
    
    /**
     * 
     * @return 
     */
    public JSONObject retrieveSources() throws JSONException
    {
        List<URI> list = storageService.retrieveSources();
        
        JSONObject sources = jsonBuilder.getSources(list);
        
        return sources;
    }

    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    /* ********************************************************************** *
     *                            ACESSOR METHODS                             *
     * ********************************************************************** */
    /**
     * 
     * @param uid
     * @return
     * @throws PersonNotFoundException 
     */
    public Integer getPersonId(String uid) throws PersonNotFoundException
    {
        if (NumberUtils.isInt(uid))
        {
            return new Integer(uid);
        }
        else
        {
            String[] key = uid.split(":");

            if (key.length != 2)
            {
                throw new IllegalArgumentException(String.format("%s is not user identificator.", uid));
            }

            Person person = storageService.retrievePerson(key[0], key[1]);

            return person.getId();
        }
    }
    
    /**
     * 
     * @param object
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    private List<URI> getSources(JSONObject object) throws JSONException, URISyntaxException
    {
        List<URI> sources = new LinkedList<URI>();

        try
        {
            JSONArray array = new JSONArray(object.getString(SIOC_NOTE));

            for (int i = 0 ; i < array.length() ; i++)
            {
                URI source = new URI(array.getString(i));

                sources.add(source);
            }
        }
        catch (JSONException ex)
        {
            URI source = new URI(object.getString(SIOC_NOTE));

            if (source.getHost() == null)
            {
                throw new URISyntaxException(object.getString(SIOC_NOTE),
                        "Protocol must be specified.");
            }

            sources.add(source);
        }

        return sources;
    }// </editor-fold>
}
