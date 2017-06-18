/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import entity.Ticket;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TicketType;
import entity.WorkSchedule;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Xaoo
 */
public class TicketFacade implements Serializable {

    public TicketFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ticket ticket) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TicketType tickettype = ticket.getTicketType();
            if (tickettype != null) {
                tickettype = em.getReference(tickettype.getClass(), tickettype.getIdTicketType());
                ticket.setTicketType(tickettype);
            }
            em.persist(ticket);
            if (tickettype != null) {
                tickettype.getTicketCollection().add(ticket);
                tickettype = em.merge(tickettype);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTicket(ticket.getIdTicket()) != null) {
                throw new PreexistingEntityException("Ticket " + ticket + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ticket ticket) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ticket persistentTicket = em.find(Ticket.class, ticket.getIdTicket());
            TicketType tickettypeOld = persistentTicket.getTicketType();
            TicketType tickettypeNew = ticket.getTicketType();
            if (tickettypeNew != null) {
                tickettypeNew = em.getReference(tickettypeNew.getClass(), tickettypeNew.getIdTicketType());
                ticket.setTicketType(tickettypeNew);
            }
            ticket = em.merge(ticket);
            if (tickettypeOld != null && !tickettypeOld.equals(tickettypeNew)) {
                tickettypeOld.getTicketCollection().remove(ticket);
                tickettypeOld = em.merge(tickettypeOld);
            }
            if (tickettypeNew != null && !tickettypeNew.equals(tickettypeOld)) {
                tickettypeNew.getTicketCollection().add(ticket);
                tickettypeNew = em.merge(tickettypeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ticket.getIdTicket();
                if (findTicket(id) == null) {
                    throw new NonexistentEntityException("The ticket with id " + id + " no longer exists.");
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
            Ticket ticket;
            try {
                ticket = em.getReference(Ticket.class, id);
                ticket.getIdTicket();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ticket with id " + id + " no longer exists.", enfe);
            }
            TicketType tickettype = ticket.getTicketType();
            if (tickettype != null) {
                tickettype.getTicketCollection().remove(ticket);
                tickettype = em.merge(tickettype);
            }
            em.remove(ticket);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ticket> findTicketEntities() {
        return findTicketEntities(true, -1, -1);
    }

    public List<Ticket> findTicketEntities(int maxResults, int firstResult) {
        return findTicketEntities(false, maxResults, firstResult);
    }

    private List<Ticket> findTicketEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ticket.class));
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

    public Ticket findTicket(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ticket.class, id);
        } finally {
            em.close();
        }
    }

    public int getTicketCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ticket> rt = cq.from(Ticket.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Ticket> showDateTicket(Date d) {
        try {
            List<Ticket> lstTicket = getEntityManager().createNamedQuery("Ticket.findByIssuedDate", Ticket.class).setParameter("issuedDate", d).getResultList();
            return lstTicket;
        } catch (NoResultException e) {
            System.out.println("brak biletów ");
            return null;
        }
    }

    public List<Ticket> showDateTicketYear(Integer year) {
        try {
            List<Ticket> lstTicket = getEntityManager().createNamedQuery("Ticket.findByIssuedDateYear", Ticket.class).setParameter("year", year).getResultList();
            return lstTicket;
        } catch (NoResultException e) {
            System.out.println("brak biletów ");
            return null;
        }
    }

    public List<Ticket> showDateTicketMonth(Integer year, Integer month) {
        try {
            List<Ticket> lstTicket = getEntityManager().createNamedQuery("Ticket.findByIssuedDateMonth", Ticket.class).setParameter("year", year).setParameter("month", month).getResultList();
            return lstTicket;
        } catch (NoResultException e) {
            System.out.println("brak biletów ");
            return null;
        }
    }

    public void addTicket(Ticket t) throws Exception {
        try {
            Date d = Calendar.getInstance().getTime();
            Ticket tic = getEntityManager().createNamedQuery("Ticket.findByTicketExist", Ticket.class).setParameter("ticketType", t.getTicketType()).setParameter("issuedDate", d).getSingleResult();
            tic.setAmount(tic.getAmount() + t.getAmount());
            tic.setCost(tic.getCost().add(t.getCost()));
            int maxId = getEntityManager().createNamedQuery("Ticket.findMaxId", Integer.class).getSingleResult();
            tic.setIdTicket(++maxId);
            edit(tic);
        } catch (NoResultException e) {
            create(t);
        }
    }

}
