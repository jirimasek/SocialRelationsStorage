package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.PersistenceManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
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
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Class <code>PersistenceManagerImpl</code> implements ... and provides ...
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Neo4j
@Stateless
public class PersistenceManagerImpl implements PersistenceManager
{

    @Inject
    @Config
    private String DATABASE_URI;

    public PersistenceManagerImpl()
    {
    }

    public PersistenceManagerImpl(String databaseURI)
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
     *                                 NODES                                  *
     * ********************************************************************** */
    /**
     * 
     * @return 
     */
    @Override
    public Integer createNode()
    {
        try
        {
            return createNode(null);
        }
        catch (InvalidPropertiesException ex)
        {
            return null;
        }
    }

    /**
     * 
     * @param properties
     * @return
     * @throws InvalidPropertiesException 
     */
    @Override
    public Integer createNode(JSONObject properties) throws InvalidPropertiesException
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
    public JSONObject retrieveNode(Integer node) throws NodeNotFoundException
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

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
    public boolean deleteNode(Integer node) throws CannotDeleteNodeException
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

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
    public void addProperties(Integer node, JSONObject properties) throws InvalidPropertiesException, NodeNotFoundException
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

        if (properties == null)
        {
            throw new IllegalArgumentException("Properties are null.");
        }

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
    public JSONObject retrieveProperties(Integer node) throws NodeNotFoundException
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

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
    public String retrieveProperty(Integer node, String property) throws PropertyNotFoundException
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

        if (property == null)
        {
            throw new IllegalArgumentException("Property name is null.");
        }

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
    public boolean deleteProperty(Integer node, String property)
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

        if (property == null)
        {
            throw new IllegalArgumentException("Property name is null.");
        }

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
    public void deleteProperties(Integer node) throws NodeNotFoundException
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

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
    public Integer createRelationship(Integer startNode, Integer endNode,
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
    public Integer createRelationship(Integer startNode, Integer endNode,
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
    public JSONObject retrieveRelationship(Integer relationship)
            throws RelationshipNotFoundException
    {
        if (relationship == null)
        {
            throw new IllegalArgumentException("Relationship ID is null.");
        }

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
    public JSONArray retrieveRelationships(Integer node, DirectionEnum direction)
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
    public JSONArray retrieveRelationships(Integer node, DirectionEnum direction,
            String relationship) throws NodeNotFoundException
    {
        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

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
    public boolean deleteRelationship(Integer relationship)
    {
        if (relationship == null)
        {
            throw new IllegalArgumentException("Relationship ID is null.");
        }

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
    public void addMetadataToRelationship(Integer relationship,
            JSONObject metadata) throws InvalidMetadataException,
            RelationshipNotFoundException
    {
        if (relationship == null)
        {
            throw new IllegalArgumentException("Relationship ID is null.");
        }

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
    public JSONObject retrieveRelationshipMetadata(Integer relationship)
            throws RelationshipNotFoundException
    {
        if (relationship == null)
        {
            throw new IllegalArgumentException("Relationship ID is null.");
        }

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
    public String retrieveRelationshipMetadata(Integer relationship,
            String metadata) throws MetadataNotFoundException
    {
        if (relationship == null)
        {
            throw new IllegalArgumentException("Relationship ID is null.");
        }

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
    public boolean deleteRelationshipMetadata(Integer relationship,
            String metadata)
    {
        if (relationship == null)
        {
            throw new IllegalArgumentException("Relationship ID is null.");
        }

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
    public void deleteRelationshipMetadata(Integer relationship)
            throws RelationshipNotFoundException
    {
        if (relationship == null)
        {
            throw new IllegalArgumentException("Relationship ID is null.");
        }

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
    public JSONArray traverse(Integer startNode, TraversalDescription t,
            TypeEnum type)
            throws JSONException, NodeNotFoundException
    {
        if (startNode == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

        String traverserURI = DATABASE_URI + "/node/" + startNode + "/traverse/"
                + type.toString().toLowerCase();

        ClientResponse response = post(traverserURI, serializeTraversalDesc(t));

        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }

        try
        {
            JSONArray output = response.getEntity(JSONArray.class);

            return output;
        }
        catch (Exception ex)
        {
            return new JSONArray();
        }
    }

    /* ********************************************************************** *
     *                                INDEXES                                 *
     * ********************************************************************** */
    /**
     * 
     * @param name
     * @throws JSONException 
     */
    @Override
    public void createNodeIndex(String name) throws JSONException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Index name is null.");
        }

        String nodeIndexEntryPointURI = DATABASE_URI + "/index/node";

        JSONObject entity = new JSONObject();

        entity.put("name", name);

        ClientResponse response = post(nodeIndexEntryPointURI, entity);
    }

    /**
     * 
     * @param name 
     */
    @Override
    public void deleteNodeIndex(String name)
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Index name is null.");
        }

        String nodeIndexURI = DATABASE_URI + "/index/node/" + name;

        ClientResponse response = delete(nodeIndexURI);
    }

    /**
     * 
     * @return
     */
    @Override
    public JSONObject retrieveListOfNodeIndexes()
    {
        JSONObject list = null;

        try
        {
            String nodeIndexEntryPointURI = DATABASE_URI + "/index/node";

            ClientResponse response = get(nodeIndexEntryPointURI);

            list = response.getEntity(JSONObject.class);
        }
        catch (UniformInterfaceException ex)
        {
        }

        return list;
    }

    /**
     * 
     * @param name
     * @param key
     * @param value
     * @param node
     * @throws JSONException
     */
    @Override
    public void addNodeToIndex(String name, String key, String value,
            Integer node) throws JSONException, NodeIndexNotFoundException
    {
        addNodeToIndex(name, key, value, node, true);
    }

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
    @Override
    public void addNodeToIndex(String name, String key, String value,
            Integer node, boolean unique) throws JSONException, NodeIndexNotFoundException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Index name is null.");
        }

        if (key == null)
        {
            throw new IllegalArgumentException("Key is null.");
        }

        if (value == null)
        {
            throw new IllegalArgumentException("Value is null.");
        }

        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

        String nodeIndexURI = DATABASE_URI + "/index/node/" + name;
        String nodeURI = DATABASE_URI + "/node/" + node;

        if (unique)
        {
            nodeIndexURI += "?unique";
        }

        JSONObject entity = new JSONObject();

        entity.put("key", key);
        entity.put("value", value);
        entity.put("uri", nodeURI);

        ClientResponse response = post(nodeIndexURI, entity);

        if (response.getStatus() == 404)
        {
            throw new NodeIndexNotFoundException();
        }
    }

    /**
     * 
     * @param name
     * @param key
     * @param value
     * @return
     * @throws JSONException
     */
    @Override
    public JSONObject retrieveNodeFromIndex(String name, String key,
            String value) throws JSONException, NodeIndexNotFoundException
    {
        JSONArray nodes = retrieveNodesFromIndex(name, key, value);

        if (nodes.length() > 0)
        {
            return nodes.getJSONObject(0);
        }

        return null;
    }

    /**
     * 
     * @param name
     * @param key
     * @param value
     * @return
     * @throws JSONException
     */
    @Override
    public JSONArray retrieveNodesFromIndex(String name, String key,
            String value) throws JSONException, NodeIndexNotFoundException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Index name is null.");
        }

        if (key == null)
        {
            throw new IllegalArgumentException("Key is null.");
        }

        if (value == null)
        {
            throw new IllegalArgumentException("Value is null.");
        }

        String nodeURI = DATABASE_URI + "/index/node/" + name + "/" + key + "/" + value;

        ClientResponse response = get(nodeURI);

        if (response.getStatus() == 404)
        {
            throw new NodeIndexNotFoundException();
        }

        JSONArray nodes = response.getEntity(JSONArray.class);

        return nodes;
    }

    /**
     * 
     * @param name
     * @param key
     * @param value
     * @param node
     * @throws NodeIndexNotFoundException 
     */
    @Override
    public void deleteNodeFromIndex(String name, String key, String value,
            Integer node)
            throws NodeIndexNotFoundException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Index name is null.");
        }

        if (key == null)
        {
            throw new IllegalArgumentException("Key is null.");
        }

        if (value == null)
        {
            throw new IllegalArgumentException("Value is null.");
        }

        if (node == null)
        {
            throw new IllegalArgumentException("Node ID is null.");
        }

        String indexNodeURI = DATABASE_URI + "/index/node/" + name + "/" + key + "/" + value + "/" + node;

        ClientResponse response = delete(indexNodeURI);

        if (response.getStatus() == 404)
        {
            throw new NodeIndexNotFoundException();
        }
    }

    /**
     * Serializuje entitu do formátu JSON.
     * 
     * @return      JSON
     */
    private JSONObject serializeTraversalDesc(TraversalDescription t) throws JSONException
    {
        JSONObject json = new JSONObject();

        json.put("order", t.getOrder());
        json.put("uniqueness", t.getUniqueness());

        if (t.getRelationships() != null)
        {
            for (int i = 0 ; i < t.getRelationships().size() ; i++)
            {
                json.append("relationships",
                        t.getRelationships().get(i).toJson());
            }
        }

        JSONObject filter = new JSONObject();

        filter.put("language", "builtin");
        filter.put("name", t.getReturnFilter());

        json.put("return_filter", filter);

        json.put("max_depth", t.getMaxDepth());

        return json;
    }
}
