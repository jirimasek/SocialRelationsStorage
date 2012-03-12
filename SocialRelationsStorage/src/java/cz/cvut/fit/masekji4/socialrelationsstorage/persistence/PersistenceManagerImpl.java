package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
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
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Třída <code>PersistenceManagerImpl</code> implementuje ... a zajišťuje ...
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
public class PersistenceManagerImpl implements PersistenceManager
{

    @Inject
    @Config
    private String DATABASE_URI;

    public PersistenceManagerImpl()
    {
    }

    protected PersistenceManagerImpl(String databaseURI)
    {
        this.DATABASE_URI = databaseURI;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funkce zajišťující HTTP požadavky.">
    /* ********************************************************************** *
     *                    Funkce zajišťující HTTP požadavky.
     * ********************************************************************** */
    
    /**
     * 
     * @param uri
     * @return 
     */
    private ClientResponse get(String uri)
    {
        return get(uri, null);
    }
    
    /**
     * 
     * @param uri
     * @param entity
     * @return 
     */
    private ClientResponse get(String uri, JSONObject entity)
    {
        Client client = new Client();
        
        WebResource resource = client.resource(uri);

        Builder builder = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON);
        
        if (entity != null)
        {
            builder.entity(entity);
        }
        
        ClientResponse response = builder.get(ClientResponse.class);
        
        return response;
    }
    
    /**
     * 
     * @param uri
     * @param entity
     * @return 
     */
    private ClientResponse post(String uri, JSONObject entity)
    {
        Client client = new Client();
        
        WebResource resource = client.resource(uri);

        Builder builder = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON);
        
        if (entity != null)
        {
            builder.entity(entity);
        }
        
        ClientResponse response = builder.post(ClientResponse.class);

        response.close();
        
        return response;
    }
    
    /**
     * 
     * @param uri
     * @return 
     */
    private ClientResponse put(String uri)
    {
        return put(uri, null);
    }
    
    /**
     * 
     * @param uri
     * @param entity
     * @return 
     */
    private ClientResponse put(String uri, JSONObject entity)
    {
        Client client = new Client();
        
        WebResource resource = client.resource(uri);

        Builder builder = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON);
        
        if (entity != null)
        {
            builder.entity(entity);
        }
        
        ClientResponse response = builder.put(ClientResponse.class);

        response.close();
        
        return response;
    }
    
    /**
     * 
     * @param uri
     * @return 
     */
    private ClientResponse delete(String uri)
    {
        return delete(uri, null);
    }
    
    /**
     * 
     * @param uri
     * @param entity
     * @return 
     */
    private ClientResponse delete(String uri, JSONObject entity)
    {
        Client client = new Client();
        
        WebResource resource = client.resource(uri);

        Builder builder = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON);
        
        if (entity != null)
        {
            builder.entity(entity);
        }
        
        ClientResponse response = builder.delete(ClientResponse.class);

        response.close();
        
        return response;
    }// </editor-fold>

    /* ********************************************************************** *
     *                         Implementace rozhraní.                         *
     * ********************************************************************** */
    
    /* ********************************************************************** *
     *                                  Uzly                                  *
     * ********************************************************************** */
    
    /**
     * 
     * @return 
     */
    @Override
    public int createNode()
    {
        try
        {
            return createNode(null);
        }
        catch (InvalidPropertiesException ex)
        {
            return 0;
        }
    }

    /**
     * 
     * @param properties
     * @return
     * @throws InvalidPropertiesException 
     */
    @Override
    public int createNode(JSONObject properties) throws InvalidPropertiesException
    {
        final String nodeEntryPointURI = DATABASE_URI + "/node";
        
        ClientResponse response = post(nodeEntryPointURI, properties);
        
        if (response.getStatus() == 400)
        {
            throw new InvalidPropertiesException();
        }
        
        String nodeURI = response.getLocation().toString();
        
        String node = nodeURI.substring(nodeURI.lastIndexOf("/") + 1);
                
        return Integer.valueOf(node);
    }

    /**
     * 
     * @param node
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONObject retrieveNode(int node) throws NodeNotFoundException
    {
        String nodeURI = DATABASE_URI + "/node/" + node; 
        
        ClientResponse response = get(nodeURI);
        
        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }
        
        JSONObject output = response.getEntity(JSONObject.class);
        
        return output;
    }

    /**
     * 
     * @param node
     * @return
     * @throws CannotDeleteNodeException 
     */
    @Override
    public boolean deleteNode(int node) throws CannotDeleteNodeException
    {
        String nodeURI = DATABASE_URI + "/node/" + node; 
        
        ClientResponse response = delete(nodeURI);
        
        if (response.getStatus() == 409)
        {
            throw new CannotDeleteNodeException();
        }
        else if (response.getStatus() == 204)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param node
     * @param properties
     * @throws InvalidPropertiesException
     * @throws NodeNotFoundException 
     */
    @Override
    public void addProperties(int node, JSONObject properties) throws InvalidPropertiesException, NodeNotFoundException
    {
        String propertiesURI = DATABASE_URI + "/node/" + node + "/properties";
        
        ClientResponse response = put(propertiesURI, properties);
        
        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }
        else if (response.getStatus() == 400)
        {
            throw new InvalidPropertiesException();
        }
    }

    /**
     * 
     * @param node
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONObject retrieveProperties(int node) throws NodeNotFoundException
    {
        String propertiesURI = DATABASE_URI + "/node/" + node + "/properties";
        
        ClientResponse response = get(propertiesURI);
        
        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }
        else if (response.getStatus() == 204)
        {
            return new JSONObject();
        }
        
        JSONObject properties = response.getEntity(JSONObject.class);
        
        return properties;
    }

    /**
     * 
     * @param node
     * @param property
     * @return
     * @throws PropertyNotFoundException 
     */
    @Override
    public String retrieveProperty(int node, String property) throws PropertyNotFoundException
    {
        String propertyURI = DATABASE_URI + "/node/" + node + "/properties/" + property;
        
        ClientResponse response = get(propertyURI);
        
        if (response.getStatus() == 404)
        {
            throw new PropertyNotFoundException();
        }
        
        String prop = response.getEntity(String.class);
        
        if (prop.startsWith("\""))
        {
            prop = prop.substring(1);
        }
        
        if (prop.endsWith("\""))
        {
            prop = prop.substring(0, prop.length() - 1);
        }
        
        return prop;
    }

    /**
     * 
     * @param node
     * @param property
     * @return 
     */
    @Override
    public boolean deleteProperty(int node, String property)
    {
        String propertyURI = DATABASE_URI + "/node/" + node + "/properties/" + property;
        
        ClientResponse response = delete(propertyURI);
        
        if (response.getStatus() == 204)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param node
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public void deleteProperties(int node) throws NodeNotFoundException
    {
        String propertiesURI = DATABASE_URI + "/node/" + node + "/properties";
        
        ClientResponse response = delete(propertiesURI);
        
        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }
    }

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
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException 
     */
    @Override
    public int createRelationship(int startNode, int endNode,
            String relationship) throws InvalidMetadataException, JSONException,
            InvalidRelationshipException, NodeNotFoundException
    {
        return createRelationship(startNode, endNode, relationship, null);
    }

    /**
     * 
     * @param startNode
     * @param endNode
     * @param relationship
     * @param metadata
     * @return
     * @throws InvalidRelationshipException
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    @Override
    public int createRelationship(int startNode, int endNode,
            String relationship, JSONObject metadata) throws InvalidRelationshipException,
            JSONException, NodeNotFoundException
    {
        String startNodeURI = DATABASE_URI + "/node/" + startNode;
        String endNodeURI = DATABASE_URI + "/node/" + endNode;
        
        String fromURI = startNodeURI + "/relationships";
                        
        JSONObject rel = new JSONObject();
        
        if (endNodeURI != null && !endNodeURI.isEmpty())
        {
            rel.put("to", endNodeURI);
        }
        
        if (relationship != null && !relationship.isEmpty())
        {
            rel.put("type", relationship);
        }
        
        if (metadata != null && metadata.length() > 0)
        {
            rel.put("data", metadata);
        }
        
        ClientResponse response = post(fromURI, rel);
        
        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }
        else if (response.getStatus() >= 400)
        {
            throw new InvalidRelationshipException();
        }
        
        String relURI = response.getLocation().toString();
        
        String node = relURI.substring(relURI.lastIndexOf("/") + 1);
                
        return Integer.valueOf(node);
    }

    /**
     * 
     * @param relationship
     * @return
     * @throws RelationshipNotFoundException 
     */
    @Override
    public JSONObject retrieveRelationship(int relationship)
            throws RelationshipNotFoundException
    {
        String relationshipURI = DATABASE_URI + "/relationship/" + relationship;
        
        ClientResponse response = get(relationshipURI);
        
        if (response.getStatus() == 404)
        {
            throw new RelationshipNotFoundException();
        }
        
        JSONObject rel = response.getEntity(JSONObject.class);
        
        return rel;
    }

    /**
     * 
     * @param node
     * @param direction
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONArray retrieveRelationships(int node, DirectionEnum direction)
            throws NodeNotFoundException
    {
        
        return retrieveRelationships(node, direction, null);
    }

    /**
     * 
     * @param node
     * @param direction
     * @param relationship
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONArray retrieveRelationships(int node, DirectionEnum direction,
            String relationship) throws NodeNotFoundException
    {
        String relationships = DATABASE_URI + "/node/" + node + "/relationships/"
                + direction.toString().toLowerCase();
        
        if (relationship != null && !relationship.isEmpty())
        {
            relationships += "/" + relationship;
        }
        
        ClientResponse response = get(relationships);
        
        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }
        
        JSONArray rel = response.getEntity(JSONArray.class);
        
        return rel;
    }

    /**
     * 
     * @param relationship
     * @return 
     */
    @Override
    public boolean deleteRelationship(int relationship)
    {
        String relationshipURI = DATABASE_URI + "/relationship/" + relationship;
        
        ClientResponse response = delete(relationshipURI);
        
        if (response.getStatus() == 204)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param relationship
     * @param metadata
     * @throws InvalidMetadataException
     * @throws RelationshipNotFoundException 
     */
    @Override
    public void addMetadataToRelationship(int relationship,
            JSONObject metadata) throws InvalidMetadataException,
            RelationshipNotFoundException
    {
        String metadataURI = DATABASE_URI + "/relationship/" + relationship + "/properties";
        
        ClientResponse response = put(metadataURI, metadata);
        
        if (response.getStatus() == 404)
        {
            throw new RelationshipNotFoundException();
        }
        else if (response.getStatus() == 400)
        {
            throw new InvalidMetadataException();
        }
    }

    /**
     * 
     * @param relationship
     * @return
     * @throws RelationshipNotFoundException 
     */
    @Override
    public JSONObject retrieveRelationshipMetadata(int relationship)
            throws RelationshipNotFoundException
    {
        String metadataURI = DATABASE_URI + "/relationship/" + relationship + "/properties";
        
        ClientResponse response = get(metadataURI);
        
        if (response.getStatus() == 404)
        {
            throw new RelationshipNotFoundException();
        }
        else if (response.getStatus() == 204)
        {
            return new JSONObject();
        }
        
        JSONObject metadata = response.getEntity(JSONObject.class);
        
        return metadata;
    }

    /**
     * 
     * @param relationship
     * @param metadata
     * @return
     * @throws MetadataNotFoundException 
     */
    @Override
    public String retrieveRelationshipMetadata(int relationship,
            String metadata) throws MetadataNotFoundException
    {
        String metadataURI = DATABASE_URI + "/relationship/" + relationship
                + "/properties/" + metadata;
        
        ClientResponse response = get(metadataURI);
        
        if (response.getStatus() == 404)
        {
            throw new MetadataNotFoundException();
        }
        
        String meta = response.getEntity(String.class);
        
        return meta;
    }

    /**
     * 
     * @param relationship
     * @param metadata
     * @return 
     */
    @Override
    public boolean  deleteRelationshipMetadata(int relationship, String metadata)
    {
        String metadataURI = DATABASE_URI + "/relationship/" + relationship
                + "/properties/" + metadata;
        
        ClientResponse response = delete(metadataURI);
        
        if (response.getStatus() == 204)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param relationship
     * @throws RelationshipNotFoundException 
     */
    @Override
    public void deleteRelationshipMetadata(int relationship)
            throws RelationshipNotFoundException
    {
        String metadataURI = DATABASE_URI + "/relationship/" + relationship + "/properties";
        
        ClientResponse response = delete(metadataURI);
        
        if (response.getStatus() == 404)
        {
            throw new RelationshipNotFoundException();
        }
    }
    
    /* ********************************************************************** *
     *                               Traversals                               *
     * ********************************************************************** */

    @Override
    public <T> List<T> traverse(int startNode, TraversalDescription t) throws NodeNotFoundException
    {
        String traverserUri = DATABASE_URI + "/node/" + startNode + "/traverse/node";
        
        //TraversalDescription t = new TraversalDescription();

        //t.setOrder(TraversalDescription.DEPTH_FIRST);
        //t.setUniqueness(TraversalDescription.NODE);
        //t.setMaxDepth(maxDepth);
        //t.setReturnFilter(TraversalDescription.ALL);
        //t.setRelationships(new Relationship(relationship, Relationship.OUT));
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /* ********************************************************************** *
     *                                Indexes                                 *
     * ********************************************************************** */

    /**
     * 
     * @param name
     * @throws JSONException 
     */
    @Override
    public void createNodeIndex(String name) throws JSONException
    {
        String nodeIndexEntryPointURI = DATABASE_URI + "/index/node";
        
        JSONObject entity = new JSONObject();
            
        entity.put("name", name);
        
        ClientResponse response = post(nodeIndexEntryPointURI, entity);
    }

    /**
     * 
     * @param nodeIndex 
     */
    @Override
    public void deleteNodeIndex(String nodeIndex)
    {
        String nodeIndexURI = DATABASE_URI + "/index/node/" + nodeIndex;
        
        ClientResponse response = delete(nodeIndexURI);
    }

    /**
     * 
     * @return 
     */
    @Override
    public JSONObject retrieveListOfNodeIndexes()
    {
        String nodeIndexEntryPointURI = DATABASE_URI + "/index/node";
        
        ClientResponse response = get(nodeIndexEntryPointURI);
        
        JSONObject list = response.getEntity(JSONObject.class);
        
        return list;
    }

    /**
     * 
     * @param nodeIndex
     * @param key
     * @param value
     * @param node
     * @throws JSONException
     */
    @Override
    public void addNodeToIndex(String nodeIndex, String key, String value,
            int node) throws JSONException
    {
        String nodeIndexURI = DATABASE_URI + "/index/node/" + nodeIndex + "?unique";
        String nodeURI = DATABASE_URI + "/node/" + node;
        
        JSONObject entity = new JSONObject();
        
        entity.put("key", key);
        entity.put("value", value);
        entity.put("uri", nodeURI);
        
        ClientResponse response = post(nodeIndexURI, entity);
    }

    /**
     * 
     * @param nodeIndex
     * @param key
     * @param value
     * @return
     * @throws JSONException
     */
    @Override
    public JSONObject retrieveNodeFromIndex(String nodeIndex, String key,
            String value) throws JSONException
    {
        String nodeURI = DATABASE_URI + "/index/node/" + nodeIndex + "/" + key+ "/" + value;
        
        ClientResponse response = get(nodeURI);
        
        JSONArray nodes = response.getEntity(JSONArray.class);
        
        if (nodes.length() > 0)
        {
            return nodes.getJSONObject(0);
        }
        
        return null;
    }

    /**
     * 
     * @param nodeIndex
     * @param key
     * @param value
     * @throws NodeIndexNotFoundException 
     */
    @Override
    public void deleteNodeFromIndex(String nodeIndex, int node, String key, String value)
            throws NodeIndexNotFoundException
    {
        String indexNodeURI = DATABASE_URI + "/index/node/" + nodeIndex + "/" + key + "/" + value + "/" + node;
        
        ClientResponse response = delete(indexNodeURI);
        
        if (response.getStatus() == 404)
        {
            throw new NodeIndexNotFoundException();
        }
    }
}
