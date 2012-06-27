package cz.cvut.fit.masekji4.socialrelationsstorage.common;

/**
 * <code>NumberUtils</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class NumberUtils
{
    
    public static boolean isInt(String string)
    {
        try
        {
            int number = Integer.parseInt(string);
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
        
        return true;
    }
}
