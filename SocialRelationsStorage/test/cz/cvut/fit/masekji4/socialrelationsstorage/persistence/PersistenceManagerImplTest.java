package cz.cvut.fit.masekji4.socialrelationsstorage.persistence;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.TraversalDescription;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.TypeEnum;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeIndexNotFoundException;
import static org.junit.Assert.*;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.ALL;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.IN;
import static cz.cvut.fit.masekji4.socialrelationsstorage.persistence.config.DirectionEnum.OUT;

import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.CannotDeleteNodeException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidMetadataException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.MetadataNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.RelationshipNotFoundException;
import org.codehaus.jettison.json.JSONException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidPropertiesException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.NodeNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.PropertyNotFoundException;
import java.util.Iterator;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * <code>PersistenceManagerImplTest</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
public class PersistenceManagerImplTest
{

    private static final String DATABASE_URI = "http://localhost:7474/db/data";
    
    private PersistenceManagerImpl pm;
    
    private static int fbJirima5ek;
    private static int twJirimasek;
    private static int ctuMasekji4;
    private static int fbBartonekLukas;
    private static int twXmorfeus;
    private static int ctuBartolu5;
    private static int foafKnows;
    private static int owlSameAs;
    
    private JSONObject fbJirima5ekProperties;
    private JSONObject fbBartonekLukasProperties;
    private JSONObject twJirimasekProperties;
    private JSONObject twXmorfeusProperties;
    private JSONObject ctuMasekji4Properties;
    private JSONObject ctuBartolu5Properties;
    private JSONObject foafKnowsProperties;
    private JSONObject owlSameAsProperties;

    @Before
    public void init() throws JSONException
    {
        pm = new PersistenceManagerImpl(DATABASE_URI);

        // Inicializace vlastností

        fbJirima5ekProperties = new JSONObject();

        fbJirima5ekProperties.put("foaf:homepage",
                "http://www.facebook.com/jirima5ek");
        fbJirima5ekProperties.put("foaf:name", "Jiří Mašek");
        fbJirima5ekProperties.put("sioc:note", "http://www.facebook.com");

        // Inicializace vlastností

        fbBartonekLukasProperties = new JSONObject();

        fbBartonekLukasProperties.put("foaf:homepage",
                "http://www.facebook.com/bartonek.lukas");
        fbBartonekLukasProperties.put("foaf:name", "Lukáš Bartoněk");
        fbBartonekLukasProperties.put("sioc:note", "http://www.facebook.com");

        twJirimasekProperties = new JSONObject();

        twJirimasekProperties.put("foaf:homepage",
                "http://twitter.com/jirimasek");
        twJirimasekProperties.put("foaf:name", "Jiří Mašek");
        twJirimasekProperties.put("sioc:note", "http://twitter.com");

        ctuMasekji4Properties = new JSONObject();

        ctuMasekji4Properties.put("foaf:homepage",
                "http://usermap.cvut.cz/profile/masekji4");
        ctuMasekji4Properties.put("foaf:name", "Jiří Mašek");
        ctuMasekji4Properties.put("sioc:note", "http://usermap.cvut.cz");

        twXmorfeusProperties = new JSONObject();

        twXmorfeusProperties.put("foaf:homepage", "http://twitter.com/xmorfeus");
        twXmorfeusProperties.put("foaf:name", "Lukáš Bartoněk");
        twXmorfeusProperties.put("sioc:note", "http://twitter.com");

        ctuBartolu5Properties = new JSONObject();

        ctuBartolu5Properties.put("foaf:homepage",
                "http://usermap.cvut.cz/profile/bartolu5");
        ctuBartolu5Properties.put("foaf:name", "Lukáš Bartoněk");
        ctuBartolu5Properties.put("sioc:note", "http://usermap.cvut.cz");
        
        foafKnowsProperties = new JSONObject();
        
        foafKnowsProperties.append("sioc:note", "http://www.facebook.com/");
        
        owlSameAsProperties = new JSONObject();
        
        owlSameAsProperties.append("sioc:note", "http://about.me/");
    }

    /**
     * 
     * @throws InvalidPropertiesException 
     */
    @Test
    public void testCreateNode() throws InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlů");

        // Vytvoření uživatele bez vlastností

        fbJirima5ek = pm.createNode();

        assertTrue(fbJirima5ek > 0);

        System.out.println("    Uzel: " + fbJirima5ek);

        // Vytvoření uživatele s vlastnostmi, přičemž objekt reprezentující
        // vlasntosti je prázdný

        JSONObject emptyProperties = new JSONObject();

        fbBartonekLukas = pm.createNode(emptyProperties);

        assertTrue(fbBartonekLukas > 0);

        System.out.println("    Uzel: " + fbBartonekLukas);
    }

    /**
     * 
     * @throws JSONException
     * @throws InvalidPropertiesException 
     */
    @Test
    public void testCreateNodeWithProperties() throws JSONException, InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlů s parametry");

        // Vytvoření uzlu s vlastnostmi

        twJirimasek = pm.createNode(twJirimasekProperties);

        assertTrue(twJirimasek > 0);

        System.out.println("    Uzel: " + twJirimasek);

        twXmorfeus = pm.createNode(twXmorfeusProperties);

        assertTrue(twXmorfeus > 0);

        System.out.println("    Uzel: " + twXmorfeus);
    }

    /**
     * 
     * @throws JSONException
     * @throws InvalidPropertiesException 
     */
    @Test(expected = InvalidPropertiesException.class)
    public void testCreateNodeWithInvalidProperties() throws JSONException, InvalidPropertiesException
    {
        System.out.println("Test vytváření uzlu s invalidními parametry");

        // Vytvoření uzlu s vlastnostmi, přičemž objekt reprezentující vlasnosti
        // obsahuje vnožený objekt

        JSONObject nestedValue = new JSONObject();

        nestedValue.put("key", "value");

        JSONObject invalidProperties = new JSONObject();

        invalidProperties.put("foaf:name", nestedValue);

        pm.createNode(invalidProperties);
    }

    /**
     * 
     * @throws NodeNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveNode() throws NodeNotFoundException, JSONException
    {
        System.out.println("Test získávání informací o uzlech");

        JSONObject object = pm.retrieveNode(twXmorfeus);

        assertNotNull(object);

        // Ověření, že se URI uzlu shodují

        assertTrue(object.getString("self").endsWith(String.valueOf(twXmorfeus)));

        // Ověření, že se shodují vlastnosti uzlu

        JSONObject data = object.getJSONObject("data");

        for (Iterator it = twXmorfeusProperties.keys() ; it.hasNext() ;)
        {
            String key = (String) it.next();

            assertTrue(data.has(key));
            assertEquals(data.get(key), twXmorfeusProperties.get(key));
        }
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testRetrieveNonexistentNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání informací o neexistujících uzlech");

        pm.retrieveNode(98968769);
    }

    /**
     * 
     * @throws NodeNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveProperties() throws NodeNotFoundException, JSONException
    {
        System.out.println("Test získávání vlastností uzlu");

        JSONObject properties = pm.retrieveProperties(twXmorfeus);

        for (Iterator it = twXmorfeusProperties.keys() ; it.hasNext() ;)
        {
            String key = (String) it.next();

            assertTrue(properties.has(key));
            assertEquals(properties.get(key), twXmorfeusProperties.get(key));
        }
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testRetrievePropertiesOfNonexistentNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání vlastností neexistujícího uzlu");

        pm.retrieveProperties(98968769);
    }

    /**
     * 
     * @throws PropertyNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveProperty() throws PropertyNotFoundException, JSONException
    {
        System.out.println("Test získávání vlastnosti uzlu");

        String property = pm.retrieveProperty(twXmorfeus, "foaf:name");

        assertEquals(property, twXmorfeusProperties.get("foaf:name"));
    }

    /**
     * 
     * @throws PropertyNotFoundException 
     */
    @Test(expected = PropertyNotFoundException.class)
    public void testRetrieveNonexistentProperty() throws PropertyNotFoundException
    {
        System.out.println("Test získávání neexistující vlastnosti uzlu");

        pm.retrieveProperty(fbJirima5ek, "foaf:name");
    }

    /**
     * 
     * @throws InvalidPropertiesException
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    @Test(expected = InvalidPropertiesException.class)
    public void testAddInvalidProperties() throws InvalidPropertiesException,
            JSONException, NodeNotFoundException
    {
        System.out.println("Test přidání invalidních parametrů uzlu");

        // Přidání vlastností uzlu, přičemž objekt reprezentující vlasnosti
        // obsahuje vnožený objekt

        JSONObject nestedValue = new JSONObject();

        nestedValue.put("key", "value");

        JSONObject invalidProperties = new JSONObject();

        invalidProperties.put("foaf:name", nestedValue);

        pm.addProperties(fbJirima5ek, invalidProperties);
    }

    /**
     * 
     * @throws InvalidPropertiesException
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testAddPropertiesToNonexistingNode() throws InvalidPropertiesException, NodeNotFoundException
    {
        System.out.println("Test přidání vlastností neexistujícímu uzlu");

        pm.addProperties(98968769, fbJirima5ekProperties);
    }

    /**
     * 
     * @throws InvalidPropertiesException
     * @throws NodeNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testAddProperties() throws InvalidPropertiesException, NodeNotFoundException, JSONException
    {
        System.out.println("Test přidávání vlastností uzlu");

        pm.addProperties(fbJirima5ek, fbJirima5ekProperties);

        JSONObject properties = pm.retrieveProperties(fbJirima5ek);

        for (Iterator it = fbJirima5ekProperties.keys() ; it.hasNext() ;)
        {
            String key = (String) it.next();

            assertTrue(properties.has(key));
            assertEquals(properties.get(key), fbJirima5ekProperties.get(key));
        }
    }

    /**
     * 
     * @throws PropertyNotFoundException 
     */
    @Test(expected = PropertyNotFoundException.class)
    public void testDeleteProperty() throws PropertyNotFoundException
    {
        System.out.println("Test smazání vlastnosti uzlu");

        boolean deleted = pm.deleteProperty(fbJirima5ek, "foaf:name");

        assertTrue(deleted);

        pm.retrieveProperty(fbJirima5ek, "foaf:name");
    }

    /**
     * 
     */
    @Test
    public void testDeleteNonexistingProperty()
    {
        System.out.println("Test smazání neexistující vlastnosti uzlu");

        boolean deleted = pm.deleteProperty(fbJirima5ek, "foaf:name");

        assertFalse(deleted);
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test
    public void testDeleteProperties() throws NodeNotFoundException
    {
        System.out.println("Test smazání vlastností uzlu");

        pm.deleteProperties(fbJirima5ek);

        JSONObject properties = pm.retrieveProperties(fbJirima5ek);

        assertTrue(properties.length() == 0);
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testDeleteNonexistingNodeProperties() throws NodeNotFoundException
    {
        System.out.println("Test smazání vlastností neexistujícího uzlu");

        pm.deleteProperties(98968769);
    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws InvalidRelationshipException
     * @throws JSONException
     * @throws NodeNotFoundException
     * @throws RelationshipNotFoundException 
     */
    @Test(expected = InvalidRelationshipException.class)
    public void testCreateInvalidRelationship_1() throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException, RelationshipNotFoundException
    {
        System.out.println("Test vytváření hrany bez definice typu vztahu");

        pm.createRelationship(fbJirima5ek, fbBartonekLukas, "");
    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws InvalidRelationshipException
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testCreateInvalidRelationship_2() throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany z neexistujícího uzlu");

        pm.createRelationship(98968769, fbBartonekLukas, "foaf:knows");
    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws InvalidRelationshipException
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    @Test(expected = InvalidRelationshipException.class)
    public void testCreateInvalidRelationship_3() throws InvalidMetadataException,
            InvalidRelationshipException, JSONException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany do neexistujícího uzlu");

        pm.createRelationship(fbJirima5ek, 98968769, "foaf:knows");
    }

    /**
     * 
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException 
     */
    @Test(expected = InvalidRelationshipException.class)
    public void testCreateRelationshipWithInvalidMetadata() throws JSONException,
            InvalidRelationshipException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany s invalidními metadaty");

        // Vytvoření hrany s metadaty, přičemž objekt reprezentující metadata
        // obsahuje vnožený objekt

        JSONObject nestedValue = new JSONObject();

        nestedValue.put("key", "value");

        JSONObject invalidMetadata = new JSONObject();

        invalidMetadata.put("foaf:name", nestedValue);

        pm.createRelationship(fbJirima5ek, 98968769, "foaf:knows", invalidMetadata);
    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException 
     */
    @Test
    public void testCreateRelationship() throws InvalidMetadataException,
            JSONException, InvalidRelationshipException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany");

        foafKnows = pm.createRelationship(fbJirima5ek, fbBartonekLukas, "foaf:knows");
        
        assertNotNull(foafKnows);
        
        System.out.println("    Hrana: " + foafKnows);
    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException
     * @throws RelationshipNotFoundException 
     */
    @Test
    public void testCreateRelationshipWithMetadata() throws InvalidMetadataException,
            JSONException, InvalidRelationshipException, NodeNotFoundException, RelationshipNotFoundException
    {
        System.out.println("Test vytváření hrany s metadaty");

        owlSameAs = pm.createRelationship(twJirimasek, fbJirima5ek, "owl:sameAs", owlSameAsProperties);
        
        assertTrue(owlSameAs > 0);
        
        System.out.println("    Hrana: " + owlSameAs);
    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException 
     */
    @Test(expected = InvalidRelationshipException.class)
    public void testCreateRelationshipWithoutType() throws InvalidMetadataException,
            JSONException, InvalidRelationshipException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany bez definovaného typu");

        pm.createRelationship(fbBartonekLukas, fbJirima5ek, "");

    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testCreateRelationshipFromNonexistingNode() throws InvalidMetadataException,
            JSONException, InvalidRelationshipException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany z neexistujícího uzlu");

        pm.createRelationship(98968769, fbJirima5ek, "foaf:knows");
    }

    /**
     * 
     * @throws InvalidMetadataException
     * @throws JSONException
     * @throws InvalidRelationshipException
     * @throws NodeNotFoundException 
     */
    @Test(expected = InvalidRelationshipException.class)
    public void testCreateRelationshipToNonexistingNode() throws InvalidMetadataException,
            JSONException, InvalidRelationshipException, NodeNotFoundException
    {
        System.out.println("Test vytváření hrany do neexistujícího uzlu");
        
        pm.createRelationship(fbJirima5ek, 98968769, "foaf:knows");
    }

    /**
     * 
     * @throws RelationshipNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveRelationship() throws RelationshipNotFoundException, JSONException
    {
        System.out.println("Test získávání informací o hraně");

        JSONObject relationship = pm.retrieveRelationship(foafKnows);
        
        assertTrue(relationship.getString("start").endsWith(String.valueOf(fbJirima5ek)));
        assertTrue(relationship.getString("end").endsWith(String.valueOf(fbBartonekLukas)));
        assertEquals(relationship.get("type"), "foaf:knows");
    }

    /**
     * 
     * @throws RelationshipNotFoundException 
     */
    @Test(expected = RelationshipNotFoundException.class)
    public void testRetrieveNonexistingRelationship() throws RelationshipNotFoundException
    {
        System.out.println("Test získávání informací o neexistující hraně");
        
        pm.retrieveRelationship(98968769);
    }

    /**
     * 
     * @throws NodeNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveRelationships() throws NodeNotFoundException, JSONException
    {
        System.out.println("Test získávání informací o hranách vedoucích z/do uzlů");

        JSONArray relationships;
        
        relationships = pm.retrieveRelationships(fbJirima5ek, IN);
        
        assertEquals(relationships.length(), 1);
        
        relationships = pm.retrieveRelationships(fbJirima5ek, OUT);
        
        assertEquals(relationships.length(), 1);
        
        JSONObject relationship;
        
        relationship = (JSONObject) relationships.get(0);
        
        assertTrue(relationship.getString("start").endsWith(String.valueOf(fbJirima5ek)));
        assertTrue(relationship.getString("end").endsWith(String.valueOf(fbBartonekLukas)));
        assertEquals(relationship.get("type"), "foaf:knows");
        
        relationships = pm.retrieveRelationships(fbJirima5ek, ALL);
        
        assertEquals(relationships.length(), 2);
        
        relationship = (JSONObject) relationships.get(0);
        
        assertTrue(relationship.getString("start").endsWith(String.valueOf(fbJirima5ek)));
        assertTrue(relationship.getString("end").endsWith(String.valueOf(fbBartonekLukas)));
        assertEquals(relationship.get("type"), "foaf:knows");
        
        relationships = pm.retrieveRelationships(fbBartonekLukas, IN);
        
        assertEquals(relationships.length(), 1);
        
        relationship = (JSONObject) relationships.get(0);
        
        assertTrue(relationship.getString("start").endsWith(String.valueOf(fbJirima5ek)));
        assertTrue(relationship.getString("end").endsWith(String.valueOf(fbBartonekLukas)));
        assertEquals(relationship.get("type"), "foaf:knows");
        
        relationships = pm.retrieveRelationships(fbBartonekLukas, OUT);
        
        assertEquals(relationships.length(), 0);
        
        relationships = pm.retrieveRelationships(fbBartonekLukas, ALL);
        
        assertEquals(relationships.length(), 1);
        
        relationship = (JSONObject) relationships.get(0);
        
        assertTrue(relationship.getString("start").endsWith(String.valueOf(fbJirima5ek)));
        assertTrue(relationship.getString("end").endsWith(String.valueOf(fbBartonekLukas)));
        assertEquals(relationship.get("type"), "foaf:knows");
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testRetrieveRelationshipsFromNonexistingNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání informací o hranách vedoucích z neexistujícího uzlu");

        pm.retrieveRelationships(98968769, OUT);
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testRetrieveRelationshipsToNonexistingNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání informací o hranách vedoucích do neexistujícího uzlu");

        pm.retrieveRelationships(98968769, IN);
    }

    /**
     * 
     * @throws NodeNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveSpecificRelationships() throws NodeNotFoundException, JSONException
    {
        System.out.println("Test získávání informací o specifických hranách vedoucích z/do uzlů");

        JSONArray relationships;
        
        relationships = pm.retrieveRelationships(fbJirima5ek, OUT, "foaf:knows");
        
        assertEquals(relationships.length(), 1);
        
        JSONObject relationship;
        
        relationship = (JSONObject) relationships.get(0);
        
        assertTrue(relationship.getString("start").endsWith(String.valueOf(fbJirima5ek)));
        assertTrue(relationship.getString("end").endsWith(String.valueOf(fbBartonekLukas)));
        assertEquals(relationship.get("type"), "foaf:knows");
        
        relationships = pm.retrieveRelationships(fbJirima5ek, OUT, "rel:met");
        
        assertEquals(relationships.length(), 0);
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testRetrieveSpecificRelationshipsFromNonexistentNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání informací o specifických hranách vedoucích z neexistujícího uzlu");

        pm.retrieveRelationships(98968769, OUT, "foaf:knows");

    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testRetrieveSpecificRelationshipsToNonexistentNode() throws NodeNotFoundException
    {
        System.out.println("Test získávání informací o specifických hranách vedoucích do neexistujícího uzlu");

        pm.retrieveRelationships(98968769, IN, "foaf:knows");
    }

    /**
     * 
     * @throws JSONException
     * @throws RelationshipNotFoundException
     * @throws InvalidMetadataException 
     */
    @Test(expected = InvalidMetadataException.class)
    public void testAddInvalidMetadata() throws JSONException,
            RelationshipNotFoundException, InvalidMetadataException
    {
        System.out.println("Test přidávání invalidních metadat hraně");

        // Přidaní metadat, přičemž objekt reprezentující metadata
        // obsahuje vnožený objekt

        JSONObject nestedValue = new JSONObject();

        nestedValue.put("key", "value");

        JSONObject invalidMetadata = new JSONObject();

        invalidMetadata.put("foaf:name", nestedValue);

        pm.addMetadataToRelationship(foafKnows, invalidMetadata);
    }

    /**
     * 
     * @throws RelationshipNotFoundException
     * @throws JSONException
     * @throws InvalidMetadataException 
     */
    @Test
    public void testAddMetadata() throws RelationshipNotFoundException, JSONException,
            InvalidMetadataException
    {
        System.out.println("Test přidávání metadat hraně");

        pm.addMetadataToRelationship(foafKnows, foafKnowsProperties);
        
        JSONObject relationship = pm.retrieveRelationship(foafKnows);
        
        JSONObject data = (JSONObject) relationship.get("data");
        
        JSONArray siocNote = (JSONArray) data.get("sioc:note");
        
        assertEquals(siocNote.get(0), foafKnowsProperties.getJSONArray("sioc:note").get(0));
    }

    /**
     * 
     * @throws RelationshipNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveRelationshipMetada() throws RelationshipNotFoundException,
            JSONException
    {
        System.out.println("Test získávání metadat hrany");

        JSONObject metadata = pm.retrieveRelationshipMetadata(owlSameAs);
        
        JSONArray siocNote = (JSONArray) metadata.get("sioc:note");
        
        assertEquals(siocNote.get(0), owlSameAsProperties.getJSONArray("sioc:note").get(0));
    }

    /**
     * 
     * @throws RelationshipNotFoundException 
     */
    @Test(expected = RelationshipNotFoundException.class)
    public void testRetrieveNonexistingRelationshipMetadata() throws RelationshipNotFoundException
    {
        System.out.println("Test získávání metadat neexistující hrany");

        pm.retrieveRelationshipMetadata(98968769);
    }

    /**
     * 
     * @throws MetadataNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testRetrieveMetadata() throws MetadataNotFoundException, JSONException
    {
        System.out.println("Test získávání metadat hrany");
        
        JSONArray siocNote = new JSONArray(pm.retrieveRelationshipMetadata(owlSameAs, "sioc:note"));
        
        assertEquals(siocNote.get(0), owlSameAsProperties.getJSONArray("sioc:note").get(0));
    }

    /**
     * 
     * @throws MetadataNotFoundException 
     */
    @Test(expected = MetadataNotFoundException.class)
    public void testRetrieveNonexistingMetadata() throws MetadataNotFoundException
    {
        System.out.println("Test získávání neexistujících metadat hrany");
        
        pm.retrieveRelationshipMetadata(owlSameAs, "sr:weight");
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test
    public void testDeleteMetadata() throws RelationshipNotFoundException
    {
        System.out.println("Test smazání metadat hrany");

        boolean deleted = pm.deleteRelationshipMetadata(foafKnows, "sioc:note");
        
        assertTrue(deleted);
        
        JSONObject metadata = pm.retrieveRelationshipMetadata(foafKnows);
        
        assertEquals(metadata.length(), 0);
    }

    /**
     * 
     * @throws NodeNotFoundException 
     */
    @Test
    public void testDeleteNonexistingMetadata() throws NodeNotFoundException
    {
        System.out.println("Test smazání neexistujících metadat hrany");

        boolean deleted  = pm.deleteRelationshipMetadata(foafKnows, "sioc:note");
        
        assertFalse(deleted);
    }

    /**
     * 
     * @throws RelationshipNotFoundException
     * @throws NodeNotFoundException 
     */
    @Test
    public void testDeleteRelationshipMetadata() throws RelationshipNotFoundException, NodeNotFoundException
    {
        System.out.println("Test smazání všech metadat hrany");
        
        pm.deleteRelationshipMetadata(owlSameAs);
        
        JSONObject metadata = pm.retrieveRelationshipMetadata(owlSameAs);
        
        assertEquals(metadata.length(), 0);
    }

    /**
     * 
     * @throws RelationshipNotFoundException 
     */
    @Test(expected = RelationshipNotFoundException.class)
    public void testDeleteNonexistingRelationshiopMetadata() throws RelationshipNotFoundException
    {
        System.out.println("Test smazání metadat neexistující hrany");

        pm.deleteRelationshipMetadata(98968769);
    }
    
    /**
     * 
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    @Test
    public void testTraverse() throws JSONException, NodeNotFoundException
    {
        System.out.println("Test traverzování");
        
        TraversalDescription t;
        
        t = new TraversalDescription();
        t.addRelationship("foaf:knows", OUT);
        
        JSONArray nodes;
        
        nodes = pm.traverse(fbBartonekLukas, t, TypeEnum.RELATIONSHIP);
        
        assertEquals(nodes.length(), 0);
        
        nodes = pm.traverse(fbJirima5ek, t, TypeEnum.RELATIONSHIP);
        
        assertEquals(nodes.length(), 1);
        
        JSONObject node = nodes.getJSONObject(0);
        
        assertTrue(node.getString("self").endsWith(String.valueOf(foafKnows)));
    }
    
    /**
     * 
     * @throws JSONException
     * @throws NodeNotFoundException 
     */
    @Test(expected = NodeNotFoundException.class)
    public void testTraverseFromNonexistentNode() throws JSONException, NodeNotFoundException
    {
        System.out.println("Test traverzování z neexistujícího uzlu");
        
        TraversalDescription t = new TraversalDescription();
        
        pm.traverse(98968769, t, TypeEnum.NODE);
    }
    
    /**
     * 
     * @throws JSONException 
     */
    @Test
    public void testCreateNodeIndex() throws JSONException
    {
        System.out.println("Test vytváření indexu");
        
        pm.createNodeIndex("fb");
        pm.createNodeIndex("tw");
    }
    
    /**
     * 
     */
    @Test
    public void testRetrieveListOfNodeIndexes()
    {
        System.out.println("Test získávání seznamu indexů");
        
        JSONObject list = pm.retrieveListOfNodeIndexes();
        
        assertTrue(list.has("fb"));
        assertTrue(list.has("tw"));
    }
    
    /**
     * 
     * @throws JSONException 
     */
    @Test
    public void testAddNodeToIndex() throws JSONException, NodeIndexNotFoundException
    {
        System.out.println("Test přidávání uzlů do indexu");
        
        pm.addNodeToIndex("fb", "username", "jirima5ek", fbJirima5ek);
    }
    
    /**
     * 
     * @throws JSONException
     * @throws NodeIndexNotFoundException 
     */
    @Test
    public void testRetrieveNodeFromIndex() throws JSONException, NodeIndexNotFoundException
    {
        System.out.println("Test získávání uzlů z indexu");
        
        JSONObject node = pm.retrieveNodeFromIndex("fb", "username", "jirima5ek");
        
        assertTrue(node.getString("self").endsWith(String.valueOf(fbJirima5ek)));
    }
    
    /**
     * 
     * @throws JSONException
     * @throws NodeIndexNotFoundException 
     */
    @Test
    public void testRetrieveNonindexedNodeFromIndex() throws JSONException, NodeIndexNotFoundException
    {
        System.out.println("Test získávání neexistujících uzlů z indexu");
        
        JSONObject node = pm.retrieveNodeFromIndex("fb", "username", "bartonekLukas");
        
        assertNull(node);
    }
    
    /**
     * 
     * @throws NodeIndexNotFoundException
     * @throws JSONException 
     */
    @Test
    public void testDeleteNodeFromIndex() throws NodeIndexNotFoundException, JSONException
    {
        System.out.println("Test smazání uzlu z indexu");
        
        pm.deleteNodeFromIndex("fb", fbJirima5ek, "username", "jirima5ek");
        
        JSONObject node = pm.retrieveNodeFromIndex("fb", "username", "jirima5ek");
        
        assertNull(node);
    }
    
    /**
     * 
     * @throws NodeIndexNotFoundException 
     */
    @Test(expected = NodeIndexNotFoundException.class)
    public void testDeleteNodeFromNonexistentIndex() throws NodeIndexNotFoundException
    {
        System.out.println("Test smazání uzlu z neexistujícího indexu");
        
        pm.deleteNodeFromIndex("ctu", 98968769, "username", "masekji4");
    }
    
    /**
     * 
     */
    @Test
    public void testDeleteNodeIndex()
    {
        System.out.println("Test smazání indexu");
        
        pm.deleteNodeIndex("fb");
        pm.deleteNodeIndex("tw");
    }
    
    /**
     * 
     * @throws CannotDeleteNodeException 
     */
    @Test(expected = CannotDeleteNodeException.class)
    public void testDeteleNodeWithRelationships() throws CannotDeleteNodeException
    {
        System.out.println("Test mazání uzlů s hranami");

        pm.deleteNode(fbJirima5ek);
    }

    /**
     * 
     */
    @Test
    public void testDeleteRelationship()
    {
        System.out.println("Test mazání hrany");
        
        boolean deleted;

        deleted = pm.deleteRelationship(foafKnows);

        assertTrue(deleted);

        deleted = pm.deleteRelationship(owlSameAs);

        assertTrue(deleted);
    }

    /**
     * 
     */
    @Test
    public void testDeleteNonexistRelationship()
    {
        System.out.println("Test mazání neexistující hrany");

        boolean deleted = pm.deleteRelationship(foafKnows);

        assertFalse(deleted);
    }

    /**
     * 
     * @throws CannotDeleteNodeException 
     */
    @Test
    public void testDeteleNode() throws CannotDeleteNodeException
    {
        System.out.println("Test mazání uzlů");

        assertTrue(pm.deleteNode(fbJirima5ek));
        assertFalse(pm.deleteNode(fbJirima5ek));

        assertTrue(pm.deleteNode(fbBartonekLukas));

        assertTrue(pm.deleteNode(twJirimasek));
        assertTrue(pm.deleteNode(twXmorfeus));
    }
}
