package uk.ac.newcastle.enterprisemiddleware.customer;

import uk.ac.newcastle.enterprisemiddleware.contact.Contact;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
