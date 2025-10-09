JAVAC=javac
JAVA=java
SRC=$(shell find . -name "*.java")
BIN=.

.PHONY: all compile run clean pack

all: compile

compile:
	$(JAVAC) -Xlint:all -d $(BIN) $(SRC)

run: compile
	$(JAVA) Main

clean:
	find . -name "*.class" -delete

pack:
	tar -czf Tarea3LP_TU_NOMBRE_TU_RUT.tar.gz \
		Main.java README.md Makefile \
		player/*.java objetos/*.java entorno/*.java

