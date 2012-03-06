package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.MetadataNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.PropertyNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import java.net.URI;
import java.rmi.UnexpectedException;
import java.util.List;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
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
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject retrieveProperties(String nodeURI) throws NodeNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String retrieveProperty(String nodeURI, String property) throws NodeNotFoundException, PropertyNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteProperty(String nodeURI, String property) throws NodeNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteProperties(String nodeURI) throws NodeNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* ********************************************************************** *
     *                                  Hrany                                 *
     * ********************************************************************** */

    @Override
    public URI createRelationship(String startNodeURI, String endNodeURI,
            String relationship) throws InvalidPropertiesException, NodeNotFoundException
    {
        String fromURI = startNodeURI + "/relationships";

        //String relationshipJson = "{"
          //      + "\"to\":\"" + endNode.toString() + "\","
            //    + "\"type\":\"" + relationship + "\"}";
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI createRelationship(String startNodeURI, String endNodeURI,
            String relationship, JSONObject properties) throws InvalidPropertiesException, NodeNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject retrieveRelationship(String relationshipURI) throws RelationshipNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param nodeURI
     * @param direction
     * @return
     * @throws NodeNotFoundException 
     */
    @Override
    public JSONArray getRelationships(String nodeURI, DirectionEnum direction)
            throws NodeNotFoundException
    {
        String relationships = nodeURI + "/relationships/" + direction.toString().toLowerCase();
        
        throw new UnsupportedOperationException("Not supported yet.");
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
    public JSONArray getRelationships(String nodeURI, DirectionEnum direction,
            String relationship) throws NodeNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param relationshipURI
     * @return 
     */
    @Override
    public boolean deleteRelationship(String relationshipURI)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param relationshipURI
     * @param metadata
     * @throws RelationshipNotFoundException 
     */
    @Override
    public void addMetadataToRelationship(String relationshipURI,
            JSONObject metadata) throws RelationshipNotFoundException
    {
        String propertyUri = relationshipURI + "/properties";

        //String entity = "{\"" + property + "\":\"" + value + "\"}";
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject retrieveRelationshipMetadata(String relationshipURI)
            throws RelationshipNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String retrieveRelationshipMetadata(String relationshipURI,
            String metadata) throws MetadataNotFoundException, RelationshipNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteRelationshipMetadata(String relationshipURI,
            String metadata) throws RelationshipNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteRelationshipMetadata(String relationshipURI) throws RelationshipNotFoundException
    {
        throw new UnsupportedOperationException("Not supported yet.");
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
