package de.riditt.pancakeotp.data.otpservice;

import java.util.Date;

public interface OtpService {
    // Methods used to build the service, store it in the database and display it to the user.
    String getIssuer();

    String getAccount();

    int getPasswordLength();

    String getSecret();

    int getCount();

    int getPeriod();

    String getAlgorithm();

    void setIssuer(String issuer);

    void setAccount(String account);

    void setPasswordLength(int passwordLength);

    void setSecret(String secret);

    void setCount(int count);

    void setPeriod(int period);

    // Methods used to perform operations on the generator or related to the generator
    String getCurrentToken(Date timestamp);

    String getNextToken(Date timestamp);

    void incrementCount();
}
