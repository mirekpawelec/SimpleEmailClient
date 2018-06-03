/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.pawelec.simpleemailclient.exception.FileNoFoundException;
import pl.pawelec.simpleemailclient.model.enum_.PathToFile;

/**
 *
 * @author mirek
 */
@WebServlet(name = "handleFiles", urlPatterns = "/app/files")
public class HandleFiles extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileType = "";
        java.nio.file.Path pathToFile = null;
        byte[] bufor = null;
        String userId = req.getParameter("userId");
        String messageId = req.getParameter("messageId");
        String fileName = req.getParameter("fileName");
        resp.setCharacterEncoding("UTF-8");
        boolean save = Boolean.valueOf(req.getParameter("save"));
        if((userId!=null && !userId.isEmpty()) &&
                (messageId!=null && !messageId.isEmpty()) &&
                    (fileName!=null && !fileName.isEmpty())){
            try{
                pathToFile = Paths.get(PathToFile.MAIN_REPOSITORY.getPath(), userId, messageId, fileName);
                bufor = Files.readAllBytes(pathToFile);
                fileType = Files.probeContentType(pathToFile);
            } catch (IOException ex) {
                throw new FileNoFoundException("The file couldn't be found!");
            }
            resp.setContentType(fileType);
            resp.setContentLength(bufor.length);
            if(save){
                resp.addHeader("Content-Disposition", "attachment; filename="+pathToFile.getFileName().toString());
            }
            OutputStream os = resp.getOutputStream();
            os.write(bufor); 
            os.flush();
        }else{
            throw new FileNoFoundException("It has gotten not enough data to search file!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
}
