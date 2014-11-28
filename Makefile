#
# Copyright (C) 2014 Will Dignazio
#
.PHONY: all clean

CC=clang
CFLAGS=-O2 -I/usr/lib/jvm/java-7-openjdk-amd64/include/ -I/home/will/System/include -L/home/will/System/lib -latasmart -shared -fPIC

all: JataSMART.h
	$(CC) $(CFLAGS) -o libjatasmart.so jatasmart.c

JataSMART.h: JataSMART.java
	javac JataSMART.java
	javah -stubs JataSMART

clean:
	rm -f *.h
	rm -f *.class
	rm -f libjatasmart.so
	rm -f *.o
