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
    int numJournalCopies;
    Journal[] rentedJournals = new Journal[3];
    
    public LibraryStaff(){  //Constructors
       name = "John Doe";
       numCopies = 0;
       numJournalCopies = 0;
       
       rentedBooks[0] = null;
       rentedBooks[1] = null;
       rentedBooks[2] = null;
       rentedJournals[0] = null;
       rentedJournals[0] = null;
   }
   public LibraryStaff(String user){
       name = user;
       numCopies = 0;
       numJournalCopies = 0;
       rentedBooks[0] = null;
       rentedBooks[1] = null;
       rentedBooks[2] = null;
       rentedJournals[0] = null;
       rentedJournals[0] = null;
   }
    
    
    public void rentJournal(Journal journal){
        if (journal.copies > 0) {
            journal.copies--;
            rentedJournals[numJournalCopies] = journal;
            System.out.print("Renting Journal: ");
            System.out.println(journal.name);
            numJournalCopies++;
        }
        else
            System.err.println("All issues rented, try another Journal");
    }
    public void returnJournal(Journal journal, int num){
        System.out.print("Returning Journal: ");
        System.out.println(journal.name);
        if(numJournalCopies == 1){
            rentedJournals[0] = null;
        }
        if(numJournalCopies == 2){     //member has 2 Journals
            if(num == 0){   //if returned Journal is first
                rentedJournals[0] = rentedJournals[1];
                rentedJournals[1] = null;
                rentedJournals[2] = null;
            }
            else{
                rentedJournals[1] = null;
            }
        }
        if(numJournalCopies == 3){     //member has 3 Journals
            if(num == 0){   //if returned book is first
                rentedJournals[0] = rentedJournals[1];
                rentedJournals[1] = rentedJournals[2];
                rentedJournals[2] = null;
            }
            if(num == 1){   //if returned book is second
                rentedJournals[1] = rentedJournals[2];
                rentedJournals[2] = null;
            }
            if(num == 2){   //if returned book is third
                rentedJournals[2] = null;
            }
            
        }
        
        numJournalCopies--;
        journal.copies++;
        
    }
    
    public void printRentedJournals(){
        for (int i = 0; i < numJournalCopies; i++){
        System.out.print(i);
        System.out.print(") ");
        System.out.print(rentedJournals[i].name);
        System.out.print(" ");
        }
    }
    
}
