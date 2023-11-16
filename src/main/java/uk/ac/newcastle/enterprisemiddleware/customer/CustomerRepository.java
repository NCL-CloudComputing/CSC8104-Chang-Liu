package uk.ac.newcastle.enterprisemiddleware.customer;

import uk.ac.newcastle.enterprisemiddleware.contact.Contact;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-09 19:02
 **/
@RequestScoped
public class CustomerRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * @description Returns a List of all persisted {@link Customer} objects, sorted alphabetically by last name.
     * @Param log
     * @return java.util.List<uk.ac.newcastle.enterprisemiddleware.customer.Customer>
     * @author Chang Liu
     * @create 2023/11/9
     */
    List<Customer> findAllOrderedByName() {
        TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
        return query.getResultList();
    }
    List<Customer> findAllByFirstName(String firstName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(contact).where(cb.equal(contact.get(Contact_.firstName), firstName));
        criteria.select(customer).where(cb.equal(customer.get("firstName"), firstName));
        return em.createQuery(criteria).getResultList();
    }

    List<Customer> findAllByLastName(String lastName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new feature in JPA 2.0.
        // criteria.select(contact).where(cb.equal(contact.get(Contact_.lastName), lastName));
        criteria.select(customer).where(cb.equal(customer.get("lastName"), lastName));
        return em.createQuery(criteria).getResultList();
    }


    /**
     * @description
     * @Param id
     * @return uk.ac.newcastle.enterprisemiddleware.customer.Customer
     * @author Chang Liu
     * @create 2023/11/9
     */
    Customer findById(Long customerId){
        return em.find(Customer.class,customerId);
    }


    Customer findByEmail(String customer_email){
        TypedQuery<Customer> emailQuery = em.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class).setParameter("email",customer_email);
        return emailQuery.getSingleResult();
    }
    Customer create(Customer customer) throws Exception {
        log.info("CustomertRepository.create() - Creating " + customer.getFirstName() + " " + customer.getLastName());

        // Write the customer to the database.
        em.persist(customer);
        return customer;
    }
    Customer update(Customer customer) throws Exception {
        log.info("CustomertRepository.update() - Updating " + customer.getFirstName() + " " + customer.getLastName());

        // Either update the contact or add it if it can't be found.
        em.merge(customer);

        return customer;
    }

    Customer delete(Customer customer) throws Exception {
        log.info("CustomertRepository.delete() - Deleting " + customer.getFirstName() + " " + customer.getLastName());

        if (customer.getCustomerId() != null) {
            em.remove(em.merge(customer));
        } else {
            log.info("CustomertRepository.delete() - No ID was found so can't Delete.");
        }

        return customer;
    }


}
