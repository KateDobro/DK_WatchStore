package org.itstep.pps2701.dao;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractRepository<T, ID extends Serializable> {

    @Inject
    protected EntityManager em;

    protected Class<T> persistentClass;

    public AbstractRepository() {
        Type genericSuperClass = getClass().getGenericSuperclass();

        ParameterizedType parametrizedType = null;
        while (parametrizedType == null) {
            if (genericSuperClass instanceof ParameterizedType) {
                parametrizedType = (ParameterizedType) genericSuperClass;
            } else {
                genericSuperClass = ((Class<?>) genericSuperClass).getGenericSuperclass();
            }
        }

        persistentClass = (Class<T>) parametrizedType.getActualTypeArguments()[0];
    }

    public List<T> findAllActive() {
        return em.createQuery("SELECT t FROM " + persistentClass.getSimpleName() + " t where t.dateClose is null ").getResultList();
    }

    public T save(T entity) {
        em.getTransaction().begin();
        T result = em.merge(entity);
        em.getTransaction().commit();
        return result;
    }

    public void delete(T entity) {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
    }

    public T getOne(ID id) {
        return em.find(persistentClass, id);
    }
}
