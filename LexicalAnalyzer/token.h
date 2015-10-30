//token.h

#ifndef TOKEN_H
#define	TOKEN_H

#include <string>
using namespace std;

struct token{
    int tokenID;
    string tokenInstance;
    int lineNum;
    
};
typedef token *tkn;
#endif	/* TOKEN_H */

