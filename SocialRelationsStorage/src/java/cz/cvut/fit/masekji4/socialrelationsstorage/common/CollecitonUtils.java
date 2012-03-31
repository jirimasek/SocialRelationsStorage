package cz.cvut.fit.masekji4.socialrelationsstorage.common;

import java.util.Collection;

/**
 * <code>CollecitonUtils</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class CollecitonUtils
{
    
    /**
     * 
     * @param c
     * @return 
     */
    public static boolean isNullOrEmpty(Collection c)
    {
        if (c == null || c.isEmpty())
        {
            return true;
        }
        
        return false;
    }
}
