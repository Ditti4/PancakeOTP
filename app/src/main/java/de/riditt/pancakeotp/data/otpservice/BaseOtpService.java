package de.riditt.pancakeotp.data.otpservice;

import java.security.Key;
import java.util.Locale;

import javax.crypto.spec.SecretKeySpec;

import de.riditt.pancakeotp.util.Base32String;

public abstract class BaseOtpService implements OtpService {
    private String issuer;
    private String account;
    private int passwordLength = 6;
    private String secret;
    private String algorithm;

    protected BaseOtpService(String issuer, String account, int passwordLength, String secret, String algorithm) {
        this.issuer = issuer;
        this.account = account;
        this.passwordLength = passwordLength;
        this.secret = secret;
        this.algorithm = algorithm;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public int getPasswordLength() {
        return passwordLength;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    public Key getSecretAsKey() {
        try {
            return new SecretKeySpec(Base32String.decode(secret), algorithm);
        } catch (Base32String.DecodingException e) {
            return new SecretKeySpec(new byte[]{}, algorithm);
        }
    }

    public String formatPassword(int password) {
        return String.format(Locale.US, "%0" + getPasswordLength() + "d", password);
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
