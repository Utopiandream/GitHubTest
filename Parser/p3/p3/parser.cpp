//Parser.cpp Functions for Parsing
#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include "token.h"
#include "scanner.h"

using namespace std;

ifstream tempor;
int error;

//Prototypes!
int keywords(string);
void callScanner(ifstream &stream, token & tkn);

void program_f(token & tkn);
void block_f(token & tkn);
void var_f(token & tkn);
void type_f(token & tkn);
void mvars_f(token & tkn);
void expr_f(token & tkn);
void T_f(token & tkn);
void F_f(token & tkn);
void R_f(token & tkn);
void stats_f(token & tkn);
void mStat_f(token & tkn);
void stat_f(token & tkn);
void in_f(token & tkn);
void out_f(token & tkn);
void if_f(token & tkn);
void loop_f(token & tkn);
void assign_f(token & tkn);
void RO_f(token & tkn);


//Might not need
enum tokenID{EOF_tk, IDENT_tk, NUM_tk, OPERATOR_tk, DELIMETER_tk, RELATIONAL_tk, BEGIN_tk, END_tk, START_tk, FINISH_tk, IF_tk, THEN_tk, LOOP_tk, INTEGER_tk, DO_tk, SCAN_tk, PRINT_tk, VOID_tk, RETURN_tk, PROGRAM_tk, DUMMY_tk};
int lineNum = 0;
string tokenNames[] = {"EndOfFile", "Identifier", "Number", "Operator", "Delimeter", "Relational",
"BEGIN keyword", "END keyword", "START keyword", "FINISH keyword",
"IF keyword", "THEN keyword", "LOOP keyword",
"INTEGER keyword", "DO keyword", "SCAN keyword", "PRINT keyword",
"VOID keyword", "RETURN keyword", "PROGRAM keyword", "DUMMY keyword"};
//WS, Alpha, Numa, EOF, Operator, Delimeter, =, !, <>, other



void parser() {
	error = 0;
	token *tkn = new(token);
	//ifstream tempor;
	tempor.open("tempor.lan");

	callScanner(tempor, *tkn); //grab first token
	program_f(*tkn); //start the parser

	/* OLD WAY
	if (tkn->tokenID == 0)
		cout << "Okay!" << endl;	//TODO: Print Tree, kinda like driver4scanner
	else
		cerr << "Something went wrong" << endl;
	*/

	if(error == 0)
		cout << "Okay!" << endl;	//TODO: Print Tree, kinda like driver4scanner
	else {
		cerr << "Something went wrong" << endl;
	}

	tempor.close();
	
}

//<program> -> PROGRAM <var> <block>
void program_f(token & tkn){
	//Make sure PROGRAM keyword produced
	if (tkn.tokenID == PROGRAM_tk) {
		callScanner(tempor, tkn);	//consume token, get next
		var_f(tkn);
		block_f(tkn);
		return;
	}
	else {
		cout << "Keyword \"PROGRAM\" Not Found" << endl;
		error++;
		return;
	}

}

//<block> -> { <var> <stats> }
void block_f(token & tkn){
	if (tkn.tokenInstance == "{") {
		callScanner(tempor, tkn);	//consume token, get next
		var_f(tkn);
		stats_f(tkn);
		if (tkn.tokenInstance == "}") {
			callScanner(tempor, tkn);
			return;
		}
		else
			cout << "Missing } " << "got " << tkn.tokenInstance<< endl;
			error++;
	}
	else {
		cout << "Missing {" << endl;
		error++;
		return;
	}

}

//<var> -> empty | <type> Identifier <mvars> ;
void var_f(token & tkn){
	if (tkn.tokenID == INTEGER_tk) {
		type_f(tkn);
		if (tkn.tokenID == IDENT_tk) {
			callScanner(tempor, tkn);	//consume token, get next
			mvars_f(tkn);
			if (tkn.tokenInstance == ";") {
				callScanner(tempor, tkn);
				return;
			}
		}
	}
	return; //empty
}

//<type> -> INTEGER
void type_f(token & tkn) {
	if (tkn.tokenID == INTEGER_tk) {
		callScanner(tempor, tkn);	//consume token, get next
		return;
	}
	return; //empty
}

//<mvars> -> empty | , Identifier <mvars>
void mvars_f(token & tkn) {
	if (tkn.tokenInstance == ",") {
		callScanner(tempor, tkn);	//consume token, get next
		if (tkn.tokenID == IDENT_tk) {
			callScanner(tempor, tkn);	//consume token, get next	
			mvars_f(tkn);
		}
	}
	return;
}

//<expr> -> <T> + <expr> | <T> - <expr> | <T>
void expr_f(token & tkn){
	T_f(tkn);
	if(tkn.tokenInstance == "+") //<T> + <expr>
	{
	 callScanner(tempor, tkn);
	 expr_f(tkn);
	 return;
	}
	else if (tkn.tokenInstance == "-") //<T> - <expr>
	{
	 callScanner(tempor, tkn);
	 expr_f(tkn);
	 return;
	 }
	else
	 return;
}

//<T> -> <F> * <T> || <F> / <T> || <F>
void T_f(token & tkn){
	F_f(tkn);
	if(tkn.tokenInstance == "*"){
		callScanner(tempor, tkn);
		T_f(tkn);
	}
	else if (tkn.tokenInstance == "/"){
		callScanner(tempor, tkn);
		T_f(tkn);
	}
	else
	 return;

}

//<F> -> -<F> || <R>
void F_f(token & tkn) {
	if(tkn.tokenInstance == "-") // -<F>
	{
	 callScanner(tempor, tkn); //consume token
	 F_f(tkn);
	 return;
	}
	else
	 R_f(tkn);

	return;
}

//<R> -> ( <expr> ) || Identifier || Number
void R_f(token & tkn) {
	if (tkn.tokenInstance == "(") {
		callScanner(tempor, tkn);
		expr_f(tkn);
		if (tkn.tokenInstance == ")") {
			callScanner(tempor, tkn);
			return;
		}
		else {
			cout << "forgot ) " << endl;
			error++;
			return;
		}
	}
	else if (tkn.tokenID == IDENT_tk) {
		callScanner(tempor, tkn);
		return;
	}
	else if (tkn.tokenID == NUM_tk) {
		callScanner(tempor, tkn);
		return;
	}
	else {
		cout << "got this " << tkn.tokenInstance << " expected ( or ID or Num" << endl;
		error++;
		return; // if none
	}
}

//<stats> -> <stat> <mStat>
void stats_f(token & tkn) {
	stat_f(tkn);
	mStat_f(tkn);
	return;
}

//<mStat> -> empty | <stat> <mStat>
void mStat_f(token & tkn) {
	if (tkn.tokenID == SCAN_tk || tkn.tokenID == PRINT_tk || tkn.tokenInstance == "{" || tkn.tokenID == IF_tk || tkn.tokenID == LOOP_tk || tkn.tokenID == IDENT_tk) {
		stat_f(tkn);
		mStat_f(tkn);
	}
	else
		return;
}

//<stat> -> <in> | <out> | <block> | <if> | <loop> | <assign>
void stat_f(token & tkn) {
	//maybe need some look ahead
	if (tkn.tokenID == SCAN_tk)
		in_f(tkn);
	if (tkn.tokenID == PRINT_tk)
		out_f(tkn);
	if (tkn.tokenInstance == "{")
		block_f(tkn);
	if (tkn.tokenID == IF_tk)
		if_f(tkn);
	if (tkn.tokenID == LOOP_tk)
		loop_f(tkn);
	if (tkn.tokenID == IDENT_tk)
		assign_f(tkn);
	
	return;

}

//<in> -> SCAN Identifier ;
void in_f(token & tkn) {
	if (tkn.tokenID == SCAN_tk) {
		callScanner(tempor, tkn);
		if (tkn.tokenID == IDENT_tk) {
			callScanner(tempor, tkn);
			if (tkn.tokenInstance == ";") {
				callScanner(tempor, tkn);
				return;
			}
			else {
				cout << "Got " << tkn.tokenInstance << " Expected ;" << endl;
				error++;
				return;
			}
		}
		else {
			cout << "Got " << tokenNames[tkn.tokenID] << " Expected Identifier" << endl;
			error++;
			return;
		}
	}
	return;
}

//<out> -> PRINT <expr> ;
void out_f(token & tkn) {
	if (tkn.tokenID == PRINT_tk) {
		callScanner(tempor, tkn);
		expr_f(tkn);
		if (tkn.tokenInstance == ";") {
			callScanner(tempor, tkn);
			return;
		}
		else {
			cout << "Forgot ; " << endl;
			error++;
			return;
		}
	}
	return;
}

//<if> -> IF ( <expr> <RO> <expr> ) THEN <block>
void if_f(token & tkn) {
	if (tkn.tokenID == IF_tk) {
		callScanner(tempor, tkn);
		if (tkn.tokenInstance == "(") {
			callScanner(tempor, tkn);
			expr_f(tkn);
			RO_f(tkn);
			expr_f(tkn);
			if (tkn.tokenInstance == ")") {
				callScanner(tempor, tkn);
				if (tkn.tokenID == THEN_tk) {
					callScanner(tempor, tkn);
					block_f(tkn);
					return;
				}
				else {
					cout << "Forgot THEN" << endl;
					error++;
					return;
				}
			}
			else {
				cout << "Forgot ) " << endl; //add line number
				error++;
				return;
			}
		}
	}
	return;
}

//<loop> -> LOOP ( <expr> <RO> <expr> ) <block>
void loop_f(token & tkn) {
	if (tkn.tokenID == LOOP_tk) {
		callScanner(tempor, tkn);
		if (tkn.tokenInstance == "(") {
			callScanner(tempor, tkn);
			expr_f(tkn);
			RO_f(tkn);
			expr_f(tkn);
			if (tkn.tokenInstance == ")") {
				callScanner(tempor, tkn);
				block_f(tkn);
				return;
			}
			else {
				cout << "Forgot ) " << endl; //add line number
				error++;
				return;
			}
		}
	}

	return;
}

//<assign> -> Identifier = <expr> .
void assign_f(token & tkn) {
	if (tkn.tokenID == IDENT_tk) {
		callScanner(tempor, tkn);
		if (tkn.tokenInstance == "=") {
			callScanner(tempor, tkn);
			expr_f(tkn);
			if (tkn.tokenInstance == ".") {
				callScanner(tempor, tkn);
				return;
			}
			else {
				cout << "Forgot . " << endl;
				error++;
					return;
			}
		}
		else {
			cout << "Forgot = " << endl; //TODO: Add line Number
			error++;
			return;
		}
	}

	return;
}

//<RO> -> => | =< | == | > | < | !=
void RO_f(token & tkn) {
	if (tkn.tokenID == RELATIONAL_tk) {
		callScanner(tempor, tkn);
		return;
	}
	else {
		cout << "got " << tokenNames[tkn.tokenID] << " Expected Relational Operator" << endl;
		error++;
		return;
	}
	return;
}



//function used to weed out spaces and check for keywords
void callScanner(ifstream &stream, token & tkn) {
	
	scanner(tempor, tkn);

	if (tkn.tokenInstance == "") {
		while (tkn.tokenInstance == "")
			scanner(tempor, tkn);	//consume token, get next
	}

	if (tkn.tokenID == IDENT_tk) {	//check for keywords if token is identifier
		tkn.tokenID = keywords(tkn.tokenInstance);
	}

	if (tkn.tokenID == EOF_tk)
		tkn.tokenInstance = "EOF";

}


int keywords(string key) {
	int tokenID = 1;
		transform(key.begin(), key.end(), key.begin(), ::tolower); //changes strings to all lower case
	if (key == "begin")
		tokenID = BEGIN_tk;
	if (key == "end")
		tokenID = END_tk;
	if (key == "start")
		tokenID = START_tk;
	if (key == "finish")
		tokenID = FINISH_tk;
	if (key == "if")
		tokenID = IF_tk;
	if (key == "then")
		tokenID = THEN_tk;
	if (key == "loop")
		tokenID = LOOP_tk;
	if (key == "integer")
		tokenID = INTEGER_tk;
	if (key == "do")
		tokenID = DO_tk;
	if (key == "scan")
		tokenID = SCAN_tk;
	if (key == "print")
		tokenID = PRINT_tk;
	if (key == "void")
		tokenID = VOID_tk;
	if (key == "return")
		tokenID = RETURN_tk;
	if (key == "program")
		tokenID = PROGRAM_tk;
	if (key == "dummy")
		tokenID = DUMMY_tk;

return tokenID;
}

