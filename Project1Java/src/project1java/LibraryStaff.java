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
public class LibraryStaff extends LibraryMember {
    
    public LibraryStaff(){  //Constructors
       name = "John Doe";
   }
   public LibraryStaff(String user){
       name = user;
   }
    
    
    public void rentJournal(Journal journal){
        if (journal.copies > 0) {
            journal.copies--;
            System.out.print("Renting Journal: ");
            System.out.println(journal.name);
        }
        else
            System.out.println("All issues rented, Try another Journal");
        // TODO function for renting a journal
    }
    public void returnJournal(Journal journal){
        System.out.print("Returning Journal: ");
        System.out.println(journal.name);
        journal.copies++;
        // TODO function for renting a journal
    }
}
