package cz.cvut.fit.masekji4.socialrelationsstorage.dao;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import static org.junit.Assert.*;
import static cz.cvut.fit.masekji4.socialrelationsstorage.dao.DirectionEnum.ALL;
import static cz.cvut.fit.masekji4.socialrelationsstorage.dao.DirectionEnum.IN;
import static cz.cvut.fit.masekji4.socialrelationsstorage.dao.DirectionEnum.OUT;

import cz.cvut.fit.masekji4.socialrelationsstorage.config.ConfigurationFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.KeyFactory;
import cz.cvut.fit.masekji4.socialrelationsstorage.business.entities.key.Namespace;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.PersistenceManagerImpl;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.Before;
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
    private static Integer ctuMasekJi4Id;
    private static Integer ctuBartolu5Id;
    
    private static Integer foafKnows1;
    private static Integer foafKnows2;
    
    private ConfigurationFactory confFactory;
    
    private PersistenceManager persistence;
    private KeyFactory keyFactory;
    private GraphDAOImpl graphDAO;
    
    private Person fbJiriMa5ek;
    private Person fbBartonekLukas;
    private Person twJiriMasek;
    private Person twXMorfeus;
    private Person ctuMasekJi4;
    private Person ctuBartolu5;

    @Before
    public void init() throws URISyntaxException
    {
        Map<String, Namespace> namespaces = new HashMap<String, Namespace>();
        
        namespaces.put("ctu", new Namespace("ctu", "http://usermap.cvut.cz/profile/", "(http|https)://usermap.cvut.cz/profile/([a-zA-Z0-9]+)", 2));
        namespaces.put("fb", new Namespace("fb", "http://www.facebook.com/", "(http|https)://(www.)?facebook.com/([a-zA-Z0-9\\.]+)", 3));
        namespaces.put("tw", new Namespace("tw", "http://www.twitter.com/", "(http|https)://(www.)?twitter.com/([a-zA-Z0-9]+)", 3));

        confFactory = new ConfigurationFactory();
        
        persistence = new PersistenceManagerImpl(DATABASE_URI);
        keyFactory = new KeyFactory(namespaces);
        graphDAO = new GraphDAOImpl(persistence, keyFactory, 10);
        
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
        
        ctuMasekJi4 = new Person();
        
        ctuMasekJi4.setProfile(new URI("http://usermap.cvut.cz/profile/masekji4"));
        ctuMasekJi4.addSource(new URI("http://usermap.cvut.cz"));
        ctuMasekJi4.addProperty("foaf:name", "Jiří Mašek");
        
        ctuBartolu5 = new Person();
        
        ctuBartolu5.setProfile(new URI("http://usermap.cvut.cz/profile/bartolu5"));
        ctuBartolu5.addSource(new URI("http://usermap.cvut.cz"));
        ctuBartolu5.addProperty("foaf:name", "Lukáš Bartoněk");
    }

    @Test
    public void testCreatePerson() throws URISyntaxException, PersonAlreadyExistsException, InvalidPersonException, InvalidProfileException
    {
        System.out.println("Testing creation of person");
        
        fbJiriMa5ekId = graphDAO.createPerson(fbJiriMa5ek);
        
        assertNotNull(fbJiriMa5ekId);
        
        fbBartonekLukasId = graphDAO.createPerson(fbBartonekLukas);
        
        assertNotNull(fbBartonekLukasId);
        
        twJiriMasekId = graphDAO.createPerson(twJiriMasek);
        
        assertNotNull(twJiriMasekId);
    }

    @Test(expected = PersonAlreadyExistsException.class)
    public void testCreateAlreadyCreatedPerson() throws URISyntaxException, PersonAlreadyExistsException, InvalidPersonException, InvalidProfileException
    {
        System.out.println("Testing creation of already created person");
        
        graphDAO.createPerson(fbJiriMa5ek);
    }
    
    @Test(expected = InvalidPersonException.class)
    public void testCreateIncompletePerson() throws URISyntaxException, PersonAlreadyExistsException, InvalidPersonException, InvalidProfileException
    {
        System.out.println("Testing creation of incomplete person");
        
        Person person = new Person();
        
        person.setProfile(new URI("http://twitter.com/xmorfeus"));
        
        graphDAO.createPerson(person);
        
    }
    
    @Test
    public void testRetrievePerson() throws PersonNotFoundException, URISyntaxException, InvalidProfileException
    {
        System.out.println("Testing retrieve of person");
        
        Person person = graphDAO.retrievePerson(fbJiriMa5ekId);
        
        assertTrue(person.equals(fbJiriMa5ek));
        
        Key key = keyFactory.createKey(new URI("http://www.facebook.com/bartonek.lukas"));
        
        person = graphDAO.retrievePerson(key);
        
        assertTrue(person.equals(fbBartonekLukas));
    }
    
    @Test(expected = PersonNotFoundException.class)
    public void testRetrieveNonexistentPersonById() throws PersonNotFoundException, URISyntaxException
    {
        System.out.println("Testing retrieve of nonexistent person by ID");
        
        graphDAO.retrievePerson(new Integer(98968769));
    }
    
    @Test(expected = PersonNotFoundException.class)
    public void testRetrieveNonexistentPersonByKey() throws PersonNotFoundException, URISyntaxException, InvalidProfileException
    {
        System.out.println("Testing retrieve of nonexistent person by key");
        
        Key key = keyFactory.createKey(new URI("http://twitter.com/xmorfeus"));
        
        graphDAO.retrievePerson(key);
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
    
    @Test
    public void testUpdatePerson() throws URISyntaxException, PersonNotFoundException, InvalidPersonException, InvalidProfileException
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
        
        ctuMasekJi4Id = graphDAO.updatePerson(ctuMasekJi4);
        
        assertNotNull(ctuMasekJi4Id);
        
        ctuBartolu5Id = graphDAO.updatePerson(ctuBartolu5);
        
        assertNotNull(ctuBartolu5Id);
    }
    
    @Test
    public void testCreateRelation() throws URISyntaxException, RelationAlreadyExistsException, PersonNotFoundException, IllegalAccessException, InvalidRelationshipException
    {
        System.out.println("Testing creation of relation");
        
        Relation relation = new Relation();
        
        relation.setObject(fbJiriMa5ekId);
        relation.setSubject(fbBartonekLukasId);
        relation.setType("foaf:knows");
        relation.addSource(new URI("http://www.facebook.com"));
        relation.addProperty("sts:weight", "1");
        
        foafKnows1 = graphDAO.createRelation(relation);
        
        assertNotNull(foafKnows1);
    }
    
    @Test(expected = RelationAlreadyExistsException.class)
    public void testCreateAlreadyCreatedRelation() throws URISyntaxException, RelationAlreadyExistsException, PersonNotFoundException, IllegalAccessException, InvalidRelationshipException
    {
        System.out.println("Testing creation of already created relation");
        
        Relation relation = new Relation();
        
        relation.setObject(fbJiriMa5ekId);
        relation.setSubject(fbBartonekLukasId);
        relation.setType("foaf:knows");
        relation.addSource(new URI("http://www.facebook.com"));
        
        graphDAO.createRelation(relation);
    }
    
    @Test
    public void testRetrieveRelations() throws RelationNotFoundException, URISyntaxException, PersonNotFoundException, IllegalAccessException, InvalidProfileException
    {
        System.out.println("Testing retrieve of relation");
        
        Relation origRel = new Relation();
        
        origRel.setObject(fbJiriMa5ekId);
        origRel.setSubject(fbBartonekLukasId);
        origRel.setType("foaf:knows");
        origRel.addSource(new URI("http://www.facebook.com"));
        origRel.addProperty("sts:weight", "1");
        
        Relation retrRel = graphDAO.retrieveRelation(foafKnows1);
        
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
    
    @Test(expected = RelationNotFoundException.class)
    public void testRetrieveNonexistentRelation() throws RelationNotFoundException, URISyntaxException, PersonNotFoundException, IllegalAccessException
    {
        graphDAO.retrieveRelation(new Integer(98968769));
    }
    
    @Test(expected = PersonNotFoundException.class)
    public void testRetrieveRelationsOfNonexistentById() throws RelationNotFoundException, URISyntaxException, PersonNotFoundException
    {
        graphDAO.retrieveRelations(new Integer(98968769), ALL);
    }
    
    @Test(expected = PersonNotFoundException.class)
    public void testRetrieveRelationsOfNonexistentByKey() throws RelationNotFoundException, URISyntaxException, PersonNotFoundException, InvalidProfileException
    {
        Key key = keyFactory.createKey(new URI("http://twitter.com/jakubsamek"));
        
        graphDAO.retrieveRelations(key, ALL);
    }
    
    @Test
    public void testUpdateRelation() throws URISyntaxException, PersonNotFoundException, RelationNotFoundException, IllegalAccessException
    {
        System.out.println("Testing update of relation");
        
        Relation relation;
        
        relation= new Relation();
        
        relation.setObject(fbJiriMa5ekId);
        relation.setSubject(fbBartonekLukasId);
        relation.setType("foaf:knows");
        relation.addSource(new URI("http://www.facebook.com"));
        relation.addProperty("sts:weight", "2");
        
        Integer oldId = graphDAO.updateRelation(relation);
        
        assertNotNull(oldId);
        assertEquals(oldId, foafKnows1);
        
        Relation rel = graphDAO.retrieveRelation(oldId);
        
        assertEquals(rel.getProperties().get("sts:weight"), "2");
        
        relation= new Relation();
        
        relation.setObject(twJiriMasekId);
        relation.setSubject(twXMorfeusId);
        relation.setType("foaf:knows");
        relation.addSource(new URI("http://twitter.com"));
        
        foafKnows2 = graphDAO.updateRelation(relation);
        
        assertNotNull(foafKnows2);
    }
    
    @Test
    public void testDeclareSameness() throws URISyntaxException, PersonNotFoundException, InvalidProfileException
    {
        System.out.println("Testing declaration of sameness");
        
        Key k1 = keyFactory.createKey(new URI("http://www.facebook.com/jirima5ek"));
        Key k2 = keyFactory.createKey(new URI("http://twitter.com/jirimasek"));
        Key k3 = keyFactory.createKey(new URI("http://usermap.cvut.cz/profile/masekji4"));
        Key k4 = keyFactory.createKey(new URI("http://www.facebook.com/bartonek.lukas"));
        Key k5 = keyFactory.createKey(new URI("http://twitter.com/xmorfeus"));
        
        List<URI> sources;
        
        sources = new ArrayList<URI>();
        
        sources.add(new URI("http://about.me"));
        
        graphDAO.declareSameness(k1, k2, sources);
        
        sources = new ArrayList<URI>();
        
        sources.add(new URI("http://usermap.cvut.cz"));
        
        graphDAO.declareSameness(k2, k3, sources);
        
        sources = new ArrayList<URI>();
        
        sources.add(new URI("http://foursquare.com"));
        
        graphDAO.declareSameness(k4, k5, sources);
    }
    
    @Test
    public void testRetrieveAlterEgos() throws PersonNotFoundException, URISyntaxException, InvalidProfileException
    {
        System.out.println("Testing retrieve of alter egos");
        
        Path path;
        
        path = graphDAO.retrieveAlterEgos(fbJiriMa5ekId);
        
        assertEquals(path.getPerson().getProfile(), fbJiriMa5ek.getProfile());
        assertEquals(path.getPersons().size(), 1);
        
        path = path.getPersons().iterator().next();
        
        assertEquals(path.getPerson().getProfile(), twJiriMasek.getProfile());
        assertEquals(path.getPersons().size(), 1);
        assertNotNull(path.getSources());
        
        path = path.getPersons().iterator().next();
        
        assertEquals(path.getPerson().getProfile(), ctuMasekJi4.getProfile());
        assertEquals(path.getPersons(), null);
        assertNotNull(path.getSources());
        
        Key key;
        
        key = keyFactory.createKey(new URI("http://twitter.com/jirimasek"));
        
        path = graphDAO.retrieveAlterEgos(key);
        
        assertEquals(path.getPerson().getProfile(), twJiriMasek.getProfile());
        assertEquals(path.getPersons().size(), 2);
        
        Iterator<Path> iterator = path.getPersons().iterator();
        
        path = iterator.next();
        
        assertEquals(path.getPersons(), null);
        assertNotNull(path.getSources());
        
        path = iterator.next();
        
        assertEquals(path.getPersons(), null);
        assertNotNull(path.getSources());
        
        key = keyFactory.createKey(new URI("http://twitter.com/xmorfeus"));
        
        path = graphDAO.retrieveAlterEgos(key);
        
        assertEquals(path.getPerson().getProfile(), twXMorfeus.getProfile());
        assertEquals(path.getPersons().size(), 1);
        
        path = path.getPersons().iterator().next();
        
        assertEquals(path.getPerson().getProfile(), fbBartonekLukas.getProfile());
        assertEquals(path.getPersons(), null);
        assertNotNull(path.getSources());
        
        path = graphDAO.retrieveAlterEgos(ctuBartolu5Id);
        
        assertEquals(path.getPerson().getProfile(), ctuBartolu5.getProfile());
        assertEquals(path.getPersons(), null);
    }
    
    @Test(expected = PersonNotFoundException.class)
    public void testRetrieveAlterEgosForNonexistentPersonById() throws PersonNotFoundException
    {
        System.out.println("Testing retrieve of alter egos for nonexistent ID");
        
        graphDAO.retrieveAlterEgos(new Integer(98968769));
    }
    
    @Test(expected = PersonNotFoundException.class)
    public void testRetrieveAlterEgosForNonexistentPersonByKey() throws PersonNotFoundException, URISyntaxException, InvalidProfileException
    {
        System.out.println("Testing retrieve of alter egos for nonexistent key");
        
        Key key = keyFactory.createKey(new URI("http://twitter.com/jakubsamek"));
        
        graphDAO.retrieveAlterEgos(key);
    }
    
    @Test
    public void testResufeSameness() throws PersonNotFoundException, URISyntaxException, InvalidProfileException
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
    
    @Test
    public void testDeleteRelation() throws IllegalAccessException
    {
        System.out.println("Testing deletion of relation");
        
        boolean deleted;
                
        deleted = graphDAO.deleteRelation(foafKnows1);
        
        assertTrue(deleted);
                
        deleted = graphDAO.deleteRelation(foafKnows1);
        
        assertFalse(deleted);
    }
    
    @Test
    public void testDeletePerson() throws URISyntaxException, InvalidProfileException
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
        
        key = keyFactory.createKey(new URI("http://usermap.cvut.cz/profile/masekji4"));
        
        deleted = graphDAO.deletePerson(key);
        
        assertTrue(deleted);
        
        key = keyFactory.createKey(new URI("http://usermap.cvut.cz/profile/bartolu5"));
        
        deleted = graphDAO.deletePerson(key);
        
        assertTrue(deleted);
    }
}
