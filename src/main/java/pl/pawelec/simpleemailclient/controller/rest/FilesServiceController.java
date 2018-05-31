/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.controller.rest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import pl.pawelec.simpleemailclient.exception.FileNoFoundException;
import pl.pawelec.simpleemailclient.model.enum_.PathToFile;

/**
 *
 * @author mirek
 */
@Path("/files")
public class FilesServiceController {
    
    @RolesAllowed({"ADMIN","USER"})
    @GET
    @Path("/{userId}/{messageId}/{fileName}")
    public Response getFile(@PathParam("userId") String userId,
                            @PathParam("messageId") String messageId,
                            @PathParam("fileName") String fileName){
        ResponseBuilder response = null;
        String fileType = "";
        java.nio.file.Path pathToFile = null;
        File file = null;
        if((userId!=null && !userId.isEmpty()) &&
                (messageId!=null && !messageId.isEmpty()) &&
                    (fileName!=null && !fileName.isEmpty())){
            try{
                pathToFile = Paths.get(PathToFile.MAIN_REPOSITORY.getPath(), userId, messageId, fileName);
                file = new File(pathToFile.toUri());
                fileType = Files.probeContentType(pathToFile);
            } catch (IOException ex) {
                throw new FileNoFoundException("The file couldn't be found!");
            }
            response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename="+file.getName());
            response.type(fileType);
        }else{
            throw new FileNoFoundException("It has gotten not enough data to search file!");
        } 
        return response.build();
    }

}
