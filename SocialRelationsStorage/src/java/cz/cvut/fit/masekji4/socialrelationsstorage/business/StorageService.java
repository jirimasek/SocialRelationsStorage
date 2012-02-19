package cz.cvut.fit.masekji4.socialrelationsstorage.business;

import cz.cvut.fit.masekji4.socialrelationsstorage.dao.entities.Relationship;
import javax.enterprise.inject.Default;

/**
 * Rozhraní <code>StorageService</code>
 *
 * @author Jiří Mašek <masekji4@fit.cvut.cz>
 */
@Default
public abstract class StorageService
{

    public StorageService()
    {
    }

    public abstract  boolean saveRelationship(Relationship relationship);
}
