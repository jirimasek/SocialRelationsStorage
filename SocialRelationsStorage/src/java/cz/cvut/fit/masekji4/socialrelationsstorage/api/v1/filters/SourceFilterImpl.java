package cz.cvut.fit.masekji4.socialrelationsstorage.api.v1.filters;

import cz.cvut.fit.masekji4.socialrelationsstorage.common.CollectionUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.common.StringUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * <code>FilterImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class SourceFilterImpl implements SourceFilter
{
    
    private Collection<URI> sources;
    
    public static SourceFilter buildFilter(String sources) throws URISyntaxException
    {
        
        Collection collection = new LinkedList();
        
        if (!StringUtils.isNullOrEmpty(sources))
        {
            String[] array = sources.split(",");
            
            for (int i = 0 ; i < array.length ; i++)
            {
                String source = array[i];
                
                collection.add(new URI(String.format("http://%s", source)));
            }
        }
        
        return new SourceFilterImpl(collection);
    }
    
    private SourceFilterImpl(Collection<URI> sources)
    {
        this.sources = sources;
    }

    @Override
    public boolean isAcceptable(URI uri)
    {
        if (CollectionUtils.isNullOrEmpty(sources))
        {
            return true;
        }
        
        return sources.contains(uri);
    }

    @Override
    public boolean isAcceptable(Collection<URI> uri)
    {
        if (CollectionUtils.isNullOrEmpty(sources))
        {
            return true;
        }
        
        for (URI u : uri)
        {
            if (sources.contains(u))
            {
                return true;
            }
        }
        
        return false;
    }
}
