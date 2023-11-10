package uk.ac.newcastle.enterprisemiddleware.customer;

import uk.ac.newcastle.enterprisemiddleware.contact.Contact;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-10 01:08
 **/
@Dependent
public class CustomerService {
    @Inject
    @Named("logger")
    Logger log;
    @Inject
    CustomerRepository customerRepository;
    @Inject
    CustomerValidator customerValidator;

    List<Customer> findAllOrderedByName() {
        return customerRepository.findAllOrderedByName();
    }

    Customer findById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    Customer findByEmail(String customer_email){ return customerRepository.findByEmail(customer_email);}
    public Customer createCustomer(Customer customer) throws Exception
    {
        log.info("CustomerService.create() - Creating" + customer.getFirstName()+customer.getLastName());

        //Make sure the data fits with the parameters in the Customer model and passes validation
        customerValidator.validateCustomer(customer);
        return customerRepository.create(customer);
    }
    public Customer updateCustomer(Customer customer) throws Exception
    {
        log.info("CustomerService.update() - updating" + customer.getFirstName()+customer.getLastName());

        customerValidator.validateCustomer(customer);
        return customerRepository.update(customer);
    }

    public Customer deleteCustomer(Customer customer) throws Exception {
        log.info("deleteCustomer() - Deleting " + customer.toString());

        Customer deletedCustomer = null;

        if (customer.getCustomerId() != null) {
            deletedCustomer = customerRepository.delete(customer);
        } else {
            log.info("delete() - No ID was found so can't Delete.");
        }

        return deletedCustomer;
    }
}
