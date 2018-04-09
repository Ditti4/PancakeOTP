package de.riditt.pancakeotp.data.otpservice;

import android.net.Uri;

import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Mac;

import de.riditt.pancakeotp.data.otpservice.exception.InvalidTokenUriException;

public class OtpServiceBuilder {
    EServiceType serviceType;
    String issuer;
    String account;
    int passwordLength = 6;
    String secret;
    int period;
    int count;
    String algorithm = "HmacSHA1";

    public OtpServiceBuilder(Uri uri) throws InvalidTokenUriException {
        // Example for TOTP: otpauth://totp/ACME%20Co:john.doe@email.com?secret=HXDMVJECJJWSRB3HWIZR4IFUGFTMXBOZ&issuer=ACME%20Co&algorithm=SHA1&digits=6&period=30
        // The account name may optionally be preceded by the issuer name, separated by a colon, either literal or URL-encoded.
        // The account name may also be preceded by a space.
        switch (uri.getAuthority()) {
            case "totp":
                serviceType = EServiceType.TOTP;
                break;
            case "hotp":
                serviceType = EServiceType.HOTP;
                break;
            default:
                serviceType = EServiceType.UNKNOWN;
                break;
        }

        issuer = uri.getQueryParameter("issuer");

        String path = uri.getPath();
        if (path.isEmpty()) {
            throw new InvalidTokenUriException();
        }
        path = path.substring(1);
        int colonIndex = path.indexOf(':');
        String issuerInPath = colonIndex >= 0 ? path.substring(0, colonIndex) : "";
        account = colonIndex >= 0 ? path.substring(colonIndex + 1) : path;

        if(issuer.equals("Steam") || issuerInPath.equals("Steam")) {
            serviceType = EServiceType.STEAM;
        }

        String digitString = uri.getQueryParameter("digits");
        if (digitString != null && !digitString.isEmpty()) {
            try {
                passwordLength = Integer.valueOf(digitString);
            } catch (NumberFormatException ignored) {
                throw new InvalidTokenUriException();
            }
        }

        String counterString = uri.getQueryParameter("counter");
        if (counterString != null && !counterString.isEmpty()) {
            try {
                count = Integer.valueOf(counterString);
            } catch (NumberFormatException ignored) {
                throw new InvalidTokenUriException();
            }
        }

        secret = uri.getQueryParameter("secret");
        if (secret == null || secret.isEmpty()) {
            throw new InvalidTokenUriException();
        }

        String periodString = uri.getQueryParameter("period");
        if (periodString != null && !periodString.isEmpty()) {
            try {
                period = Integer.valueOf(periodString);
            } catch (NumberFormatException ignored) {
                throw new InvalidTokenUriException();
            }
        }

        String shortAlgorithm = uri.getQueryParameter("algorithm");
        try {
            Mac.getInstance("Hmac" + shortAlgorithm.toUpperCase(Locale.US));
        } catch (NoSuchAlgorithmException ignored) {
            throw new InvalidTokenUriException();
        }
        algorithm = "Hmac" + shortAlgorithm.toUpperCase(Locale.US);
    }

    public OtpServiceBuilder(EServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public OtpServiceBuilder setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public OtpServiceBuilder setAccount(String account) {
        this.account = account;
        return this;
    }

    public OtpServiceBuilder setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        return this;
    }

    public OtpServiceBuilder setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public OtpServiceBuilder setPeriod(int period) {
        this.period = period;
        return this;
    }

    public OtpServiceBuilder setCount(int count) {
        this.count = count;
        return this;
    }

    public OtpServiceBuilder setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public OtpService build() {
        switch (serviceType) {
            case HOTP:
                return new HotpService(issuer, account, passwordLength, secret, count);
            case TOTP:
                return new TotpService(issuer, account, passwordLength, secret, period, algorithm);
            case STEAM:
                return new SteamTotpService(issuer, account, passwordLength, secret, period, algorithm);
            case UNKNOWN:
            default:
                return null;
        }
    }
}
