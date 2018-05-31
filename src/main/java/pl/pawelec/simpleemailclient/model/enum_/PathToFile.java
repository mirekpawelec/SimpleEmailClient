/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.pawelec.simpleemailclient.model.enum_;

import java.io.File;

/**
 *
 * @author mirek
 */
public enum PathToFile {
    MAIN_REPOSITORY( "C:"+File.separator
                    +"Users"+File.separator
                    +"mirek"+File.separator
                    +"Documents"+File.separator
                    +"NetBeansProjects"+File.separator
                    +"SimpleEmailClient"+File.separator
                    +"files");

    private String path;
    
    private PathToFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
