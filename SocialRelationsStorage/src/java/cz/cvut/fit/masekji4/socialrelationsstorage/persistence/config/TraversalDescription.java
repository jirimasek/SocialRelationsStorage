package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Relationship;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Třída <code>TraversalDescription</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public final class TraversalDescription
{

    public static final String DEPTH_FIRST = "depth first";
    public static final String NODE = "node";
    public static final String ALL = "all";
    private String uniqueness = NODE;
    private int maxDepth = 1;
    private String returnFilter = ALL;
    private String order = DEPTH_FIRST;
    private List<Relationship> relationships = new ArrayList<Relationship>();

    public void setOrder(String order)
    {
        this.order = order;
    }

    public void setUniqueness(String uniqueness)
    {
        this.uniqueness = uniqueness;
    }

    public void setMaxDepth(int maxDepth)
    {
        this.maxDepth = maxDepth;
    }

    public void setReturnFilter(String returnFilter)
    {
        this.returnFilter = returnFilter;
    }

    public void setRelationships(Relationship relation)
    {
        this.relationships = Arrays.asList(relation);
    }

    /**
     * Serializuje entitu do formátu JSON.
     * 
     * @return      JSON
     */
    public JSONObject toJson() throws JSONException
    {
        JSONObject json = new JSONObject();

        json.put("order", order);
        json.put("uniqueness", uniqueness);

        for (int i = 0 ; i < relationships.size() ; i++)
        {
            json.put("relationships", relationships.get(i).toJson());
        }

        JSONObject filter = new JSONObject();
        
        filter.put("language", "builtin");
        filter.put("name", returnFilter);

        json.put("return filter", filter);

        json.put("max depth", maxDepth);

        return json;
    }
}
