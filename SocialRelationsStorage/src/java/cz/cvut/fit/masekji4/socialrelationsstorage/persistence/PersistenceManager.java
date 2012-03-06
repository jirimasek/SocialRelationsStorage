package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.GraphElement;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.MetadataNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.PropertyNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
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
    public URI createNode();
    
    /**
     * 
     * @param properties
     * @return
     * @throws InvalidPropertiesException 
     */
    public URI createNode(JSONObject properties) throws InvalidPropertiesException;

    /**
     * 
     * @param nodeURI
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONObject retrieveNode(String nodeURI) throws NodeNotFoundException;
    
    /**
     * 
     * @param nodeURI
     * @return
     * @throws CannotDeleteNodeException 
     */
    public boolean deleteNode(String nodeURI) throws CannotDeleteNodeException;
    
    /**
     * 
     * @param nodeURI       URI uzlu
     * @param properties 
     */
    public void addProperties(String nodeURI, JSONObject properties)
            throws InvalidPropertiesException, NodeNotFoundException;
    
    /**
     * 
     * @param nodeURI
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONObject retrieveProperties(String nodeURI)
            throws NodeNotFoundException;
    
    /**
     * 
     * @param nodeURI
     * @param property
     * @return
     * @throws NodeNotFoundException
     * @throws PropertyNotFoundException 
     */
    public String retrieveProperty(String nodeURI, String property)
            throws NodeNotFoundException, PropertyNotFoundException;
    
    /**
     * 
     * @param nodeURI
     * @param property
     * @return
     * @throws NodeNotFoundException 
     */
    public boolean deleteProperty(String nodeURI, String property)
            throws NodeNotFoundException;
    /**
     * 
     * @param nodeURI
     * @return
     * @throws NodeNotFoundException 
     */
    public boolean deleteProperties(String nodeURI) throws NodeNotFoundException;
    
    /* ********************************************************************** *
     *                                  Hrany                                 *
     * ********************************************************************** */
    
    /**
     * 
     * @param startNodeURI
     * @param endNodeURI
     * @param relationship
     * @return
     * @throws InvalidPropertiesException
     * @throws NodeNotFoundException 
     */
    public URI createRelationship(String startNodeURI, String endNodeURI,
            String relationship)
            throws InvalidPropertiesException, NodeNotFoundException;
    
    /**
     * 
     * @param startNodeURI
     * @param endNodeURI
     * @param relationship
     * @param properties
     * @return
     * @throws InvalidPropertiesException
     * @throws NodeNotFoundException 
     */
    public URI createRelationship(String startNodeURI, String endNodeURI,
            String relationship, JSONObject properties)
            throws InvalidPropertiesException, NodeNotFoundException;
    
    /**
     * 
     * @param relationshipURI
     * @return
     * @throws RelationshipNotFoundException 
     */
    public JSONObject retrieveRelationship(String relationshipURI)
            throws RelationshipNotFoundException;
    
    /**
     * 
     * @param nodeURI
     * @param direction
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONArray getRelationships(String nodeURI, DirectionEnum direction)
        throws NodeNotFoundException;
    
    /**
     * 
     * @param nodeURI
     * @param direction
     * @param relationship
     * @return
     * @throws NodeNotFoundException 
     */
    public JSONArray getRelationships(String nodeURI, DirectionEnum direction,
            String relationship)
        throws NodeNotFoundException;
    
    /**
     * 
     * @param relationshipURI
     * @return 
     */
    public boolean deleteRelationship(String relationshipURI);
    
    /**
     * 
     * @param relationshipURI
     * @param metadata
     * @throws RelationNotFoundException 
     */
    public void addMetadataToRelationship(String relationshipURI, JSONObject metadata)
        throws RelationshipNotFoundException;
    
    /**
     * 
     * @param relationshipURI
     * @return
     * @throws RelationshipNotFoundException 
     */
    public JSONObject retrieveRelationshipMetadata(String relationshipURI)
            throws RelationshipNotFoundException;
    
    /**
     * 
     * @param relationshipURI
     * @param metadata
     * @return
     * @throws MetadataNotFoundException
     * @throws RelationshipNotFoundException 
     */
    public String retrieveRelationshipMetadata(String relationshipURI, String metadata)
            throws MetadataNotFoundException, RelationshipNotFoundException;
    
    /**
     * 
     * @param relationshipURI
     * @param metadata
     * @return
     * @throws RelationNotFoundException 
     */
    public boolean deleteRelationshipMetadata(String relationshipURI, String metadata)
            throws RelationshipNotFoundException;
    
    /**
     * 
     * @param relationshipURI
     * @return
     * @throws RelationshipNotFoundException 
     */
    public boolean deleteRelationshipMetadata(String relationshipURI)
            throws RelationshipNotFoundException;
    
    /* ********************************************************************** *
     *                              Traverzování                              *
     * ********************************************************************** */
    
    /**
     * 
     * @param <T>
     * @param startNode
     * @param t
     * @return
     * @throws NodeNotFoundException 
     */
    public <T> List<T> traverse(String startNode, TraversalDescription t)
            throws NodeNotFoundException;
}
