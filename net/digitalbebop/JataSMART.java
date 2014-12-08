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

	/**
	 * Open the given device, and produce an SkDisk object.
	 * This creates a specialized SkDisk object using the underlying
	 * JNI wrapper code, giving an interface to the libatasmart
	 * library methods.
	 *
	 * The path given in this is expected to be a block device that
	 * supports SMART.
	 * 
	 * @param path Path to disk device
	 * @return SkDisk SkDisk device interface object
	 */
	public native SkDisk open(String path);
}
