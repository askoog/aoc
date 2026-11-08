package se.askware.aoc2015.dec04;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		try {
			String password = "yzbqklnj";
			for (int i = 0; i < 10000000; i++) {

				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update((password + i).getBytes());
				byte[] digest = md.digest();
				//			String myHash = DatatypeConverter
				//					.printHexBinary(digest).toUpperCase();
				BigInteger bigInt = new BigInteger(1, digest);
				String hashtext = bigInt.toString(16);
				// Now we need to zero pad it if you actually want the full 32 chars.
				if (hashtext.length() <= 32 - 6) {
					System.out.println(hashtext);
					System.out.println(i);
					break;
				}
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

}
