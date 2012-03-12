package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidMetadataException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.MetadataNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeIndexNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.PropertyNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import java.util.List;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Rozhraní <code>PersistenceManager</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface PersistenceManager
{
    
    /* ********************************************************************** *
     *                                  Uzly                                  *
     * ********************************************************************** */
    
    /**
     * 
     * @return
     */
    public int createNode();
    
    /**
     * 
     * @param properties
     * @return
     * @throws InvalidPropertiesException 
     */
    public int createNode(JSONObject properties) throws InvalidPropertiesException;

    /**
     * 
     * @param node
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONObject retrieveNode(int node) throws NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @return
     * @throws CannotDeleteNodeException 
     */
    public boolean deleteNode(int node) throws CannotDeleteNodeException;
    
    /**
     * 
     * @param node
     * @param properties 
     */
    public void addProperties(int node, JSONObject properties)
            throws InvalidPropertiesException, NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONObject retrieveProperties(int node)
            throws NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @param property
     * @return
     * @throws NodeNotFoundException
     * @throws PropertyNotFoundException 
     */
    public String retrieveProperty(int node, String property)
            throws PropertyNotFoundException;
    
    /**
     * 
     * @param node
     * @param property
     * @return
     * @throws NodeNotFoundException 
     */
    public boolean deleteProperty(int node, String property)
            throws PropertyNotFoundException;
    /**
     * 
     * @param node
     * @throws NodeNotFoundException 
     */
    public void deleteProperties(int node) throws NodeNotFoundException;
    
    /* ********************************************************************** *
     *                                  Hrany                                 *
     * ********************************************************************** */
    
    /**
     * 
     * @param startNode
     * @param endNode
     * @param relationship
     * @return
     * @throws InvalidPropertiesException
     * @throws NodeNotFoundException 
     */
    public int createRelationship(int startNode, int endNode,
            String relationship) throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException;
    
    /**
     * 
     * @param startNode
     * @param endNode
     * @param relationship
     * @param metadata
     * @return
     * @throws InvalidPropertiesException
     * @throws NodeNotFoundException 
     */
    public int createRelationship(int startNode, int endNode,
            String relationship, JSONObject metadata)
            throws InvalidMetadataException, InvalidRelationshipException,
            JSONException, NodeNotFoundException;
    
    /**
     * 
     * @param relationship
     * @return
     * @throws RelationshipNotFoundException 
     */
    public JSONObject retrieveRelationship(int relationship)
            throws RelationshipNotFoundException;
    
    /**
     * 
     * @param node
     * @param direction
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONArray retrieveRelationships(int node, DirectionEnum direction)
        throws NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @param direction
     * @param relationship
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONArray retrieveRelationships(int node, DirectionEnum direction,
            String relationship)
        throws NodeNotFoundException;
    
    /**
     * 
     * @param relationship
     * @return 
     */
    public boolean deleteRelationship(int relationship);
    
    /**
     * 
     * @param relationship
     * @param metadata
     * @throws InvalidMetadataException
     * @throws RelationshipNotFoundException 
     */
    public void addMetadataToRelationship(int relationship, JSONObject metadata)
        throws InvalidMetadataException, RelationshipNotFoundException;
    
    /**
     * 
     * @param relationship
     * @return
     * @throws RelationshipNotFoundException 
     */
    public JSONObject retrieveRelationshipMetadata(int relationship)
            throws RelationshipNotFoundException;
    
    /**
     * 
     * @param relationship
     * @param metadata
     * @return
     * @throws MetadataNotFoundException
     * @throws RelationshipNotFoundException 
     */
    public String retrieveRelationshipMetadata(int relationship, String metadata)
            throws MetadataNotFoundException;
    
    /**
     * 
     * @param relationship
     * @param metadata
     * @return 
     */
    public boolean deleteRelationshipMetadata(int relationship, String metadata);
    
    /**
     * 
     * @param relationship
     * @throws RelationshipNotFoundException 
     */
    public void deleteRelationshipMetadata(int relationship)
            throws RelationshipNotFoundException;
    
    /* ********************************************************************** *
     *                               Traversals                               *
     * ********************************************************************** */
    
    /**
     * 
     * @param <T>
     * @param start
     * @param t
     * @return
     * @throws NodeNotFoundException 
     */
    public <T> List<T> traverse(int start, TraversalDescription t)
            throws NodeNotFoundException; 
    
    /* ********************************************************************** *
     *                                Indexes                                 *
     * ********************************************************************** */          
    
    /**
     * 
     * @param name
     * @throws JSONException 
     */
    public void createNodeIndex(String name) throws JSONException;
    
    /**
     * 
     * @param nodeIndex 
     */
    public void deleteNodeIndex(String nodeIndex);
    
    /**
     * 
     * @return 
     */
    public JSONObject retrieveListOfNodeIndexes();
    
    /**
     * 
     * @param nodeIndex
     * @param key
     * @param value
     * @param node
     * @throws JSONException
     * @throws NodeAlreadyIndexedException 
     */
    public void addNodeToIndex(String nodeIndex, String key, String value, int node)
            throws JSONException;
    
    /**
     * 
     * @param nodeIndex
     * @param key
     * @param value
     * @return
     * @throws JSONException 
     */
    public JSONObject retrieveNodeFromIndex(String nodeIndex, String key, String value)
            throws JSONException;
    
    /**
     * 
     * @param nodeIndex
     * @param key
     * @param value
     * @throws NodeIndexNotFoundException 
     */
    public void deleteNodeFromIndex(String nodeIndex, int node, String key, String value)
            throws NodeIndexNotFoundException;
}
