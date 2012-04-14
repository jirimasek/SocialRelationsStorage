package cz.cvut.fit.masekji4.socialrelationsstorage.common;

import java.util.Collection;

/**
 * <code>CollectionUtils</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class CollectionUtils
{
 
    /**
     * 
     * @param collection
     * @return 
     */
    public static boolean isNullOrEmpty(Collection collection)
    {
        if (collection == null || collection.isEmpty())
        {
            return true;
        }
        
        return false;
    }
}
