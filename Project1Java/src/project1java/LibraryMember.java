/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1java;

public class LibraryMember {
    int numCopies;      //Book Number constraint    
    Book[] rentedBooks = new Book[3];   //Array of Books Held by Member
    String name;    //name of member
   
   public LibraryMember(){  //Constructors
       name = "John Doe";
       numCopies = 0;
       rentedBooks[0] = null;
       rentedBooks[1] = null;
       rentedBooks[2] = null;
   }
   public LibraryMember(String user){
       name = user;
       numCopies = 0;
       rentedBooks[0] = null;
       rentedBooks[1] = null;
       rentedBooks[2] = null;
   }
   
   
    public void rentBook(Book book){   //function for renting a book
        if (book.copies > 0) {
            book.copies--;
            rentedBooks[numCopies] = book;
            System.out.print("Renting Book: ");
            System.out.println(book.name);
            numCopies++;
        }
        else
            System.err.println("All issues rented, try another Book");
    }
    public void returnBook(Book book, int num){     //function for returning a book
        System.out.print("Returning Book: ");
        System.out.println(book.name);
        if(numCopies == 1){
            rentedBooks[0] = null;
        }
        if(numCopies == 2){     //member has 2 books
            if(num == 0){   //if returned book is first
                rentedBooks[0] = rentedBooks[1];
                rentedBooks[1] = null;
                rentedBooks[2] = null;
            }
            else{
                rentedBooks[1] = null;
            }
        }
        if(numCopies == 3){     //member has 3 books
            if(num == 0){   //if returned book is first
                rentedBooks[0] = rentedBooks[1];
                rentedBooks[1] = rentedBooks[2];
                rentedBooks[2] = null;
            }
            if(num == 1){   //if returned book is second
                rentedBooks[1] = rentedBooks[2];
                rentedBooks[2] = null;
            }
            if(num == 2){   //if returned book is third
                rentedBooks[2] = null;
            }
            
        }
        
        numCopies--;
        book.copies++;
        
    }
    
    public void printRentedBooks(){
        for (int i = 0; i < numCopies; i++){
        System.out.print(i);
        System.out.print(") ");
        System.out.print(rentedBooks[i].name);
        System.out.print(" ");
        }
    }
    
}
