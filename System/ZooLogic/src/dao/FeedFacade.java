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
import entity.Warehouse;
import entity.AnimalFeed;
import entity.Feed;
import java.util.ArrayList;
import java.util.Collection;
import entity.OrderPosition;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class FeedFacade implements Serializable {

    public FeedFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Feed feed) throws PreexistingEntityException, Exception {
        if (feed.getAnimalFeedCollection() == null) {
            feed.setAnimalFeedCollection(new ArrayList<AnimalFeed>());
        }
        if (feed.getOrderPositionCollection() == null) {
            feed.setOrderPositionCollection(new ArrayList<OrderPosition>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Warehouse warehouse = feed.getWarehouse();
            if (warehouse != null) {
                warehouse = em.getReference(warehouse.getClass(), warehouse.getIdWarehouse());
                feed.setWarehouse(warehouse);
            }
            Collection<AnimalFeed> attachedAnimalFeedCollection = new ArrayList<AnimalFeed>();
            for (AnimalFeed animalFeedCollectionAnimalFeedToAttach : feed.getAnimalFeedCollection()) {
                animalFeedCollectionAnimalFeedToAttach = em.getReference(animalFeedCollectionAnimalFeedToAttach.getClass(), animalFeedCollectionAnimalFeedToAttach.getIdAnimalFeed());
                attachedAnimalFeedCollection.add(animalFeedCollectionAnimalFeedToAttach);
            }
            feed.setAnimalFeedCollection(attachedAnimalFeedCollection);
            Collection<OrderPosition> attachedOrderPositionCollection = new ArrayList<OrderPosition>();
            for (OrderPosition orderPositionCollectionOrderPositionToAttach : feed.getOrderPositionCollection()) {
                orderPositionCollectionOrderPositionToAttach = em.getReference(orderPositionCollectionOrderPositionToAttach.getClass(), orderPositionCollectionOrderPositionToAttach.getIdOrderPosition());
                attachedOrderPositionCollection.add(orderPositionCollectionOrderPositionToAttach);
            }
            feed.setOrderPositionCollection(attachedOrderPositionCollection);
            em.persist(feed);
            if (warehouse != null) {
                warehouse.getFeedCollection().add(feed);
                warehouse = em.merge(warehouse);
            }
            for (AnimalFeed animalFeedCollectionAnimalFeed : feed.getAnimalFeedCollection()) {
                Feed oldFeedOfAnimalFeedCollectionAnimalFeed = animalFeedCollectionAnimalFeed.getFeed();
                animalFeedCollectionAnimalFeed.setFeed(feed);
                animalFeedCollectionAnimalFeed = em.merge(animalFeedCollectionAnimalFeed);
                if (oldFeedOfAnimalFeedCollectionAnimalFeed != null) {
                    oldFeedOfAnimalFeedCollectionAnimalFeed.getAnimalFeedCollection().remove(animalFeedCollectionAnimalFeed);
                    oldFeedOfAnimalFeedCollectionAnimalFeed = em.merge(oldFeedOfAnimalFeedCollectionAnimalFeed);
                }
            }
            for (OrderPosition orderPositionCollectionOrderPosition : feed.getOrderPositionCollection()) {
                Feed oldFeedOfOrderPositionCollectionOrderPosition = orderPositionCollectionOrderPosition.getFeed();
                orderPositionCollectionOrderPosition.setFeed(feed);
                orderPositionCollectionOrderPosition = em.merge(orderPositionCollectionOrderPosition);
                if (oldFeedOfOrderPositionCollectionOrderPosition != null) {
                    oldFeedOfOrderPositionCollectionOrderPosition.getOrderPositionCollection().remove(orderPositionCollectionOrderPosition);
                    oldFeedOfOrderPositionCollectionOrderPosition = em.merge(oldFeedOfOrderPositionCollectionOrderPosition);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFeed(feed.getIdFeed()) != null) {
                throw new PreexistingEntityException("Feed " + feed + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Feed feed) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Feed persistentFeed = em.find(Feed.class, feed.getIdFeed());
            Warehouse warehouseOld = persistentFeed.getWarehouse();
            Warehouse warehouseNew = feed.getWarehouse();
            Collection<AnimalFeed> animalFeedCollectionOld = persistentFeed.getAnimalFeedCollection();
            Collection<AnimalFeed> animalFeedCollectionNew = feed.getAnimalFeedCollection();
            Collection<OrderPosition> orderPositionCollectionOld = persistentFeed.getOrderPositionCollection();
            Collection<OrderPosition> orderPositionCollectionNew = feed.getOrderPositionCollection();
            List<String> illegalOrphanMessages = null;
            for (AnimalFeed animalFeedCollectionOldAnimalFeed : animalFeedCollectionOld) {
                if (!animalFeedCollectionNew.contains(animalFeedCollectionOldAnimalFeed)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AnimalFeed " + animalFeedCollectionOldAnimalFeed + " since its feed field is not nullable.");
                }
            }
            for (OrderPosition orderPositionCollectionOldOrderPosition : orderPositionCollectionOld) {
                if (!orderPositionCollectionNew.contains(orderPositionCollectionOldOrderPosition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrderPosition " + orderPositionCollectionOldOrderPosition + " since its feed field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (warehouseNew != null) {
                warehouseNew = em.getReference(warehouseNew.getClass(), warehouseNew.getIdWarehouse());
                feed.setWarehouse(warehouseNew);
            }
            Collection<AnimalFeed> attachedAnimalFeedCollectionNew = new ArrayList<AnimalFeed>();
            for (AnimalFeed animalFeedCollectionNewAnimalFeedToAttach : animalFeedCollectionNew) {
                animalFeedCollectionNewAnimalFeedToAttach = em.getReference(animalFeedCollectionNewAnimalFeedToAttach.getClass(), animalFeedCollectionNewAnimalFeedToAttach.getIdAnimalFeed());
                attachedAnimalFeedCollectionNew.add(animalFeedCollectionNewAnimalFeedToAttach);
            }
            animalFeedCollectionNew = attachedAnimalFeedCollectionNew;
            feed.setAnimalFeedCollection(animalFeedCollectionNew);
            Collection<OrderPosition> attachedOrderPositionCollectionNew = new ArrayList<OrderPosition>();
            for (OrderPosition orderPositionCollectionNewOrderPositionToAttach : orderPositionCollectionNew) {
                orderPositionCollectionNewOrderPositionToAttach = em.getReference(orderPositionCollectionNewOrderPositionToAttach.getClass(), orderPositionCollectionNewOrderPositionToAttach.getIdOrderPosition());
                attachedOrderPositionCollectionNew.add(orderPositionCollectionNewOrderPositionToAttach);
            }
            orderPositionCollectionNew = attachedOrderPositionCollectionNew;
            feed.setOrderPositionCollection(orderPositionCollectionNew);
            feed = em.merge(feed);
            if (warehouseOld != null && !warehouseOld.equals(warehouseNew)) {
                warehouseOld.getFeedCollection().remove(feed);
                warehouseOld = em.merge(warehouseOld);
            }
            if (warehouseNew != null && !warehouseNew.equals(warehouseOld)) {
                warehouseNew.getFeedCollection().add(feed);
                warehouseNew = em.merge(warehouseNew);
            }
            for (AnimalFeed animalFeedCollectionNewAnimalFeed : animalFeedCollectionNew) {
                if (!animalFeedCollectionOld.contains(animalFeedCollectionNewAnimalFeed)) {
                    Feed oldFeedOfAnimalFeedCollectionNewAnimalFeed = animalFeedCollectionNewAnimalFeed.getFeed();
                    animalFeedCollectionNewAnimalFeed.setFeed(feed);
                    animalFeedCollectionNewAnimalFeed = em.merge(animalFeedCollectionNewAnimalFeed);
                    if (oldFeedOfAnimalFeedCollectionNewAnimalFeed != null && !oldFeedOfAnimalFeedCollectionNewAnimalFeed.equals(feed)) {
                        oldFeedOfAnimalFeedCollectionNewAnimalFeed.getAnimalFeedCollection().remove(animalFeedCollectionNewAnimalFeed);
                        oldFeedOfAnimalFeedCollectionNewAnimalFeed = em.merge(oldFeedOfAnimalFeedCollectionNewAnimalFeed);
                    }
                }
            }
            for (OrderPosition orderPositionCollectionNewOrderPosition : orderPositionCollectionNew) {
                if (!orderPositionCollectionOld.contains(orderPositionCollectionNewOrderPosition)) {
                    Feed oldFeedOfOrderPositionCollectionNewOrderPosition = orderPositionCollectionNewOrderPosition.getFeed();
                    orderPositionCollectionNewOrderPosition.setFeed(feed);
                    orderPositionCollectionNewOrderPosition = em.merge(orderPositionCollectionNewOrderPosition);
                    if (oldFeedOfOrderPositionCollectionNewOrderPosition != null && !oldFeedOfOrderPositionCollectionNewOrderPosition.equals(feed)) {
                        oldFeedOfOrderPositionCollectionNewOrderPosition.getOrderPositionCollection().remove(orderPositionCollectionNewOrderPosition);
                        oldFeedOfOrderPositionCollectionNewOrderPosition = em.merge(oldFeedOfOrderPositionCollectionNewOrderPosition);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = feed.getIdFeed();
                if (findFeed(id) == null) {
                    throw new NonexistentEntityException("The feed with id " + id + " no longer exists.");
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
            Feed feed;
            try {
                feed = em.getReference(Feed.class, id);
                feed.getIdFeed();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The feed with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AnimalFeed> animalFeedCollectionOrphanCheck = feed.getAnimalFeedCollection();
            for (AnimalFeed animalFeedCollectionOrphanCheckAnimalFeed : animalFeedCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Feed (" + feed + ") cannot be destroyed since the AnimalFeed " + animalFeedCollectionOrphanCheckAnimalFeed + " in its animalFeedCollection field has a non-nullable feed field.");
            }
            Collection<OrderPosition> orderPositionCollectionOrphanCheck = feed.getOrderPositionCollection();
            for (OrderPosition orderPositionCollectionOrphanCheckOrderPosition : orderPositionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Feed (" + feed + ") cannot be destroyed since the OrderPosition " + orderPositionCollectionOrphanCheckOrderPosition + " in its orderPositionCollection field has a non-nullable feed field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Warehouse warehouse = feed.getWarehouse();
            if (warehouse != null) {
                warehouse.getFeedCollection().remove(feed);
                warehouse = em.merge(warehouse);
            }
            em.remove(feed);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Feed> findFeedEntities() {
        return findFeedEntities(true, -1, -1);
    }

    public List<Feed> findFeedEntities(int maxResults, int firstResult) {
        return findFeedEntities(false, maxResults, firstResult);
    }

    private List<Feed> findFeedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Feed.class));
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

    public Feed findFeed(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Feed.class, id);
        } finally {
            em.close();
        }
    }

    public int getFeedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Feed> rt = cq.from(Feed.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public String deleteFeed(Integer id){
        try {
            destroy(id);
            System.out.println("OK usowanie jedzenie");
            return "OK";
        } catch (Exception e) {
            return "BLAD";
        }
    }
    
    public String addFeed(Feed f){
        try {
            int maxId = getEntityManager().createNamedQuery("Feed.findMaxId", Integer.class).getSingleResult();
            f.setIdFeed(++maxId);
            create(f);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }
    
    public String editFeed(Feed f){
        try {
            edit(f);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

}
