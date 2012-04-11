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

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        final Key other = (Key) obj;

        if ((this.prefix == null) ? (other.prefix != null) : !this.prefix.equals(
                other.prefix))
        {
            return false;
        }

        if ((this.username == null) ? (other.username != null) : !this.username.
                equals(other.username))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        return hash;
    }
}
