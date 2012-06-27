package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import java.util.List;

/**
 * <code>TraversalDescription</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface TraversalDescription
{

    public String getOrder();

    public void setOrder(String order);

    public String getUniqueness();

    public void setUniqueness(String uniqueness);

    public int getMaxDepth();

    public void setMaxDepth(int maxDepth);

    public String getReturnFilter();

    public void setReturnFilter(String returnFilter);

    public List<Relationship> getRelationships();

    public void setRelationships(List<Relationship> relationships);

    public void addRelationship(String type, DirectionEnum direction);
}
