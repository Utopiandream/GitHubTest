//scanner.cpp
#include <iostream>
#include "token.h"
#include <string>
#include <vector>
#include <fstream>

using namespace std;
int filter(char);
int check(int);

//TODO add 2-D table table[#states][Alphabet= ws, letter, number, relational, delimeter, other operator, keyword]

//TODO Read characters from file through a filter
char words[200];
int table[19][10] = { 
//WS, Alpha, Numa, EOF, Operator, Delimeter, =, !, <>, other
{ 1000, 1, 9, 2000, 1002, 1004, 17, 18, 1005, -2 },		  //S:0
{ 1000, 2, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:1-8 ID
{ 1000, 3, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:2 ID
{ 1000, 4, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:3 ID
{ 1000, 5, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:4 ID
{ 1000, 6, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:5 ID
{ 1000, 7, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:6 ID
{ 1000, 8, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:7 ID
{ 1000, 8, 1000, 1000, 1000, 1000, 1000, 1000, 1000, -2 },//S:8 ID
{ 1001, 1001, 10, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:9-16 number
{ 1001, 1001, 11, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:10 number
{ 1001, 1001, 12, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:11 number
{ 1001, 1001, 13, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:12 number
{ 1001, 1001, 14, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:13 number
{ 1001, 1001, 15, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:14 number
{ 1001, 1001, 16, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:15 number
{ 1001, 1001, 16, 1001, 1001, 1001, 1001, 1001, 1001, -2 },//S:16 number
{ 1002, 1002, 1002, 1002, 1002, 1002, 1005, 1002, 1005, -2 },//S:17 Relational Operator
{ -1, -1, -1, -1, -1, -1, 1005, -1, -1, -2 } //S:18 Relational ! or Errors
};


void scanner(ifstream &stream, token & tkn){
	string str;
	char c;
	int currentState = 0;
	int firstchar = 0;
	int nextState;
	int finalState = 0;
	int count1 = 0;
	memset(words, 0, sizeof words);	//Sets the char array back to 0!

 if (stream.is_open()){
			while (stream.get(c)) {
				
				char p = stream.peek();	//if the next char is EOF, save your shit and get out
					
					firstchar = filter(c);
					finalState = table[currentState][firstchar];

				if (finalState >= 1000) {
					if (c != ' ' && '\n') {
						words[count1] = c;
					}
					str = words;
					tkn.tokenInstance = str;
					tkn.tokenID = check(finalState);
						count1 = 0;
						currentState = 0;
						return;
				}
				else if (finalState < 0) {
					cout << "Error! no token matched!" << endl;
					tkn.tokenID = 0; // end of file
					tkn.tokenInstance = "";
					return;
				}
				else {
					if (c != ' ' && '\n') {
						words[count1] = c;
						count1 ++;
						currentState = finalState;
					}
					//if not final, check next char if we are on string
					firstchar = filter(p);
					finalState = table[currentState][firstchar];
					
					//if currentState == 17 or == 18
					if (currentState == 17) {
						if (finalState != 1005) {
							str = words;
							tkn.tokenInstance = str;
							tkn.tokenID = check(finalState);
							count1 = 0;
							currentState = 0;
							return;
						}
					}
					if (finalState == 1000 || finalState == 1001) { //if next char is
						str = words;
						tkn.tokenInstance = str;
						tkn.tokenID = check(finalState);
						count1 = 0;
						currentState = 0;
						return;
					}

				}
			} //end of getc
			if (stream.eof()) {
				tkn.tokenID = 0; // end of file
				words[count1] = c;
				str = words;
				tkn.tokenInstance = str;
				return;
			}
 }//End of If Open


 else
	cout << "Could Not Read File!" << endl;
}


//TODO remove comments, suppose to return nextStatement
int filter(char c){
	int nextstate = 0;

		if (c == ' ' || c == '\n') nextstate = 0;
		else if (isalpha(c)) nextstate = 1;
		else if (isdigit(c)) nextstate = 2;
		else if (c == EOF) nextstate = 3;
		else if (c == ':' || c == '+' || c == '-' || c == '*' || c == '/' || c == '%') nextstate = 4;
		else if (c == '.' || c == '(' || c == ')' || c == ',' || c == '{' || c == '}' || c == ';' || c == '[' || c == ']') nextstate = 5;
		else if (c == '=') nextstate = 6;
		else if (c == '!') nextstate = 7;
		else if (c == '<' || c == '>') nextstate = 8;
		else nextstate = 9;

		return nextstate;
}

int check(int whatis) {
	enum tokenID { EOF_tk, IDENT_tk, NUM_tk,};
	switch (whatis)
	{
	case 1000:
		return 1;
	case 1001:
		return 2;
	case 1002:
		return 3;
	case 1003:
		return 5;
	case 1004:
		return 4;
	case 1005:
		return 5;
	case 2000:
		return 0;	
	default:
		return 0;
		break;
	}


}
