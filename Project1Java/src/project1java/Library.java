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
public class Library {
    
    
    Book[] book = new Book[10];
    Journal[] journal = new Journal[4];
   
    public Library (){
    book[0] = new Book("FirstBook");
    book[1] = new Book("SecondBook");
    book[2] = new Book("ThirdBook");
    book[3] = new Book("FourthBook");
    book[4] = new Book("FifthBook");
    book[5] = new Book("SixthBook");
    book[6] = new Book("SeventhBook");
    book[7] = new Book("EighthBook");
    book[8] = new Book("NinthBook");
    book[9] = new Book("TenthBook");
    journal[0] = new Journal("FirstJournal");
    journal[1] = new Journal("SecondJournal");
    journal[2] = new Journal("ThirdJournal");
    journal[3] = new Journal("FourthJournal");
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
