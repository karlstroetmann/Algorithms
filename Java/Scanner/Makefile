Calculator.class: Calculator.java MyScanner.java
	javac -g Calculator.java

MyScanner.java: MyScanner.lex
	java -cp /usr/share/java/jlex.jar JLex.Main MyScanner.lex
	mv MyScanner.lex.java MyScanner.java

test:
	echo "(1 + 2^3)*(5 +3 **4 )" | java Calculator 

