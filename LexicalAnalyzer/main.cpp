//main.cpp
#include <iostream>
#include <fstream>
#include <string>
#include <string.h>
#include <vector>
#include <sstream>
#include <algorithm>
#include "token.h"
#include "scanner.h"
#include "driver4scanner.h"

using namespace std;

void printArr(vector<string>);
void printToken(token);
static int LENGTH = 10;

int main (int argc, char *argv[]){
vector<string>words;
string lines;
string sub;
token	*tkn = NULL;

if(argv[1] == NULL)		//If user enters nothing after run, finish later
{
 cout << "Enter Input: ";
 getline(cin, lines);
	 ofstream ofs;
	 ofs.open("tempor.dat");
	 ofs << lines;
	 ofs.close();
 }	//End of Enter Input
else if(argv[2] == NULL)				//If user enters name of data file
{
	ifstream is(strcat(argv[1], ".dat"));
	if(is.is_open())
	{
	ofstream ofs;
	ofs.open("tempor.dat");
	while(getline(is, lines))
		ofs << lines;
	ofs.close();
	is.close();
	}
	else
	{
	cout << "Invalid File Name." <<endl;
	return 0;
	}
}
else					//If user enters data from keyboard
{
int i = 1;
ofstream ofs;
ofs.open("tempor.dat");
while(argv[i])
	{
 	 ofs << argv[i]<< " ";
	 i++;
	}
 ofs.close();
}

ifstream is("tempor.dat");		//read data from temporary file and insert into vector
if(is.is_open())
	{
	int linenumber= 1;
	 while(getline(is, lines))
	 {
		 for(int i = 0; i < lines.length(); i++)		//checking input for symbols, produce error if found
		 {
		 if(!isalnum(lines.at(i)) && lines.at(i) != ' ')
		 {
		  cerr << "Error " << " invalid character: " << lines.at(i) << " Position: " << i << endl;
		  return 0;
		  }
		 }
		linenumber++;
	  stringstream iss(lines); 
	  while (iss >> sub)
		{
		transform(sub.begin(), sub.end(), sub.begin(), ::tolower); //converts all strings to lowercase to remove distinction
		 words.push_back(sub);
		}
	  }	 							// end OF reading in file

		//Insert Array Given to Us
	cout << "Inserting: ";
	printArr(words);
	is.close();
	//printTree(tree);	//call function to call traversals
	}										//end of ifOpen
else
	{
	 cout << "Could Not Open File. " << endl;
    	}

return 0;
} //end method main


//method to print Tree
void printToken(token tkn){
	//Show graphical view of tree, and the 3 orders
	cout << endl << "*** Pre-Order ***" << endl;
	
}

void printArr(vector<string> arr){
	for (int i = 0; i < arr.size(); i++)
	{
	 cout << arr[i] << " ";
	}
	cout << endl;
}
