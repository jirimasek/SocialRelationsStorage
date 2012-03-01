package cz.cvut.fit.masekji4.socialrelationsstorage.utils;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Třída <code>NamespaceService</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Stateless
public class NamespaceService
{

    @Inject
    @Config
    Map<String, Namespace> namespaces;

    public String getUid(URI uri) throws IllegalArgumentException
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

                String username = matcher.group(namespace.getGroup());
                String uid = prefix + ":" + username;

                return uid;
            }
        }

        throw new IllegalArgumentException();
    }

    public URI getUri(String uid) throws IllegalArgumentException, URISyntaxException
    {
        String[] s = uid.split(":");

        String prefix = s[0];
        String username = s[1];

        if (namespaces.containsKey(prefix))
        {
            return new URI(namespaces.get(prefix) + username);
        }

        throw new IllegalArgumentException();
    }
}
