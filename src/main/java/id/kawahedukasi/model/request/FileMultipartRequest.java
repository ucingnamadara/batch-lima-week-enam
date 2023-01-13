package id.kawahedukasi.model.request;

import javax.ws.rs.FormParam;

public class FileMultipartRequest {
    @FormParam("file")
    public byte[] file;
}
