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
import entity.Employee;
import entity.Holiday;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class HolidayFacade implements Serializable {

    public HolidayFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Holiday holiday) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employee = holiday.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getIdEmployee());
                holiday.setEmployee(employee);
            }
            em.persist(holiday);
            if (employee != null) {
                employee.getHolidayCollection().add(holiday);
                employee = em.merge(employee);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHoliday(holiday.getIdHoliday()) != null) {
                throw new PreexistingEntityException("Holiday " + holiday + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Holiday holiday) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Holiday persistentHoliday = em.find(Holiday.class, holiday.getIdHoliday());
            Employee employeeOld = persistentHoliday.getEmployee();
            Employee employeeNew = holiday.getEmployee();
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getIdEmployee());
                holiday.setEmployee(employeeNew);
            }
            holiday = em.merge(holiday);
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.getHolidayCollection().remove(holiday);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                employeeNew.getHolidayCollection().add(holiday);
                employeeNew = em.merge(employeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = holiday.getIdHoliday();
                if (findHoliday(id) == null) {
                    throw new NonexistentEntityException("The holiday with id " + id + " no longer exists.");
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
            Holiday holiday;
            try {
                holiday = em.getReference(Holiday.class, id);
                holiday.getIdHoliday();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The holiday with id " + id + " no longer exists.", enfe);
            }
            Employee employee = holiday.getEmployee();
            if (employee != null) {
                employee.getHolidayCollection().remove(holiday);
                employee = em.merge(employee);
            }
            em.remove(holiday);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Holiday> findHolidayEntities() {
        return findHolidayEntities(true, -1, -1);
    }

    public List<Holiday> findHolidayEntities(int maxResults, int firstResult) {
        return findHolidayEntities(false, maxResults, firstResult);
    }

    private List<Holiday> findHolidayEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Holiday.class));
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

    public Holiday findHoliday(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Holiday.class, id);
        } finally {
            em.close();
        }
    }

    public int getHolidayCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Holiday> rt = cq.from(Holiday.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public String addHoliday(Holiday h) {
        try {
            int maxId = getEntityManager().createNamedQuery("Holiday.findMaxId", Integer.class).getSingleResult();
            h.setIdHoliday(++maxId);
            create(h);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }
    
    public String editHoliday(Holiday holiday) {
        try {
            edit(holiday);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }
}
