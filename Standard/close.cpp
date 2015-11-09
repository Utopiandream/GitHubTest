// reading a text file
#include <iostream>
#include <fstream>
using namespace std;

int main (int argc, char *argv[]) {
char words[200][200];
int count1 = 0;
int count2 = 0;

if(argv[1] == NULL)
	{
	char str[256];
	cout << "Enter Input: ";
	cin.get (str, 256); 
	return 1;
	}

if (argv[2] == NULL)
{
ifstream is(strcat (argv[1], ".dat"));
	if(is.is_open())
	{
	 char c;
	 while( is.get(c))
	 {
		 if(c != ' ' && c != '\n')
		 {
		 words[count2][count1] = c;		
		 count1 ++;	
		  }
	 	 else
		 {
		  count1 = 0;
		  count2++;
		  }
	  }							// reading in file and storing in words

	for (int i = 0; i < count2; i ++)
	{
	 int j = 0;
	 while(words[i][j])
	  {
	  cout << words[i][j];
	  j++;
	  }
	 cout << " ";
	 }							// end of displaying file
cout << endl;	

is.close();
}								// end of ifopen
else
 {
	cout <<  "Could Not Open File. " << endl;
	}
} 								// end of if user enters file name

else
{								// If user enters data from keyboard!
	int i = 1;
	cout << "Will use as input: " << endl;
	while (argv[i]){
	cout <<  argv[i] << " ";
	i++;	
	} 
	cout << endl;
 }

return 0;
}
