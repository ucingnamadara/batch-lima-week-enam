package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import io.quarkus.scheduler.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class SchedulerService {
    Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Inject
    EntityManager entityManager;

    //setiap satu jam

    //saran hindari penamaan column yang ambigu (sytax sql)
    @Scheduled(every = "1h")
    public void deleteZeroItem(){
        String queryString = "SELECT * FROM item_schema.item WHERE \"count\" = 0";
        Query query = entityManager.createNativeQuery(queryString, Item.class);
        List<Item> list = query.getResultList();
        deleteItem(list);
        logger.info("Total Item Deleted");
    }


    @Transactional
    public void deleteItem(List<Item> items){
        for(Item item : items){
            item.delete();
        }
    }
}
