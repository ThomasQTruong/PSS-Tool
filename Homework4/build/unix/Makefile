# By Thomas Truong
# Quickly compiles every .java file in the src folder,
# and places them into the bin folder.

# All the functions to run.
all: Main Test

# Main function, creates the main program.
Main: $(wildcard ../../src/main/*.java)
	javac -d ../../bin/ $^ -cp "../../lib/*:../../bin/"

# Test function, creates the test files.
Test: $(wildcard ../../src/test/*.java)
	javac -d ../../bin/test/ $^ -cp "../../lib/*:../../bin/"

# Cleans up every class file in bin.
clean:
	rm -rf ../../bin/*
