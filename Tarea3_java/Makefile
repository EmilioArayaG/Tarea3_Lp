JAVAC=javac
JAVA=java
SRC=$(shell find . -name "*.java")
BIN=.

all: compile

compile:
	$(JAVAC) -Xlint:all -d $(BIN) $(SRC)

run: compile
	$(JAVA) Main

clean:
	find . -name "*.class" -delete

pack:
	tar -czf Tarea3LP_TUROL.tar.gz \
		Main.java README.md Makefile \
		player/*.java objetos/*.java entorno/*.java
