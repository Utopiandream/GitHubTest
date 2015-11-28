//token.h
#ifndef TOKEN_H
#define TOKEN_H

#include <string>
using namespace std;

struct token {
	int tokenID;
	string tokenInstance;
	int lineNum;
	token *lookAhead;
};
typedef token *tkn;
#endif
