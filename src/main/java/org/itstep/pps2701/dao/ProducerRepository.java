package org.itstep.pps2701.dao;

import org.itstep.pps2701.entities.Producer;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProducerRepository extends AbstractRepository<Producer, Long> {

    public List<Producer> findByNameLike(String name) {
        String pattern = "%" + name + "%";
        TypedQuery<Producer> typedQuery = em.createQuery("SELECT s FROM Producer s WHERE s.name LIKE :name", Producer.class);
        typedQuery.setParameter("name", pattern);
        List<Producer> results = typedQuery.getResultList();

        return typedQuery.getResultList();
    }
}
