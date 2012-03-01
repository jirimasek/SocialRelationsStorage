package cz.cvut.fit.masekji4.socialrelationsstorage.business.entities;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Node;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.utils.NamespaceService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Třída <code>EntityFactory</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Stateless
public class EntityFactory
{
    
    @Inject
    private NamespaceService ns;
    
    @Inject
    @Config
    private String PROPERTY_HOMEPAGE;

    public Person create(Node node) throws URISyntaxException
    {
        Person person = new Person();
        
        URI homepage = new URI(node.getProperties().get(PROPERTY_HOMEPAGE));

        person.setUid(ns.getUid(homepage));

        return person;
    }

    public List<Person> create(List<Node> nodes) throws URISyntaxException
    {
        List<Person> persons = new ArrayList<Person>();
        
        for (Node node : nodes)
        {
            persons.add(create(node));
        }
        
        return persons;
    }

    public Relationship create(Relation relation)
    {
        Relationship relationship = new Relationship();
        
        return relationship;
    }
}
