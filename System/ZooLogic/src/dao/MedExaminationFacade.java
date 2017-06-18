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
import entity.Animal;
import entity.Employee;
import entity.MedExamination;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class MedExaminationFacade implements Serializable {

    public MedExaminationFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MedExamination medExamination) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Animal animal = medExamination.getAnimal();
            if (animal != null) {
                animal = em.getReference(animal.getClass(), animal.getIdAnimal());
                medExamination.setAnimal(animal);
            }
            Employee veterinarian = medExamination.getVeterinarian();
            if (veterinarian != null) {
                veterinarian = em.getReference(veterinarian.getClass(), veterinarian.getIdEmployee());
                medExamination.setVeterinarian(veterinarian);
            }
            em.persist(medExamination);
            if (animal != null) {
                animal.getMedExaminationCollection().add(medExamination);
                animal = em.merge(animal);
            }
            if (veterinarian != null) {
                veterinarian.getMedExaminationCollection().add(medExamination);
                veterinarian = em.merge(veterinarian);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedExamination(medExamination.getIdMedExamination()) != null) {
                throw new PreexistingEntityException("MedExamination " + medExamination + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MedExamination medExamination) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MedExamination persistentMedExamination = em.find(MedExamination.class, medExamination.getIdMedExamination());
            Animal animalOld = persistentMedExamination.getAnimal();
            Animal animalNew = medExamination.getAnimal();
            Employee veterinarianOld = persistentMedExamination.getVeterinarian();
            Employee veterinarianNew = medExamination.getVeterinarian();
            if (animalNew != null) {
                animalNew = em.getReference(animalNew.getClass(), animalNew.getIdAnimal());
                medExamination.setAnimal(animalNew);
            }
            if (veterinarianNew != null) {
                veterinarianNew = em.getReference(veterinarianNew.getClass(), veterinarianNew.getIdEmployee());
                medExamination.setVeterinarian(veterinarianNew);
            }
            medExamination = em.merge(medExamination);
            if (animalOld != null && !animalOld.equals(animalNew)) {
                animalOld.getMedExaminationCollection().remove(medExamination);
                animalOld = em.merge(animalOld);
            }
            if (animalNew != null && !animalNew.equals(animalOld)) {
                animalNew.getMedExaminationCollection().add(medExamination);
                animalNew = em.merge(animalNew);
            }
            if (veterinarianOld != null && !veterinarianOld.equals(veterinarianNew)) {
                veterinarianOld.getMedExaminationCollection().remove(medExamination);
                veterinarianOld = em.merge(veterinarianOld);
            }
            if (veterinarianNew != null && !veterinarianNew.equals(veterinarianOld)) {
                veterinarianNew.getMedExaminationCollection().add(medExamination);
                veterinarianNew = em.merge(veterinarianNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = medExamination.getIdMedExamination();
                if (findMedExamination(id) == null) {
                    throw new NonexistentEntityException("The medExamination with id " + id + " no longer exists.");
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
            MedExamination medExamination;
            try {
                medExamination = em.getReference(MedExamination.class, id);
                medExamination.getIdMedExamination();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medExamination with id " + id + " no longer exists.", enfe);
            }
            Animal animal = medExamination.getAnimal();
            if (animal != null) {
                animal.getMedExaminationCollection().remove(medExamination);
                animal = em.merge(animal);
            }
            Employee veterinarian = medExamination.getVeterinarian();
            if (veterinarian != null) {
                veterinarian.getMedExaminationCollection().remove(medExamination);
                veterinarian = em.merge(veterinarian);
            }
            em.remove(medExamination);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MedExamination> findMedExaminationEntities() {
        return findMedExaminationEntities(true, -1, -1);
    }

    public List<MedExamination> findMedExaminationEntities(int maxResults, int firstResult) {
        return findMedExaminationEntities(false, maxResults, firstResult);
    }

    private List<MedExamination> findMedExaminationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MedExamination.class));
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

    public MedExamination findMedExamination(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MedExamination.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedExaminationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MedExamination> rt = cq.from(MedExamination.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public String deleteAnimalMed(Integer id) {
        try {
            destroy(id);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }
    
    public String addAnimalMed(MedExamination med) {
        try {
            int maxId = getEntityManager().createNamedQuery("MedExamination.findMaxId", Integer.class).getSingleResult();
            med.setIdMedExamination(++maxId);
            create(med);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }
    
    public String editAnimalMed(MedExamination med) {
        try {
            edit(med);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }
    
    public List<MedExamination> getHistoryMed(){
        List<MedExamination> lst = getEntityManager().createNamedQuery("MedExamination.findByCondition", MedExamination.class).setParameter("condition", "Wykonane").getResultList();
        return lst;
    }
    
    public List<MedExamination> getPlanMed(){
        List<MedExamination> lst = getEntityManager().createNamedQuery("MedExamination.findByCondition", MedExamination.class).setParameter("condition", "Zaplanowane").getResultList();
        return lst;
    }
    
    public int countTodayPlanMed(){
        Date d = Calendar.getInstance().getTime();
        return ((Number)getEntityManager().createNamedQuery("MedExamination.findCountTodayPlanMed", Integer.class).setParameter("medDate", d).getSingleResult()).intValue();
    }
    
}
