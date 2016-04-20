package br.com.sba.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public abstract class GenericDAO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppolodorusPU");
    private EntityManager em;

    private Class<T> entityClass;

    public void createEntityManager() {
        em = emf.createEntityManager();
    }

    public Query createNativeQuery(String sql) {
        return em.createNativeQuery(sql);
    }

    public void closeEntityManager() {
        if (em.isOpen()) {
            em.close();
        }
    }

    public void beginTransaction() {
        createEntityManager();
        em.getTransaction().begin();
    }

    public void commit() {
        em.getTransaction().commit();
    }

    public void rollback() {
        em.getTransaction().rollback();
    }

    public void closeTransaction() {
        closeEntityManager();
    }

    public void commitAndCloseTransaction() {
        commit();
        closeTransaction();
    }

    public void flush() {
        em.flush();
    }

    public void joinTransaction() {
        createEntityManager();
        em.joinTransaction();
    }

    public GenericDAO(Class entityClass) {
        this.entityClass = entityClass;
    }

    public void save(T entity) {
        em.persist(entity);
    }

    public void delete(Object id) {
        T entityToBeRemoved = em.getReference(entityClass, id);

        em.remove(entityToBeRemoved);
    }

    public T update(T entity) {
        return em.merge(entity);
    }

    public T find(int entityID) {
        return em.find(entityClass, entityID);
    }

    public T findReferenceOnly(int entityID) {
        return em.getReference(entityClass, entityID);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    @SuppressWarnings("unchecked")
    protected T findOneResult(String namedQuery, Map parameters) {
        T result = null;

        try {
            Query query = em.createNamedQuery(namedQuery);

            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = (T) query.getSingleResult();

        } catch (NoResultException e) {
            System.out.println("No result found for named query: " + namedQuery);
        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    protected List findAllResults(String namedQuery, Map parameters) {
        List result = null;

        try {
            Query query = em.createNamedQuery(namedQuery);

            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = (List) query.getResultList();

        } catch (NoResultException e) {
            System.out.println("No result found for named query: " + namedQuery);
        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    protected List findAllResultsNQ(String nativeQuery, Map parameters) {
        List result = null;

        try {
            Query query = em.createNativeQuery(nativeQuery);

            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = query.getResultList();

        } catch (NoResultException e) {
            System.out.println("No result found for named query: " + nativeQuery);
        } catch (Exception e) {
            System.out.println("Error while running query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private void populateQueryParameters(Query query, Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }
}

//abstract class GenericDAO<T> implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppolodorusPU");
//	private EntityManager em;
//
//	private Class<T> entityClass;
//
//	public void beginTransaction() {
//		em = emf.createEntityManager();
//
//		em.getTransaction().begin();
//	}
//
//	public void commit() {
//		em.getTransaction().commit();
//	}
//
//	public void rollback() {
//		em.getTransaction().rollback();
//	}
//
//	public void closeTransaction() {
//		em.close();
//	}
//
//	public void commitAndCloseTransaction() {
//		commit();
//		closeTransaction();
//	}
//
//	public void flush() {
//		em.flush();
//	}
//
//	public void joinTransaction() {
//		em = emf.createEntityManager();
//		em.joinTransaction();
//	}
//
//	public GenericDAO(Class<T> entityClass) {
//		this.entityClass = entityClass;
//	}
//
//	public void save(T entity) {
//		em.persist(entity);
//	}
//
//	public void delete(Object id, Class<T> classe) {
//		T entityToBeRemoved = em.getReference(classe, id);
//		 
//        em.remove(entityToBeRemoved);
//	}
//
//	public T update(T entity) {
//		return em.merge(entity);
//	}
//
//	public T find(int entityID) {
//		return em.find(entityClass, entityID);
//	}
//
//	public T findReferenceOnly(int entityID) {
//		return em.getReference(entityClass, entityID);
//	}
//
//	// Using the unchecked because JPA does not have a
//	// em.getCriteriaBuilder().createQuery()<T> method
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public List<T> findAll() {
//		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//		cq.select(cq.from(entityClass));
//		return em.createQuery(cq).getResultList();
//	}
//
//	// Using the unchecked because JPA does not have a
//	// query.getSingleResult()<T> method
//	@SuppressWarnings("unchecked")
//	protected T findOneResult(String namedQuery, Map<String, Object> parameters) {
//		T result = null;
//
//		try {
//			Query query = em.createNamedQuery(namedQuery);
//
//			// Method that will populate parameters if they are passed not null and empty
//			if (parameters != null && !parameters.isEmpty()) {
//				populateQueryParameters(query, parameters);
//			}
//
//			result = (T) query.getSingleResult();
//
//		} catch (NoResultException e) {
//			System.out.println("No result found for named query: " + namedQuery);
//			e.printStackTrace();
//		} catch (Exception e) {
//			System.out.println("Error while running query: " + e.getMessage());
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	private void populateQueryParameters(Query query, Map<String, Object> parameters) {
//		for (Entry<String, Object> entry : parameters.entrySet()) {
//			query.setParameter(entry.getKey(), entry.getValue());
//		}
//	}
//}
