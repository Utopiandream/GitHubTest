//main.cpp
#include <iostream>
#include <fstream>
#include <string>
#include "token.h"
#include "driver4scanner.h"

using namespace std;
//TODO ADD COMMENT REMOVER!!

int main (int argc, char *argv[]){
string lines;
string sub;

if(argv[1] == NULL)		//If user enters nothing after run
{
 cout << "Enter Input: ";
 getline(cin, lines);
	 ofstream ofs;
	 ofs.open("tempor.lan");
	 ofs << lines;
	 ofs.close();
 }	//End of Enter Input
else if(argv[2] == NULL)				//If user enters name of data file
{
#pragma warning(push)
#pragma warning(disable: 4996) //4996 for _CRT_SECURE_NO_WARNINGS equivalent
	ifstream is(strcat(argv[1], ".lan"));
#pragma warning(pop)
	if(is.is_open())
	{
	ofstream ofs;
	ofs.open("tempor.lan");
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
ofs.open("tempor.lan");
while(argv[i])
	{
 	 ofs << argv[i]<< " ";
	 i++;
	}
 ofs.close();
}

DRIVER4SCANNER();
return 0;
} //end method main

