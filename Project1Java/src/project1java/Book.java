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
public class Book {
    int copies = 2;
    String name;
    
    public Book(){
        copies = 2;
        name = "Random";
    }
    public Book(String inpt){
        copies = 2;
        name = inpt;
    }
}
