/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1java;

/**
 *
 * @author Douglas
 */
public class Journal {
    int copies = 4;
    String name;
    int catalogNum;
    
    public Journal(){
        copies = 4;
        name = "Random";
        catalogNum = 0;
    }
    public Journal(String inpt, int catalog){
        copies = 4;
        name = inpt;
        catalogNum = catalog;
    }
}
