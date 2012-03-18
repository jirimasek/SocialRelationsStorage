package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import static org.junit.Assert.*;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.ALL;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.IN;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.OUT;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.ConfigurationFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.KeyFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManager;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManagerImpl;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <code>PersistenceManagerImplTest</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class GraphDAOImplTest
{

    private static final String DATABASE_URI = "http://localhost:7474/db/data";
    
    private static Integer fbJiriMa5ekId;
    private static Integer fbBartonekLukasId;
    private static Integer twJiriMasekId;
    private static Integer twXMorfeusId;
    
    private static Integer foafKnows;
    
    private ConfigurationFactory confFactory;
    
    private PersistenceManager persistence;
    private KeyFactory keyFactory;
    private GraphDAOImpl graphDAO;
    
    private Person fbJiriMa5ek;
    private Person fbBartonekLukas;
    private Person twJiriMasek;
    private Person twXMorfeus;

    /**
     * 
     * @throws URISyntaxException 
     */
    @Before
    public void init() throws URISyntaxException
    {
        confFactory = new ConfigurationFactory();
        
        persistence = new PersistenceManagerImpl(DATABASE_URI);
        keyFactory = new KeyFactory(confFactory.getNamespaces(null));
        graphDAO = new GraphDAOImpl(persistence, keyFactory);
        
        fbJiriMa5ek = new Person();
        
        fbJiriMa5ek.setProfile(new URI("http://www.facebook.com/jirima5ek"));
        fbJiriMa5ek.addSource(new URI("http://www.facebook.com"));
        fbJiriMa5ek.addProperty("foaf:name", "Jiří Mašek");
        
        fbBartonekLukas = new Person();
        
        fbBartonekLukas.setProfile(new URI("http://www.facebook.com/bartonek.lukas"));
        fbBartonekLukas.addSource(new URI("http://www.facebook.com"));
        fbBartonekLukas.addProperty("foaf:name", "Lukáš Bartoněk");
        
        twJiriMasek = new Person();
        
        twJiriMasek.setProfile(new URI("http://twitter.com/jirimasek"));
        twJiriMasek.addSource(new URI("http://twitter.com"));
        twJiriMasek.addProperty("foaf:name", "Jiří Mašek");
        
        twXMorfeus = new Person();
        
        twXMorfeus.setProfile(new URI("http://twitter.com/xmorfeus"));
        twXMorfeus.addSource(new URI("http://twitter.com"));
        twXMorfeus.addProperty("foaf:name", "Lukáš Bartoněk");
    }

    /**
     * 
     * @throws URISyntaxException
     * @throws PersonAlreadyExistsException 
     */
    @Test
    public void testCreatePerson() throws URISyntaxException, PersonAlreadyExistsException
    {
        System.out.println("Testing creation of person");
        
        fbJiriMa5ekId = graphDAO.createPerson(fbJiriMa5ek);
        
        assertNotNull(fbJiriMa5ekId);
        
        fbBartonekLukasId = graphDAO.createPerson(fbBartonekLukas);
        
        assertNotNull(fbBartonekLukasId);
        
        twJiriMasekId = graphDAO.createPerson(twJiriMasek);
        
        assertNotNull(twJiriMasekId);
    }

    /**
     * 
     * @throws URISyntaxException
     * @throws PersonAlreadyExistsException 
     */
    @Test(expected = PersonAlreadyExistsException.class)
    public void testCreateAlreadyCreatedPerson() throws URISyntaxException, PersonAlreadyExistsException
    {
        System.out.println("Testing creation of already created person");
        
        graphDAO.createPerson(fbJiriMa5ek);
    }
    
    /**
     * 
     * @throws URISyntaxException
     * @throws PersonAlreadyExistsException 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateIncompletePerson() throws URISyntaxException, PersonAlreadyExistsException
    {
        System.out.println("Testing creation of incomplete person");
        
        Person person = new Person();
        
        person.setProfile(new URI("http://twitter.com/xmorfeus"));
        
        graphDAO.createPerson(person);
        
    }
    
    /**
     * 
     * @throws PersonNotFoundException
     * @throws URISyntaxException 
     */
    @Test
    public void testRetrievePerson() throws PersonNotFoundException, URISyntaxException
    {
        System.out.println("Testing retrieve of person");
        
        Person person = graphDAO.retrievePerson(fbJiriMa5ekId);
        
        assertTrue(person.equals(fbJiriMa5ek));
        
        Key key = keyFactory.createKey(new URI("http://www.facebook.com/bartonek.lukas"));
        
        person = graphDAO.retrievePerson(key);
        
        assertTrue(person.equals(fbBartonekLukas));
    }
    
    @Test
    public void testRetrievePersons() throws URISyntaxException
    {
        System.out.println("Testing retrieve of persons");
        
        List<Person> persons;
        
        persons = graphDAO.retrievePersons(new URI("http://www.facebook.com"));
        
        assertEquals(persons.size(), 2);
        assertTrue(persons.contains(fbJiriMa5ek));
        assertTrue(persons.contains(fbBartonekLukas));
        
        persons = graphDAO.retrievePersons(new URI("http://twitter.com"));
        
        assertEquals(persons.size(), 1);
        assertTrue(persons.contains(twJiriMasek));
        
        persons = graphDAO.retrievePersons(new URI("http://www.twitter.com"));
        
        assertEquals(persons.size(), 0);
        
        persons = graphDAO.retrievePersons(new URI("http://usermap.cvut.cz"));
        
        assertEquals(persons.size(), 0);
    }
    
    /**
     * 
     * @throws URISyntaxException
     * @throws PersonNotFoundException 
     */
    @Test
    public void testUpdatePerson() throws URISyntaxException, PersonNotFoundException
    {
        System.out.println("Testing update of person");
        
        fbJiriMa5ek.addSource(new URI("http://jirimasek.cz"));
        fbJiriMa5ek.addProperty("foaf:birthday", "1988-07-11");
        
        Integer oldId = graphDAO.updatePerson(fbJiriMa5ek);
        
        assertNotNull(oldId);
        assertEquals(oldId, fbJiriMa5ekId);
        
        Person person;
        
        person = graphDAO.retrievePerson(fbJiriMa5ekId);
        
        assertTrue(person.getSources().contains(new URI("http://jirimasek.cz")));
        assertTrue(person.getProperties().containsKey("foaf:birthday"));
        
        twXMorfeusId = graphDAO.updatePerson(twXMorfeus);
        
        assertNotNull(twXMorfeusId);
    }
    
    /**
     * 
     * @throws URISyntaxException
     * @throws RelationAlreadyExistsException
     * @throws PersonNotFoundException 
     */
    @Test
    public void testCreateRelation() throws URISyntaxException, RelationAlreadyExistsException, PersonNotFoundException
    {
        System.out.println("Testing creation of relation");
        
        Relation relation = new Relation();
        
        relation.setObject(fbJiriMa5ekId);
        relation.setSubject(fbBartonekLukasId);
        relation.setType("foaf:knows");
        relation.addSource(new URI("http://www.facebook.com"));
        relation.addProperty("sts:weight", "1");
        
        foafKnows = graphDAO.createRelation(relation);
        
        assertNotNull(foafKnows);
    }
    
    /**
     * 
     * @throws RelationNotFoundException
     * @throws URISyntaxException
     * @throws PersonNotFoundException 
     */
    @Test
    public void testRetrieveRelation() throws RelationNotFoundException, URISyntaxException, PersonNotFoundException
    {
        System.out.println("Testing retrieve of relation");
        
        Relation origRel = new Relation();
        
        origRel.setObject(fbJiriMa5ekId);
        origRel.setSubject(fbBartonekLukasId);
        origRel.setType("foaf:knows");
        origRel.addSource(new URI("http://www.facebook.com"));
        origRel.addProperty("sts:weight", "1");
        
        Relation retrRel = graphDAO.retrieveRelation(foafKnows);
        
        assertEquals(retrRel, origRel);
        
        List<Relation> relations;
        
        relations = graphDAO.retrieveRelations(fbJiriMa5ekId, ALL);
        
        assertNotNull(relations);
        assertEquals(relations.size(), 1);
        assertEquals(relations.get(0), origRel);
        
        relations = graphDAO.retrieveRelations(fbBartonekLukasId, IN);
        
        assertNotNull(relations);
        assertEquals(relations.size(), 1);
        assertEquals(relations.get(0), origRel);
        
        relations = graphDAO.retrieveRelations(fbBartonekLukasId, OUT);
        
        assertNotNull(relations);
        assertEquals(relations.size(), 0);
        
        Key key;
        
        key = keyFactory.createKey(new URI("http://www.facebook.com/jirima5ek"));
        
        relations = graphDAO.retrieveRelations(key, OUT);
        
        assertNotNull(relations);
        assertEquals(relations.size(), 1);
        assertEquals(relations.get(0), origRel);
        
        key = keyFactory.createKey(new URI("http://twitter.com/jirimasek"));
        
        relations = graphDAO.retrieveRelations(key, ALL);
        
        assertNotNull(relations);
        assertEquals(relations.size(), 0);
    }
    
    /**
     * 
     * @throws URISyntaxException
     * @throws PersonNotFoundException 
     */
    @Test
    public void testDeclareSameness() throws URISyntaxException, PersonNotFoundException
    {
        System.out.println("Testing declaration of sameness");
        
        Key k1 = keyFactory.createKey(new URI("http://www.facebook.com/jirima5ek"));
        Key k2 = keyFactory.createKey(new URI("http://twitter.com/jirimasek"));
        
        List<URI> sources = new ArrayList<URI>();
        
        sources.add(new URI("http://about.me"));
        
        graphDAO.declareSameness(k1, k2, sources);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 
     * @throws PersonNotFoundException
     * @throws URISyntaxException 
     */
    @Test
    public void testResufeSameness() throws PersonNotFoundException, URISyntaxException
    {
        System.out.println("Testing refusal of sameness");
        
        Key k1 = keyFactory.createKey(new URI("http://www.facebook.com/jirima5ek"));
        Key k2 = keyFactory.createKey(new URI("http://twitter.com/jirimasek"));
        
        boolean deleted;
        
        deleted = graphDAO.refuseSameness(k1, k2);
        
        assertTrue(deleted);
        
        deleted = graphDAO.refuseSameness(k1, k2);
        
        assertFalse(deleted);
    }
    
    /**
     * 
     */
    @Test
    public void testDeleteRelation()
    {
        System.out.println("Testing deletion of relation");
        
        boolean deleted;
                
        deleted = graphDAO.deleteRelation(foafKnows);
        
        assertTrue(deleted);
                
        deleted = graphDAO.deleteRelation(foafKnows);
        
        assertFalse(deleted);
    }
    
    /**
     * 
     * @throws URISyntaxException 
     */
    @Test
    public void testDeletePerson() throws URISyntaxException
    {
        System.out.println("Testing deletion of person");
        
        boolean deleted;
        
        deleted = graphDAO.deletePerson(fbJiriMa5ekId);
        
        assertTrue(deleted);
        
        deleted = graphDAO.deletePerson(fbJiriMa5ekId);
        
        assertFalse(deleted);
        
        Key key;
        
        key = keyFactory.createKey(new URI("http://www.facebook.com/bartonek.lukas"));
        
        deleted = graphDAO.deletePerson(key);
        
        assertTrue(deleted);
        
        deleted = graphDAO.deletePerson(key);
        
        assertFalse(deleted);
        
        key = keyFactory.createKey(new URI("http://twitter.com/jirimasek"));
        
        deleted = graphDAO.deletePerson(key);
        
        assertTrue(deleted);
        
        key = keyFactory.createKey(new URI("http://twitter.com/xmorfeus"));
        
        deleted = graphDAO.deletePerson(key);
        
        assertTrue(deleted);
    }
}
