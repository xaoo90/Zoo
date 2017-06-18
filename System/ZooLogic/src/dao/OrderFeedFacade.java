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
import entity.Employee;
import entity.OrderFeed;
import entity.OrderPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class OrderFeedFacade implements Serializable {

    public OrderFeedFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OrderFeed orderFeed) throws PreexistingEntityException, Exception {
        if (orderFeed.getOrderPositionCollection() == null) {
            orderFeed.setOrderPositionCollection(new ArrayList<OrderPosition>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee warehouseman = orderFeed.getWarehouseMen();
            if (warehouseman != null) {
                warehouseman = em.getReference(warehouseman.getClass(), warehouseman.getIdEmployee());
                orderFeed.setWarehouseMen(warehouseman);
            }
            Collection<OrderPosition> attachedOrderPositionCollection = new ArrayList<OrderPosition>();
            for (OrderPosition orderPositionCollectionOrderPositionToAttach : orderFeed.getOrderPositionCollection()) {
                orderPositionCollectionOrderPositionToAttach = em.getReference(orderPositionCollectionOrderPositionToAttach.getClass(), orderPositionCollectionOrderPositionToAttach.getIdOrderPosition());
                attachedOrderPositionCollection.add(orderPositionCollectionOrderPositionToAttach);
            }
            orderFeed.setOrderPositionCollection(attachedOrderPositionCollection);
            em.persist(orderFeed);
            if (warehouseman != null) {
                warehouseman.getOrderFeedCollection().add(orderFeed);
                warehouseman = em.merge(warehouseman);
            }
            for (OrderPosition orderPositionCollectionOrderPosition : orderFeed.getOrderPositionCollection()) {
                OrderFeed oldOrderfeedOfOrderPositionCollectionOrderPosition = orderPositionCollectionOrderPosition.getOrderFeed();
                orderPositionCollectionOrderPosition.setOrderFeed(orderFeed);
                orderPositionCollectionOrderPosition = em.merge(orderPositionCollectionOrderPosition);
                if (oldOrderfeedOfOrderPositionCollectionOrderPosition != null) {
                    oldOrderfeedOfOrderPositionCollectionOrderPosition.getOrderPositionCollection().remove(orderPositionCollectionOrderPosition);
                    oldOrderfeedOfOrderPositionCollectionOrderPosition = em.merge(oldOrderfeedOfOrderPositionCollectionOrderPosition);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrderFeed(orderFeed.getIdOrder()) != null) {
                throw new PreexistingEntityException("OrderFeed " + orderFeed + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OrderFeed orderFeed) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OrderFeed persistentOrderFeed = em.find(OrderFeed.class, orderFeed.getIdOrder());
            Employee warehousemanOld = persistentOrderFeed.getWarehouseMen();
            Employee warehousemanNew = orderFeed.getWarehouseMen();
            Collection<OrderPosition> orderPositionCollectionOld = persistentOrderFeed.getOrderPositionCollection();
            Collection<OrderPosition> orderPositionCollectionNew = orderFeed.getOrderPositionCollection();
            List<String> illegalOrphanMessages = null;
            for (OrderPosition orderPositionCollectionOldOrderPosition : orderPositionCollectionOld) {
                if (!orderPositionCollectionNew.contains(orderPositionCollectionOldOrderPosition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrderPosition " + orderPositionCollectionOldOrderPosition + " since its orderfeed field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (warehousemanNew != null) {
                warehousemanNew = em.getReference(warehousemanNew.getClass(), warehousemanNew.getIdEmployee());
                orderFeed.setWarehouseMen(warehousemanNew);
            }
            Collection<OrderPosition> attachedOrderPositionCollectionNew = new ArrayList<OrderPosition>();
            for (OrderPosition orderPositionCollectionNewOrderPositionToAttach : orderPositionCollectionNew) {
                orderPositionCollectionNewOrderPositionToAttach = em.getReference(orderPositionCollectionNewOrderPositionToAttach.getClass(), orderPositionCollectionNewOrderPositionToAttach.getIdOrderPosition());
                attachedOrderPositionCollectionNew.add(orderPositionCollectionNewOrderPositionToAttach);
            }
            orderPositionCollectionNew = attachedOrderPositionCollectionNew;
            orderFeed.setOrderPositionCollection(orderPositionCollectionNew);
            orderFeed = em.merge(orderFeed);
            if (warehousemanOld != null && !warehousemanOld.equals(warehousemanNew)) {
                warehousemanOld.getOrderFeedCollection().remove(orderFeed);
                warehousemanOld = em.merge(warehousemanOld);
            }
            if (warehousemanNew != null && !warehousemanNew.equals(warehousemanOld)) {
                warehousemanNew.getOrderFeedCollection().add(orderFeed);
                warehousemanNew = em.merge(warehousemanNew);
            }
            for (OrderPosition orderPositionCollectionNewOrderPosition : orderPositionCollectionNew) {
                if (!orderPositionCollectionOld.contains(orderPositionCollectionNewOrderPosition)) {
                    OrderFeed oldOrderfeedOfOrderPositionCollectionNewOrderPosition = orderPositionCollectionNewOrderPosition.getOrderFeed();
                    orderPositionCollectionNewOrderPosition.setOrderFeed(orderFeed);
                    orderPositionCollectionNewOrderPosition = em.merge(orderPositionCollectionNewOrderPosition);
                    if (oldOrderfeedOfOrderPositionCollectionNewOrderPosition != null && !oldOrderfeedOfOrderPositionCollectionNewOrderPosition.equals(orderFeed)) {
                        oldOrderfeedOfOrderPositionCollectionNewOrderPosition.getOrderPositionCollection().remove(orderPositionCollectionNewOrderPosition);
                        oldOrderfeedOfOrderPositionCollectionNewOrderPosition = em.merge(oldOrderfeedOfOrderPositionCollectionNewOrderPosition);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orderFeed.getIdOrder();
                if (findOrderFeed(id) == null) {
                    throw new NonexistentEntityException("The orderFeed with id " + id + " no longer exists.");
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
            OrderFeed orderFeed;
            try {
                orderFeed = em.getReference(OrderFeed.class, id);
                orderFeed.getIdOrder();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderFeed with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<OrderPosition> orderPositionCollectionOrphanCheck = orderFeed.getOrderPositionCollection();
            for (OrderPosition orderPositionCollectionOrphanCheckOrderPosition : orderPositionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This OrderFeed (" + orderFeed + ") cannot be destroyed since the OrderPosition " + orderPositionCollectionOrphanCheckOrderPosition + " in its orderPositionCollection field has a non-nullable orderfeed field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Employee warehouseman = orderFeed.getWarehouseMen();
            if (warehouseman != null) {
                warehouseman.getOrderFeedCollection().remove(orderFeed);
                warehouseman = em.merge(warehouseman);
            }
            em.remove(orderFeed);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OrderFeed> findOrderFeedEntities() {
        return findOrderFeedEntities(true, -1, -1);
    }

    public List<OrderFeed> findOrderFeedEntities(int maxResults, int firstResult) {
        return findOrderFeedEntities(false, maxResults, firstResult);
    }

    private List<OrderFeed> findOrderFeedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OrderFeed.class));
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

    public OrderFeed findOrderFeed(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OrderFeed.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderFeedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OrderFeed> rt = cq.from(OrderFeed.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<OrderPosition> showPosition(OrderFeed o) {
        ArrayList<OrderPosition> lst = new ArrayList();
        for (OrderPosition p : o.getOrderPositionCollection()) {
            lst.add(p);
        }
        return lst;
    }

    public List<OrderFeed> getOrderCondition(String s) {
        List<OrderFeed> lst = getEntityManager().createNamedQuery("OrderFeed.findHistoryOrder", OrderFeed.class).setParameter("condition", s).getResultList();
        return lst;
    }

    public OrderFeed addOrderFeed(OrderFeed of) throws Exception {
        int maxId = getEntityManager().createNamedQuery("OrderFeed.findMaxId", Integer.class).getSingleResult();
        of.setIdOrder(++maxId);
        create(of);
        return getEntityManager().createNamedQuery("OrderFeed.findLastAddEntity", OrderFeed.class).getSingleResult();
    }

    public String editOrderFeed(OrderFeed of) {
        try {
            edit(of);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

}
