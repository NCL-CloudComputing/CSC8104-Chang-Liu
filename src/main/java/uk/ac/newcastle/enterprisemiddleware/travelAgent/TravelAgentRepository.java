package uk.ac.newcastle.enterprisemiddleware.travelAgent;

import io.quarkus.logging.Log;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * @program: CSC8104-Chang-Liu
 * @description:
 * @author: CHANG LIU
 * @create: 2023-11-13 16:56
 **/
@RequestScoped
public class TravelAgentRepository {

    @Inject
    Logger logger;

    @Inject
    EntityManager entityManager;

    /**
     * @description find travelAgent by id
     * @Param id:
     * @return uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent
     * @author Chang Liu
     * @create 2023/11/14
     */

    TravelAgent findTravelAgentById(Long id){
        return entityManager.find(TravelAgent.class,id);
    }

    /**
     * @description: find all travel agent
     * @return java.util.List<uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent>
     * @author Chang Liu
     * @create 2023/11/14
     */

    List<TravelAgent> findAllTravelAgent() {
        TypedQuery<TravelAgent> query = entityManager.createNamedQuery(TravelAgent.FIND_ALL_TRAVELAGENT, TravelAgent.class);
        return query.getResultList();
    }
    /**
     * @description: creat travel agent
     * @Param travelAgent:
     * @return uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent
     * @author Chang Liu
     * @create 2023/11/14
     */

    TravelAgent createTravelAgent(TravelAgent travelAgent) throws Exception{
        logger.info("TravelAgentRepository.createTravelAgent() - Creating"+travelAgent.getId());

        entityManager.merge(travelAgent);

        return travelAgent;
    }

    /**
     * @description: delete travel agent
     * @Param travelAgent:
     * @return uk.ac.newcastle.enterprisemiddleware.travelAgent.TravelAgent
     * @author Chang Liu
     * @create 2023/11/14
     */

    TravelAgent deleteTravelAgent(TravelAgent travelAgent) throws Exception {
        logger.info("TravelAgentRepository.deleteTravelAgent() - Deleting " + travelAgent.getId());

        if (travelAgent.getId() != null) {

            entityManager.remove(entityManager.merge(travelAgent));

        } else {
            logger.info("TravelAgentRepository.deleteTravelAgent() - No ID was found so can't Delete.");
        }

        return travelAgent;
    }


}
