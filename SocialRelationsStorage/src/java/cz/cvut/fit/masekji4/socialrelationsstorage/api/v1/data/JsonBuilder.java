package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data;

import cz.cvut.fit.masekji4.socialrelationsstorage.common.NumberUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.StringUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Class <code>JsonBuilder</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class JsonBuilder
{
    
    private static int CONTEXT_PERSON = 1;
    private static int CONTEXT_PERSONS = 2;
    private static int CONTEXT_RELATION = 3;
    private static int CONTEXT_RELATIONS = 4;
    
    @Inject
    @Config
    private String FOAF_NS;
    @Inject
    @Config
    private String FOAF_URI;
    @Inject
    @Config
    private String FOAF_HOMEPAGE;
    @Inject
    @Config
    private String FOAF_PERSON;
    @Inject
    @Config
    private String SIOC_NS;
    @Inject
    @Config
    private String SIOC_URI;
    @Inject
    @Config
    private String SIOC_NOTE;
    @Inject
    @Config
    private String OWL_NS;
    @Inject
    @Config
    private String OWL_URI;
    @Inject
    @Config
    private String OWL_SAME_AS;
    @Inject
    @Config
    private String REL_NS;
    @Inject
    @Config
    private String REL_URI;
    @Inject
    @Config
    private String REL_PARTICIPANT;
    @Inject
    @Config
    private String PEL_PARTICIPANT_IN;
    @Inject
    @Config
    private String REL_RELATIONSHIP;
    @Inject
    @Config
    private String JSON_LD_ID;
    @Inject
    @Config
    private String JSON_LD_CONTEXT;
    @Inject
    @Config
    private String JSON_LD_TYPE;
    @Inject
    @Config
    private String JSON_LD_GRAPH;
    @Inject
    @Config
    private String JSON_LD_CONTAINER;
    @Inject
    @Config
    private String JSON_LD_LIST;
    
    @Inject
    private UriBuilder uriBuilder;

    /**
     * 
     * @param person
     * @param uid
     * @param sameAs
     * @return
     * @throws JSONException 
     */
    public JSONObject getPerson(Person person, String uid,
            Collection<String> sameAs) throws JSONException
    {
        boolean numeric = NumberUtils.isInt(uid);
        
        String nuid = numeric ? person.getKey().toString() : person.getId().toString();
        
        String uri = uriBuilder.getPersonURI(nuid);
        
        JSONObject obj = new JSONObject();

        obj.put(JSON_LD_CONTEXT, getContext(CONTEXT_PERSON));
        obj.put(JSON_LD_TYPE, FOAF_PERSON);
        obj.put(JSON_LD_ID, uri);

        obj.put(FOAF_HOMEPAGE, person.getProfile());
        obj.put(SIOC_NOTE, new JSONArray(person.getSources()));

        if (person.getProperties() != null)
        {
            for (String key : person.getProperties().keySet())
            {
                obj.put(key, person.getProperties().get(key));
            }
        }

        obj.put(PEL_PARTICIPANT_IN, uriBuilder.getRelationsURI(nuid, null, null));
        
        for (String alterego : sameAs)
        {
            obj.accumulate(OWL_SAME_AS, uriBuilder.getPersonURI(alterego));
        }

        return obj;
    }
    
    /**
     * 
     * @param persons
     * @return
     * @throws JSONException 
     */
    public JSONObject getPersons(Collection<Person> persons)
            throws JSONException
    {
        return getPersons(persons, null);
    }

    /**
     * 
     * @param persons
     * @param source
     * @return
     * @throws JSONException 
     */
    public JSONObject getPersons(Collection<Person> persons, String source)
            throws JSONException
    {   
        String uri;
        
        if (StringUtils.isNullOrEmpty(source))
        {
            uri = uriBuilder.getPersonsURI();
        }
        else
        {
            uri = uriBuilder.getSourceURI(source);
        }
        
        JSONObject obj = new JSONObject();

        obj.put(JSON_LD_CONTEXT, getContext(CONTEXT_PERSONS));
        obj.put(JSON_LD_ID, uri);

        for (Person person : persons)
        {
            JSONObject o = new JSONObject();

            o.put(JSON_LD_ID, uriBuilder.getPersonURI(person.getId().toString()));
            o.put(JSON_LD_TYPE, FOAF_PERSON);

            obj.accumulate(JSON_LD_GRAPH, o);
        }

        return obj;
    }

    /**
     * 
     * @param relation
     * @return
     * @throws JSONException 
     */
    public JSONObject getRelation(Relation relation) throws JSONException
    {
        JSONObject type = new JSONObject();

        type.put(JSON_LD_ID, uriBuilder.getPersonURI(relation.getSubject().toString()));
        type.put(JSON_LD_TYPE, FOAF_PERSON);

        JSONObject participant = new JSONObject();

        participant.put(JSON_LD_ID, uriBuilder.getPersonURI(relation.getObject().toString()));
        participant.put(JSON_LD_TYPE, FOAF_PERSON);
        participant.put(relation.getType(), type);

        JSONObject rel = new JSONObject();

        rel.put(JSON_LD_CONTEXT, getContext(CONTEXT_RELATION));
        rel.put(JSON_LD_ID, uriBuilder.getRelationURI(relation.getId()));
        rel.put(JSON_LD_TYPE, REL_RELATIONSHIP);
        rel.put(REL_PARTICIPANT, participant);
        rel.put(SIOC_NOTE, new JSONArray(relation.getSources()));

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
     * @param relations
     * @param object
     * @return
     * @throws JSONException 
     */
    public JSONObject getRelations(Collection<Relation> relations, String object)
            throws JSONException
    {
        return getRelations(relations, object, null, null);
    }
    
    /**
     * 
     * @param relations
     * @param object
     * @param type
     * @return
     * @throws JSONException 
     */
    public JSONObject getRelations(Collection<Relation> relations, String object, String type)
            throws JSONException
    {
        return getRelations(relations, object, type, null);
    }
    
    /**
     * 
     * @param relations
     * @param object
     * @param type
     * @param subject
     * @return
     * @throws JSONException 
     */
    public JSONObject getRelations(Collection<Relation> relations, String object, String type, String subject)
            throws JSONException
    {
        String uri = uriBuilder.getRelationsURI(object, type, subject);
        
        JSONObject rels = new JSONObject();

        rels.put(JSON_LD_CONTEXT, getContext(CONTEXT_RELATIONS));
        rels.put(JSON_LD_ID, uri);

        for (Relation rel : relations)
        {
            String relUri = uriBuilder.getRelationURI(rel.getId());

            JSONObject relation = new JSONObject();

            relation.put(JSON_LD_ID, relUri);
            relation.put(JSON_LD_TYPE, REL_RELATIONSHIP);

            rels.accumulate(JSON_LD_GRAPH, relation);
        }

        return rels;
    }
    
    /**
     * 
     * @param sources
     * @return
     * @throws JSONException 
     */
    public JSONObject getSources(List<URI> sources) throws JSONException
    {   
        
        JSONObject obj = new JSONObject();

        obj.put(JSON_LD_CONTEXT, getContext(CONTEXT_PERSONS));
        obj.put(JSON_LD_ID, uriBuilder.getSourcesURI());

        for (URI uri : sources)
        {
            JSONObject o = new JSONObject();

            o.put(JSON_LD_ID, uriBuilder.getSourceURI(uri.getHost()));

            obj.accumulate(JSON_LD_GRAPH, o);
        }

        return obj;
    }

    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    /* ********************************************************************** *
     *                            ACESSOR METHODS                             *
     * ********************************************************************** */
    /**
     * 
     * @param type
     * @return
     * @throws JSONException 
     */
    private JSONObject getContext(int type) throws JSONException
    {
        JSONObject context = new JSONObject();

        // FOAF

        if (type == CONTEXT_PERSON || type == CONTEXT_PERSONS || type == CONTEXT_RELATION)
        {
            context.put(FOAF_NS, FOAF_URI);
        }

        if (type == CONTEXT_PERSON)
        {
            JSONObject homepage = new JSONObject();

            homepage.put(JSON_LD_ID, String.format("%s%s", FOAF_URI, "homepage"));
            homepage.put(JSON_LD_TYPE, JSON_LD_ID);

            context.put(FOAF_HOMEPAGE, homepage);
        }

        // SIOC

        if (type == CONTEXT_PERSON || type == CONTEXT_RELATION)
        {
            context.put(SIOC_NS, SIOC_URI);

            JSONObject note = new JSONObject();

            note.put(JSON_LD_ID, String.format("%s%s", SIOC_URI, "note"));
            note.put(JSON_LD_CONTAINER, JSON_LD_LIST);

            context.put(SIOC_NOTE, note);
        }

        // REL

        if (type == CONTEXT_PERSON || type == CONTEXT_RELATION || type == CONTEXT_RELATIONS)
        {
            context.put(REL_NS, REL_URI);
        }

        // OWL

        if (type == CONTEXT_PERSON)
        {
            context.put(OWL_NS, OWL_URI);
        }

        return context;
    }// </editor-fold>
}
