package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ItemService {

    Logger logger = LoggerFactory.getLogger(ItemService.class);

    public Map<String, Object> create(JsonObject request){
        String name = request.getString("name");
        Double price = request.getDouble("price");
        String type = request.getString("type");
        Long count = request.getLong("count");
        String description = request.getString("description");

        if(name == null || price == null || type == null || count == null){
            throw new ValidationException("BAD_REQUEST");
        }
        Item item = persistItem(name, price, type, count, description);

        return Map.of("id", item.getId());
    }

    @Transactional //karena kita mau save data item
    public Item persistItem(String name, Double price, String type, Long count, String description){
        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setType(type);
        item.setCount(count);
        item.setDescription(description);

        item.persist();
        logger.info("Created New Item -> {}", item.getId());
        return item;
    }

    public List<Map<String, Object>> getAll(){
        List<Item> items = Item.listAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for(Item item : items){
            Map<String, Object> map = new HashMap<>();
            map.put("id", item.getId());
            map.put("name", item.getName());
            map.put("price", item.getPrice());
            map.put("type", item.getType());
            map.put("count", item.getCount());
            map.put("description", item.getDescription());

            result.add(map);
        }

        return result;
    }

    @Transactional
    public Map<String,Object> update(String id, JsonObject request){
        String name = request.getString("name");
        Double price = request.getDouble("price");
        String type = request.getString("type");
        Long count = request.getLong("count");

        if(name == null || price == null || type == null || count == null){
            throw new ValidationException("BAD_REQUEST");
        }

        Item item = Item.findById(id);
        if(item == null){
            throw new ValidationException("ITEM_NOT_FOUND");
        }

        item.setName(name);
        item.setPrice(price);
        item.setType(type);
        item.setCount(count);
        item.setDescription(request.getString("description"));

        item.persist();

        return Map.of("id", item.getId());
    }

    @Transactional
    public Map<String,Object> delete(String id){
        Item item = Item.findById(id);
        if(item == null){
            throw new ValidationException("ITEM_ALREADY_DELETED");
        }
//        Item.deleteById(id);
        item.delete();

        return Map.of("id", item.getId());
    }
}
