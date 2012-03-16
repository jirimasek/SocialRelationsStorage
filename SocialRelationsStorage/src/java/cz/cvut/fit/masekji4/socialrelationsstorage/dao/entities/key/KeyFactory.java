package cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import java.net.URI;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Třída <code>KeyFactory</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Stateless
@Default
public class KeyFactory
{

    @Inject
    @Config
    private Map<String, Namespace> namespaces;

    public KeyFactory()
    {
    }

    public KeyFactory(Map<String, Namespace> namespaces)
    {
        this.namespaces = namespaces;
    }
    
    public Key createKey(URI uri) throws IllegalArgumentException
    {
        for (String prefix : namespaces.keySet())
        {
            Namespace namespace = namespaces.get(prefix);

            Pattern pattern = Pattern.compile(namespace.getRegex());
            Matcher matcher = pattern.matcher(uri.toString());

            if (matcher.matches())
            {
                matcher = pattern.matcher(uri.toString());
                matcher.find();
                
                Key key = new Key();
                
                key.setPrefix(prefix);
                key.setUsername(matcher.group(namespace.getGroup()));
                
                return key;
            }
        }

        throw new IllegalArgumentException();
    }
}
