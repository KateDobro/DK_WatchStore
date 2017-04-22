package org.itstep.pps2701.dao;

import org.itstep.pps2701.entities.Watch;
import javax.persistence.TypedQuery;
import java.util.List;

public class WatchRepository extends AbstractRepository<Watch, Long> {

    public List<Watch> findByNameLike(String name) {
        String pattern = "%" + name + "%";
        TypedQuery<Watch> typedQuery = em.createQuery("SELECT s FROM Watch s WHERE s.name LIKE :name", Watch.class);
        typedQuery.setParameter("name", pattern);
        List<Watch> results = typedQuery.getResultList();

        return typedQuery.getResultList();
    }


}
