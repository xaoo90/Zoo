/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import entity.Animal;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Cage;
import entity.AnimalFeed;
import java.util.ArrayList;
import java.util.Collection;
import entity.MedExamination;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class AnimalFacade implements Serializable {

    public AnimalFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Animal animal) throws PreexistingEntityException, Exception {
        if (animal.getAnimalFeedCollection() == null) {
            animal.setAnimalFeedCollection(new ArrayList<AnimalFeed>());
        }
        if (animal.getMedExaminationCollection() == null) {
            animal.setMedExaminationCollection(new ArrayList<MedExamination>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cage cage = animal.getCage();
            if (cage != null) {
                cage = em.getReference(cage.getClass(), cage.getIdCage());
                animal.setCage(cage);
            }
            Collection<AnimalFeed> attachedAnimalFeedCollection = new ArrayList<AnimalFeed>();
            for (AnimalFeed animalFeedCollectionAnimalFeedToAttach : animal.getAnimalFeedCollection()) {
                animalFeedCollectionAnimalFeedToAttach = em.getReference(animalFeedCollectionAnimalFeedToAttach.getClass(), animalFeedCollectionAnimalFeedToAttach.getIdAnimalFeed());
                attachedAnimalFeedCollection.add(animalFeedCollectionAnimalFeedToAttach);
            }
            animal.setAnimalFeedCollection(attachedAnimalFeedCollection);
            Collection<MedExamination> attachedMedExaminationCollection = new ArrayList<MedExamination>();
            for (MedExamination medExaminationCollectionMedExaminationToAttach : animal.getMedExaminationCollection()) {
                medExaminationCollectionMedExaminationToAttach = em.getReference(medExaminationCollectionMedExaminationToAttach.getClass(), medExaminationCollectionMedExaminationToAttach.getIdMedExamination());
                attachedMedExaminationCollection.add(medExaminationCollectionMedExaminationToAttach);
            }
            animal.setMedExaminationCollection(attachedMedExaminationCollection);
            em.persist(animal);
            if (cage != null) {
                cage.getAnimalCollection().add(animal);
                cage = em.merge(cage);
            }
            for (AnimalFeed animalFeedCollectionAnimalFeed : animal.getAnimalFeedCollection()) {
                Animal oldAnimalOfAnimalFeedCollectionAnimalFeed = animalFeedCollectionAnimalFeed.getAnimal();
                animalFeedCollectionAnimalFeed.setAnimal(animal);
                animalFeedCollectionAnimalFeed = em.merge(animalFeedCollectionAnimalFeed);
                if (oldAnimalOfAnimalFeedCollectionAnimalFeed != null) {
                    oldAnimalOfAnimalFeedCollectionAnimalFeed.getAnimalFeedCollection().remove(animalFeedCollectionAnimalFeed);
                    oldAnimalOfAnimalFeedCollectionAnimalFeed = em.merge(oldAnimalOfAnimalFeedCollectionAnimalFeed);
                }
            }
            for (MedExamination medExaminationCollectionMedExamination : animal.getMedExaminationCollection()) {
                Animal oldAnimalOfMedExaminationCollectionMedExamination = medExaminationCollectionMedExamination.getAnimal();
                medExaminationCollectionMedExamination.setAnimal(animal);
                medExaminationCollectionMedExamination = em.merge(medExaminationCollectionMedExamination);
                if (oldAnimalOfMedExaminationCollectionMedExamination != null) {
                    oldAnimalOfMedExaminationCollectionMedExamination.getMedExaminationCollection().remove(medExaminationCollectionMedExamination);
                    oldAnimalOfMedExaminationCollectionMedExamination = em.merge(oldAnimalOfMedExaminationCollectionMedExamination);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnimal(animal.getIdAnimal()) != null) {
                throw new PreexistingEntityException("Animal " + animal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Animal animal) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Animal persistentAnimal = em.find(Animal.class, animal.getIdAnimal());
            Cage cageOld = persistentAnimal.getCage();
            Cage cageNew = animal.getCage();
            Collection<AnimalFeed> animalFeedCollectionOld = persistentAnimal.getAnimalFeedCollection();
            Collection<AnimalFeed> animalFeedCollectionNew = animal.getAnimalFeedCollection();
            Collection<MedExamination> medExaminationCollectionOld = persistentAnimal.getMedExaminationCollection();
            Collection<MedExamination> medExaminationCollectionNew = animal.getMedExaminationCollection();
            List<String> illegalOrphanMessages = null;
            for (AnimalFeed animalFeedCollectionOldAnimalFeed : animalFeedCollectionOld) {
                if (!animalFeedCollectionNew.contains(animalFeedCollectionOldAnimalFeed)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AnimalFeed " + animalFeedCollectionOldAnimalFeed + " since its animal field is not nullable.");
                }
            }
            for (MedExamination medExaminationCollectionOldMedExamination : medExaminationCollectionOld) {
                if (!medExaminationCollectionNew.contains(medExaminationCollectionOldMedExamination)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MedExamination " + medExaminationCollectionOldMedExamination + " since its animal field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cageNew != null) {
                cageNew = em.getReference(cageNew.getClass(), cageNew.getIdCage());
                animal.setCage(cageNew);
            }
            Collection<AnimalFeed> attachedAnimalFeedCollectionNew = new ArrayList<AnimalFeed>();
            for (AnimalFeed animalFeedCollectionNewAnimalFeedToAttach : animalFeedCollectionNew) {
                animalFeedCollectionNewAnimalFeedToAttach = em.getReference(animalFeedCollectionNewAnimalFeedToAttach.getClass(), animalFeedCollectionNewAnimalFeedToAttach.getIdAnimalFeed());
                attachedAnimalFeedCollectionNew.add(animalFeedCollectionNewAnimalFeedToAttach);
            }
            animalFeedCollectionNew = attachedAnimalFeedCollectionNew;
            animal.setAnimalFeedCollection(animalFeedCollectionNew);
            Collection<MedExamination> attachedMedExaminationCollectionNew = new ArrayList<MedExamination>();
            for (MedExamination medExaminationCollectionNewMedExaminationToAttach : medExaminationCollectionNew) {
                medExaminationCollectionNewMedExaminationToAttach = em.getReference(medExaminationCollectionNewMedExaminationToAttach.getClass(), medExaminationCollectionNewMedExaminationToAttach.getIdMedExamination());
                attachedMedExaminationCollectionNew.add(medExaminationCollectionNewMedExaminationToAttach);
            }
            medExaminationCollectionNew = attachedMedExaminationCollectionNew;
            animal.setMedExaminationCollection(medExaminationCollectionNew);
            animal = em.merge(animal);
            if (cageOld != null && !cageOld.equals(cageNew)) {
                cageOld.getAnimalCollection().remove(animal);
                cageOld = em.merge(cageOld);
            }
            if (cageNew != null && !cageNew.equals(cageOld)) {
                cageNew.getAnimalCollection().add(animal);
                cageNew = em.merge(cageNew);
            }
            for (AnimalFeed animalFeedCollectionNewAnimalFeed : animalFeedCollectionNew) {
                if (!animalFeedCollectionOld.contains(animalFeedCollectionNewAnimalFeed)) {
                    Animal oldAnimalOfAnimalFeedCollectionNewAnimalFeed = animalFeedCollectionNewAnimalFeed.getAnimal();
                    animalFeedCollectionNewAnimalFeed.setAnimal(animal);
                    animalFeedCollectionNewAnimalFeed = em.merge(animalFeedCollectionNewAnimalFeed);
                    if (oldAnimalOfAnimalFeedCollectionNewAnimalFeed != null && !oldAnimalOfAnimalFeedCollectionNewAnimalFeed.equals(animal)) {
                        oldAnimalOfAnimalFeedCollectionNewAnimalFeed.getAnimalFeedCollection().remove(animalFeedCollectionNewAnimalFeed);
                        oldAnimalOfAnimalFeedCollectionNewAnimalFeed = em.merge(oldAnimalOfAnimalFeedCollectionNewAnimalFeed);
                    }
                }
            }
            for (MedExamination medExaminationCollectionNewMedExamination : medExaminationCollectionNew) {
                if (!medExaminationCollectionOld.contains(medExaminationCollectionNewMedExamination)) {
                    Animal oldAnimalOfMedExaminationCollectionNewMedExamination = medExaminationCollectionNewMedExamination.getAnimal();
                    medExaminationCollectionNewMedExamination.setAnimal(animal);
                    medExaminationCollectionNewMedExamination = em.merge(medExaminationCollectionNewMedExamination);
                    if (oldAnimalOfMedExaminationCollectionNewMedExamination != null && !oldAnimalOfMedExaminationCollectionNewMedExamination.equals(animal)) {
                        oldAnimalOfMedExaminationCollectionNewMedExamination.getMedExaminationCollection().remove(medExaminationCollectionNewMedExamination);
                        oldAnimalOfMedExaminationCollectionNewMedExamination = em.merge(oldAnimalOfMedExaminationCollectionNewMedExamination);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = animal.getIdAnimal();
                if (findAnimal(id) == null) {
                    throw new NonexistentEntityException("The animal with id " + id + " no longer exists.");
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
            Animal animal;
            try {
                animal = em.getReference(Animal.class, id);
                animal.getIdAnimal();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The animal with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<AnimalFeed> animalFeedCollectionOrphanCheck = animal.getAnimalFeedCollection();
            for (AnimalFeed animalFeedCollectionOrphanCheckAnimalFeed : animalFeedCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Animal (" + animal + ") cannot be destroyed since the AnimalFeed " + animalFeedCollectionOrphanCheckAnimalFeed + " in its animalFeedCollection field has a non-nullable animal field.");
            }
            Collection<MedExamination> medExaminationCollectionOrphanCheck = animal.getMedExaminationCollection();
            for (MedExamination medExaminationCollectionOrphanCheckMedExamination : medExaminationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Animal (" + animal + ") cannot be destroyed since the MedExamination " + medExaminationCollectionOrphanCheckMedExamination + " in its medExaminationCollection field has a non-nullable animal field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cage cage = animal.getCage();
            if (cage != null) {
                cage.getAnimalCollection().remove(animal);
                cage = em.merge(cage);
            }
            em.remove(animal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Animal> findAnimalEntities() {
        return findAnimalEntities(true, -1, -1);
    }

    public List<Animal> findAnimalEntities(int maxResults, int firstResult) {
        return findAnimalEntities(false, maxResults, firstResult);
    }

    private List<Animal> findAnimalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Animal.class));
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

    public Animal findAnimal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Animal.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnimalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Animal> rt = cq.from(Animal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public String addAnimal(Animal a) {
        try {
            int maxId = getEntityManager().createNamedQuery("Animal.findMaxId", Integer.class).getSingleResult();
            a.setIdAnimal(++maxId);
            create(a);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

    public String editAnimal(Animal a) {
        try {
            edit(a);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

    public String deleteAnimal(Integer id) {
        try {
            destroy(id);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }

    public ArrayList<AnimalFeed> showAnimalFeed(Animal a) {
        ArrayList<AnimalFeed> lst = new ArrayList();
        for (AnimalFeed af : a.getAnimalFeedCollection()) {
            lst.add(af);
        }
        return lst;
    }

    public ArrayList<MedExamination> showAnimalMed(Animal a) {
        ArrayList<MedExamination> lst = new ArrayList();
        for (MedExamination m : a.getMedExaminationCollection()) {
            lst.add(m);
        }
        return lst;
    }

}
