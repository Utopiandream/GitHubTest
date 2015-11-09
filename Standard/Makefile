CCC = g++ -g

testScanner : main.o driver4scanner.o scanner.o
	$(CCC) -o testScanner main.o driver4scanner.o scanner.o
driver4scanner.o : driver4scanner.cpp scanner.h
	$(CCC) -c driver4scanner.cpp
scanner.o : scanner.cpp scanner.h
	$(CCC) -c scanner.cpp
main.o : main.cpp driver4scanner.h scanner.h token.h
	$(CCC) -c main.cpp

clean : 
	$rm *.o testScanner
