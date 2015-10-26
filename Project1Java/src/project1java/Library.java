/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1java;

public class Library {
    
    
    Book[] book = new Book[10];
    Journal[] journal = new Journal[4];
   
    public Library (){
    book[0] = new Book("FirstBook", 0);
    book[1] = new Book("SecondBook", 1);
    book[2] = new Book("ThirdBook", 2);
    book[3] = new Book("FourthBook", 3);
    book[4] = new Book("FifthBook", 4);
    book[5] = new Book("SixthBook", 5);
    book[6] = new Book("SeventhBook", 6);
    book[7] = new Book("EighthBook", 7);
    book[8] = new Book("NinthBook", 8);
    book[9] = new Book("TenthBook", 9);
    journal[0] = new Journal("FirstJournal", 0);
    journal[1] = new Journal("SecondJournal", 1);
    journal[2] = new Journal("ThirdJournal", 2);
    journal[3] = new Journal("FourthJournal", 3);
    }
    
    void printBook (){
        for(int i = 0; i < 10; i ++){
        System.out.print(i);
        System.out.print(") ");
        System.out.print(book[i].name);
        System.out.print("(");
        System.out.print(book[i].copies);
        System.out.print(") ");
        }
        System.out.println();
    }
    
    void printJournal (){
        for(int i = 0; i < 4; i ++){
        System.out.print(i);
        System.out.print(") ");
        System.out.print(journal[i].name);
        System.out.print("(");
        System.out.print(journal[i].copies);
        System.out.print(") ");
        }
        System.out.println();
    }
    
    void updateCatalog (Book book, int copies){
        book.copies = copies;
    }
    void updateCatalog (Journal journal, int copies){
        journal.copies = copies;
    }
}
