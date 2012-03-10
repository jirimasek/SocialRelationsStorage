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
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.PropertyNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import java.net.URI;
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
    public URI createNode()
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
    public URI createNode(JSONObject properties) throws InvalidPropertiesException
    {
        final String nodeEntryPointURI = DATABASE_URI + "/node";
        
        ClientResponse response = post(nodeEntryPointURI, properties);
        
        if (response.getStatus() == 400)
        {
            throw new InvalidPropertiesException();
        }
        
        return response.getLocation();
    }

    /**
     * 
     * @param nodeURI
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONObject retrieveNode(String nodeURI) throws NodeNotFoundException
    {
        ClientResponse response = get(nodeURI);
        
        if (response.getStatus() == 404)
        {
            throw new NodeNotFoundException();
        }
        
        JSONObject node = response.getEntity(JSONObject.class);
        
        return node;
    }

    /**
     * 
     * @param nodeURI
     * @return
     * @throws CannotDeleteNodeException 
     */
    @Override
    public boolean deleteNode(String nodeURI) throws CannotDeleteNodeException
    {
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

    @Override
    public void addProperties(String nodeURI, JSONObject properties) throws InvalidPropertiesException, NodeNotFoundException
    {
        String propertiesURI = nodeURI + "/properties";
        
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
     * @param nodeURI
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONObject retrieveProperties(String nodeURI) throws NodeNotFoundException
    {
        String propertiesURI = nodeURI + "/properties";
        
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
     * @param nodeURI
     * @param property
     * @return
     * @throws PropertyNotFoundException 
     */
    @Override
    public String retrieveProperty(String nodeURI, String property) throws PropertyNotFoundException
    {
        String propertyURI = nodeURI + "/properties/" + property;
        
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
     * @param nodeURI
     * @param property
     * @return 
     */
    @Override
    public boolean deleteProperty(String nodeURI, String property)
    {
        String propertyURI = nodeURI + "/properties/" + property;
        
        ClientResponse response = delete(propertyURI);
        
        if (response.getStatus() == 204)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param nodeURI
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public void deleteProperties(String nodeURI) throws NodeNotFoundException
    {
        String propertiesURI = nodeURI + "/properties";
        
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
     * @param startNodeURI
     * @param endNodeURI
     * @param relationship
     * @return
     * @throws InvalidMetadataException
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException 
     */
    @Override
    public URI createRelationship(String startNodeURI, String endNodeURI,
            String relationship) throws InvalidMetadataException, JSONException,
            InvalidRelationshipException, NodeNotFoundException
    {
        return createRelationship(startNodeURI, endNodeURI, relationship, null);
    }

    /**
     * 
     * @param startNodeURI
     * @param endNodeURI
     * @param relationship
     * @param metadata
     * @return
     * @throws InvalidMetadataException
     * @throws InvalidRelationshipException
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    @Override
    public URI createRelationship(String startNodeURI, String endNodeURI,
            String relationship, JSONObject metadata) throws InvalidRelationshipException,
            JSONException, NodeNotFoundException
    {
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
        
        URI relationshipURI = response.getLocation();
        
        return relationshipURI;
    }

    /**
     * 
     * @param relationshipURI
     * @return
     * @throws RelationshipNotFoundException 
     */
    @Override
    public JSONObject retrieveRelationship(String relationshipURI)
            throws RelationshipNotFoundException
    {
        ClientResponse response = get(relationshipURI);
        
        if (response.getStatus() == 404)
        {
            throw new RelationshipNotFoundException();
        }
        
        JSONObject relationship = response.getEntity(JSONObject.class);
        
        return relationship;
    }

    /**
     * 
     * @param nodeURI
     * @param direction
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONArray retrieveRelationships(String nodeURI, DirectionEnum direction)
            throws NodeNotFoundException
    {
        
        return retrieveRelationships(nodeURI, direction, null);
    }

    /**
     * 
     * @param nodeURI
     * @param direction
     * @param relationship
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONArray retrieveRelationships(String nodeURI, DirectionEnum direction,
            String relationship) throws NodeNotFoundException
    {
        String relationships = nodeURI + "/relationships/" + direction.toString().toLowerCase();
        
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
     * @param relationshipURI
     * @return 
     */
    @Override
    public boolean deleteRelationship(String relationshipURI)
    {
        ClientResponse response = delete(relationshipURI);
        
        if (response.getStatus() == 204)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param relationshipURI
     * @param metadata
     * @throws InvalidMetadataException
     * @throws RelationshipNotFoundException 
     */
    @Override
    public void addMetadataToRelationship(String relationshipURI,
            JSONObject metadata) throws InvalidMetadataException,
            RelationshipNotFoundException
    {
        String metadataURI = relationshipURI + "/properties";
        
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
     * @param relationshipURI
     * @return
     * @throws RelationshipNotFoundException 
     */
    @Override
    public JSONObject retrieveRelationshipMetadata(String relationshipURI)
            throws RelationshipNotFoundException
    {
        String metadataURI = relationshipURI + "/properties";
        
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
     * @param relationshipURI
     * @param metadata
     * @return
     * @throws MetadataNotFoundException 
     */
    @Override
    public String retrieveRelationshipMetadata(String relationshipURI,
            String metadata) throws MetadataNotFoundException
    {
        String metadataURI = relationshipURI + "/properties/" + metadata;
        
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
     * @param relationshipURI
     * @param metadata
     * @return 
     */
    @Override
    public boolean  deleteRelationshipMetadata(String relationshipURI, String metadata)
    {
        String metadataURI = relationshipURI + "/properties/" + metadata;
        
        ClientResponse response = delete(metadataURI);
        
        if (response.getStatus() == 204)
        {
            return true;
        }
        
        return false;
    }

    /**
     * 
     * @param relationshipURI
     * @throws RelationshipNotFoundException 
     */
    @Override
    public void deleteRelationshipMetadata(String relationshipURI)
            throws RelationshipNotFoundException
    {
        String metadataURI = relationshipURI + "/properties";
        
        ClientResponse response = delete(metadataURI);
        
        if (response.getStatus() == 404)
        {
            throw new RelationshipNotFoundException();
        }
    }

    /* ********************************************************************** *
     *                              Traverzování                              *
     * ********************************************************************** */

    @Override
    public <T> List<T> traverse(String startNode, TraversalDescription t) throws NodeNotFoundException
    {
        String traverserUri = startNode + "/traverse/node";
        
        //TraversalDescription t = new TraversalDescription();

        //t.setOrder(TraversalDescription.DEPTH_FIRST);
        //t.setUniqueness(TraversalDescription.NODE);
        //t.setMaxDepth(maxDepth);
        //t.setReturnFilter(TraversalDescription.ALL);
        //t.setRelationships(new Relationship(relationship, Relationship.OUT));
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
