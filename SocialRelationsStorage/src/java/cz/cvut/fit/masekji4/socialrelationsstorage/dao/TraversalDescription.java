package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import java.util.List;

/**
 * Interface <code>TraversalDescription</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface TraversalDescription
{

    /**
     * 
     * @return 
     */
    public String getOrder();

    /**
     * 
     * @param order 
     */
    public void setOrder(String order);

    /**
     * 
     * @return 
     */
    public String getUniqueness();

    /**
     * 
     * @param uniqueness 
     */
    public void setUniqueness(String uniqueness);

    /**
     * 
     * @return 
     */
    public int getMaxDepth();

    /**
     * 
     * @param maxDepth 
     */
    public void setMaxDepth(int maxDepth);

    /**
     * 
     * @return 
     */
    public String getReturnFilter();

    /**
     * 
     * @param returnFilter 
     */
    public void setReturnFilter(String returnFilter);

    /**
     * 
     * @return 
     */
    public List<Relationship> getRelationships();

    /**
     * 
     * @param relationships 
     */
    public void setRelationships(List<Relationship> relationships);

    /**
     * 
     * @param type
     * @param direction 
     */
    public void addRelationship(String type, DirectionEnum direction);
}
