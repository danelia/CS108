import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import sun.security.jca.GetInstance;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();

	/*
	 * Given a byte[] array, produces a hex String, such as "234a6f". with 2
	 * chars for each byte in the array. (provided code)
	 */
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff; // remove higher bits, sign
			if (val < 16)
				buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	/*
	 * Given a string of hex byte values such as "24a26f", creates a byte[]
	 * array of those values, one byte value -128..127 for each 2 chars.
	 * (provided code)
	 */
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
		}
		return result;
	}

	// possible test values:
	// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3

	static CountDownLatch latch;
	private String hash;
	private int maxLength;

	public class Worker extends Thread {
		private int start;
		private int length;
		private String res;

		public Worker(int start, int length) {
			this.start = start;
			this.length = length;
			res = "";
		}

		@Override
		public void run() {
			for(int i = start; i < start + length; i++){
				char curr = CHARS[i];
				helper("" + curr, maxLength - 1);
			}
			if(res != "")
				System.out.println(res);
			
			latch.countDown();
		}
		
		private void helper(String curr, int length){
			if(hash.equals(Cracker.hashCode(curr))){
				res = curr;
				return;
			}else if(length < 1){
				return;
			}else{
				for(char ch : CHARS)
					helper(curr + ch, length - 1);
			}
		}
	}

	public Cracker(String hash, int maxLength, int numThreads) {
		this.hash = hash;
		this.maxLength = maxLength;

		int lenForThreads = CHARS.length / numThreads;
		int left = CHARS.length % numThreads;

		int start = 0, len;

		for (int i = 0; i < numThreads; i++) {
			len = lenForThreads + (left > 0 ? 1 : 0);
			
			Worker curr = new Worker(start, len);
			curr.start();

			start += len;
			left --;
		}

	}

	public static String hashCode(String string) {
		MessageDigest dig = null;
		try {
			dig = MessageDigest.getInstance("SHA");
			dig.update(string.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return hexToString(dig.digest());
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("No input");
			System.exit(0);
		} else if (args.length < 3) {
			System.out.println(hashCode(args[0]));
		} else {
			latch = new CountDownLatch(Integer.parseInt(args[2]));

			new Cracker(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));

			try {
				latch.await();
			} catch (InterruptedException Ignored) { }
			System.out.println("All done.");
		}
	}

}
