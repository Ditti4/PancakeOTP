package de.riditt.pancakeotp.data.generator;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;

/**
 * This class serves as an extension for the amazing java-otp lib this app uses. It adds the ability
 * to return the password generated by {@link #generateOneTimePassword(Key, long)} without having it
 * being run through the modulo operation the original method performs. This way, the calculated
 * one-time password can then be converted to a proper Steam one-time password (which uses numbers
 * and letters). This technically violates the standard but there's nothing we can do about that,
 * that's Valve's fault.
 */
public class SteamOneTimePasswordGenerator extends TimeBasedOneTimePasswordGenerator {
    public SteamOneTimePasswordGenerator() throws NoSuchAlgorithmException {
        super();
    }

    public SteamOneTimePasswordGenerator(final long timeStep, final TimeUnit timeStepUnit) throws NoSuchAlgorithmException {
        super(timeStep, timeStepUnit);
    }

    public SteamOneTimePasswordGenerator(final long timeStep, final TimeUnit timeStepUnit, final int passwordLength) throws NoSuchAlgorithmException {
        super(timeStep, timeStepUnit, passwordLength);
    }

    public SteamOneTimePasswordGenerator(final long timeStep, final TimeUnit timeStepUnit, final int passwordLength, final String algorithm) throws NoSuchAlgorithmException {
        super(timeStep, timeStepUnit, passwordLength, algorithm);
    }

    public int generateOneTimePassword(final Key key, final long counter) throws InvalidKeyException {
        final Mac mac;

        try {
            mac = Mac.getInstance(this.getAlgorithm());
            mac.init(key);
        } catch (final NoSuchAlgorithmException e) {
            // This should never happen since we verify that the algorithm is legit in the constructor.
            throw new RuntimeException(e);
        }

        final ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, counter);

        final byte[] hmac = mac.doFinal(buffer.array());
        final int offset = hmac[hmac.length - 1] & 0x0f;

        for (int i = 0; i < 4; i++) {
            // Note that we're re-using the first four bytes of the buffer here; we just ignore the latter four from
            // here on out.
            buffer.put(i, hmac[i + offset]);
        }

        final int hotp = buffer.getInt(0) & 0x7fffffff;

        return hotp;
    }
}