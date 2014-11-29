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


		public native boolean isSMARTAvailable();
		public native long getSize();
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
