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

    public static final String DEPTH_FIRST = "depth_first";
    public static final String BREADTH_FIRST = "breadth_first";
    
    public static final String NODE_GLOBAL = "node_global";
    public static final String NONE = "none";
    public static final String RELATIONSHIP_GLOBAL = "relationship_global";
    public static final String NODE_PATH = "node_path";
    public static final String RELATIONSHIP_PATH = "relationship_path";
    
    public static final String ALL = "all";
    public static final String ALL_BUT_START_NODE = "all_but_start_node";
    
    private String order;
    private String uniqueness;
    private int maxDepth;
    private String returnFilter;
    private List<Relationship> relationships;

    public TraversalDescription()
    {
        uniqueness = NODE_GLOBAL;
        maxDepth = 1;
        returnFilter = ALL;
        order = DEPTH_FIRST;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getUniqueness()
    {
        return uniqueness;
    }

    public void setUniqueness(String uniqueness)
    {
        this.uniqueness = uniqueness;
    }

    public int getMaxDepth()
    {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth)
    {
        this.maxDepth = maxDepth;
    }

    public String getReturnFilter()
    {
        return returnFilter;
    }

    public void setReturnFilter(String returnFilter)
    {
        this.returnFilter = returnFilter;
    }

    public List<Relationship> getRelationships()
    {
        return relationships;
    }

    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }

    public void addRelationship(String type, DirectionEnum direction)
    {
        if (relationships == null)
        {
            relationships = new ArrayList<Relationship>();
        }
        
        Relationship relation = new Relationship(type, direction);
        
        relationships.add(relation);
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

        if (relationships != null)
        {
            for (int i = 0 ; i < relationships.size() ; i++)
            {
                json.append("relationships", relationships.get(i).toJson());
            }
        }

        JSONObject filter = new JSONObject();

        filter.put("language", "builtin");
        filter.put("name", returnFilter);

        json.put("return_filter", filter);

        json.put("max_depth", maxDepth);

        return json;
    }
}
