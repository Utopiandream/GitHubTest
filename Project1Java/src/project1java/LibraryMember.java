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
public class LibraryMember {
    int numCopies = 0;
    String[] bookKeeper = new String[3];
    
    String name;
   
   public LibraryMember(){  //Constructors
       name = "John Doe";
   }
   public LibraryMember(String user){
       name = user;
   }
   
   
    public void rentBook(Book book){   //function for renting a book
        if (numCopies < 3){
        if (book.copies > 0) {
            book.copies--;
            bookKeeper[numCopies] = book.name;
            numCopies++;
            System.out.print("Renting Book: ");
            System.out.println(book.name);
        }
        else
            System.out.println("All issues rented, try another Book");
        }
        else
            System.out.println("You have Maximum Amount, Return a Book!");
    }
    public void returnBook(Book book){     //function for returning a book
        if(numCopies > 0){
        System.out.print("Returning Book: ");
        System.out.println(book.name);
        if(numCopies == 2){
            bookKeeper[numCopies-1] = bookKeeper[numCopies];
        }
        numCopies--;
        book.copies++;
        }
        else
            System.out.println("You Don't have books Rented!");
    }
    
    public void printRentedBooks(){
        for (int i = 0; i < numCopies; i++){
        System.out.print(i);
        System.out.print(") ");
        System.out.print(bookKeeper[i]);
        System.out.print(" ");
        }
    }
    
}
