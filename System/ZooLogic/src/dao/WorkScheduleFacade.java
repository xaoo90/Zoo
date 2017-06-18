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
import entity.EmployeeSchedule;
import entity.WorkSchedule;
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
public class WorkScheduleFacade implements Serializable {

    public WorkScheduleFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WorkSchedule workSchedule) throws PreexistingEntityException, Exception {
        if (workSchedule.getEmployeeScheduleCollection() == null) {
            workSchedule.setEmployeeScheduleCollection(new ArrayList<EmployeeSchedule>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<EmployeeSchedule> attachedEmployeeScheduleCollection = new ArrayList<EmployeeSchedule>();
            for (EmployeeSchedule employeeScheduleCollectionEmployeeScheduleToAttach : workSchedule.getEmployeeScheduleCollection()) {
                employeeScheduleCollectionEmployeeScheduleToAttach = em.getReference(employeeScheduleCollectionEmployeeScheduleToAttach.getClass(), employeeScheduleCollectionEmployeeScheduleToAttach.getIdEmployeeSchedule());
                attachedEmployeeScheduleCollection.add(employeeScheduleCollectionEmployeeScheduleToAttach);
            }
            workSchedule.setEmployeeScheduleCollection(attachedEmployeeScheduleCollection);
            em.persist(workSchedule);
            for (EmployeeSchedule employeeScheduleCollectionEmployeeSchedule : workSchedule.getEmployeeScheduleCollection()) {
                WorkSchedule oldWorkscheduleOfEmployeeScheduleCollectionEmployeeSchedule = employeeScheduleCollectionEmployeeSchedule.getWorkSchedule();
                employeeScheduleCollectionEmployeeSchedule.setWorkSchedule(workSchedule);
                employeeScheduleCollectionEmployeeSchedule = em.merge(employeeScheduleCollectionEmployeeSchedule);
                if (oldWorkscheduleOfEmployeeScheduleCollectionEmployeeSchedule != null) {
                    oldWorkscheduleOfEmployeeScheduleCollectionEmployeeSchedule.getEmployeeScheduleCollection().remove(employeeScheduleCollectionEmployeeSchedule);
                    oldWorkscheduleOfEmployeeScheduleCollectionEmployeeSchedule = em.merge(oldWorkscheduleOfEmployeeScheduleCollectionEmployeeSchedule);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWorkSchedule(workSchedule.getIdWorkSchedule()) != null) {
                throw new PreexistingEntityException("WorkSchedule " + workSchedule + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WorkSchedule workSchedule) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WorkSchedule persistentWorkSchedule = em.find(WorkSchedule.class, workSchedule.getIdWorkSchedule());
            Collection<EmployeeSchedule> employeeScheduleCollectionOld = persistentWorkSchedule.getEmployeeScheduleCollection();
            Collection<EmployeeSchedule> employeeScheduleCollectionNew = workSchedule.getEmployeeScheduleCollection();
            List<String> illegalOrphanMessages = null;
            for (EmployeeSchedule employeeScheduleCollectionOldEmployeeSchedule : employeeScheduleCollectionOld) {
                if (!employeeScheduleCollectionNew.contains(employeeScheduleCollectionOldEmployeeSchedule)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EmployeeSchedule " + employeeScheduleCollectionOldEmployeeSchedule + " since its workschedule field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<EmployeeSchedule> attachedEmployeeScheduleCollectionNew = new ArrayList<EmployeeSchedule>();
            for (EmployeeSchedule employeeScheduleCollectionNewEmployeeScheduleToAttach : employeeScheduleCollectionNew) {
                employeeScheduleCollectionNewEmployeeScheduleToAttach = em.getReference(employeeScheduleCollectionNewEmployeeScheduleToAttach.getClass(), employeeScheduleCollectionNewEmployeeScheduleToAttach.getIdEmployeeSchedule());
                attachedEmployeeScheduleCollectionNew.add(employeeScheduleCollectionNewEmployeeScheduleToAttach);
            }
            employeeScheduleCollectionNew = attachedEmployeeScheduleCollectionNew;
            workSchedule.setEmployeeScheduleCollection(employeeScheduleCollectionNew);
            workSchedule = em.merge(workSchedule);
            for (EmployeeSchedule employeeScheduleCollectionNewEmployeeSchedule : employeeScheduleCollectionNew) {
                if (!employeeScheduleCollectionOld.contains(employeeScheduleCollectionNewEmployeeSchedule)) {
                    WorkSchedule oldWorkscheduleOfEmployeeScheduleCollectionNewEmployeeSchedule = employeeScheduleCollectionNewEmployeeSchedule.getWorkSchedule();
                    employeeScheduleCollectionNewEmployeeSchedule.setWorkSchedule(workSchedule);
                    employeeScheduleCollectionNewEmployeeSchedule = em.merge(employeeScheduleCollectionNewEmployeeSchedule);
                    if (oldWorkscheduleOfEmployeeScheduleCollectionNewEmployeeSchedule != null && !oldWorkscheduleOfEmployeeScheduleCollectionNewEmployeeSchedule.equals(workSchedule)) {
                        oldWorkscheduleOfEmployeeScheduleCollectionNewEmployeeSchedule.getEmployeeScheduleCollection().remove(employeeScheduleCollectionNewEmployeeSchedule);
                        oldWorkscheduleOfEmployeeScheduleCollectionNewEmployeeSchedule = em.merge(oldWorkscheduleOfEmployeeScheduleCollectionNewEmployeeSchedule);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = workSchedule.getIdWorkSchedule();
                if (findWorkSchedule(id) == null) {
                    throw new NonexistentEntityException("The workSchedule with id " + id + " no longer exists.");
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
            WorkSchedule workSchedule;
            try {
                workSchedule = em.getReference(WorkSchedule.class, id);
                workSchedule.getIdWorkSchedule();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The workSchedule with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<EmployeeSchedule> employeeScheduleCollectionOrphanCheck = workSchedule.getEmployeeScheduleCollection();
            for (EmployeeSchedule employeeScheduleCollectionOrphanCheckEmployeeSchedule : employeeScheduleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This WorkSchedule (" + workSchedule + ") cannot be destroyed since the EmployeeSchedule " + employeeScheduleCollectionOrphanCheckEmployeeSchedule + " in its employeeScheduleCollection field has a non-nullable workschedule field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(workSchedule);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WorkSchedule> findWorkScheduleEntities() {
        return findWorkScheduleEntities(true, -1, -1);
    }

    public List<WorkSchedule> findWorkScheduleEntities(int maxResults, int firstResult) {
        return findWorkScheduleEntities(false, maxResults, firstResult);
    }

    private List<WorkSchedule> findWorkScheduleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WorkSchedule.class));
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

    public WorkSchedule findWorkSchedule(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WorkSchedule.class, id);
        } finally {
            em.close();
        }
    }

    public int getWorkScheduleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WorkSchedule> rt = cq.from(WorkSchedule.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public WorkSchedule getWorkSchedule(WorkSchedule ws) throws Exception {
        try {
            WorkSchedule workSchedule = getEntityManager().createNamedQuery("WorkSchedule.findWorkScheduleExist", WorkSchedule.class).setParameter("workDate", ws.getWorkDate()).setParameter("workShifts", ws.getWorkShifts()).getSingleResult();
            return workSchedule;
        } catch (NoResultException e) {
            int maxId = getEntityManager().createNamedQuery("WorkSchedule.findMaxId", Integer.class).getSingleResult();
            ws.setIdWorkSchedule(++maxId);
            create(ws);
            return getEntityManager().createNamedQuery("WorkSchedule.findLastAddEntity", WorkSchedule.class).getSingleResult();
        }
    }

}
