void scanner(ifstream &stream, token & tkn){
	string str;
 if (stream.is_open()){
	char c;
	while (stream.get(c)){
		int p = stream.peek();	//if the next char is EOF, save what u got and shut SHIT DOWN
		if (p == EOF) {
			if (stream.eof()) {
			tkn.tokenID = 0; // end of file
			words[count1] = c;
			str = words;
			tkn.tokenInstance = str;
			break;
		}
			else
				cout << "I have no idea"; // error
		}
	 if(c != ' ' && c != '\n'){
	  words[count1] = c;
	  count1 ++;
	  }
	 else{
		 str = words;
		 tkn.tokenInstance = str;
	  count1 = 0;
	  memset(words, 0, sizeof words);	//Sets the char array back to 0!
	  break;
	  }
	}	//End of Reading File
 }	//End of If Open
 else
	cout << "Could Not Read File!" << endl;
}