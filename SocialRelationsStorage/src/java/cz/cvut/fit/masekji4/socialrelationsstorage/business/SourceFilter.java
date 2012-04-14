package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import java.net.URI;
import java.util.Collection;

/**
 * Interface <code>Filter</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public interface SourceFilter
{
    
    public boolean isAcceptable(URI uri);
    
    public boolean isAcceptable(Collection<URI> uri);
}
