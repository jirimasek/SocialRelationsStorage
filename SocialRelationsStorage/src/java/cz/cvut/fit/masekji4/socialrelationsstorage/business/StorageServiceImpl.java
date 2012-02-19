package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relationship;
import javax.ejb.Stateless;

/**
 * Třída <code>StorageServiceImpl</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Storage
@Stateless
public class StorageServiceImpl extends StorageService
{

    public StorageServiceImpl()
    {
    }

    @Override
    public boolean saveRelationship(Relationship relationship)
    {
        return true;
    }
    
}
