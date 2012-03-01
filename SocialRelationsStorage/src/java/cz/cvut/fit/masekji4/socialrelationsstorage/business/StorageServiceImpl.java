package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.EntityFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.config.Config;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.OUT;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relationship;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.SameAs;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Node;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.utils.NamespaceService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.json.JSONException;

/**
 * Třída <code>StorageServiceImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Stateless
public class StorageServiceImpl implements StorageService
{

    @Inject
    private PersistenceManager pm;
    
    @Inject
    private NamespaceService ns;
    
    @Inject
    private EntityFactory ef;
    
    @Inject
    @Config
    private String PROPERTY_HOMEPAGE;
    
    @Inject
    @Config
    private String RELATION_SAMEAS;
    
    @Inject
    @Config
    private int RELATION_SAMEAS_TRAVERSE_MAX_DEPTH;

    /**
     * 
     * @param uid
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    @Override
    public Person getPerson(String uid) throws JSONException, URISyntaxException
    {
        Node person = pm.retrieveNode(uid);

        if (person != null)
        {
            Person p = ef.create(person);
/*
            List<Node> nodes = pm.traverse(person.getUri(), RELATION_SAMEAS,
                    RELATION_SAMEAS_TRAVERSE_MAX_DEPTH);
            
            List<Person> persons = ef.create(nodes);
            List<SameAs> sameAs = new ArrayList<SameAs>();
            
            for (Person per : persons)
            {
                sameAs.add(new SameAs());
            }
            
            p.setSameAs(sameAs);
*/
            return p;
        }

        return null;
    }

    /**
     * 
     * @param uid
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    @Override
    public List<Relationship> getRelationships(String uid) throws JSONException, URISyntaxException
    {
        Node person = pm.retrieveNode(uid);

        if (person != null)
        {
            List<Relation> relations = pm.getRelations(person.getUri(), OUT);

            List<Relationship> relationships = new ArrayList<Relationship>();

            for (Relation rel : relations)
            {
                Node subject = pm.retrieveNode(rel.getEnd());

                URI sub = new URI(subject.getProperties().get(PROPERTY_HOMEPAGE));

                Relationship r = new Relationship();

                Person o = new Person(uid);
                Person s = new Person(ns.getUid(sub));

                r.setObject(o);
                r.setSubject(s);
                r.setRelationship(rel.getType());

                relationships.add(r);
            }

            return relationships;
        }

        return null;
    }

    /**
     * 
     * @param uid
     * @param relation
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    @Override
    public List<Relationship> getRelationships(String uid, String relation)
            throws JSONException, URISyntaxException
    {
        Node person = pm.retrieveNode(uid);

        if (person != null)
        {
            List<Relation> relations = pm.getRelations(person.getUri(), OUT);

            List<Relationship> relationships = new ArrayList<Relationship>();

            for (Relation rel : relations)
            {
                if (rel.getType().equals(relation))
                {
                    Node subject = pm.retrieveNode(rel.getEnd());

                    URI sub = new URI(subject.getProperties().get(PROPERTY_HOMEPAGE));

                    Relationship r = new Relationship();

                    Person o = new Person(uid);
                    Person s = new Person(ns.getUid(sub));

                    r.setObject(o);
                    r.setSubject(s);
                    r.setRelationship(relation);

                    relationships.add(r);
                }
            }

            return relationships;
        }

        return null;
    }

    /**
     * 
     * @param object
     * @param subject
     * @param relation
     * @return
     * @throws JSONException
     * @throws URISyntaxException 
     */
    @Override
    public Relationship getRelationship(String object, String subject,
            String relation)
            throws JSONException, URISyntaxException
    {
        Node obj = pm.retrieveNode(object);
        Node sub = pm.retrieveNode(subject);

        Relation rel = pm.getRelation(obj.getUri(), sub.getUri(), relation);

        if (rel != null)
        {
            Relationship r = new Relationship();

            Person o = new Person(object);
            Person s = new Person(subject);

            r.setObject(o);
            r.setSubject(s);
            r.setRelationship(relation);

            return r;
        }

        return null;
    }

    /**
     * 
     * @param person
     * @return 
     */
    @Override
    public boolean savePerson(Person person)
    {
        return true;
    }

    /**
     * 
     * @param relation
     * @return 
     */
    @Override
    public boolean saveRelation(Relationship relation)
    {
        return true;
    }
}
