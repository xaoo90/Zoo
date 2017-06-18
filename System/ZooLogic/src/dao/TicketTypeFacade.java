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
import entity.Ticket;
import entity.TicketType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class TicketTypeFacade implements Serializable {

    public TicketTypeFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TicketType ticketType) throws PreexistingEntityException, Exception {
        if (ticketType.getTicketCollection() == null) {
            ticketType.setTicketCollection(new ArrayList<Ticket>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Ticket> attachedTicketCollection = new ArrayList<Ticket>();
            for (Ticket ticketCollectionTicketToAttach : ticketType.getTicketCollection()) {
                ticketCollectionTicketToAttach = em.getReference(ticketCollectionTicketToAttach.getClass(), ticketCollectionTicketToAttach.getIdTicket());
                attachedTicketCollection.add(ticketCollectionTicketToAttach);
            }
            ticketType.setTicketCollection(attachedTicketCollection);
            em.persist(ticketType);
            for (Ticket ticketCollectionTicket : ticketType.getTicketCollection()) {
                TicketType oldTickettypeOfTicketCollectionTicket = ticketCollectionTicket.getTicketType();
                ticketCollectionTicket.setTicketType(ticketType);
                ticketCollectionTicket = em.merge(ticketCollectionTicket);
                if (oldTickettypeOfTicketCollectionTicket != null) {
                    oldTickettypeOfTicketCollectionTicket.getTicketCollection().remove(ticketCollectionTicket);
                    oldTickettypeOfTicketCollectionTicket = em.merge(oldTickettypeOfTicketCollectionTicket);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTicketType(ticketType.getIdTicketType()) != null) {
                throw new PreexistingEntityException("TicketType " + ticketType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TicketType ticketType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TicketType persistentTicketType = em.find(TicketType.class, ticketType.getIdTicketType());
            Collection<Ticket> ticketCollectionOld = persistentTicketType.getTicketCollection();
            Collection<Ticket> ticketCollectionNew = ticketType.getTicketCollection();
            List<String> illegalOrphanMessages = null;
            for (Ticket ticketCollectionOldTicket : ticketCollectionOld) {
                if (!ticketCollectionNew.contains(ticketCollectionOldTicket)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ticket " + ticketCollectionOldTicket + " since its tickettype field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Ticket> attachedTicketCollectionNew = new ArrayList<Ticket>();
            for (Ticket ticketCollectionNewTicketToAttach : ticketCollectionNew) {
                ticketCollectionNewTicketToAttach = em.getReference(ticketCollectionNewTicketToAttach.getClass(), ticketCollectionNewTicketToAttach.getIdTicket());
                attachedTicketCollectionNew.add(ticketCollectionNewTicketToAttach);
            }
            ticketCollectionNew = attachedTicketCollectionNew;
            ticketType.setTicketCollection(ticketCollectionNew);
            ticketType = em.merge(ticketType);
            for (Ticket ticketCollectionNewTicket : ticketCollectionNew) {
                if (!ticketCollectionOld.contains(ticketCollectionNewTicket)) {
                    TicketType oldTickettypeOfTicketCollectionNewTicket = ticketCollectionNewTicket.getTicketType();
                    ticketCollectionNewTicket.setTicketType(ticketType);
                    ticketCollectionNewTicket = em.merge(ticketCollectionNewTicket);
                    if (oldTickettypeOfTicketCollectionNewTicket != null && !oldTickettypeOfTicketCollectionNewTicket.equals(ticketType)) {
                        oldTickettypeOfTicketCollectionNewTicket.getTicketCollection().remove(ticketCollectionNewTicket);
                        oldTickettypeOfTicketCollectionNewTicket = em.merge(oldTickettypeOfTicketCollectionNewTicket);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ticketType.getIdTicketType();
                if (findTicketType(id) == null) {
                    throw new NonexistentEntityException("The ticketType with id " + id + " no longer exists.");
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
            TicketType ticketType;
            try {
                ticketType = em.getReference(TicketType.class, id);
                ticketType.getIdTicketType();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ticketType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Ticket> ticketCollectionOrphanCheck = ticketType.getTicketCollection();
            for (Ticket ticketCollectionOrphanCheckTicket : ticketCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TicketType (" + ticketType + ") cannot be destroyed since the Ticket " + ticketCollectionOrphanCheckTicket + " in its ticketCollection field has a non-nullable tickettype field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ticketType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TicketType> findTicketTypeEntities() {
        return findTicketTypeEntities(true, -1, -1);
    }

    public List<TicketType> findTicketTypeEntities(int maxResults, int firstResult) {
        return findTicketTypeEntities(false, maxResults, firstResult);
    }

    private List<TicketType> findTicketTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TicketType.class));
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

    public TicketType findTicketType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TicketType.class, id);
        } finally {
            em.close();
        }
    }

    public int getTicketTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TicketType> rt = cq.from(TicketType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Ticket> showTicket(TicketType tt) {
        ArrayList<Ticket> lst = new ArrayList();
        for (Ticket t : tt.getTicketCollection()) {
            lst.add(t);
        }
        return lst;
    }
    
    public String addTicketType(TicketType tt) {
        try {
            int maxId = getEntityManager().createNamedQuery("TicketType.findMaxId", Integer.class).getSingleResult();
            tt.setIdTicketType(++maxId);
            create(tt);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }
    
    public String editTicketType(TicketType tt) {
        try {
            edit(tt);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

    public String deleteTicketType(Integer id) {
        try {
            destroy(id);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }
    
}
