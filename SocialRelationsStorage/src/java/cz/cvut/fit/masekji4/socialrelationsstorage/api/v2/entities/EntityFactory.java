package cz.cvut.fit.masekji4.socialrelationsstorage.api.v2.entities;

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
        
        JSONObject links = new JSONObject();
        
        links.put("self", String.format("/persons/%d", person.getId()));
        links.put("relations", String.format("/persons/%d/relations", person.getId()));
        
        p.put("_links", links);
        
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
        
        JSONArray owlSameAs = new JSONArray();
        
        for (Person alterEgo : alterEgos)
        {
            owlSameAs.put(String.format("%s/persons/%s:%s", API_URI,
                    alterEgo.getKey().getPrefix(), alterEgo.getKey().getUsername()));
        }
        
        if (owlSameAs.length() > 0)
        {
            p.put("owl:sameAs", owlSameAs);
        }
        
        return p;
    }

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
