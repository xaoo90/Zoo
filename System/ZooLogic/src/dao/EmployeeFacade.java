/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import entity.Employee;
import utilLogic.UtilLogic;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.OrderFeed;
import java.util.ArrayList;
import java.util.Collection;
import entity.EmployeeSchedule;
import entity.Sector;
import entity.MedExamination;
import entity.Holiday;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author Xaoo
 */
public class EmployeeFacade implements Serializable {

    public EmployeeFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) throws PreexistingEntityException, Exception {
        if (employee.getOrderFeedCollection() == null) {
            employee.setOrderFeedCollection(new ArrayList<OrderFeed>());
        }
        if (employee.getEmployeeScheduleCollection() == null) {
            employee.setEmployeeScheduleCollection(new ArrayList<EmployeeSchedule>());
        }
        if (employee.getSectorCollection() == null) {
            employee.setSectorCollection(new ArrayList<Sector>());
        }
        if (employee.getMedExaminationCollection() == null) {
            employee.setMedExaminationCollection(new ArrayList<MedExamination>());
        }
        if (employee.getHolidayCollection() == null) {
            employee.setHolidayCollection(new ArrayList<Holiday>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<OrderFeed> attachedOrderFeedCollection = new ArrayList<OrderFeed>();
            for (OrderFeed orderFeedCollectionOrderFeedToAttach : employee.getOrderFeedCollection()) {
                orderFeedCollectionOrderFeedToAttach = em.getReference(orderFeedCollectionOrderFeedToAttach.getClass(), orderFeedCollectionOrderFeedToAttach.getIdOrder());
                attachedOrderFeedCollection.add(orderFeedCollectionOrderFeedToAttach);
            }
            employee.setOrderFeedCollection(attachedOrderFeedCollection);
            Collection<EmployeeSchedule> attachedEmployeeScheduleCollection = new ArrayList<EmployeeSchedule>();
            for (EmployeeSchedule employeeScheduleCollectionEmployeeScheduleToAttach : employee.getEmployeeScheduleCollection()) {
                employeeScheduleCollectionEmployeeScheduleToAttach = em.getReference(employeeScheduleCollectionEmployeeScheduleToAttach.getClass(), employeeScheduleCollectionEmployeeScheduleToAttach.getIdEmployeeSchedule());
                attachedEmployeeScheduleCollection.add(employeeScheduleCollectionEmployeeScheduleToAttach);
            }
            employee.setEmployeeScheduleCollection(attachedEmployeeScheduleCollection);
            Collection<Sector> attachedSectorCollection = new ArrayList<Sector>();
            for (Sector sectorCollectionSectorToAttach : employee.getSectorCollection()) {
                sectorCollectionSectorToAttach = em.getReference(sectorCollectionSectorToAttach.getClass(), sectorCollectionSectorToAttach.getIdSector());
                attachedSectorCollection.add(sectorCollectionSectorToAttach);
            }
            employee.setSectorCollection(attachedSectorCollection);
            Collection<MedExamination> attachedMedExaminationCollection = new ArrayList<MedExamination>();
            for (MedExamination medExaminationCollectionMedExaminationToAttach : employee.getMedExaminationCollection()) {
                medExaminationCollectionMedExaminationToAttach = em.getReference(medExaminationCollectionMedExaminationToAttach.getClass(), medExaminationCollectionMedExaminationToAttach.getIdMedExamination());
                attachedMedExaminationCollection.add(medExaminationCollectionMedExaminationToAttach);
            }
            employee.setMedExaminationCollection(attachedMedExaminationCollection);
            Collection<Holiday> attachedHolidayCollection = new ArrayList<Holiday>();
            for (Holiday holidayCollectionHolidayToAttach : employee.getHolidayCollection()) {
                holidayCollectionHolidayToAttach = em.getReference(holidayCollectionHolidayToAttach.getClass(), holidayCollectionHolidayToAttach.getIdHoliday());
                attachedHolidayCollection.add(holidayCollectionHolidayToAttach);
            }
            employee.setHolidayCollection(attachedHolidayCollection);
            em.persist(employee);
            for (OrderFeed orderFeedCollectionOrderFeed : employee.getOrderFeedCollection()) {
                Employee oldWarehousemanOfOrderFeedCollectionOrderFeed = orderFeedCollectionOrderFeed.getWarehouseMen();
                orderFeedCollectionOrderFeed.setWarehouseMen(employee);
                orderFeedCollectionOrderFeed = em.merge(orderFeedCollectionOrderFeed);
                if (oldWarehousemanOfOrderFeedCollectionOrderFeed != null) {
                    oldWarehousemanOfOrderFeedCollectionOrderFeed.getOrderFeedCollection().remove(orderFeedCollectionOrderFeed);
                    oldWarehousemanOfOrderFeedCollectionOrderFeed = em.merge(oldWarehousemanOfOrderFeedCollectionOrderFeed);
                }
            }
            for (EmployeeSchedule employeeScheduleCollectionEmployeeSchedule : employee.getEmployeeScheduleCollection()) {
                Employee oldEmployeeOfEmployeeScheduleCollectionEmployeeSchedule = employeeScheduleCollectionEmployeeSchedule.getEmployee();
                employeeScheduleCollectionEmployeeSchedule.setEmployee(employee);
                employeeScheduleCollectionEmployeeSchedule = em.merge(employeeScheduleCollectionEmployeeSchedule);
                if (oldEmployeeOfEmployeeScheduleCollectionEmployeeSchedule != null) {
                    oldEmployeeOfEmployeeScheduleCollectionEmployeeSchedule.getEmployeeScheduleCollection().remove(employeeScheduleCollectionEmployeeSchedule);
                    oldEmployeeOfEmployeeScheduleCollectionEmployeeSchedule = em.merge(oldEmployeeOfEmployeeScheduleCollectionEmployeeSchedule);
                }
            }
            for (Sector sectorCollectionSector : employee.getSectorCollection()) {
                Employee oldManagerOfSectorCollectionSector = sectorCollectionSector.getManager();
                sectorCollectionSector.setManager(employee);
                sectorCollectionSector = em.merge(sectorCollectionSector);
                if (oldManagerOfSectorCollectionSector != null) {
                    oldManagerOfSectorCollectionSector.getSectorCollection().remove(sectorCollectionSector);
                    oldManagerOfSectorCollectionSector = em.merge(oldManagerOfSectorCollectionSector);
                }
            }
            for (MedExamination medExaminationCollectionMedExamination : employee.getMedExaminationCollection()) {
                Employee oldVeterinarianOfMedExaminationCollectionMedExamination = medExaminationCollectionMedExamination.getVeterinarian();
                medExaminationCollectionMedExamination.setVeterinarian(employee);
                medExaminationCollectionMedExamination = em.merge(medExaminationCollectionMedExamination);
                if (oldVeterinarianOfMedExaminationCollectionMedExamination != null) {
                    oldVeterinarianOfMedExaminationCollectionMedExamination.getMedExaminationCollection().remove(medExaminationCollectionMedExamination);
                    oldVeterinarianOfMedExaminationCollectionMedExamination = em.merge(oldVeterinarianOfMedExaminationCollectionMedExamination);
                }
            }
            for (Holiday holidayCollectionHoliday : employee.getHolidayCollection()) {
                Employee oldEmployeeOfHolidayCollectionHoliday = holidayCollectionHoliday.getEmployee();
                holidayCollectionHoliday.setEmployee(employee);
                holidayCollectionHoliday = em.merge(holidayCollectionHoliday);
                if (oldEmployeeOfHolidayCollectionHoliday != null) {
                    oldEmployeeOfHolidayCollectionHoliday.getHolidayCollection().remove(holidayCollectionHoliday);
                    oldEmployeeOfHolidayCollectionHoliday = em.merge(oldEmployeeOfHolidayCollectionHoliday);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmployee(employee.getIdEmployee()) != null) {
                throw new PreexistingEntityException("Employee " + employee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Employee employee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee persistentEmployee = em.find(Employee.class, employee.getIdEmployee());
            Collection<OrderFeed> orderFeedCollectionOld = persistentEmployee.getOrderFeedCollection();
            Collection<OrderFeed> orderFeedCollectionNew = employee.getOrderFeedCollection();
            Collection<EmployeeSchedule> employeeScheduleCollectionOld = persistentEmployee.getEmployeeScheduleCollection();
            Collection<EmployeeSchedule> employeeScheduleCollectionNew = employee.getEmployeeScheduleCollection();
            Collection<Sector> sectorCollectionOld = persistentEmployee.getSectorCollection();
            Collection<Sector> sectorCollectionNew = employee.getSectorCollection();
            Collection<MedExamination> medExaminationCollectionOld = persistentEmployee.getMedExaminationCollection();
            Collection<MedExamination> medExaminationCollectionNew = employee.getMedExaminationCollection();
            Collection<Holiday> holidayCollectionOld = persistentEmployee.getHolidayCollection();
            Collection<Holiday> holidayCollectionNew = employee.getHolidayCollection();
            List<String> illegalOrphanMessages = null;
            for (OrderFeed orderFeedCollectionOldOrderFeed : orderFeedCollectionOld) {
                if (!orderFeedCollectionNew.contains(orderFeedCollectionOldOrderFeed)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain OrderFeed " + orderFeedCollectionOldOrderFeed + " since its warehouseman field is not nullable.");
                }
            }
            for (EmployeeSchedule employeeScheduleCollectionOldEmployeeSchedule : employeeScheduleCollectionOld) {
                if (!employeeScheduleCollectionNew.contains(employeeScheduleCollectionOldEmployeeSchedule)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EmployeeSchedule " + employeeScheduleCollectionOldEmployeeSchedule + " since its employee field is not nullable.");
                }
            }
            for (MedExamination medExaminationCollectionOldMedExamination : medExaminationCollectionOld) {
                if (!medExaminationCollectionNew.contains(medExaminationCollectionOldMedExamination)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MedExamination " + medExaminationCollectionOldMedExamination + " since its veterinarian field is not nullable.");
                }
            }
            for (Holiday holidayCollectionOldHoliday : holidayCollectionOld) {
                if (!holidayCollectionNew.contains(holidayCollectionOldHoliday)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Holiday " + holidayCollectionOldHoliday + " since its employee field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<OrderFeed> attachedOrderFeedCollectionNew = new ArrayList<OrderFeed>();
            for (OrderFeed orderFeedCollectionNewOrderFeedToAttach : orderFeedCollectionNew) {
                orderFeedCollectionNewOrderFeedToAttach = em.getReference(orderFeedCollectionNewOrderFeedToAttach.getClass(), orderFeedCollectionNewOrderFeedToAttach.getIdOrder());
                attachedOrderFeedCollectionNew.add(orderFeedCollectionNewOrderFeedToAttach);
            }
            orderFeedCollectionNew = attachedOrderFeedCollectionNew;
            employee.setOrderFeedCollection(orderFeedCollectionNew);
            Collection<EmployeeSchedule> attachedEmployeeScheduleCollectionNew = new ArrayList<EmployeeSchedule>();
            for (EmployeeSchedule employeeScheduleCollectionNewEmployeeScheduleToAttach : employeeScheduleCollectionNew) {
                employeeScheduleCollectionNewEmployeeScheduleToAttach = em.getReference(employeeScheduleCollectionNewEmployeeScheduleToAttach.getClass(), employeeScheduleCollectionNewEmployeeScheduleToAttach.getIdEmployeeSchedule());
                attachedEmployeeScheduleCollectionNew.add(employeeScheduleCollectionNewEmployeeScheduleToAttach);
            }
            employeeScheduleCollectionNew = attachedEmployeeScheduleCollectionNew;
            employee.setEmployeeScheduleCollection(employeeScheduleCollectionNew);
            Collection<Sector> attachedSectorCollectionNew = new ArrayList<Sector>();
            for (Sector sectorCollectionNewSectorToAttach : sectorCollectionNew) {
                sectorCollectionNewSectorToAttach = em.getReference(sectorCollectionNewSectorToAttach.getClass(), sectorCollectionNewSectorToAttach.getIdSector());
                attachedSectorCollectionNew.add(sectorCollectionNewSectorToAttach);
            }
            sectorCollectionNew = attachedSectorCollectionNew;
            employee.setSectorCollection(sectorCollectionNew);
            Collection<MedExamination> attachedMedExaminationCollectionNew = new ArrayList<MedExamination>();
            for (MedExamination medExaminationCollectionNewMedExaminationToAttach : medExaminationCollectionNew) {
                medExaminationCollectionNewMedExaminationToAttach = em.getReference(medExaminationCollectionNewMedExaminationToAttach.getClass(), medExaminationCollectionNewMedExaminationToAttach.getIdMedExamination());
                attachedMedExaminationCollectionNew.add(medExaminationCollectionNewMedExaminationToAttach);
            }
            medExaminationCollectionNew = attachedMedExaminationCollectionNew;
            employee.setMedExaminationCollection(medExaminationCollectionNew);
            Collection<Holiday> attachedHolidayCollectionNew = new ArrayList<Holiday>();
            for (Holiday holidayCollectionNewHolidayToAttach : holidayCollectionNew) {
                holidayCollectionNewHolidayToAttach = em.getReference(holidayCollectionNewHolidayToAttach.getClass(), holidayCollectionNewHolidayToAttach.getIdHoliday());
                attachedHolidayCollectionNew.add(holidayCollectionNewHolidayToAttach);
            }
            holidayCollectionNew = attachedHolidayCollectionNew;
            employee.setHolidayCollection(holidayCollectionNew);
            employee = em.merge(employee);
            for (OrderFeed orderFeedCollectionNewOrderFeed : orderFeedCollectionNew) {
                if (!orderFeedCollectionOld.contains(orderFeedCollectionNewOrderFeed)) {
                    Employee oldWarehousemanOfOrderFeedCollectionNewOrderFeed = orderFeedCollectionNewOrderFeed.getWarehouseMen();
                    orderFeedCollectionNewOrderFeed.setWarehouseMen(employee);
                    orderFeedCollectionNewOrderFeed = em.merge(orderFeedCollectionNewOrderFeed);
                    if (oldWarehousemanOfOrderFeedCollectionNewOrderFeed != null && !oldWarehousemanOfOrderFeedCollectionNewOrderFeed.equals(employee)) {
                        oldWarehousemanOfOrderFeedCollectionNewOrderFeed.getOrderFeedCollection().remove(orderFeedCollectionNewOrderFeed);
                        oldWarehousemanOfOrderFeedCollectionNewOrderFeed = em.merge(oldWarehousemanOfOrderFeedCollectionNewOrderFeed);
                    }
                }
            }
            for (EmployeeSchedule employeeScheduleCollectionNewEmployeeSchedule : employeeScheduleCollectionNew) {
                if (!employeeScheduleCollectionOld.contains(employeeScheduleCollectionNewEmployeeSchedule)) {
                    Employee oldEmployeeOfEmployeeScheduleCollectionNewEmployeeSchedule = employeeScheduleCollectionNewEmployeeSchedule.getEmployee();
                    employeeScheduleCollectionNewEmployeeSchedule.setEmployee(employee);
                    employeeScheduleCollectionNewEmployeeSchedule = em.merge(employeeScheduleCollectionNewEmployeeSchedule);
                    if (oldEmployeeOfEmployeeScheduleCollectionNewEmployeeSchedule != null && !oldEmployeeOfEmployeeScheduleCollectionNewEmployeeSchedule.equals(employee)) {
                        oldEmployeeOfEmployeeScheduleCollectionNewEmployeeSchedule.getEmployeeScheduleCollection().remove(employeeScheduleCollectionNewEmployeeSchedule);
                        oldEmployeeOfEmployeeScheduleCollectionNewEmployeeSchedule = em.merge(oldEmployeeOfEmployeeScheduleCollectionNewEmployeeSchedule);
                    }
                }
            }
            for (Sector sectorCollectionOldSector : sectorCollectionOld) {
                if (!sectorCollectionNew.contains(sectorCollectionOldSector)) {
                    sectorCollectionOldSector.setManager(null);
                    sectorCollectionOldSector = em.merge(sectorCollectionOldSector);
                }
            }
            for (Sector sectorCollectionNewSector : sectorCollectionNew) {
                if (!sectorCollectionOld.contains(sectorCollectionNewSector)) {
                    Employee oldManagerOfSectorCollectionNewSector = sectorCollectionNewSector.getManager();
                    sectorCollectionNewSector.setManager(employee);
                    sectorCollectionNewSector = em.merge(sectorCollectionNewSector);
                    if (oldManagerOfSectorCollectionNewSector != null && !oldManagerOfSectorCollectionNewSector.equals(employee)) {
                        oldManagerOfSectorCollectionNewSector.getSectorCollection().remove(sectorCollectionNewSector);
                        oldManagerOfSectorCollectionNewSector = em.merge(oldManagerOfSectorCollectionNewSector);
                    }
                }
            }
            for (MedExamination medExaminationCollectionNewMedExamination : medExaminationCollectionNew) {
                if (!medExaminationCollectionOld.contains(medExaminationCollectionNewMedExamination)) {
                    Employee oldVeterinarianOfMedExaminationCollectionNewMedExamination = medExaminationCollectionNewMedExamination.getVeterinarian();
                    medExaminationCollectionNewMedExamination.setVeterinarian(employee);
                    medExaminationCollectionNewMedExamination = em.merge(medExaminationCollectionNewMedExamination);
                    if (oldVeterinarianOfMedExaminationCollectionNewMedExamination != null && !oldVeterinarianOfMedExaminationCollectionNewMedExamination.equals(employee)) {
                        oldVeterinarianOfMedExaminationCollectionNewMedExamination.getMedExaminationCollection().remove(medExaminationCollectionNewMedExamination);
                        oldVeterinarianOfMedExaminationCollectionNewMedExamination = em.merge(oldVeterinarianOfMedExaminationCollectionNewMedExamination);
                    }
                }
            }
            for (Holiday holidayCollectionNewHoliday : holidayCollectionNew) {
                if (!holidayCollectionOld.contains(holidayCollectionNewHoliday)) {
                    Employee oldEmployeeOfHolidayCollectionNewHoliday = holidayCollectionNewHoliday.getEmployee();
                    holidayCollectionNewHoliday.setEmployee(employee);
                    holidayCollectionNewHoliday = em.merge(holidayCollectionNewHoliday);
                    if (oldEmployeeOfHolidayCollectionNewHoliday != null && !oldEmployeeOfHolidayCollectionNewHoliday.equals(employee)) {
                        oldEmployeeOfHolidayCollectionNewHoliday.getHolidayCollection().remove(holidayCollectionNewHoliday);
                        oldEmployeeOfHolidayCollectionNewHoliday = em.merge(oldEmployeeOfHolidayCollectionNewHoliday);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = employee.getIdEmployee();
                if (findEmployee(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
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
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getIdEmployee();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<OrderFeed> orderFeedCollectionOrphanCheck = employee.getOrderFeedCollection();
            for (OrderFeed orderFeedCollectionOrphanCheckOrderFeed : orderFeedCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the OrderFeed " + orderFeedCollectionOrphanCheckOrderFeed + " in its orderFeedCollection field has a non-nullable warehouseman field.");
            }
            Collection<EmployeeSchedule> employeeScheduleCollectionOrphanCheck = employee.getEmployeeScheduleCollection();
            for (EmployeeSchedule employeeScheduleCollectionOrphanCheckEmployeeSchedule : employeeScheduleCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the EmployeeSchedule " + employeeScheduleCollectionOrphanCheckEmployeeSchedule + " in its employeeScheduleCollection field has a non-nullable employee field.");
            }
            Collection<MedExamination> medExaminationCollectionOrphanCheck = employee.getMedExaminationCollection();
            for (MedExamination medExaminationCollectionOrphanCheckMedExamination : medExaminationCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the MedExamination " + medExaminationCollectionOrphanCheckMedExamination + " in its medExaminationCollection field has a non-nullable veterinarian field.");
            }
            Collection<Holiday> holidayCollectionOrphanCheck = employee.getHolidayCollection();
            for (Holiday holidayCollectionOrphanCheckHoliday : holidayCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Employee (" + employee + ") cannot be destroyed since the Holiday " + holidayCollectionOrphanCheckHoliday + " in its holidayCollection field has a non-nullable employee field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Sector> sectorCollection = employee.getSectorCollection();
            for (Sector sectorCollectionSector : sectorCollection) {
                sectorCollectionSector.setManager(null);
                sectorCollectionSector = em.merge(sectorCollectionSector);
            }
            em.remove(employee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
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

    public Employee findEmployee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public String registerEmployee(Employee employee) {
        try {
            getEntityManager().createNamedQuery("Employee.findByLogin", Employee.class).setParameter("login", employee.getLogin()).getSingleResult();
        } catch (NoResultException e) {
            Random rand = new Random();
            String pass = "Start" + String.valueOf(rand.nextInt(89999) + 10000);
            int maxId = getEntityManager().createNamedQuery("Employee.findMaxId", Integer.class).getSingleResult();
            employee.setIdEmployee(++maxId);
            employee.setPassword(pass);
            employee.setHoliday((short) 20);
            try {
                create(employee);
                return "OK";
            } catch (Exception ex) {
                Logger.getLogger(EmployeeFacade.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "ERR";
    }

    public Employee loginEmployee(String pLogin, String pPass) {
        try {
            Employee employee = getEntityManager().createNamedQuery("Employee.findByLogin", Employee.class).setParameter("login", pLogin).getSingleResult();
            if (employee.getPassword().equals(pPass)) {
                UtilLogic.setSessionEmployee(employee);
                return employee;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Employee> findEmployeePosition(String s) {
        try {
            List<Employee> lstEmp = getEntityManager().createNamedQuery("Employee.findByPosition", Employee.class).setParameter("position", s).getResultList();
            return lstEmp;
        } catch (NoResultException e) {
        }
        return null;
    }

    public ArrayList<Holiday> showHoliday(Employee e) {
        ArrayList<Holiday> lst = new ArrayList();
        for (Holiday h : e.getHolidayCollection()) {
            lst.add(h);
        }
        return lst;
    }

    public ArrayList<Sector> showSector(Employee e) {
        ArrayList<Sector> lst = new ArrayList();
        for (Sector s : e.getSectorCollection()) {
            lst.add(s);
        }
        return lst;
    }

    public ArrayList<OrderFeed> showOrderFeed(Employee e) {
        ArrayList<OrderFeed> lst = new ArrayList();
        for (OrderFeed o : e.getOrderFeedCollection()) {
            lst.add(o);
        }
        return lst;
    }

    public ArrayList<MedExamination> showMed(Employee e) {
        ArrayList<MedExamination> lst = new ArrayList();
        for (MedExamination m : e.getMedExaminationCollection()) {
            lst.add(m);
        }
        return lst;
    }

    public String deleteEmployee(Integer id) {
        try {
            destroy(id);
            System.out.println("OK usowanie");
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }

    public List<String> getPosition() {
        try {
            List<String> lst = getEntityManager().createNamedQuery("Employee.findDistinctPosition", String.class).getResultList();
            return lst;
        } catch (Exception e) {
            System.err.println("blad pozycja");
            return null;
        }
    }

    public List<String> getCondition() {
        List<String> lst = getEntityManager().createNamedQuery("Employee.findDistinctCondition", String.class).getResultList();
        System.out.println("ok statusa");
        return lst;
    }

    public String editEmpl(Employee emp) throws Exception {
        try {
            edit(emp);
            return "OK";
        } catch (Exception e) {
            return "ERR";
        }
    }

}
