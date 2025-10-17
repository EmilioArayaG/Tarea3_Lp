JAVAC = javac
JAVA  = java

SOURCES := $(shell find . -name "*.java")

.PHONY: all build run clean zip

all: build

build:
	$(JAVAC) -Xlint:all $(SOURCES)

run: build
	$(JAVA) Main

clean:
	find . -name "*.class" -delete

zip:
	tar -czf Tarea3LP_Entregable.tar.gz Main.java Makefile \
		player/*.java objetos/*.java entorno/*.java
