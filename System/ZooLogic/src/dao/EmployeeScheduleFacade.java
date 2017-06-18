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
import entity.EmployeeSchedule;
import entity.WorkSchedule;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class EmployeeScheduleFacade implements Serializable {

    public EmployeeScheduleFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EmployeeSchedule employeeSchedule) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employee = employeeSchedule.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getIdEmployee());
                employeeSchedule.setEmployee(employee);
            }
            WorkSchedule workschedule = employeeSchedule.getWorkSchedule();
            if (workschedule != null) {
                workschedule = em.getReference(workschedule.getClass(), workschedule.getIdWorkSchedule());
                employeeSchedule.setWorkSchedule(workschedule);
            }
            em.persist(employeeSchedule);
            if (employee != null) {
                employee.getEmployeeScheduleCollection().add(employeeSchedule);
                employee = em.merge(employee);
            }
            if (workschedule != null) {
                workschedule.getEmployeeScheduleCollection().add(employeeSchedule);
                workschedule = em.merge(workschedule);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmployeeSchedule(employeeSchedule.getIdEmployeeSchedule()) != null) {
                throw new PreexistingEntityException("EmployeeSchedule " + employeeSchedule + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EmployeeSchedule employeeSchedule) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EmployeeSchedule persistentEmployeeSchedule = em.find(EmployeeSchedule.class, employeeSchedule.getIdEmployeeSchedule());
            Employee employeeOld = persistentEmployeeSchedule.getEmployee();
            Employee employeeNew = employeeSchedule.getEmployee();
            WorkSchedule workscheduleOld = persistentEmployeeSchedule.getWorkSchedule();
            WorkSchedule workscheduleNew = employeeSchedule.getWorkSchedule();
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getIdEmployee());
                employeeSchedule.setEmployee(employeeNew);
            }
            if (workscheduleNew != null) {
                workscheduleNew = em.getReference(workscheduleNew.getClass(), workscheduleNew.getIdWorkSchedule());
                employeeSchedule.setWorkSchedule(workscheduleNew);
            }
            employeeSchedule = em.merge(employeeSchedule);
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.getEmployeeScheduleCollection().remove(employeeSchedule);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                employeeNew.getEmployeeScheduleCollection().add(employeeSchedule);
                employeeNew = em.merge(employeeNew);
            }
            if (workscheduleOld != null && !workscheduleOld.equals(workscheduleNew)) {
                workscheduleOld.getEmployeeScheduleCollection().remove(employeeSchedule);
                workscheduleOld = em.merge(workscheduleOld);
            }
            if (workscheduleNew != null && !workscheduleNew.equals(workscheduleOld)) {
                workscheduleNew.getEmployeeScheduleCollection().add(employeeSchedule);
                workscheduleNew = em.merge(workscheduleNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = employeeSchedule.getIdEmployeeSchedule();
                if (findEmployeeSchedule(id) == null) {
                    throw new NonexistentEntityException("The employeeSchedule with id " + id + " no longer exists.");
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
            EmployeeSchedule employeeSchedule;
            try {
                employeeSchedule = em.getReference(EmployeeSchedule.class, id);
                employeeSchedule.getIdEmployeeSchedule();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employeeSchedule with id " + id + " no longer exists.", enfe);
            }
            Employee employee = employeeSchedule.getEmployee();
            if (employee != null) {
                employee.getEmployeeScheduleCollection().remove(employeeSchedule);
                employee = em.merge(employee);
            }
            WorkSchedule workschedule = employeeSchedule.getWorkSchedule();
            if (workschedule != null) {
                workschedule.getEmployeeScheduleCollection().remove(employeeSchedule);
                workschedule = em.merge(workschedule);
            }
            em.remove(employeeSchedule);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EmployeeSchedule> findEmployeeScheduleEntities() {
        return findEmployeeScheduleEntities(true, -1, -1);
    }

    public List<EmployeeSchedule> findEmployeeScheduleEntities(int maxResults, int firstResult) {
        return findEmployeeScheduleEntities(false, maxResults, firstResult);
    }

    private List<EmployeeSchedule> findEmployeeScheduleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EmployeeSchedule.class));
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

    public EmployeeSchedule findEmployeeSchedule(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EmployeeSchedule.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeScheduleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EmployeeSchedule> rt = cq.from(EmployeeSchedule.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public String addEmpSchedule(EmployeeSchedule es) throws Exception {
        try {
            int maxId = getEntityManager().createNamedQuery("EmployeeSchedule.findMaxId", Integer.class).getSingleResult();
            es.setIdEmployeeSchedule(++maxId);
            create(es);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }

    public String editEmpSchedule(EmployeeSchedule es) {
        try {
            edit(es);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }

}
