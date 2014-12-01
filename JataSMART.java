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
		public native boolean isIdentifyAvailable();
		public native boolean getSMARTStatus();
		public native long getPowerOn();
		public native long getPowerCycle();
		public native long getTemperature();

		public native String toString();
	}

	private native SkDisk open(String path);

	public static void main(String[] args)
	{
		JataSMART jsmart;
		JataSMART.SkDisk disk;
		
		jsmart = new JataSMART();
		disk = jsmart.open("/dev/xvda");
		System.out.println(disk.isSMARTAvailable());
		System.out.println(disk.getSize());
	}
}
