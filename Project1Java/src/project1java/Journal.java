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
    
    public Journal(){
        copies = 4;
        name = "Random";
    }
    public Journal(String inpt){
        copies = 4;
        name = inpt;
    }
}
