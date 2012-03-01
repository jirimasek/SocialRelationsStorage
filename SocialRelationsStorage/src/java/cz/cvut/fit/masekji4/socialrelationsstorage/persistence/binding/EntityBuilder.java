package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.binding;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Node;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Relation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Třída <code>EntityBuilder</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Singleton
public class EntityBuilder
{

    /**
     * 
     * @param json
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    public Node getNode(String json) throws JSONException, URISyntaxException
    {
        List<Node> nodes = getNodes(json);

        if (!nodes.isEmpty())
        {
            return nodes.get(0);
        }

        return null;
    }

    /**
     * 
     * @param json
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    public List<Node> getNodes(String json) throws JSONException, URISyntaxException
    {
        JSONArray jsonArray = new JSONArray(json);

        List<Node> nodes = new ArrayList<Node>();

        for (int i = 0 ; i < json.length() ; i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            JSONObject data = obj.getJSONObject("data");

            URI uri = new URI((String) obj.get("self"));

            Map<String, String> properties = new HashMap<String, String>();

            for (Iterator it = data.keys() ; it.hasNext() ;)
            {
                String key = (String) it.next();

                Object property = data.get(key);

                if (property instanceof String)
                {
                    properties.put(key, (String) property);
                }
            }

            Node node = new Node();

            node.setUri(uri);
            node.setProperties(properties);

            nodes.add(node);
        }

        return nodes;
    }

    /**
     * 
     * @param json
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    public List<Relation> getRelations(String json) throws JSONException, URISyntaxException
    {
        JSONArray relations = new JSONArray(json);

        List<Relation> output = new ArrayList<Relation>();

        for (int i = 0 ; i < relations.length() ; i++)
        {
            JSONObject relation = relations.getJSONObject(i);

            Relation r = new Relation();

            r.setSelf(new URI(relation.getString("self")));
            r.setType(relation.getString("type"));
            r.setStart(new URI(relation.getString("start")));
            r.setEnd(new URI(relation.getString("end")));

            JSONObject data = relation.getJSONObject("data");

            if (data.length() > 0)
            {
                Map<String, String> properties = new HashMap<String, String>();

                for (Iterator it = data.keys() ; it.hasNext() ;)
                {
                    String key = (String) it.next();

                    Object property = data.get(key);

                    if (property instanceof String)
                    {
                        properties.put(key, (String) property);
                    }
                }

                r.setProperties(properties);
            }

            output.add(r);
        }

        return output;
    }
}
