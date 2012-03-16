package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key;

/**
 * Třída <code>Namespace</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class Namespace
{
    private String prefix;
    private String uri;
    private String regex;
    private int group;

    public Namespace(String prefix, String uri, String regex, int group)
    {
        this.prefix = prefix;
        this.uri = uri;
        this.regex = regex;
        this.group = group;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getRegex()
    {
        return regex;
    }

    public void setRegex(String regex)
    {
        this.regex = regex;
    }

    public int getGroup()
    {
        return group;
    }

    public void setGroup(int group)
    {
        this.group = group;
    }
}
