package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class GraphDAO implements IGraphDAO
{

    private final String SERVER_ROOT_URI = "http://localhost:7474/db/data/";

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
    public URI addRelationship(URI startNode, URI endNode, String relationship)
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
        
        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                                    .type(MediaType.APPLICATION_JSON)
                                    .entity(entity)
                                    .put(ClientResponse.class);
        
        response.close();
    }
    
    @Override
    public URI getNodeURI(String id) throws URISyntaxException, JSONException
    {
        TraversalDescription t = new TraversalDescription();
        
        t.setOrder(TraversalDescription.DEPTH_FIRST);
        t.setUniqueness(TraversalDescription.NODE);
        t.setMaxDepth(1);
        t.setReturnFilter(TraversalDescription.ALL);
        t.setRelationships(new Relationship(id, Relationship.OUT));

        URI traverserUri = new URI( "http://localhost:7474/db/data/node/0" + "/traverse/node" );

        WebResource resource = Client.create().resource(traverserUri);
        String jsonTraverserPayload = t.toJson();

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .entity(jsonTraverserPayload)
            .post(ClientResponse.class);
 
        String json = response.getEntity(String.class);
        
        response.close();
        
        JSONArray jsonArray = new JSONArray(json);
        
        if (jsonArray.length() > 0)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            
            return new URI((String) jsonObject.get("self"));
        }
        
        return null;
    }
    
    @Override
    public boolean getRelationURI(URI startNode, URI endNode, String relationship) throws URISyntaxException, JSONException
    {
        TraversalDescription t = new TraversalDescription();
        
        t.setOrder(TraversalDescription.DEPTH_FIRST);
        t.setUniqueness(TraversalDescription.NODE);
        t.setMaxDepth(1);
        t.setReturnFilter(TraversalDescription.ALL);
        t.setRelationships(new Relationship(relationship, Relationship.OUT));

        URI traverserUri = new URI( startNode.toString() + "/traverse/node" );

        WebResource resource = Client.create().resource(traverserUri);
        String jsonTraverserPayload = t.toJson();

        ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
            .type(MediaType.APPLICATION_JSON)
            .entity(jsonTraverserPayload)
            .post(ClientResponse.class);
 
        String json = response.getEntity(String.class);
        
        response.close();
        
        JSONArray jsonArray = new JSONArray(json);
        
        for (int i = 0 ; i < jsonArray.length() ; i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            
            if (((String) jsonObject.get("self")).equals(endNode.toString()))
            {
                return true;
            }
        }
        
        return false;
    }

    private final class Relationship
    {

        public static final String OUT = "out";
        public static final String IN = "in";
        public static final String BOTH = "both";
        private String type;
        private String direction;

        public String toJsonCollection()
        {
            StringBuilder sb = new StringBuilder();

            sb.append("{ ");
            sb.append(" \"type\" : \"").append(type).append("\"");

            if (direction != null)
            {
                sb.append(", \"direction\" : \"").append(direction).append("\"");
            }

            sb.append(" }");

            return sb.toString();
        }

        public Relationship(String type, String direction)
        {
            this.setType(type);
            this.setDirection(direction);
        }

        public Relationship(String type)
        {
            this(type, null);
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public void setDirection(String direction)
        {
            this.direction = direction;
        }
    }

    private final class TraversalDescription
    {

        public static final String DEPTH_FIRST = "depth first";
        public static final String NODE = "node";
        public static final String ALL = "all";
        
        private String uniqueness = NODE;
        private int maxDepth = 1;
        private String returnFilter = ALL;
        private String order = DEPTH_FIRST;
        private List<Relationship> relationships = new ArrayList<Relationship>();

        public void setOrder(String order)
        {
            this.order = order;
        }

        public void setUniqueness(String uniqueness)
        {
            this.uniqueness = uniqueness;
        }

        public void setMaxDepth(int maxDepth)
        {
            this.maxDepth = maxDepth;
        }

        public void setReturnFilter(String returnFilter)
        {
            this.returnFilter = returnFilter;
        }

        public void setRelationships(Relationship relationships)
        {
            this.relationships = Arrays.asList(relationships);
        }

        public String toJson()
        {
            StringBuilder sb = new StringBuilder();
            
            sb.append("{ ");
            sb.append(" \"order\" : \"").append(order).append("\"");
            sb.append(", ");
            sb.append(" \"uniqueness\" : \"").append(uniqueness).append("\"");
            sb.append(", ");
            
            if (relationships.size() > 0)
            {
                sb.append("\"relationships\" : [");
                
                for (int i = 0 ; i < relationships.size() ; i++)
                {
                    sb.append(relationships.get(i).toJsonCollection());
                    
                    if (i < relationships.size() - 1)
                    { // Miss off the final comma
                        sb.append(", ");
                    }
                }
                
                sb.append("], ");
            }
            
            sb.append("\"return filter\" : { ");
            sb.append("\"language\" : \"builtin\", ");
            sb.append("\"name\" : \"");
            sb.append(returnFilter);
            sb.append("\" }, ");
            sb.append("\"max depth\" : ");
            sb.append(maxDepth);
            sb.append(" }");
            
            return sb.toString();
        }
    }
}
