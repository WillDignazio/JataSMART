public class JataSMART
{
	/* One time initialize libatasmart */
	static
	{
		System.loadLibrary("jatasmart");
	}
	
	private class SkDisk
	{
		public long _skDiskAddr;
		
		private SkDisk()
		{
		}
	}

	private native SkDisk open(String path);
	private native void hello();

	public static void main(String[] args)
	{
		JataSMART jsmart;
		JataSMART.SkDisk disk;
		
		jsmart = new JataSMART();
		disk = jsmart.open("/dev/xvda");
	}
}
