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

		/**
		 * Gets the size of the opened disk.
		 * @return long Size of disk in bytes.
		 */
		public native long getSize();

		/**
		 * Returns whether SMART commands are available for
		 * this device.
		 * @return boolean Whether SMART is available.
		 */
		public native boolean isSMARTAvailable();

		/**
		 * Returns whether the disk is currently in sleep mode.
		 * @return boolean True if device is awake, False if asleep
		 */
		public native boolean checkSleepMode();

		/**
		 * Returns whether the drive has identify capability.
		 * @return boolean True if it does, False if it does not
		 */
		public native boolean isIdentifyAvailable();

		/**
		 * Get the general SMART status of the disk.
		 * @return boolean True for good, False for bad.
		 */
		public native boolean getSMARTStatus();

		/**
		 * Get a 64 bit timestamp for power on time of the device.
		 * @return long Power on time of device.
		 */
		public native long getPowerOn();

		/**
		 * Get number of times device has been power cycled.
		 * @return long Power cycle count of device.
		 */
		public native long getPowerCycle();

		/**
		 * Get number of known bad sectors on device.
		 * @return long Number of known bad sectors
		 */
		public native long getBadSectors();

		/**
		 * Get the temperature of the device in Kelvins.
		 * @return long Temperature of device in Kelvins
		 */
		public native long getTemperature();
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
