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
import entity.AnimalFeed;
import entity.Feed;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Xaoo
 */
public class AnimalFeedFacade implements Serializable {

    public AnimalFeedFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AnimalFeed animalFeed) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Animal animal = animalFeed.getAnimal();
            if (animal != null) {
                animal = em.getReference(animal.getClass(), animal.getIdAnimal());
                animalFeed.setAnimal(animal);
            }
            Feed feed = animalFeed.getFeed();
            if (feed != null) {
                feed = em.getReference(feed.getClass(), feed.getIdFeed());
                animalFeed.setFeed(feed);
            }
            em.persist(animalFeed);
            if (animal != null) {
                animal.getAnimalFeedCollection().add(animalFeed);
                animal = em.merge(animal);
            }
            if (feed != null) {
                feed.getAnimalFeedCollection().add(animalFeed);
                feed = em.merge(feed);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnimalFeed(animalFeed.getIdAnimalFeed()) != null) {
                throw new PreexistingEntityException("AnimalFeed " + animalFeed + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AnimalFeed animalFeed) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AnimalFeed persistentAnimalFeed = em.find(AnimalFeed.class, animalFeed.getIdAnimalFeed());
            Animal animalOld = persistentAnimalFeed.getAnimal();
            Animal animalNew = animalFeed.getAnimal();
            Feed feedOld = persistentAnimalFeed.getFeed();
            Feed feedNew = animalFeed.getFeed();
            if (animalNew != null) {
                animalNew = em.getReference(animalNew.getClass(), animalNew.getIdAnimal());
                animalFeed.setAnimal(animalNew);
            }
            if (feedNew != null) {
                feedNew = em.getReference(feedNew.getClass(), feedNew.getIdFeed());
                animalFeed.setFeed(feedNew);
            }
            animalFeed = em.merge(animalFeed);
            if (animalOld != null && !animalOld.equals(animalNew)) {
                animalOld.getAnimalFeedCollection().remove(animalFeed);
                animalOld = em.merge(animalOld);
            }
            if (animalNew != null && !animalNew.equals(animalOld)) {
                animalNew.getAnimalFeedCollection().add(animalFeed);
                animalNew = em.merge(animalNew);
            }
            if (feedOld != null && !feedOld.equals(feedNew)) {
                feedOld.getAnimalFeedCollection().remove(animalFeed);
                feedOld = em.merge(feedOld);
            }
            if (feedNew != null && !feedNew.equals(feedOld)) {
                feedNew.getAnimalFeedCollection().add(animalFeed);
                feedNew = em.merge(feedNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = animalFeed.getIdAnimalFeed();
                if (findAnimalFeed(id) == null) {
                    throw new NonexistentEntityException("The animalFeed with id " + id + " no longer exists.");
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
            AnimalFeed animalFeed;
            try {
                animalFeed = em.getReference(AnimalFeed.class, id);
                animalFeed.getIdAnimalFeed();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The animalFeed with id " + id + " no longer exists.", enfe);
            }
            Animal animal = animalFeed.getAnimal();
            if (animal != null) {
                animal.getAnimalFeedCollection().remove(animalFeed);
                animal = em.merge(animal);
            }
            Feed feed = animalFeed.getFeed();
            if (feed != null) {
                feed.getAnimalFeedCollection().remove(animalFeed);
                feed = em.merge(feed);
            }
            em.remove(animalFeed);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AnimalFeed> findAnimalFeedEntities() {
        return findAnimalFeedEntities(true, -1, -1);
    }

    public List<AnimalFeed> findAnimalFeedEntities(int maxResults, int firstResult) {
        return findAnimalFeedEntities(false, maxResults, firstResult);
    }

    private List<AnimalFeed> findAnimalFeedEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AnimalFeed.class));
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

    public AnimalFeed findAnimalFeed(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AnimalFeed.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnimalFeedCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AnimalFeed> rt = cq.from(AnimalFeed.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public String addAnimalFeed(AnimalFeed af) {
        try {
            int maxId = getEntityManager().createNamedQuery("AnimalFeed.findMaxId", Integer.class).getSingleResult();
            af.setIdAnimalFeed(++maxId);
            create(af);
            return ("OK");
        } catch (Exception e) {
            return ("ERR");
        }
    }

    public String deleteAnimalFeed(Integer id) {
        try {
            destroy(id);
            System.out.println("OK usowanie");
            return "OK";
        } catch (Exception e) {
            return "BLAD";
        }
    }
    
    public List<AnimalFeed> getAnimalFeedType(String feedTime) {
        try {
            List<AnimalFeed> lst = getEntityManager().createNamedQuery("AnimalFeed.findByFeedtime", AnimalFeed.class).setParameter("feedTime", feedTime).getResultList();
            return lst;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<AnimalFeed> getAnimalFeedFreq(String freq) {
        try {
            List<AnimalFeed> lst = getEntityManager().createNamedQuery("AnimalFeed.findByFrequency", AnimalFeed.class).setParameter("frequency", freq).getResultList();
            return lst;
        } catch (Exception e) {
            return null;
        }
    }

}
