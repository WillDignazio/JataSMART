#
# Copyright (C) 2014 Will Dignazio
#
.PHONY: all clean

CC=clang
CFLAGS=-O2 -Wall -Werror -pedantic -I/usr/lib/jvm/java-7-openjdk-amd64/include/ -I/home/will/System/include -L/home/will/System/lib -latasmart -shared -fPIC

all: net/digitalbebop/JataSMART.h
	$(CC) $(CFLAGS) -o libjatasmart.so jatasmart.c

net/digitalbebop/JataSMART.h: net/digitalbebop/JataSMART.java
	javac net/digitalbebop/JataSMART.java
	javah -stubs net.digitalbebop.JataSMART
	jar -cvf JataSMART.jar net/digitalbebop/JataSMART.class net/digitalbebop/JataSMART$$SkDisk.class

clean:
	rm -f *.h
	rm -f net/digitalbebop/*.class
	rm -f libjatasmart.so
	rm -f *.o
