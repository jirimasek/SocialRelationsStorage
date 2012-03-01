package cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities;

import java.net.URI;
import java.util.Map;

/**
 * Třída <code>Node</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Node implements GraphElement
{
    private URI uri;
    private Map<String, String> properties;

    public Node()
    {
        this.uri = null;
        this.properties = null;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }
}
