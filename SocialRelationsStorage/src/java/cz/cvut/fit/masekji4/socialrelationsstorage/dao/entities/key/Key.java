package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key;

/**
 * Třída <code>Key</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Key
{
    private String prefix;
    private String username;

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
