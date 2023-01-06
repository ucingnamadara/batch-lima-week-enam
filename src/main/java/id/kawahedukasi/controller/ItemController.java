package id.kawahedukasi.controller;

import id.kawahedukasi.model.Item;
import id.kawahedukasi.model.ResponseCustom;
import io.vertx.core.json.JsonObject;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/item")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemController {

    @POST
    @Transactional //karena kita mau save data item
    public ResponseCustom create(JsonObject request){
        String name = request.getString("name");
        Double price = request.getDouble("price");
        String type = request.getString("type");
        Long count = request.getLong("count");

        if(name == null || price == null || type == null || count == null){
            Map<String, Object> map = new HashMap<>();
            map.put("reason", "DATA_NOT_COMPLETED");

            ResponseCustom responseCustom = new ResponseCustom();
            responseCustom.message = "BAD_REQUEST";
            responseCustom.statusCode = 400;
            responseCustom.data = map;

            return responseCustom;
        }

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setType(type);
        item.setCount(count);
        item.setDescription(request.getString("description"));

        item.persist();

        ResponseCustom responseCustom = new ResponseCustom();
        responseCustom.message = "CREATED";
        responseCustom.statusCode = 200;
        responseCustom.data = Map.of("id", item.getId());

        return responseCustom;
//        return Response.status(Response.Status.CREATED).entity(Map.of("id", item.getId())).build();
    }

    @GET
    public Response getAll(){
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

        return Response.ok().entity(result).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") String id, JsonObject request){
        String name = request.getString("name");
        Double price = request.getDouble("price");
        String type = request.getString("type");
        Long count = request.getLong("count");

        if(name == null || price == null || type == null || count == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "BAD_REQUEST"))
                    .build();
        }

        Item item = Item.findById(id);
        if(item == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "ITEM_NOT_FOUND"))
                    .build();
        }

        item.setName(name);
        item.setPrice(price);
        item.setType(type);
        item.setCount(count);
        item.setDescription(request.getString("description"));

        item.persist();

        return Response.ok().entity(Map.of("id", item.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") String id){
        Item item = Item.findById(id);
        if(item == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("message", "ITEM_ALREADY_DELETED"))
                    .build();
        }
//        Item.deleteById(id);
        item.delete();

        return Response.status(Response.Status.NO_CONTENT).entity(Map.of("id", item.getId())).build();
    }
}
