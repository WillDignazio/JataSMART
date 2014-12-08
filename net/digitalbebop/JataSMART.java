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

	private native SkDisk open(String path);
}
