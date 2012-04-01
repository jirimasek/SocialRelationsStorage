package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.GraphDAO;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.DirectionEnum;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * Třída <code>StorageServiceImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
@Stateless
public class StorageServiceImpl implements StorageService
{

    @Inject
    private GraphDAO graphDAO;

    /* ********************************************************************** *
     *                                PERSONS                                 *
     * ********************************************************************** */
    
    @Override
    public List<Person> retrievePersons(String source)
    {
        try
        {
            return graphDAO.retrievePersons(new URI(source));
        }
        catch (URISyntaxException ex)
        {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public Person retrievePerson(Integer id) throws PersonNotFoundException
    {
        return graphDAO.retrievePerson(id);
    }
    
    @Override
    public Person retrievePerson(String prefix, String username) throws PersonNotFoundException
    {
        Key key = new Key();
        
        key.setPrefix(prefix);
        key.setUsername(username);
        
        return graphDAO.retrievePerson(key);
    }

    @Override
    public List<Person> retrieveAlterEgos(Integer id) throws PersonNotFoundException
    {
        Path alterEgos = graphDAO.retrieveAlterEgos(id);
        
        return filterAlterAgos(alterEgos, null);
    }

    @Override
    public List<Person> retrieveAlterEgos(String prefix, String username) throws PersonNotFoundException
    {
        Key key = new Key();
        
        key.setPrefix(prefix);
        key.setUsername(username);
        
        Path alterEgos = graphDAO.retrieveAlterEgos(key);
        
        return filterAlterAgos(alterEgos, null);
    }
    
    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */

    /**
     * 
     * @param id
     * @return
     * @throws RelationNotFoundException
     * @throws IllegalAccessException 
     */
    @Override
    public Relation retrieveRelation(Integer id)
            throws RelationNotFoundException, IllegalAccessException
    {
        return graphDAO.retrieveRelation(id);
    }

    /**
     * 
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(Integer id) throws PersonNotFoundException
    {
        return graphDAO.retrieveRelations(id, DirectionEnum.ALL);
    }

    /**
     * 
     * @param prefix
     * @param username
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(String prefix, String username)
            throws PersonNotFoundException
    {
        Key key = new Key();
        
        key.setPrefix(prefix);
        key.setUsername(username);
        
        return graphDAO.retrieveRelations(key, DirectionEnum.ALL);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    /* ********************************************************************** *
     *                            Accessor Methods                            *
     * ********************************************************************** */
    /**
     * 
     * @param alterEgos
     * @param acceptableSource
     * @return 
     */
    private List<Person> filterAlterAgos(Path alterEgos, Set<URI> acceptableSource)
    {
        List<Person> ae = new ArrayList<Person>();
        
        if (alterEgos.getPersons() != null && !alterEgos.getPersons().isEmpty())
        {
            Stack<Iterator<Path>> stack = new Stack<Iterator<Path>>();
        
            Iterator<Path> iterator = alterEgos.getPersons().iterator();
        
            while (iterator.hasNext())
            {
                Path path = iterator.next();
                
                // Filtrování
                //if (path.getSources().contains(...))
                {
                    ae.add(path.getPerson());
                    
                    if (path.getPersons() != null && !path.getPersons().isEmpty())
                    {
                        stack.add(iterator);
                        
                        iterator = path.getPersons().iterator();
                    }
                    
                    if (!iterator.hasNext() && !stack.empty())
                    {
                        iterator = stack.pop();
                    }
                }
            }
        }
        
        return ae;
    }// </editor-fold>
}
