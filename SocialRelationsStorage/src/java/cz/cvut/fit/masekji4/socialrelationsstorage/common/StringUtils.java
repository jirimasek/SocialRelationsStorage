package cz.cvut.fit.masekji4.socialrelationsstorage.common;

/**
 * <code>StringUtils</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class StringUtils
{
    
    public static boolean isNullOrEmpty(String string)
    {
        if (string == null || string.isEmpty())
        {
            return true;
        }
            
        return false;
    }
}
