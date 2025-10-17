JAVAC = javac
JAVA  = java

SOURCES := $(shell find . -name "*.java")

.PHONY: all build run clean

all: build

build:
	$(JAVAC) -Xlint:all $(SOURCES)
run: build
	$(JAVA) Main
clean:
	find . -name "*.class" -delete
