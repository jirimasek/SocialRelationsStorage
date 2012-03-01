package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.IN;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.OUT;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.ALL;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.binding.EntityBuilder;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Node;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.GraphElement;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Třída <code>PersistenceManagerImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
public class PersistenceManagerImpl implements PersistenceManager
{

    @Inject
    @Config
    private String SERVER_ROOT_URI;
    
    @Inject
    private EntityBuilder entityBuilder;

    @Override
    public URI createNode() throws UniformInterfaceException
    {
        final String nodeEntryPointUri = SERVER_ROOT_URI + "node";

        WebResource resource = Client.create().resource(nodeEntryPointUri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON).entity("{}").post(
                ClientResponse.class);

        final URI location = response.getLocation();

        response.close();

        return location;
    }

    @Override
    public void addProperty(URI uri, String property, String value)
    {
        String propertyUri = uri.toString() + "/properties/" + property;

        WebResource resource = Client.create().resource(propertyUri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON).entity("\"" + value + "\"").put(
                ClientResponse.class);

        response.close();
    }
    
    @Override
    public Node retrieveNode(String uid) throws URISyntaxException, JSONException
    {
        System.out.println("jsme tu");
        
        TraversalDescription t = new TraversalDescription();

        t.setOrder(TraversalDescription.DEPTH_FIRST);
        t.setUniqueness(TraversalDescription.NODE);
        t.setMaxDepth(1);
        t.setReturnFilter(TraversalDescription.ALL);
        t.setRelationships(new Relation(uid, OUT));

        URI traverserUri = new URI(SERVER_ROOT_URI + "node/0/traverse/node");

        
        
        String json = get(traverserUri, t.toJson(), String.class);
        
        System.out.println(json.toString());
        
        Node node = entityBuilder.getNode(json.toString());
        
        return node;
    }

    @Override
    public Node retrieveNode(URI uri) throws URISyntaxException, JSONException
    {
        WebResource resource = Client.create().resource(uri);
        
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                get(ClientResponse.class);

        String json = response.getEntity(String.class);

        response.close();
        
        json = "[ " + json + " ]";
        
        Node node = entityBuilder.getNode(json);
        
        return node;
    }

    @Override
    public URI createRelation(URI startNode, URI endNode, String relationship)
            throws URISyntaxException
    {
        URI fromUri = new URI(startNode.toString() + "/relationships");

        String relationshipJson = "{"
                + "\"to\":\"" + endNode.toString() + "\","
                + "\"type\":\"" + relationship + "\"}";

        WebResource resource = Client.create().resource(fromUri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON).entity(relationshipJson).post(
                ClientResponse.class);

        final URI location = response.getLocation();

        response.close();

        return location;
    }

    @Override
    public void addMetadataToProperty(URI uri, String property, String value)
            throws URISyntaxException
    {
        URI propertyUri = new URI(uri.toString() + "/properties");

        String entity = "{\"" + property + "\":\"" + value + "\"}";

        WebResource resource = Client.create().resource(propertyUri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON).entity(entity).put(
                ClientResponse.class);

        response.close();
    }

    @Override
    public Relation getRelation(URI startNode, URI endNode, String relationship)
            throws URISyntaxException, JSONException
    {
        List<Relation> relations = getRelations(startNode, OUT);

        for (Relation relation : relations)
        {
            if (relation.getEnd().toString().equals(endNode.toString()))
            {
                return relation;
            }
        }
        
        return null;
    }

    @Override
    public Relation getRelation(URI node)
            throws URISyntaxException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Relation> getRelations(URI node, DirectionEnum direction)
            throws JSONException, URISyntaxException
    {
        URI relationships = new URI(node.toString() + "/relationships/" + direction.
                toString().toLowerCase());

        WebResource resource = Client.create().resource(relationships);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                get(ClientResponse.class);

        String json = response.getEntity(String.class);

        response.close();
        
        List<Relation> relations = entityBuilder.getRelations(json);

        return relations;
    }

    @Override
    public <T extends GraphElement> List<T> traverse(URI startNode, TraversalDescription t)
            throws URISyntaxException, JSONException
    {
        /*
         * TraversalDescription t = new TraversalDescription();

        t.setOrder(TraversalDescription.DEPTH_FIRST);
        t.setUniqueness(TraversalDescription.NODE);
        t.setMaxDepth(maxDepth);
        t.setReturnFilter(TraversalDescription.ALL);
        t.setRelationships(new Relationship(relationship, Relationship.OUT));
         * 
         */

        URI traverserUri = new URI(startNode.toString() + "/traverse/node");

        String json = post(traverserUri, t.toJson(), String.class);
        
        //List<T> nodes = entityBuilder.getNodes(json);

        return null;
    }
    
    private <T> T get(URI uri, JSONObject entity, Class<T> c)
    {
        WebResource resource = Client.create().resource(uri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON).entity(entity.toString()).
                post(ClientResponse.class);

        T output = response.getEntity(c);

        response.close();
        
        return output;
    }
    
    private <T> T post(URI uri, JSONObject entity, Class<T> c)
    {
        WebResource resource = Client.create().resource(uri);

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON).
                type(MediaType.APPLICATION_JSON).entity(entity.toString()).
                post(ClientResponse.class);

        T output = response.getEntity(c);

        response.close();
        
        return output;
    }
}
