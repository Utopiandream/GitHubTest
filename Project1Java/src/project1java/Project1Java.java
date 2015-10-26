/*
 * Name: Douglas Stroer
 * SSOID: Dmst3b
 * Class: CMPSCI 4500 Intro To Software Profession
 * Project 1
 */
package project1java;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Project1Java {

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        int choice_user = 1;
        String inpt = "none";       //initialize choice variables
        
        Library library = new Library();
        
        LibraryMember[] member = new LibraryMember[3];
        member[0] = new LibraryMember("member1");
        member[1] = new LibraryMember("member2");
        member[2] = new LibraryMember("member3");
        
        LibraryStaff[] staff = new LibraryStaff[2];
        staff[0] = new LibraryStaff("staff1");
        staff[1] = new LibraryStaff("staff2");
        
        System.out.println("Enter Username to log in, or quit to exit.");
        while(choice_user !=10 ){
            System.out.print("MEMBERS: ");
            System.out.print(member[0].name); System.out.print(" "); System.out.print(member[1].name); System.out.print(" "); System.out.print(member[2].name);
            System.out.print(" STAFF: ");
            System.out.print(staff[0].name); System.out.print(" "); System.out.println(staff[1].name);
            inpt = sc.next();
            switch (inpt) {
                case "member1":
                    useChoiceMember(member[0], library);
                    break;
                case "member2":
                    useChoiceMember(member[1], library);
                    break;
                case "member3":
                    useChoiceMember(member[2], library);
                    break;
                case "staff1":
                    useChoiceStaff(staff[0], library);
                    break;
                case "staff2":
                    useChoiceStaff(staff[1], library);
                    break;
                case "quit":
                    choice_user = 10;
                    break;
                default: System.out.println("That is not a correct name. Try Again, or quit to exit.");
                    break;
            } //end of user switch
                
        }
    }   //end of Main
        
static void useChoiceMember(LibraryMember member, Library library) throws IOException {
    int choice_use = 1;
    int choice_num = 0;
    int logout = 1;
    Scanner sc = new Scanner(System.in);
    System.out.print("Welcome to the library "); System.out.println(member.name);
        
    while(logout != 0) {
        System.out.println("Options: 1 to Rent a Book, 2 to return a Book, 3 to see Books Catalog, or 0 to Logout.");
        try {
        choice_use = sc.nextInt();
        switch (choice_use) {
            case 1:
                System.out.println("Lists of Books: ");
                System.out.print("Select Book Number: ");
                library.printBook();
                try{
                    choice_num = sc.nextInt();
                    member.rentBook(library.book[choice_num]);
                }
                catch (InputMismatchException e) {
                    System.err.println("Please Enter a Number!");
                    sc.next();
                }
                break;
            case 2:     //TODO Return a book
                System.out.println("Lists of Books You Have: ");
                member.printRentedBooks();
                System.out.print("Select Book Number: ");
                try{
                    choice_num = sc.nextInt();
                    member.returnBook(library.book[choice_num]);
                }
                catch (InputMismatchException e) {
                    System.err.println("Please Enter a Number!");
                    sc.next();
                }
                break;
            case 3: 
                System.out.println("Heres the catalog of Books(Copies for each): ");
                library.printBook();
                break;
            case 0:
                logout = 0;
                break;
            default: System.out.println("That is not an option. Try Again");
                break;
         }   //end of use switch
        }
        catch (InputMismatchException e) {
            System.err.println("Please Enter a Number!");
            sc.next();
        }
                //TODO UPDATE CATALlog
    } //end of while uses
}

static void useChoiceStaff(LibraryStaff staff, Library library){
    int choice_use = 1;
    int choice_num = 0;
    int logout = 1;
    Scanner sc = new Scanner(System.in);
        
    System.out.print("Welcome to the library "); System.out.println(staff.name);
  
        while(logout != 0) {
                System.out.println("Options: 1 to Rent a Book, 2 to return Book, 3 to rent a Journal, 4 to return a Journal, 5 to see catalog, 0 to logout.");
            try {
                choice_use = sc.nextInt();
                switch (choice_use) {
                case 1: 
                    System.out.println("Lists of Books: ");
                    System.out.print("Select Book Number: ");
                    library.printBook();
                    try{
                        choice_num = sc.nextInt();
                        staff.rentBook(library.book[choice_num]);
                    }
                    catch (InputMismatchException e) {
                        System.err.println("Please Enter a Number!");
                        sc.next();
                    }
                    break;
                case 2:     //TODO return book
                    break;
                case 3:     //TODO Rent Journal
                    System.out.println("You've rented a journal");
                    break;
                case 4:     //TODO return journal
                    break;
                case 5: System.out.println("Heres the catalog of Books and Journals & number of Copies for each: ");
                    library.printBook();
                    library.printJournal();
                    break;
                case 0:
                    logout = 0;
                    break;
                default: System.out.println("That is not an option. Try Again");
                    break;
                }   //end of use switch
            }
                //TODO UPDATE CATALlog
            catch (InputMismatchException e) {
                System.err.println("Please Enter a Number!");
                sc.next();
            }
        } //end of while uses
 }
    
}   //end of class
