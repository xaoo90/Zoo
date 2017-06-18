/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Feed;
import entity.OrderFeed;
import entity.OrderPosition;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class OrderPositionFacade implements Serializable {

    public OrderPositionFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrderPosition orderPosition) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Feed feed = orderPosition.getFeed();
            if (feed != null) {
                feed = em.getReference(feed.getClass(), feed.getIdFeed());
                orderPosition.setFeed(feed);
            }
            OrderFeed orderfeed = orderPosition.getOrderFeed();
            if (orderfeed != null) {
                orderfeed = em.getReference(orderfeed.getClass(), orderfeed.getIdOrder());
                orderPosition.setOrderFeed(orderfeed);
            }
            em.persist(orderPosition);
            if (feed != null) {
                feed.getOrderPositionCollection().add(orderPosition);
                feed = em.merge(feed);
            }
            if (orderfeed != null) {
                orderfeed.getOrderPositionCollection().add(orderPosition);
                orderfeed = em.merge(orderfeed);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrderPosition(orderPosition.getIdOrderPosition()) != null) {
                throw new PreexistingEntityException("OrderPosition " + orderPosition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrderPosition orderPosition) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrderPosition persistentOrderPosition = em.find(OrderPosition.class, orderPosition.getIdOrderPosition());
            Feed feedOld = persistentOrderPosition.getFeed();
            Feed feedNew = orderPosition.getFeed();
            OrderFeed orderfeedOld = persistentOrderPosition.getOrderFeed();
            OrderFeed orderfeedNew = orderPosition.getOrderFeed();
            if (feedNew != null) {
                feedNew = em.getReference(feedNew.getClass(), feedNew.getIdFeed());
                orderPosition.setFeed(feedNew);
            }
            if (orderfeedNew != null) {
                orderfeedNew = em.getReference(orderfeedNew.getClass(), orderfeedNew.getIdOrder());
                orderPosition.setOrderFeed(orderfeedNew);
            }
            orderPosition = em.merge(orderPosition);
            if (feedOld != null && !feedOld.equals(feedNew)) {
                feedOld.getOrderPositionCollection().remove(orderPosition);
                feedOld = em.merge(feedOld);
            }
            if (feedNew != null && !feedNew.equals(feedOld)) {
                feedNew.getOrderPositionCollection().add(orderPosition);
                feedNew = em.merge(feedNew);
            }
            if (orderfeedOld != null && !orderfeedOld.equals(orderfeedNew)) {
                orderfeedOld.getOrderPositionCollection().remove(orderPosition);
                orderfeedOld = em.merge(orderfeedOld);
            }
            if (orderfeedNew != null && !orderfeedNew.equals(orderfeedOld)) {
                orderfeedNew.getOrderPositionCollection().add(orderPosition);
                orderfeedNew = em.merge(orderfeedNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orderPosition.getIdOrderPosition();
                if (findOrderPosition(id) == null) {
                    throw new NonexistentEntityException("The orderPosition with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrderPosition orderPosition;
            try {
                orderPosition = em.getReference(OrderPosition.class, id);
                orderPosition.getIdOrderPosition();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderPosition with id " + id + " no longer exists.", enfe);
            }
            Feed feed = orderPosition.getFeed();
            if (feed != null) {
                feed.getOrderPositionCollection().remove(orderPosition);
                feed = em.merge(feed);
            }
            OrderFeed orderfeed = orderPosition.getOrderFeed();
            if (orderfeed != null) {
                orderfeed.getOrderPositionCollection().remove(orderPosition);
                orderfeed = em.merge(orderfeed);
            }
            em.remove(orderPosition);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrderPosition> findOrderPositionEntities() {
        return findOrderPositionEntities(true, -1, -1);
    }

    public List<OrderPosition> findOrderPositionEntities(int maxResults, int firstResult) {
        return findOrderPositionEntities(false, maxResults, firstResult);
    }

    private List<OrderPosition> findOrderPositionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrderPosition.class));
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

    public OrderPosition findOrderPosition(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrderPosition.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderPositionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrderPosition> rt = cq.from(OrderPosition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public String addOrderPosition(OrderPosition o) throws Exception {
        try {
            int maxId = getEntityManager().createNamedQuery("OrderPosition.findMaxId", Integer.class).getSingleResult();
            o.setIdOrderPosition(++maxId);
            create(o);
            return "OK";
        } catch (Exception e) {
            return ("ERR");
        }
    }

    public String editOrderPosition(OrderPosition o) {
        try {
            edit(o);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

}
