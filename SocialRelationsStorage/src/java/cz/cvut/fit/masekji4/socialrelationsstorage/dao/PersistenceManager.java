package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.TypeEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidMetadataException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.MetadataNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeIndexNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.PropertyNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * <code>PersistenceManager</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface PersistenceManager
{
    
    // <editor-fold defaultstate="collapsed" desc="Nodes">
    public Integer createNode();
    
    public Integer createNode(JSONObject properties) throws InvalidPropertiesException;

    public JSONObject retrieveNode(Integer node) throws NodeNotFoundException;
    
    public boolean deleteNode(Integer node) throws CannotDeleteNodeException;
    
    public void addProperties(Integer node, JSONObject properties)
            throws InvalidPropertiesException, NodeNotFoundException;
    
    public JSONObject retrieveProperties(Integer node)
            throws NodeNotFoundException;
    
    public String retrieveProperty(Integer node, String property)
            throws PropertyNotFoundException;
    
    public boolean deleteProperty(Integer node, String property)
            throws PropertyNotFoundException;
    
    public void deleteProperties(Integer node) throws NodeNotFoundException;// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Edges">
    public Integer createRelationship(Integer startNode, Integer endNode,
            String relationship) throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException;
    
    public Integer createRelationship(Integer startNode, Integer endNode,
            String relationship, JSONObject metadata)
            throws InvalidMetadataException, InvalidRelationshipException,
            JSONException, NodeNotFoundException;
    
    public JSONObject retrieveRelationship(Integer relationship)
            throws RelationshipNotFoundException;
    
    public JSONArray retrieveRelationships(Integer node, DirectionEnum direction)
        throws NodeNotFoundException;
    
    public JSONArray retrieveRelationships(Integer node, DirectionEnum direction,
            String relationship)
        throws NodeNotFoundException;
    
    public boolean deleteRelationship(Integer relationship);
    
    public void addMetadataToRelationship(Integer relationship, JSONObject metadata)
        throws InvalidMetadataException, RelationshipNotFoundException;
    
    public JSONObject retrieveRelationshipMetadata(Integer relationship)
            throws RelationshipNotFoundException;
    
    public String retrieveRelationshipMetadata(Integer relationship, String metadata)
            throws MetadataNotFoundException;
    
    public boolean deleteRelationshipMetadata(Integer relationship, String metadata);
    
    public void deleteRelationshipMetadata(Integer relationship)
            throws RelationshipNotFoundException;// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Traversals">
    public JSONArray traverse(Integer startNode, TraversalDescription t, TypeEnum type)
            throws JSONException, NodeNotFoundException;// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Indexes">
    public void createNodeIndex(String name) throws JSONException;
    
    public void deleteNodeIndex(String name);
    
    public JSONObject retrieveListOfNodeIndexes();
    
    public void addNodeToIndex(String name, String key, String value, Integer node)
            throws JSONException, NodeIndexNotFoundException;
    
    public void addNodeToIndex(String name, String key, String value, Integer node, boolean unique)
            throws JSONException, NodeIndexNotFoundException;
    
    public JSONObject retrieveNodeFromIndex(String name, String key, String value)
            throws JSONException, NodeIndexNotFoundException;
    
    public JSONArray retrieveNodesFromIndex(String name, String key, String value)
            throws JSONException, NodeIndexNotFoundException;
    
    public void deleteNodeFromIndex(String name, String key, String value, Integer node)
            throws NodeIndexNotFoundException;// </editor-fold>
}
