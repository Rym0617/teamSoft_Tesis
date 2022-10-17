/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.teamsoft.JPAControllerClasses;

import org.teamsoft.entity.County;
import org.teamsoft.entity.Municipality;
import org.teamsoft.exceptions.NonexistentEntityException;
import org.teamsoft.exceptions.PreexistingEntityException;
import org.teamsoft.exceptions.RollbackFailureException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author yoyo
 */
public class MunicipalityJpaController implements Serializable {

    public MunicipalityJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Municipality municipality) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            County countyFk = municipality.getCountyFk();
            if (countyFk != null) {
                countyFk = em.getReference(countyFk.getClass(), countyFk.getId());
                municipality.setCountyFk(countyFk);
            }
            em.persist(municipality);
            if (countyFk != null) {
                countyFk.getMunicipalityList().add(municipality);
                countyFk = em.merge(countyFk);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMunicipality(municipality.getId()) != null) {
                throw new PreexistingEntityException("Municipality " + municipality + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Municipality municipality) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Municipality persistentMunicipality = em.find(Municipality.class, municipality.getId());
            County countyFkOld = persistentMunicipality.getCountyFk();
            County countyFkNew = municipality.getCountyFk();
            if (countyFkNew != null) {
                countyFkNew = em.getReference(countyFkNew.getClass(), countyFkNew.getId());
                municipality.setCountyFk(countyFkNew);
            }
            municipality = em.merge(municipality);
            if (countyFkOld != null && !countyFkOld.equals(countyFkNew)) {
                countyFkOld.getMunicipalityList().remove(municipality);
                countyFkOld = em.merge(countyFkOld);
            }
            if (countyFkNew != null && !countyFkNew.equals(countyFkOld)) {
                countyFkNew.getMunicipalityList().add(municipality);
                countyFkNew = em.merge(countyFkNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = municipality.getId();
                if (findMunicipality(id) == null) {
                    throw new NonexistentEntityException("The municipality with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Municipality municipality;
            try {
                municipality = em.getReference(Municipality.class, id);
                municipality.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The municipality with id " + id + " no longer exists.", enfe);
            }
            County countyFk = municipality.getCountyFk();
            if (countyFk != null) {
                countyFk.getMunicipalityList().remove(municipality);
                countyFk = em.merge(countyFk);
            }
            em.remove(municipality);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Municipality> findMunicipalityEntities() {
        return findMunicipalityEntities(true, -1, -1);
    }

    public List<Municipality> findMunicipalityEntities(int maxResults, int firstResult) {
        return findMunicipalityEntities(false, maxResults, firstResult);
    }

    private List<Municipality> findMunicipalityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Municipality.class));
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

    public Municipality findMunicipality(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Municipality.class, id);
        } finally {
            em.close();
        }
    }

    public int getMunicipalityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Municipality> rt = cq.from(Municipality.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
