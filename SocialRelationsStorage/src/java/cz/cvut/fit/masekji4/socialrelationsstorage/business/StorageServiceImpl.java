package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.common.CollectionUtils;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.GraphDAO;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Path;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Person;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relation;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.key.Key;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidPersonException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.InvalidProfileException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.PersonNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationAlreadyExistsException;
import cz.cvut.fit.masekji4.socialrelationsstorage.dao.exceptions.RelationNotFoundException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.exceptions.InvalidRelationshipException;
import cz.cvut.fit.masekji4.socialrelationsstorage.persistence.traversal.DirectionEnum;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
    
    /**
     * 
     * @param person
     * @return
     * @throws InvalidPersonException
     * @throws InvalidProfileException
     * @throws PersonAlreadyExistsException 
     */
    @Override
    public Integer createPerson(Person person) throws InvalidPersonException, InvalidProfileException, PersonAlreadyExistsException
    {
        return graphDAO.createPerson(person);
    }

    /**
     * 
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public Person retrievePerson(Integer id) throws PersonNotFoundException
    {
        return graphDAO.retrievePerson(id);
    }
    
    /**
     * 
     * @param prefix
     * @param username
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public Person retrievePerson(String prefix, String username) throws PersonNotFoundException
    {
        Key key = new Key();
        
        key.setPrefix(prefix);
        key.setUsername(username);
        
        return graphDAO.retrievePerson(key);
    }
    
    /**
     * 
     * 
     * @return 
     */
    @Override
    public List<Person> retrievePersons()
    {
        return graphDAO.retrievePersons();
    }
    
    /**
     * 
     * @param source
     * @return 
     */
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

    /**
     * 
     * @param id
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Person> retrieveAlterEgos(Integer id, SourceFilter filter) throws PersonNotFoundException
    {
        Path alterEgos = graphDAO.retrieveAlterEgos(id);
        
        return processAlterEgos(alterEgos, filter);
    }
    
    @Override
    public Integer updatePerson(Person person) throws InvalidPersonException, InvalidProfileException
    {
        return graphDAO.updatePerson(person);
    }
    
    /**
     * 
     * @param id
     * @return 
     */
    @Override
    public boolean deletePerson(Integer id)
    {
        return graphDAO.deletePerson(id);
    }
    
    /* ********************************************************************** *
     *                                SAMENESS                                *
     * ********************************************************************** */
    
    /**
     * 
     * @param person
     * @param alterEgo
     * @param sources
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public boolean declareSameness(Integer person, Integer alterEgo, List<URI> sources)
            throws PersonNotFoundException
    {
        return graphDAO.declareSameness(person, alterEgo, sources);   
    }
    
    /**
     * 
     * @param person
     * @param alterEgo
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public boolean refuseSameness(Integer person, Integer alterEgo)
            throws PersonNotFoundException
    {
        return graphDAO.refuseSameness(person, alterEgo);
    }
    
    /* ********************************************************************** *
     *                               REALTIONS                                *
     * ********************************************************************** */

    @Override
    public Integer createRelation(Relation relation) throws PersonNotFoundException, RelationAlreadyExistsException, IllegalAccessException, InvalidRelationshipException
    {
        return graphDAO.createRelation(relation);
    }
    
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
    public List<Relation> retrieveRelations(Integer id, SourceFilter filter) throws PersonNotFoundException
    {
        
        List<Relation> relations = graphDAO.retrieveRelations(id, DirectionEnum.ALL);
                
        return processRelations(relations, filter);
    }
    
    /**
     * 
     * @param id
     * @param type
     * @param filter
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(Integer id, String type, SourceFilter filter) throws PersonNotFoundException
    {
        List<Relation> relations = graphDAO.retrieveRelations(id, DirectionEnum.ALL, type);
                
        return processRelations(relations, filter);
    }
    
    /**
     * 
     * @param object
     * @param subject
     * @param type
     * @param filter
     * @return
     * @throws PersonNotFoundException 
     */
    @Override
    public List<Relation> retrieveRelations(Integer object, Integer subject, String type, SourceFilter filter) throws PersonNotFoundException
    {
        List<Relation> relations = graphDAO.retrieveRelations(object, DirectionEnum.ALL, type);
        
        relations = processRelations(relations, filter);
        
        List<Relation> list = new LinkedList<Relation>();
        
        for (Relation relation : relations)
        {
            if (relation.getObject().equals(subject) || relation.getSubject().equals(subject))
            {
                list.add(relation);
            }
        }
                
        return list;
    }

    /**
     * 
     * @param relation
     * @return
     * @throws PersonNotFoundException
     * @throws IllegalAccessException 
     */
    @Override
    public Integer updateRelation(Relation relation)
            throws PersonNotFoundException, IllegalAccessException
    {
        return graphDAO.updateRelation(relation);
    }

    /**
     * 
     * @param id
     * @throws IllegalAccessException 
     */
    @Override
    public boolean deleteRelation(Integer id) throws IllegalAccessException
    {
        return graphDAO.deleteRelation(id);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Accessor Methods">
    /* ********************************************************************** *
     *                            Accessor Methods                            *
     * ********************************************************************** */
    /**
     * 
     * @param relations
     * @param filter
     * @return 
     */
    private List<Relation> processRelations(List<Relation> relations,
            SourceFilter filter)
    {
        List<Relation> list = new LinkedList<Relation>();
        
        for (Relation relation : relations)
        {
            if (filter.isAcceptable(relation.getSources()))
            {
                list.add(relation);
            }
        }
        
        return list;
    }
    /**
     * 
     * @param alterEgos
     * @param filter
     * @return 
     */
    private List<Person> processAlterEgos(Path alterEgos, SourceFilter filter)
    {   
        List<Person> list = new LinkedList<Person>();
        
        if (!CollectionUtils.isNullOrEmpty(alterEgos.getPersons()))
        {
            Stack<Iterator<Path>> stack = new Stack<Iterator<Path>>();
        
            Iterator<Path> iterator = alterEgos.getPersons().iterator();
        
            while (iterator.hasNext())
            {
                Path path = iterator.next();
                
                if (filter.isAcceptable(path.getSources()))
                {
                    list.add(path.getPerson());
                    
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
        
        return list;
    }// </editor-fold>
}
