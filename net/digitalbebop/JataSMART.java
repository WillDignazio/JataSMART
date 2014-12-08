/**
 * Copyright © 2014 Will Dignazio
 *
 * Java implementation file for the JNI wrapper of libatasmart.
 * This provides an interface to produce SkDisk objects that are
 * synonymous with the SkDisk structures within the atasmart library.
 *
 * The underlying implementation code is compiled from a .c file(s),
 * the primary of which being jatasmart.c.
 *
 * @author Will Dignazio <wdignazio@gmail.com>
 */

package net.digitalbebop;

public class JataSMART
{
	/* One time initialize libatasmart */
	static
	{
		System.loadLibrary("jatasmart");
	}
	
	private class SkDisk
	{
		private long _skDiskAddr;
		
		private SkDisk()
		{
		}

		public native long getSize();
		public native boolean isSMARTAvailable();
		public native boolean isSleepMode();
		public native boolean isIdentifyAvailabe();
		public native boolean getSMARTStatus();
		public native long getPowerOn();
		public native long getPowerCycle();
		public native long getBadSectors();
		public native long getTemperature();

		public native String toString();
	}

	public native SkDisk open(String path);
}
