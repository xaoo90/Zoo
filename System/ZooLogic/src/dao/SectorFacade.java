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
import entity.Cage;
import entity.EmployeeSchedule;
import entity.Sector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Xaoo
 */
public class SectorFacade implements Serializable {

    public SectorFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Sector sector) throws PreexistingEntityException, Exception {
        if (sector.getCageCollection() == null) {
            sector.setCageCollection(new ArrayList<Cage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee manager = sector.getManager();
            if (manager != null) {
                manager = em.getReference(manager.getClass(), manager.getIdEmployee());
                sector.setManager(manager);
            }
            Collection<Cage> attachedCageCollection = new ArrayList<Cage>();
            for (Cage cageCollectionCageToAttach : sector.getCageCollection()) {
                cageCollectionCageToAttach = em.getReference(cageCollectionCageToAttach.getClass(), cageCollectionCageToAttach.getIdCage());
                attachedCageCollection.add(cageCollectionCageToAttach);
            }
            sector.setCageCollection(attachedCageCollection);
            em.persist(sector);
            if (manager != null) {
                manager.getSectorCollection().add(sector);
                manager = em.merge(manager);
            }
            for (Cage cageCollectionCage : sector.getCageCollection()) {
                Sector oldSectorOfCageCollectionCage = cageCollectionCage.getSector();
                cageCollectionCage.setSector(sector);
                cageCollectionCage = em.merge(cageCollectionCage);
                if (oldSectorOfCageCollectionCage != null) {
                    oldSectorOfCageCollectionCage.getCageCollection().remove(cageCollectionCage);
                    oldSectorOfCageCollectionCage = em.merge(oldSectorOfCageCollectionCage);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSector(sector.getIdSector()) != null) {
                throw new PreexistingEntityException("Sector " + sector + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Sector sector) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sector persistentSector = em.find(Sector.class, sector.getIdSector());
            Employee managerOld = persistentSector.getManager();
            Employee managerNew = sector.getManager();
            Collection<Cage> cageCollectionOld = persistentSector.getCageCollection();
            Collection<Cage> cageCollectionNew = sector.getCageCollection();
            List<String> illegalOrphanMessages = null;
            for (Cage cageCollectionOldCage : cageCollectionOld) {
                if (!cageCollectionNew.contains(cageCollectionOldCage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cage " + cageCollectionOldCage + " since its sector field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (managerNew != null) {
                managerNew = em.getReference(managerNew.getClass(), managerNew.getIdEmployee());
                sector.setManager(managerNew);
            }
            Collection<Cage> attachedCageCollectionNew = new ArrayList<Cage>();
            for (Cage cageCollectionNewCageToAttach : cageCollectionNew) {
                cageCollectionNewCageToAttach = em.getReference(cageCollectionNewCageToAttach.getClass(), cageCollectionNewCageToAttach.getIdCage());
                attachedCageCollectionNew.add(cageCollectionNewCageToAttach);
            }
            cageCollectionNew = attachedCageCollectionNew;
            sector.setCageCollection(cageCollectionNew);
            sector = em.merge(sector);
            if (managerOld != null && !managerOld.equals(managerNew)) {
                managerOld.getSectorCollection().remove(sector);
                managerOld = em.merge(managerOld);
            }
            if (managerNew != null && !managerNew.equals(managerOld)) {
                managerNew.getSectorCollection().add(sector);
                managerNew = em.merge(managerNew);
            }
            for (Cage cageCollectionNewCage : cageCollectionNew) {
                if (!cageCollectionOld.contains(cageCollectionNewCage)) {
                    Sector oldSectorOfCageCollectionNewCage = cageCollectionNewCage.getSector();
                    cageCollectionNewCage.setSector(sector);
                    cageCollectionNewCage = em.merge(cageCollectionNewCage);
                    if (oldSectorOfCageCollectionNewCage != null && !oldSectorOfCageCollectionNewCage.equals(sector)) {
                        oldSectorOfCageCollectionNewCage.getCageCollection().remove(cageCollectionNewCage);
                        oldSectorOfCageCollectionNewCage = em.merge(oldSectorOfCageCollectionNewCage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sector.getIdSector();
                if (findSector(id) == null) {
                    throw new NonexistentEntityException("The sector with id " + id + " no longer exists.");
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
            Sector sector;
            try {
                sector = em.getReference(Sector.class, id);
                sector.getIdSector();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sector with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cage> cageCollectionOrphanCheck = sector.getCageCollection();
            for (Cage cageCollectionOrphanCheckCage : cageCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Sector (" + sector + ") cannot be destroyed since the Cage " + cageCollectionOrphanCheckCage + " in its cageCollection field has a non-nullable sector field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Employee manager = sector.getManager();
            if (manager != null) {
                manager.getSectorCollection().remove(sector);
                manager = em.merge(manager);
            }
            em.remove(sector);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Sector> findSectorEntities() {
        return findSectorEntities(true, -1, -1);
    }

    public List<Sector> findSectorEntities(int maxResults, int firstResult) {
        return findSectorEntities(false, maxResults, firstResult);
    }

    private List<Sector> findSectorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sector.class));
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

    public Sector findSector(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Sector.class, id);
        } finally {
            em.close();
        }
    }

    public int getSectorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sector> rt = cq.from(Sector.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Cage> showCage(Sector s) {
        ArrayList<Cage> lst = new ArrayList();
        for (Cage c : s.getCageCollection()) {
            lst.add(c);
        }
        return lst;
    }

    public String addSector(Sector s) throws Exception {
        try {
            getEntityManager().createNamedQuery("Sector.findByName", Sector.class).setParameter("name", s.getName()).getSingleResult();
        } catch (NoResultException e) {
            int maxId = getEntityManager().createNamedQuery("Sector.findMaxId", Integer.class).getSingleResult();
            s.setIdSector(++maxId);
            create(s);
            return "OK";
        }
        return "ERR";
    }

    public List<Sector> getNullSector() {
        try {
            List<Sector> lst = getEntityManager().createNamedQuery("Sector.findManagerNull", Sector.class).getResultList();
            return lst;
        } catch (Exception e) {
            return null;
        }
    }

    public String deleteSector(Integer id) {
        try {
            destroy(id);
            System.out.println("OK usowanie sector");
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }
    
    public String editSector(Sector s){
        try {
            edit(s);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }

}
