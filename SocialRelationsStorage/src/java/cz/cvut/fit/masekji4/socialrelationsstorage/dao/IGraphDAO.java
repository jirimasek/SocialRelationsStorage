package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import com.sun.jersey.api.client.UniformInterfaceException;
import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONException;

/**
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface IGraphDAO
{
    public URI createNode() throws UniformInterfaceException;
    
    public void addProperty(URI uri, String property, String value);
    
    public URI addRelationship(URI startNode, URI endNode, String relationship)
        throws URISyntaxException;
    
    public void addMetadataToProperty(URI uri, String property, String value)
        throws URISyntaxException;
    
    public URI getNodeURI(String id)
        throws URISyntaxException, JSONException;
    
    public boolean getRelationURI(URI startNode, URI endNode, String relationship)
        throws URISyntaxException, JSONException;
}
