package com.common.utils;

import java.security.SecureRandom;

import com.exception.handling.models.ValidationException;

public final class OTPUtils {

	/**
	 * Allowed characters:
	 * - Uppercase letters A–Z (excluding O, I)
	 * - Lowercase letters a–z (excluding l)
	 * - Digits 1–9 (excluding 0)
	 */
	private static final char[] OTP_CHARS = ("ABCDEFGHJKLMNPQRSTUVWXYZ" + "abcdefghijkmnopqrstuvwxyz" + "123456789")
			.toCharArray();

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private OTPUtils() {
	} // prevents instantiation

	/**
	 * Generates an alphanumeric OTP without confusing characters.
	 *
	 * @param length OTP length
	 * @return generated OTP
	 */
	public static String generateOtp(int length) {
		if (length <= 0) {
			throw new ValidationException("OTP length must be greater than zero");
		}
		StringBuilder otp = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			otp.append(OTP_CHARS[SECURE_RANDOM.nextInt(OTP_CHARS.length)]);
		}
		return otp.toString();
	}

}
