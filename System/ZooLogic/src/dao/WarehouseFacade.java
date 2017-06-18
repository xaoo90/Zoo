/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Feed;
import entity.Warehouse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Xaoo
 */
public class WarehouseFacade implements Serializable {

    public WarehouseFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Warehouse warehouse) throws PreexistingEntityException, Exception {
        if (warehouse.getFeedCollection() == null) {
            warehouse.setFeedCollection(new ArrayList<Feed>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Feed> attachedFeedCollection = new ArrayList<Feed>();
            for (Feed feedCollectionFeedToAttach : warehouse.getFeedCollection()) {
                feedCollectionFeedToAttach = em.getReference(feedCollectionFeedToAttach.getClass(), feedCollectionFeedToAttach.getIdFeed());
                attachedFeedCollection.add(feedCollectionFeedToAttach);
            }
            warehouse.setFeedCollection(attachedFeedCollection);
            em.persist(warehouse);
            for (Feed feedCollectionFeed : warehouse.getFeedCollection()) {
                Warehouse oldWarehouseOfFeedCollectionFeed = feedCollectionFeed.getWarehouse();
                feedCollectionFeed.setWarehouse(warehouse);
                feedCollectionFeed = em.merge(feedCollectionFeed);
                if (oldWarehouseOfFeedCollectionFeed != null) {
                    oldWarehouseOfFeedCollectionFeed.getFeedCollection().remove(feedCollectionFeed);
                    oldWarehouseOfFeedCollectionFeed = em.merge(oldWarehouseOfFeedCollectionFeed);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWarehouse(warehouse.getIdWarehouse()) != null) {
                throw new PreexistingEntityException("Warehouse " + warehouse + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Warehouse warehouse) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Warehouse persistentWarehouse = em.find(Warehouse.class, warehouse.getIdWarehouse());
            Collection<Feed> feedCollectionOld = persistentWarehouse.getFeedCollection();
            Collection<Feed> feedCollectionNew = warehouse.getFeedCollection();
            List<String> illegalOrphanMessages = null;
            for (Feed feedCollectionOldFeed : feedCollectionOld) {
                if (!feedCollectionNew.contains(feedCollectionOldFeed)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Feed " + feedCollectionOldFeed + " since its warehouse field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Feed> attachedFeedCollectionNew = new ArrayList<Feed>();
            for (Feed feedCollectionNewFeedToAttach : feedCollectionNew) {
                feedCollectionNewFeedToAttach = em.getReference(feedCollectionNewFeedToAttach.getClass(), feedCollectionNewFeedToAttach.getIdFeed());
                attachedFeedCollectionNew.add(feedCollectionNewFeedToAttach);
            }
            feedCollectionNew = attachedFeedCollectionNew;
            warehouse.setFeedCollection(feedCollectionNew);
            warehouse = em.merge(warehouse);
            for (Feed feedCollectionNewFeed : feedCollectionNew) {
                if (!feedCollectionOld.contains(feedCollectionNewFeed)) {
                    Warehouse oldWarehouseOfFeedCollectionNewFeed = feedCollectionNewFeed.getWarehouse();
                    feedCollectionNewFeed.setWarehouse(warehouse);
                    feedCollectionNewFeed = em.merge(feedCollectionNewFeed);
                    if (oldWarehouseOfFeedCollectionNewFeed != null && !oldWarehouseOfFeedCollectionNewFeed.equals(warehouse)) {
                        oldWarehouseOfFeedCollectionNewFeed.getFeedCollection().remove(feedCollectionNewFeed);
                        oldWarehouseOfFeedCollectionNewFeed = em.merge(oldWarehouseOfFeedCollectionNewFeed);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = warehouse.getIdWarehouse();
                if (findWarehouse(id) == null) {
                    throw new NonexistentEntityException("The warehouse with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Warehouse warehouse;
            try {
                warehouse = em.getReference(Warehouse.class, id);
                warehouse.getIdWarehouse();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The warehouse with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Feed> feedCollectionOrphanCheck = warehouse.getFeedCollection();
            for (Feed feedCollectionOrphanCheckFeed : feedCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Warehouse (" + warehouse + ") cannot be destroyed since the Feed " + feedCollectionOrphanCheckFeed + " in its feedCollection field has a non-nullable warehouse field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(warehouse);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Warehouse> findWarehouseEntities() {
        return findWarehouseEntities(true, -1, -1);
    }

    public List<Warehouse> findWarehouseEntities(int maxResults, int firstResult) {
        return findWarehouseEntities(false, maxResults, firstResult);
    }

    private List<Warehouse> findWarehouseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Warehouse.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Warehouse findWarehouse(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Warehouse.class, id);
        } finally {
            em.close();
        }
    }

    public int getWarehouseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Warehouse> rt = cq.from(Warehouse.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Feed> showFeed(Warehouse w) {
        ArrayList<Feed> lst = new ArrayList();
        for (Feed f : w.getFeedCollection()) {
            lst.add(f);
        }
        return lst;
    }

    public String addWarehouse(Warehouse w) throws Exception {
        try {
            getEntityManager().createNamedQuery("Warehouse.findByName", Warehouse.class).setParameter("name", w.getName()).getSingleResult();
        } catch (NoResultException e) {
            int maxId = getEntityManager().createNamedQuery("Warehouse.findMaxId", Integer.class).getSingleResult();
            w.setIdWarehouse(++maxId);
            create(w);
            return "OK";
        }
        return "ERR";
    }

    public String editWarehouse(Warehouse warehouse) throws Exception {
        try {
            edit(warehouse);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }

    public String deleteWarehouse(Integer id) {
        try {
            destroy(id);
            System.out.println("OK usowanie magazyn");
            return "OK";
        } catch (Exception e) {
            return "BLAD";
        }
    }

}
