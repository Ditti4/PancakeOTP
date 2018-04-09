package de.riditt.pancakeotp.data.otpservice;

import com.eatthepath.otp.HmacOneTimePasswordGenerator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class HotpService extends BaseOtpService {
    private int count;
    private HmacOneTimePasswordGenerator generator;

    public HotpService(String issuer, String account, int passwordLength, String secret, int count) {
        super(issuer, account, passwordLength, secret, HmacOneTimePasswordGenerator.HOTP_HMAC_ALGORITHM);
        this.count = count;
        try {
            generator = new HmacOneTimePasswordGenerator(getPasswordLength());
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getPeriod() {
        return 0;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void setPeriod(int period) { /* no-op */ }

    @Override
    public String getCurrentToken(Date timestamp) {
        try {
            return formatPassword(generator.generateOneTimePassword(getSecretAsKey(), count));
        } catch (InvalidKeyException ignored) {
            return "";
        }
    }

    @Override
    public String getNextToken(Date timestamp) {
        try {
            return formatPassword(generator.generateOneTimePassword(getSecretAsKey(), count + 1));
        } catch (InvalidKeyException ignored) {
            return "";
        }
    }

    @Override
    public void incrementCount() {
        count++;
    }
}
