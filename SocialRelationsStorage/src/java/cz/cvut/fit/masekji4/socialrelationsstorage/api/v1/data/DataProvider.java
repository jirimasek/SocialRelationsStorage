package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
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
    private StorageService storageService;

    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
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
    public JSONObject retrievePerson(String prefix, String username, String fields)
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
     * @param source
     * @return
     * @throws JSONException 
     */
    public JSONObject retrievePersons(String source) throws JSONException
    {
        List<Person> list = storageService.retrievePersons("http://" + source);
        
        String uri = String.format("%s%s%s/%s", ROOT_URI, API_V1_URI, API_V1_PERSONS, source);
        
        JSONObject persons = getPersons(list, uri);
            
        return persons;
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
     *                               REALTIONS                                *
     * ********************************************************************** */
    
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
    public JSONObject retrieveRelations(String prefix, String username, String fields)
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
        
            context.put("foaf:homepage", homepage);
        }
        
        // SIOC
        
        if (type == CONTEXT_PERSON || type == CONTEXT_RELATION)
        {
            context.put("sioc", "http://rdfs.org/sioc/ns#");
        
            JSONObject note = new JSONObject();
        
            note.put("@id", "http://rdfs.org/sioc/ns#note");
            note.put("@container", "@list");
        
            context.put("sioc:note", note);
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
        String uri = String.format("%s%s%s/%d", ROOT_URI, API_V1_URI, API_V1_PERSONS, id);
        
        return uri;
    }
    
    /**
     * 
     * @param key
     * @return 
     */
    private String getPersonURI(Key key)
    {
        String uri = String.format("%s%s%s/%s:%s", ROOT_URI, API_V1_URI, API_V1_PERSONS, key.getPrefix(), key.getUsername());
        
        return uri;
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    private String getRelationURI(Integer id)
    {
        String uri = String.format("%s%s%s/%d", ROOT_URI, API_V1_URI, API_V1_RELATIONS, id);
        
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
     * @param uri
     * @param sameAs
     * @return
     * @throws JSONException 
     */
    private JSONObject getPerson(Person person, String uri, Collection<String> sameAs)
            throws JSONException
    {
        JSONObject per = new JSONObject();
        
        per.put("@context", getContext(CONTEXT_PERSON));
        per.put("@type", "foaf:Person");
        per.put("@id", uri);
        
        per.put("foaf:homepage", person.getProfile());
        per.put("sioc:note", new JSONArray(person.getSources()));

        if (person.getProperties() != null)
        {
            for (String key : person.getProperties().keySet())
            {
                per.put(key, person.getProperties().get(key));
            }
        }
        
        per.put("rel:participantIn", getRelationsURI(uri));
        per.put("owl:sameAs", new JSONArray(sameAs));
        
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
            person.put("@type", "foaf:Person");
            
            pers.accumulate("@graph", person);
        }
        
        return pers;
        
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
        type.put("@type", "foaf:Person");
        
        JSONObject participant = new JSONObject();
        
        participant.put("@id", getPersonURI(relation.getObject()));
        participant.put("@type", "foaf:Person");
        participant.put(relation.getType(), type);
        
        JSONObject rel = new JSONObject();
        
        rel.put("@context", getContext(CONTEXT_RELATION));
        rel.put("@id", getRelationURI(relation.getId()));
        rel.put("@type", "rel:Relationship");
        rel.put("rel:participant", participant);
        rel.put("sioc:note", new JSONArray(relation.getSources()));

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
            relation.put("@type", "rel:Relationship");
            
            rels.accumulate("@graph", relation);
        }
        
        return rels;
    }// </editor-fold>
}
