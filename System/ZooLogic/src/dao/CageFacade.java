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
import entity.Sector;
import entity.Animal;
import entity.Cage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class CageFacade implements Serializable {

    public CageFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cage cage) throws PreexistingEntityException, Exception {
        if (cage.getAnimalCollection() == null) {
            cage.setAnimalCollection(new ArrayList<Animal>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sector sector = cage.getSector();
            if (sector != null) {
                sector = em.getReference(sector.getClass(), sector.getIdSector());
                cage.setSector(sector);
            }
            Collection<Animal> attachedAnimalCollection = new ArrayList<Animal>();
            for (Animal animalCollectionAnimalToAttach : cage.getAnimalCollection()) {
                animalCollectionAnimalToAttach = em.getReference(animalCollectionAnimalToAttach.getClass(), animalCollectionAnimalToAttach.getIdAnimal());
                attachedAnimalCollection.add(animalCollectionAnimalToAttach);
            }
            cage.setAnimalCollection(attachedAnimalCollection);
            em.persist(cage);
            if (sector != null) {
                sector.getCageCollection().add(cage);
                sector = em.merge(sector);
            }
            for (Animal animalCollectionAnimal : cage.getAnimalCollection()) {
                Cage oldCageOfAnimalCollectionAnimal = animalCollectionAnimal.getCage();
                animalCollectionAnimal.setCage(cage);
                animalCollectionAnimal = em.merge(animalCollectionAnimal);
                if (oldCageOfAnimalCollectionAnimal != null) {
                    oldCageOfAnimalCollectionAnimal.getAnimalCollection().remove(animalCollectionAnimal);
                    oldCageOfAnimalCollectionAnimal = em.merge(oldCageOfAnimalCollectionAnimal);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCage(cage.getIdCage()) != null) {
                throw new PreexistingEntityException("Cage " + cage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cage cage) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cage persistentCage = em.find(Cage.class, cage.getIdCage());
            Sector sectorOld = persistentCage.getSector();
            Sector sectorNew = cage.getSector();
            Collection<Animal> animalCollectionOld = persistentCage.getAnimalCollection();
            Collection<Animal> animalCollectionNew = cage.getAnimalCollection();
            List<String> illegalOrphanMessages = null;
            for (Animal animalCollectionOldAnimal : animalCollectionOld) {
                if (!animalCollectionNew.contains(animalCollectionOldAnimal)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Animal " + animalCollectionOldAnimal + " since its cage field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (sectorNew != null) {
                sectorNew = em.getReference(sectorNew.getClass(), sectorNew.getIdSector());
                cage.setSector(sectorNew);
            }
            Collection<Animal> attachedAnimalCollectionNew = new ArrayList<Animal>();
            for (Animal animalCollectionNewAnimalToAttach : animalCollectionNew) {
                animalCollectionNewAnimalToAttach = em.getReference(animalCollectionNewAnimalToAttach.getClass(), animalCollectionNewAnimalToAttach.getIdAnimal());
                attachedAnimalCollectionNew.add(animalCollectionNewAnimalToAttach);
            }
            animalCollectionNew = attachedAnimalCollectionNew;
            cage.setAnimalCollection(animalCollectionNew);
            cage = em.merge(cage);
            if (sectorOld != null && !sectorOld.equals(sectorNew)) {
                sectorOld.getCageCollection().remove(cage);
                sectorOld = em.merge(sectorOld);
            }
            if (sectorNew != null && !sectorNew.equals(sectorOld)) {
                sectorNew.getCageCollection().add(cage);
                sectorNew = em.merge(sectorNew);
            }
            for (Animal animalCollectionNewAnimal : animalCollectionNew) {
                if (!animalCollectionOld.contains(animalCollectionNewAnimal)) {
                    Cage oldCageOfAnimalCollectionNewAnimal = animalCollectionNewAnimal.getCage();
                    animalCollectionNewAnimal.setCage(cage);
                    animalCollectionNewAnimal = em.merge(animalCollectionNewAnimal);
                    if (oldCageOfAnimalCollectionNewAnimal != null && !oldCageOfAnimalCollectionNewAnimal.equals(cage)) {
                        oldCageOfAnimalCollectionNewAnimal.getAnimalCollection().remove(animalCollectionNewAnimal);
                        oldCageOfAnimalCollectionNewAnimal = em.merge(oldCageOfAnimalCollectionNewAnimal);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cage.getIdCage();
                if (findCage(id) == null) {
                    throw new NonexistentEntityException("The cage with id " + id + " no longer exists.");
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
            Cage cage;
            try {
                cage = em.getReference(Cage.class, id);
                cage.getIdCage();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cage with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Animal> animalCollectionOrphanCheck = cage.getAnimalCollection();
            for (Animal animalCollectionOrphanCheckAnimal : animalCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cage (" + cage + ") cannot be destroyed since the Animal " + animalCollectionOrphanCheckAnimal + " in its animalCollection field has a non-nullable cage field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Sector sector = cage.getSector();
            if (sector != null) {
                sector.getCageCollection().remove(cage);
                sector = em.merge(sector);
            }
            em.remove(cage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cage> findCageEntities() {
        return findCageEntities(true, -1, -1);
    }

    public List<Cage> findCageEntities(int maxResults, int firstResult) {
        return findCageEntities(false, maxResults, firstResult);
    }

    private List<Cage> findCageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cage.class));
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

    public Cage findCage(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cage.class, id);
        } finally {
            em.close();
        }
    }

    public int getCageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cage> rt = cq.from(Cage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ArrayList<Animal> showAnimal(Cage c) {
        ArrayList<Animal> lst = new ArrayList();
        for (Animal a : c.getAnimalCollection()) {
            lst.add(a);
        }
        return lst;
    }

    public List<String> getCageType() {
        try {
            List<String> lst = getEntityManager().createNamedQuery("Cage.findByDistinctType", String.class).getResultList();
            return lst;
        } catch (Exception e) {
            System.err.println("blad rodzaje klatek");
            return null;
        }
    }

    public String addCage(Cage c) {
        try {
            int maxId = getEntityManager().createNamedQuery("Cage.findMaxId", Integer.class).getSingleResult();
            c.setIdCage(++maxId);
            c.setCondition("Pusta");
            create(c);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }
    
    public String editCage(Cage c) {
        try {
            edit(c);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

    public String deleteCage(Integer id) {
        try {
            destroy(id);
            System.out.println("OK usowanie");
            return "OK";
        } catch (Exception e) {
            return "BLAD";
        }
    }

    public String deleteCondition(Integer id) {
        try {
            destroy(id);
            System.out.println("OK usowanie");
            return "OK";
        } catch (Exception e) {
            return "BLAD";
        }
    }

}
