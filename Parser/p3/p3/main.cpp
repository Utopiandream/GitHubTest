/*main.cpp
Dmst3b
P1 Lexical Analyzer
CS4280
*/

#include <iostream>
#include <fstream>
#include <string>
#include "token.h"
#include "parser.h"

using namespace std;

int main(int argc, char *argv[]) {
	string lines;
	string sub;

	if (argv[1] == NULL)		//If user enters nothing after run
	{
		cout << "Enter Input: ";
		getline(cin, lines);
		ofstream ofs;
		ofs.open("tempor.lan");
		ofs << lines;
		ofs.close();
	}	//End of Enter Input
	else if (argv[2] == NULL)				//If user enters name of data file
	{
#pragma warning(push)
#pragma warning(disable: 4996) //4996 for _CRT_SECURE_NO_WARNINGS equivalent
		ifstream is(strcat(argv[1], ".lan"));
#pragma warning(pop)
		if (is.is_open())
		{
			ofstream ofs;
			ofs.open("tempor.lan");
			while (getline(is, lines)) {
				for (unsigned int i = 0; i < lines.length(); i++) {
					if (lines.at(i) == '#') {
						i = lines.length(); //if comment found, end loop
					}
					else
						ofs << lines.at(i);
				}
			}
			ofs.close();
			is.close();
		}
		else
		{
			cout << "Invalid File Name." << endl;
			return 0;
		}
	}
	else					//If user enters data from keyboard
	{
		int i = 1;
		ofstream ofs;
		ofs.open("tempor.lan");
		while (argv[i])
		{
			ofs << argv[i] << " ";
			i++;
		}
		ofs.close();
	}

	parser();
	return 0;
} //end method main

