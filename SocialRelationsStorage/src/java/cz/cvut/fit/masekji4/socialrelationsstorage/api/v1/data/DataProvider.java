package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.NumberUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.KeyFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidSamenessException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.exceptions.BadRequestException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

    private static int CONTEXT_PERSON = 1;
    private static int CONTEXT_PERSONS = 2;
    private static int CONTEXT_RELATION = 3;
    private static int CONTEXT_RELATIONS = 4;
    @Inject
    @Config
    private String ROOT_URI;
    @Inject
    @Config
    private String API_V1_URI;
    @Inject
    @Config
    private String API_V1_PERSONS;
    @Inject
    @Config
    private String API_V1_RELATIONS;
    @Inject
    @Config
    private String API_V1_SOURCES;
    @Inject
    private StorageService storageService;
    @Inject
    private KeyFactory keyFactory;
    private String foafHomepage = "foaf:homepage";
    private String foafPerson = "foaf:Person";
    private String siocNote = "sioc:note";
    private String owlSameAs = "owl:sameAs";
    private String relParticipant = "rel:participant";
    private String relParticipantIn = "rel:participantIn";
    private String relRelationship = "rel:Relationship";

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
        Person obj = getPerson(person);

        Integer id = storageService.createPerson(obj);

        return retrievePerson(id, null);
    }

    /**
     * 
     * @param id
     * @param fields
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrievePerson(Integer id, String fields)
            throws PersonNotFoundException, JSONException
    {
        Person obj = storageService.retrievePerson(id);
        List<Person> list = storageService.retrieveAlterEgos(id);

        Collection<String> sameAs = new LinkedList<String>();

        // TODO - Add filtering.

        for (Person person : list)
        {
            sameAs.add(getPersonURI(person.getKey()));
        }

        JSONObject person = getPerson(obj, getPersonURI(obj.getKey()), sameAs);

        return person;

    }

    /**
     * 
     * @param prefix
     * @param username
     * @param fields
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrievePerson(String prefix, String username,
            String fields)
            throws PersonNotFoundException, JSONException
    {
        Person obj = storageService.retrievePerson(prefix, username);
        List<Person> list = storageService.retrieveAlterEgos(prefix, username);

        Collection<String> sameAs = new LinkedList<String>();

        // TODO - Add filtering.

        for (Person person : list)
        {
            sameAs.add(getPersonURI(person.getId()));
        }

        JSONObject person = getPerson(obj, getPersonURI(obj.getId()), sameAs);

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

        String uri = String.format("%s%s%s", ROOT_URI, API_V1_URI, API_V1_PERSONS);

        JSONObject persons = getPersons(list, uri);

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

        String uri = String.format("%s%s%s/%s", ROOT_URI, API_V1_URI,
                API_V1_PERSONS, source);

        JSONObject persons = getPersons(list, uri);

        return persons;
    }

    /**
     * 
     * @param id
     * @param person
     * @return
     * @throws JSONException
     * @throws URISyntaxException
     * @throws InvalidPersonException
     * @throws InvalidProfileException
     * @throws PersonNotFoundException 
     */
    public JSONObject updatePerson(Integer id, JSONObject person)
            throws JSONException, URISyntaxException, InvalidPersonException,
            InvalidProfileException, PersonNotFoundException
    {
        Person obj = getPerson(person);
        
        Person e = storageService.retrievePerson(id);
        
        if (person.has("@id")) 
        {
            String uri = person.getString("@id");
            String uid = uri.substring(uri.lastIndexOf("/") + 1);
            
            if (NumberUtils.isInt(uid))
            {
                if (!e.getId().equals(new Integer(uid)))
                {
                    throw new InvalidPersonException("ID in attribute @id is not corresponding to ID of this resource.");
                }
            }
            else
            {
                String[] key = uid.split(":");

                if (key.length != 2)
                {
                    throw new InvalidPersonException("ID in attribute @id is not valid.");
                }
                
                if (!e.getKey().getPrefix().equals(key[0])
                        || !e.getKey().getUsername().equals(key[1]))
                {
                    throw new InvalidPersonException("ID in attribute @id is not corresponding to ID of this resource.");
                }
            }
        }
        
        if (obj.getProfile() != null)
        {
            Key key = keyFactory.createKey(obj.getProfile());
            
            if (!e.getKey().equals(key))
            {
                throw new InvalidProfileException("Profile URI cannot be changed.");
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
        
        id = storageService.updatePerson(obj);

        return retrievePerson(id, null);
    }
    
    /**
     * 
     * @param prefix
     * @param username
     * @param person
     * @return
     * @throws JSONException
     * @throws InvalidProfileException
     * @throws URISyntaxException
     * @throws PersonNotFoundException
     * @throws InvalidPersonException 
     */
    public JSONObject updatePerson(String prefix, String username, JSONObject person)
            throws JSONException, InvalidProfileException, URISyntaxException,
            PersonNotFoundException, InvalidPersonException
    {
        Person obj = getPerson(person);
        
        Person e = storageService.retrievePerson(prefix, username);
        
        if (person.has("@id")) 
        {
            String uri = person.getString("@id");
            String uid = uri.substring(uri.lastIndexOf("/") + 1);
            
            if (NumberUtils.isInt(uid))
            {
                if (!e.getId().equals(new Integer(uid)))
                {
                    throw new InvalidPersonException("ID in attribute @id is not corresponding to ID of this resource.");
                }
            }
            else
            {
                String[] key = uid.split(":");

                if (key.length != 2)
                {
                    throw new InvalidPersonException("ID in attribute @id is not valid.");
                }
                
                if (!e.getKey().getPrefix().equals(key[0])
                        || !e.getKey().getUsername().equals(key[1]))
                {
                    throw new InvalidPersonException("ID in attribute @id is not corresponding to ID of this resource.");
                }
            }
        }
        
        if (obj.getProfile() != null)
        {
            Key key = keyFactory.createKey(obj.getProfile());

            if (!e.getKey().equals(key))
            {
                throw new InvalidProfileException("Profile URI cannot be changed.");
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
        
        Integer id = storageService.updatePerson(obj);

        return retrievePerson(id, null);
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    public boolean deletePerson(Integer id)
    {
        return storageService.deletePerson(id);
    }

    /**
     * 
     * @param prefix
     * @param username
     * @return 
     */
    public boolean deletePerson(String prefix, String username)
    {
        return storageService.deletePerson(prefix, username);
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
    public URI declareSameness(Integer id, JSONObject sameness)
            throws JSONException, URISyntaxException, InvalidSamenessException, PersonNotFoundException
    {
        if (sameness.has(owlSameAs) && sameness.has(siocNote))
        {
            List<URI> sources = getSources(sameness);

            String uid = sameness.getString(owlSameAs);

            Integer alterEgo;

            if (NumberUtils.isInt(uid))
            {
                alterEgo = new Integer(uid);
            }
            else
            {
                String[] key = uid.split(":");

                if (key.length != 2)
                {
                    // TODO - Add exception message.
                    throw new InvalidSamenessException();
                }

                Person person = storageService.retrievePerson(key[0], key[1]);
                
                alterEgo = person.getId();
            }

            if (storageService.declareSameness(id, alterEgo, sources))
            {
                URI uri = new URI(getSamenessURI(id.toString(), uid));
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
     * @param prefix
     * @param username
     * @param sameness
     * @return
     * @throws JSONException
     * @throws URISyntaxException
     * @throws PersonNotFoundException
     * @throws InvalidSamenessException 
     */
    public URI declareSameness(String prefix, String username, JSONObject sameness)
            throws JSONException, URISyntaxException, PersonNotFoundException, InvalidSamenessException
    {
        if (sameness.has(owlSameAs) && sameness.has(siocNote))
        {
            Person person = storageService.retrievePerson(prefix, username);
            
            Integer id = person.getId();
            
            List<URI> sources = getSources(sameness);

            String uid = sameness.getString(owlSameAs);

            Integer alterEgo;

            if (NumberUtils.isInt(uid))
            {
                alterEgo = new Integer(uid);
            }
            else
            {
                String[] key = uid.split(":");

                if (key.length != 2)
                {
                    // TODO - Add exception message.
                    throw new InvalidSamenessException();
                }

                person = storageService.retrievePerson(key[0], key[1]);
                
                alterEgo = person.getId();
            }
            
            if (storageService.declareSameness(id, alterEgo, sources))
            {
                String key = String.format("%s:%s", prefix, username);
                
                return new URI(getSamenessURI(key, uid));
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
    public boolean refuseSameness(Integer person, Integer alterEgo)
            throws PersonNotFoundException
    {
        return storageService.refuseSameness(person, alterEgo);
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
        Relation obj = getRelation(relation);

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

        JSONObject relation = getRelation(rel);

        return relation;
    }

    /**
     * 
     * @param id
     * @param fields
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrieveRelations(Integer id, String fields)
            throws PersonNotFoundException, JSONException
    {
        Person person = storageService.retrievePerson(id);
        List<Relation> list = storageService.retrieveRelations(id);

        // TODO - Add filtering.

        String uri = getRelationsURI(getPersonURI(person.getKey()));

        JSONObject relations = getRelations(uri, list);

        return relations;
    }

    /**
     * 
     * @param prefix
     * @param username
     * @param fields
     * @return
     * @throws PersonNotFoundException
     * @throws JSONException 
     */
    public JSONObject retrieveRelations(String prefix, String username,
            String fields)
            throws PersonNotFoundException, JSONException
    {
        Person person = storageService.retrievePerson(prefix, username);
        List<Relation> list = storageService.retrieveRelations(prefix, username);

        // TODO - Add filtering.

        String uri = getRelationsURI(getPersonURI(person.getId()));

        JSONObject relations = getRelations(uri, list);

        return relations;
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

    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    /**
     * 
     * @param type
     * @return
     * @throws JSONException 
     */
    private JSONObject getContext(int type) throws JSONException
    {
        JSONObject context = new JSONObject();

        //context.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        //context.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        //context.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        //context.put("dc", "http://purl.org/dc/elements/1.1/");
        //context.put("grddl", "http://www.w3.org/2003/g/data-view#");

        // FOAF

        if (type == CONTEXT_PERSON || type == CONTEXT_PERSONS || type == CONTEXT_RELATION)
        {
            context.put("foaf", "http://xmlns.com/foaf/0.1/");
        }

        if (type == CONTEXT_PERSON)
        {
            JSONObject homepage = new JSONObject();

            homepage.put("@id", "http://xmlns.com/foaf/0.1/homepage");
            homepage.put("@type", "@id");

            context.put(foafHomepage, homepage);
        }

        // SIOC

        if (type == CONTEXT_PERSON || type == CONTEXT_RELATION)
        {
            context.put("sioc", "http://rdfs.org/sioc/ns#");

            JSONObject note = new JSONObject();

            note.put("@id", "http://rdfs.org/sioc/ns#note");
            note.put("@container", "@list");

            context.put(siocNote, note);
        }

        // REL

        if (type == CONTEXT_PERSON || type == CONTEXT_RELATION || type == CONTEXT_RELATIONS)
        {
            context.put("rel", "http://purl.org/vocab/relationship/");
        }

        // OWL

        if (type == CONTEXT_PERSON)
        {
            context.put("owl", "http://www.w3.org/2002/07/owl#");
        }

        return context;
    }

    /**
     * 
     * @param id
     * @return 
     */
    private String getPersonURI(Integer id)
    {
        String uri = String.format("%s%s%s/%d", ROOT_URI, API_V1_URI,
                API_V1_PERSONS, id);

        return uri;
    }

    /**
     * 
     * @param key
     * @return 
     */
    private String getPersonURI(Key key)
    {
        String uri = String.format("%s%s%s/%s:%s", ROOT_URI, API_V1_URI,
                API_V1_PERSONS, key.getPrefix(), key.getUsername());

        return uri;
    }

    /**
     * 
     * @param id
     * @return 
     */
    private String getRelationURI(Integer id)
    {
        String uri = String.format("%s%s%s/%d", ROOT_URI, API_V1_URI,
                API_V1_RELATIONS, id);

        return uri;
    }

    /**
     * 
     * @param personURI
     * @return 
     */
    private String getRelationsURI(String personURI)
    {
        String uri = String.format("%s%s", personURI, API_V1_RELATIONS);

        return uri;
    }
    
    /**
     * 
     * @param person
     * @param alterEgo
     * @return 
     */
    private String getSamenessURI(String person, String alterEgo)
    {
        String uri = String.format("%s%s%s/%s/sameas/%s", ROOT_URI, API_V1_URI,
                API_V1_PERSONS, person, alterEgo);

        return uri;   
    }

    /**
     * 
     * @param source
     * @return 
     */
    private String getSourceURI(String source)
    {
        String uri = String.format("%s%s%s/%s", ROOT_URI, API_V1_URI,
                API_V1_SOURCES, source);

        return uri;
    }

    private Person getPerson(JSONObject person) throws JSONException, URISyntaxException
    {
        Person obj = new Person();

        // Retrieve profile URI

        if (person.has(foafHomepage))
        {
            URI profile = new URI(person.getString(foafHomepage));

            obj.setProfile(profile);

            person.remove(foafHomepage);
        }

        // Retrieve sources of the node information

        if (person.has(siocNote))
        {
            List<URI> sources = getSources(person);

            obj.setSources(sources);

            person.remove(siocNote);
        }

        // Retrieve other information about person

        if (person.length() > 0)
        {
            Map<String, String> properties = new HashMap<String, String>();

            for (Iterator it = person.keys() ; it.hasNext() ;)
            {
                String key = (String) it.next();

                properties.put(key, person.getString(key));
            }

            obj.setProperties(properties);
        }

        return obj;
    }

    /**
     * 
     * @param person
     * @param uri
     * @param sameAs
     * @return
     * @throws JSONException 
     */
    private JSONObject getPerson(Person person, String uri,
            Collection<String> sameAs)
            throws JSONException
    {
        JSONObject per = new JSONObject();

        per.put("@context", getContext(CONTEXT_PERSON));
        per.put("@type", foafPerson);
        per.put("@id", uri);

        per.put(foafHomepage, person.getProfile());
        per.put(siocNote, new JSONArray(person.getSources()));

        if (person.getProperties() != null)
        {
            for (String key : person.getProperties().keySet())
            {
                per.put(key, person.getProperties().get(key));
            }
        }

        per.put(relParticipantIn, getRelationsURI(uri));
        per.put(owlSameAs, new JSONArray(sameAs));

        return per;
    }

    /**
     * 
     * @param persons
     * @param uri
     * @return
     * @throws JSONException 
     */
    private JSONObject getPersons(Collection<Person> persons, String uri)
            throws JSONException
    {
        JSONObject pers = new JSONObject();

        pers.put("@context", getContext(CONTEXT_PERSONS));
        pers.put("@id", uri);

        for (Person per : persons)
        {
            JSONObject person = new JSONObject();

            person.put("@id", getPersonURI(per.getId()));
            person.put("@type", foafPerson);

            pers.accumulate("@graph", person);
        }

        return pers;

    }

    /**
     * 
     * @param relation
     * @return
     * @throws JSONException
     * @throws PersonNotFoundException
     * @throws InvalidRelationshipException
     * @throws URISyntaxException 
     */
    private Relation getRelation(JSONObject relation) throws JSONException, PersonNotFoundException, InvalidRelationshipException, URISyntaxException
    {
        Relation obj = new Relation();

        // Retrieve profile URI

        if (relation.has(relParticipant))
        {
            JSONObject participant = relation.getJSONObject(relParticipant);

            if (participant.has("@id"))
            {
                String object = participant.getString("@id");
                String uid = object.substring(object.lastIndexOf("/") + 1);

                if (NumberUtils.isInt(uid))
                {
                    obj.setObject(new Integer(uid));
                }
                else
                {
                    String[] key = uid.split(":");

                    if (key.length != 2)
                    {
                        // TODO - Add exception message.
                        throw new InvalidRelationshipException();
                    }

                    Person person = storageService.retrievePerson(key[0], key[1]);

                    obj.setObject(person.getId());
                }

                participant.remove("@id");
            }

            for (Iterator it = participant.keys() ; it.hasNext() ;)
            {
                String type = (String) it.next();

                obj.setType(type);

                participant = participant.getJSONObject(type);

                if (participant.has("@id"))
                {
                    String subject = participant.getString("@id");
                    String uid = subject.substring(subject.lastIndexOf("/") + 1);

                    if (NumberUtils.isInt(uid))
                    {
                        obj.setSubject(new Integer(uid));
                    }
                    else
                    {
                        String[] key = uid.split(":");

                        if (key.length != 2)
                        {
                            // TODO - Add exception message.
                            throw new InvalidRelationshipException();
                        }

                        Person person = storageService.retrievePerson(key[0],
                                key[1]);

                        obj.setSubject(person.getId());
                    }
                }

                break;
            }

            relation.remove(relParticipant);
        }

        // Retrieve sources of the node information

        if (relation.has(siocNote))
        {
            List<URI> sources = getSources(relation);

            obj.setSources(sources);

            relation.remove(siocNote);
        }

        // Retrieve other information about person

        if (relation.length() > 0)
        {
            Map<String, String> properties = new HashMap<String, String>();

            for (Iterator it = relation.keys() ; it.hasNext() ;)
            {
                String key = (String) it.next();

                properties.put(key, relation.getString(key));
            }

            obj.setProperties(properties);
        }

        return obj;
    }

    /**
     * 
     * @param relation
     * @return
     * @throws JSONException 
     */
    private JSONObject getRelation(Relation relation) throws JSONException
    {
        JSONObject type = new JSONObject();

        type.put("@id", getPersonURI(relation.getSubject()));
        type.put("@type", foafPerson);

        JSONObject participant = new JSONObject();

        participant.put("@id", getPersonURI(relation.getObject()));
        participant.put("@type", foafPerson);
        participant.put(relation.getType(), type);

        JSONObject rel = new JSONObject();

        rel.put("@context", getContext(CONTEXT_RELATION));
        rel.put("@id", getRelationURI(relation.getId()));
        rel.put("@type", relRelationship);
        rel.put(relParticipant, participant);
        rel.put(siocNote, new JSONArray(relation.getSources()));

        if (relation.getProperties() != null)
        {
            for (String key : relation.getProperties().keySet())
            {
                rel.put(key, relation.getProperties().get(key));
            }
        }

        return rel;
    }

    /**
     * 
     * @param uri
     * @param relations
     * @return
     * @throws JSONException 
     */
    private JSONObject getRelations(String uri, Collection<Relation> relations)
            throws JSONException
    {
        JSONObject rels = new JSONObject();

        rels.put("@context", getContext(CONTEXT_RELATIONS));
        rels.put("@id", uri);

        for (Relation rel : relations)
        {
            String relUri = getRelationURI(rel.getId());

            JSONObject relation = new JSONObject();

            relation.put("@id", relUri);
            relation.put("@type", relRelationship);

            rels.accumulate("@graph", relation);
        }

        return rels;
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
            JSONArray array = new JSONArray(object.getString(siocNote));

            for (int i = 0 ; i < array.length() ; i++)
            {
                URI source = new URI(array.getString(i));

                sources.add(source);
            }
        }
        catch (JSONException ex)
        {
            URI source = new URI(object.getString(siocNote));

            if (source.getHost() == null)
            {
                throw new URISyntaxException(object.getString(siocNote),
                        "Protocol must be specified.");
            }

            sources.add(source);
        }

        return sources;
    }// </editor-fold>
}
