package id.kawahedukasi.controller;

import id.kawahedukasi.model.request.FileMultipartRequest;
import id.kawahedukasi.service.DocumentService;
import io.vertx.core.json.JsonObject;
import net.sf.jasperreports.engine.JRException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path("/document")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentController {
    @Inject
    DocumentService documentService;

    @POST
    @Path("/importItem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importItem(@MultipartForm FileMultipartRequest request){
        try{
            documentService.importItem(request);
            Map<String, Object> result = new HashMap<>();
            result.put("message", "SUCCESS");
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (ValidationException | IOException e){
            Map<String, Object> result = new HashMap<>();
            result.put("message", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }

    }

    @GET
    @Path("/reportItem")
    @Produces("application/pdf")
    public Response reportItem() throws JRException {
        return documentService.reportItem();
    }
}
