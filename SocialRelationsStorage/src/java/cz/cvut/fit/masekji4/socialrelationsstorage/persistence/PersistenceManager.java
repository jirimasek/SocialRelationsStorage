package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import com.sun.jersey.api.client.UniformInterfaceException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.GraphElement;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Node;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Relation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.json.JSONException;

/**
 * Rozhraní <code>PersistenceManager</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface PersistenceManager
{
    public URI createNode() throws UniformInterfaceException;
    
    public void addProperty(URI uri, String property, String value);
    
    public Node retrieveNode(String uid) throws URISyntaxException, JSONException;

    public Node retrieveNode(URI uri) throws URISyntaxException, JSONException;
    
    public URI createRelation(URI startNode, URI endNode, String relationship)
        throws URISyntaxException;
    
    public void addMetadataToProperty(URI uri, String property, String value)
        throws URISyntaxException;
    
    public Relation getRelation(URI node) throws URISyntaxException;
    
    public Relation getRelation(URI startNode, URI endNode, String relationship)
        throws URISyntaxException, JSONException;
    
    public List<Relation> getRelations(URI node, DirectionEnum direction)
        throws JSONException, URISyntaxException;
    
    public <T extends GraphElement> List<T> traverse(URI startNode, TraversalDescription t)
            throws URISyntaxException, JSONException;
}
