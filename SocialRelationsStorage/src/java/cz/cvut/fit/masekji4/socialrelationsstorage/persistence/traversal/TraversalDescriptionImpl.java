package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.Relationship;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída <code>TraversalDescriptionImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public final class TraversalDescriptionImpl implements TraversalDescription
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

    /**
     * 
     */
    public TraversalDescriptionImpl()
    {
        uniqueness = NODE_GLOBAL;
        maxDepth = 1;
        returnFilter = ALL;
        order = DEPTH_FIRST;
    }

    /**
     * 
     * @return 
     */
    public String getOrder()
    {
        return order;
    }

    /**
     * 
     * @param order 
     */
    public void setOrder(String order)
    {
        this.order = order;
    }

    /**
     * 
     * @return 
     */
    public String getUniqueness()
    {
        return uniqueness;
    }

    /**
     * 
     * @param uniqueness 
     */
    public void setUniqueness(String uniqueness)
    {
        this.uniqueness = uniqueness;
    }

    /**
     * 
     * @return 
     */
    public int getMaxDepth()
    {
        return maxDepth;
    }

    /**
     * 
     * @param maxDepth 
     */
    public void setMaxDepth(int maxDepth)
    {
        this.maxDepth = maxDepth;
    }

    /**
     * 
     * @return 
     */
    public String getReturnFilter()
    {
        return returnFilter;
    }

    /**
     * 
     * @param returnFilter 
     */
    public void setReturnFilter(String returnFilter)
    {
        this.returnFilter = returnFilter;
    }

    /**
     * 
     * @return 
     */
    public List<Relationship> getRelationships()
    {
        return relationships;
    }

    /**
     * 
     * @param relationships 
     */
    public void setRelationships(List<Relationship> relationships)
    {
        this.relationships = relationships;
    }

    /**
     * 
     * @param type
     * @param direction 
     */
    public void addRelationship(String type, DirectionEnum direction)
    {
        if (relationships == null)
        {
            relationships = new ArrayList<Relationship>();
        }
        
        Relationship relation = new Relationship(type, direction);
        
        relationships.add(relation);
    }
}
