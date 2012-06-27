package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.data;

import cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.StorageService;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.NumberUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * <code>ObjectBuilder</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class ObjectBuilder
{
    
    @Inject
    @Config
    private String FOAF_HOMEPAGE;
    @Inject
    @Config
    private String SIOC_NOTE;
    @Inject
    @Config
    private String OWL_SAME_AS;
    @Inject
    @Config
    private String REL_PARTICIPANT;
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
    private StorageService storageService;
    
    public Person getPerson(JSONObject person) throws JSONException, URISyntaxException
    {
        Person obj = new Person();

        // Remove @id attribute

        person.remove(JSON_LD_ID);

        // Remove @context attribute

        person.remove(JSON_LD_CONTEXT);

        // Remove @type attribute

        person.remove(JSON_LD_TYPE);

        // Remove owl:sameAs

        person.remove(OWL_SAME_AS);

        // Retrieve profile URI

        if (person.has(FOAF_HOMEPAGE))
        {
            URI profile = new URI(person.getString(FOAF_HOMEPAGE));

            obj.setProfile(profile);

            person.remove(FOAF_HOMEPAGE);
        }

        // Retrieve sources of the node information

        if (person.has(SIOC_NOTE))
        {
            List<URI> sources = getSources(person);

            obj.setSources(sources);

            person.remove(SIOC_NOTE);
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

    public Relation getRelation(JSONObject relation) throws JSONException, PersonNotFoundException, InvalidRelationshipException, URISyntaxException
    {
        Relation obj = new Relation();

        // Remove @id attribute

        relation.remove(JSON_LD_ID);

        // Remove @context attribute

        relation.remove(JSON_LD_CONTEXT);

        // Remove @type attribute

        relation.remove(JSON_LD_TYPE);

        // Retrieve profile URI

        if (relation.has(REL_PARTICIPANT))
        {
            JSONObject participant = relation.getJSONObject(REL_PARTICIPANT);

            if (participant.has(JSON_LD_ID))
            {
                String object = participant.getString(JSON_LD_ID);
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

                participant.remove(JSON_LD_ID);
            }

            for (Iterator it = participant.keys() ; it.hasNext() ;)
            {
                String type = (String) it.next();

                obj.setType(type);

                participant = participant.getJSONObject(type);

                if (participant.has(JSON_LD_ID))
                {
                    String subject = participant.getString(JSON_LD_ID);
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

            relation.remove(REL_PARTICIPANT);
        }

        // Retrieve sources of the node information

        if (relation.has(SIOC_NOTE))
        {
            List<URI> sources = getSources(relation);

            obj.setSources(sources);

            relation.remove(SIOC_NOTE);
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

    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
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
