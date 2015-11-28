//driver4scanner.cpp
#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include "token.h"
#include "scanner.h"
using namespace std;

int keywords(string);

void DRIVER4SCANNER ()
{
token *tkn = new(token);
ifstream tempor;
tempor.open("tempor.lan");

enum tokenID{EOF_tk, IDENT_tk, NUM_tk, OPERATOR_tk, DELIMETER_tk, RELATIONAL_tk, BEGIN_tk, END_tk, START_tk, FINISH_tk, IF_tk, THEN_tk, LOOP_tk, INTEGER_tk, DO_tk, SCAN_tk, PRINT_tk, VOID_tk, RETURN_tk, PROGRAM_tk, DUMMY_tk};
int lineNum = 0;
string tokenNames[] = {"EndOfFile", "Identifier", "Number", "Operator", "Delimeter", "Relational",
"BEGIN keyword", "END keyword", "START keyword", "FINISH keyword",
"IF keyword", "THEN keyword", "LOOP keyword",
"INTEGER keyword", "DO keyword", "SCAN keyword", "PRINT keyword",
"VOID keyword", "RETURN keyword", "PROGRAM keyword", "DUMMY keyword"};
//WS, Alpha, Numa, EOF, Operator, Delimeter, =, !, <>, other


while (tkn->tokenID != EOF_tk) {
	scanner(tempor, *tkn);
	//line#, tokenID, name
	
	if (tkn->tokenInstance != ""){
		if (tkn->tokenID == 1) {	//check for keywords if token is identifier
			tkn->tokenID = keywords(tkn->tokenInstance);
			}
		if(tkn->tokenID == 0)
			tkn->tokenInstance = "EOF";
		cout << "{#" << lineNum << " " << tokenNames[tkn->tokenID] << " " << tkn->tokenInstance << "} " << endl;
		lineNum++;
		}
 }
tempor.close();
}


int keywords(string key) {
	int tokenID = 1;
	transform(key.begin(), key.end(), key.begin(), ::tolower); //changes strings to all lower case
	if (key == "begin")
		tokenID = 6;
	if (key == "end")
		tokenID = 7;
	if (key == "start")
		tokenID = 8;
	if (key == "finish")
		tokenID = 9;
	if (key == "if")
		tokenID = 10;
	if (key == "then")
		tokenID = 11;
	if (key == "loop")
		tokenID = 12;
	if (key == "do")
		tokenID = 13;
	if (key == "scan")
		tokenID = 14;
	if (key == "print")
		tokenID = 15;
	if (key == "void")
		tokenID = 16;
	if (key == "return")
		tokenID = 17;
	if (key == "program")
		tokenID = 18;
	if (key == "dummy")
		tokenID = 19;

	return tokenID;
}

