package cz.cvut.fit.masekji4.socialrelationsstorage.common;

import javax.ws.rs.core.MultivaluedMap;

/**
 * Třída <code>FilterService</code>
 *
 * @author Jiří Mašek <email@jirimasek.cz>
 */
public class FilterService
{
    
    public Filter createFilter(MultivaluedMap<String, String> params)
    {
        Filter filter = new Filter();
        
        return filter;
    }
}
