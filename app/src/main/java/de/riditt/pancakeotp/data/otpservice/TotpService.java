package de.riditt.pancakeotp.data.otpservice;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TotpService extends BaseOtpService {
    private int period;
    private TimeBasedOneTimePasswordGenerator generator;

    public TotpService(String issuer, String account, int passwordLength, String secret, int period, String algorithm) {
        super(issuer, account, passwordLength, secret, algorithm);
        this.period = period;
        try {
            generator = new TimeBasedOneTimePasswordGenerator(period, TimeUnit.SECONDS, passwordLength, algorithm);
        } catch (NoSuchAlgorithmException ignored) {
        }
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public int getPeriod() {
        return period;
    }

    @Override
    public void setCount(int count) {
        /* no-op */
    }

    @Override
    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public String getCurrentToken(Date timestamp) {
        try {
            return formatPassword(generator.generateOneTimePassword(getSecretAsKey(), timestamp));
        } catch (InvalidKeyException ignored) {
            return "";
        }
    }

    @Override
    public String getNextToken(Date timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.SECOND, period);
        return getCurrentToken(cal.getTime());
    }

    @Override
    public void incrementCount() {
        /* no-op */
    }
}
