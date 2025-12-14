package com.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.exception.handling.models.ValidationException;

class OTPUtilsTest {

    private static final Set<Character> ALLOWED_CHARS = Set.of(
            // Uppercase (excluding O, I)
            'A','B','C','D','E','F','G','H','J','K','L','M','N',
            'P','Q','R','S','T','U','V','W','X','Y','Z',

            // Lowercase (excluding l)
            'a','b','c','d','e','f','g','h','i','j','k','m','n',
            'o','p','q','r','s','t','u','v','w','x','y','z',

            // Digits (excluding 0)
            '1','2','3','4','5','6','7','8','9'
    );

    @Test
    @DisplayName("Should generate OTP with requested length")
    void shouldGenerateOtpWithCorrectLength() {
        int length = 6;
        String otp = OTPUtils.generateOtp(length);

        assertNotNull(otp);
        assertEquals(length, otp.length());
    }

    @Test
    @DisplayName("OTP should contain only allowed characters")
    void otpShouldContainOnlyAllowedCharacters() {
        String otp = OTPUtils.generateOtp(20);

        for (char c : otp.toCharArray()) {
            assertTrue(ALLOWED_CHARS.contains(c),
                    "Invalid character found in OTP: " + c);
        }
    }

    @Test
    @DisplayName("OTP should not contain confusing characters")
    void otpShouldNotContainConfusingCharacters() {
        String otp = OTPUtils.generateOtp(50);

        assertFalse(otp.contains("O"));
        assertFalse(otp.contains("I"));
        assertFalse(otp.contains("l"));
        assertFalse(otp.contains("0"));
    }

    @Test
    @DisplayName("Should generate different OTPs on multiple calls")
    void shouldGenerateDifferentOtps() {
        String otp1 = OTPUtils.generateOtp(8);
        String otp2 = OTPUtils.generateOtp(8);

        assertNotEquals(otp1, otp2,
                "Two consecutive OTPs should not be equal");
    }

    @Test
    @DisplayName("Should throw ValidationException for zero length")
    void shouldThrowExceptionForZeroLength() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> OTPUtils.generateOtp(0)
        );

        assertEquals("OTP length must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw ValidationException for negative length")
    void shouldThrowExceptionForNegativeLength() {
        assertThrows(
                ValidationException.class,
                () -> OTPUtils.generateOtp(-5)
        );
    }
}