package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.entities;

import cz.cvut.fit.masekji4.socialrelationsstorage.common.CollecitonUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * <code>EntityFactory</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class EntityFactory
{

    @Inject
    @Config
    private String API_URI;

    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    /**
     * 
     * @param person
     * @return
     * @throws JSONException 
     */
    private JSONObject getPersonLinks(Person person, List<Person> alterEgos)
            throws JSONException
    {
        String self = String.format("%s/persons/%d", API_URI, person.getId());
        String relations = String.format("%s/persons/%d/relations", API_URI,
                person.getId());
        
        JSONObject links = new JSONObject();

        links.put("self", self);
        links.put("relations", relations);
        
        for (Person alterEgo : alterEgos)
        {
            String string = String.format("%s/persons/%s:%s", API_URI,
                    alterEgo.getKey().getPrefix(),
                    alterEgo.getKey().getUsername());

            links.accumulate("owl:sameAs", string);
        }

        return links;
    }// </editor-fold>

    /**
     * 
     * @param person
     * @param alterEgos
     * @return
     * @throws JSONException 
     */
    public JSONObject serialize(Person person, List<Person> alterEgos) throws JSONException
    {
        JSONObject p = new JSONObject();

        p.put("_links", getPersonLinks(person, alterEgos));

        p.put("foaf:homepage", person.getProfile());

        List<String> sources = new ArrayList<String>();

        for (URI source : person.getSources())
        {
            sources.add(source.toString());
        }

        p.put("sioc:note", new JSONArray(sources));

        if (person.getProperties() != null)
        {
            for (String key : person.getProperties().keySet())
            {
                p.put(key, person.getProperties().get(key));
            }
        }

        return p;
    }

    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */
    /* ********************************************************************** *
     *                               EXCEPTIONS                               *
     * ********************************************************************** */
    /**
     * 
     * @param th
     * @return
     * @throws JSONException 
     */
    public JSONObject serialize(Throwable th) throws JSONException
    {
        JSONObject message = new JSONObject();

        message.put("message", th.getMessage());

        StringBuilder sb = new StringBuilder();

        sb.append(th.getClass().getCanonicalName());

        if (th.getMessage() != null)
        {
            sb.append(":");
            sb.append(th.getMessage());
        }

        JSONArray stacktrace = new JSONArray();

        for (int i = 0 ; i < th.getStackTrace().length ; i++)
        {
            stacktrace.put(th.getStackTrace()[i].toString());
        }

        message.put("stacktrace", stacktrace);

        return message;
    }
}
