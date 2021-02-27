package edu.uark.registerapp.commands.employees.helpers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

public class EmployeeHelper {
	public static String padEmployeeId(final int employeeId) {
		final String employeeIdAsString = Integer.toString(employeeId);

		return ((employeeIdAsString.length() < EMPLOYEE_ID_MAXIMUM_LENGTH)
				? StringUtils.leftPad(employeeIdAsString, EMPLOYEE_ID_MAXIMUM_LENGTH, "0")
				: employeeIdAsString);
	}

	public static byte[] hashPassword(final String password) {

		// TODO: Hash the password using a MessageDigest. An example can be found at
		// http://tutorials.jenkov.com/java-cryptography/messagedigest.html
		byte[] bPassword;
		bPassword = password.getBytes();
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(bPassword);
			return messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return new byte[0];
		//in the original code
	}

	private static final int EMPLOYEE_ID_MAXIMUM_LENGTH = 5;
}
