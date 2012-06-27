package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import com.sun.jersey.api.client.UniformInterfaceException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.TraversalDescription;
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
    public Integer createNode();
    
    /**
     * 
     * @param properties
     * @return
     * @throws InvalidPropertiesException 
     */
    public Integer createNode(JSONObject properties) throws InvalidPropertiesException;

    /**
     * 
     * @param node
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONObject retrieveNode(Integer node) throws NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @return
     * @throws CannotDeleteNodeException 
     */
    public boolean deleteNode(Integer node) throws CannotDeleteNodeException;
    
    /**
     * 
     * @param node
     * @param properties 
     */
    public void addProperties(Integer node, JSONObject properties)
            throws InvalidPropertiesException, NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONObject retrieveProperties(Integer node)
            throws NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @param property
     * @return
     * @throws NodeNotFoundException
     * @throws PropertyNotFoundException 
     */
    public String retrieveProperty(Integer node, String property)
            throws PropertyNotFoundException;
    
    /**
     * 
     * @param node
     * @param property
     * @return
     * @throws NodeNotFoundException 
     */
    public boolean deleteProperty(Integer node, String property)
            throws PropertyNotFoundException;
    /**
     * 
     * @param node
     * @throws NodeNotFoundException 
     */
    public void deleteProperties(Integer node) throws NodeNotFoundException;
    
    /* ********************************************************************** *
     *                                  Hrany                                 *
     * ********************************************************************** */
    
    /**
     * 
     * @param startNode
     * @param endNode
     * @param relationship
     * @return
     * @throws InvalidMetadataException
     * @throws InvalidRelationshipException
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    public Integer createRelationship(Integer startNode, Integer endNode,
            String relationship) throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException;
    
    /**
     * 
     * @param startNode
     * @param endNode
     * @param relationship
     * @param metadata
     * @return
     * @throws InvalidMetadataException
     * @throws InvalidRelationshipException
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    public Integer createRelationship(Integer startNode, Integer endNode,
            String relationship, JSONObject metadata)
            throws InvalidMetadataException, InvalidRelationshipException,
            JSONException, NodeNotFoundException;
    
    /**
     * 
     * @param relationship
     * @return
     * @throws RelationshipNotFoundException 
     */
    public JSONObject retrieveRelationship(Integer relationship)
            throws RelationshipNotFoundException;
    
    /**
     * 
     * @param node
     * @param direction
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONArray retrieveRelationships(Integer node, DirectionEnum direction)
        throws NodeNotFoundException;
    
    /**
     * 
     * @param node
     * @param direction
     * @param relationship
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONArray retrieveRelationships(Integer node, DirectionEnum direction,
            String relationship)
        throws NodeNotFoundException;
    
    /**
     * 
     * @param relationship
     * @return 
     */
    public boolean deleteRelationship(Integer relationship);
    
    /**
     * 
     * @param relationship
     * @param metadata
     * @throws InvalidMetadataException
     * @throws RelationshipNotFoundException 
     */
    public void addMetadataToRelationship(Integer relationship, JSONObject metadata)
        throws InvalidMetadataException, RelationshipNotFoundException;
    
    /**
     * 
     * @param relationship
     * @return
     * @throws RelationshipNotFoundException 
     */
    public JSONObject retrieveRelationshipMetadata(Integer relationship)
            throws RelationshipNotFoundException;
    
    /**
     * 
     * @param relationship
     * @param metadata
     * @return
     * @throws MetadataNotFoundException
     * @throws RelationshipNotFoundException 
     */
    public String retrieveRelationshipMetadata(Integer relationship, String metadata)
            throws MetadataNotFoundException;
    
    /**
     * 
     * @param relationship
     * @param metadata
     * @return 
     */
    public boolean deleteRelationshipMetadata(Integer relationship, String metadata);
    
    /**
     * 
     * @param relationship
     * @throws RelationshipNotFoundException 
     */
    public void deleteRelationshipMetadata(Integer relationship)
            throws RelationshipNotFoundException;
    
    /* ********************************************************************** *
     *                               Traversals                               *
     * ********************************************************************** */
    
    /**
     * 
     * @param startNode
     * @param t
     * @param type
     * @return
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    public JSONArray traverse(Integer startNode, TraversalDescription t, TypeEnum type)
            throws JSONException, NodeNotFoundException; 
    
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
     * @param name 
     */
    public void deleteNodeIndex(String name);
    
    /**
     * 
     * @return
     */
    public JSONObject retrieveListOfNodeIndexes();
    
    /**
     * 
     * @param name
     * @param key
     * @param value
     * @param node
     * @throws JSONException
     * @throws NodeIndexNotFoundException 
     */
    public void addNodeToIndex(String name, String key, String value, Integer node)
            throws JSONException, NodeIndexNotFoundException;
    
    /**
     * 
     * @param name
     * @param key
     * @param value
     * @param node
     * @param unique
     * @throws JSONException
     * @throws NodeIndexNotFoundException 
     */
    public void addNodeToIndex(String name, String key, String value, Integer node, boolean unique)
            throws JSONException, NodeIndexNotFoundException;
    
    /**
     * 
     * @param name
     * @param key
     * @param value
     * @return
     * @throws JSONException
     * @throws NodeIndexNotFoundException 
     */
    public JSONObject retrieveNodeFromIndex(String name, String key, String value)
            throws JSONException, NodeIndexNotFoundException;
    
    /**
     * 
     * @param name
     * @param key
     * @param value
     * @return
     * @throws JSONException
     * @throws NodeIndexNotFoundException 
     */
    public JSONArray retrieveNodesFromIndex(String name, String key, String value)
            throws JSONException, NodeIndexNotFoundException;
    
    /**
     * 
     * @param name
     * @param key
     * @param value
     * @param node
     * @throws NodeIndexNotFoundException 
     */
    public void deleteNodeFromIndex(String name, String key, String value, Integer node)
            throws NodeIndexNotFoundException;
}
