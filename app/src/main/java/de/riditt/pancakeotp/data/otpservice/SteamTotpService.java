package de.riditt.pancakeotp.data.otpservice;

public class SteamTotpService extends TotpService {
    public static char[] STEAMCHARS = new char[] {
            '2', '3', '4', '5', '6', '7', '8', '9', 'B', 'C',
            'D', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q',
            'R', 'T', 'V', 'W', 'X', 'Y'};

    public SteamTotpService(String issuer, String account, int passwordLength, String secret, int period, String algorithm) {
        super(issuer, account, passwordLength, secret, period, algorithm);
    }

    @Override
    public String formatPassword(int password) {
        /* This logic is courtesy of WinAuth, an awesome application for Windows which can handle
         * all of your authentication needs (and even handles Steam confirmations!).
         * The logic below, specifically, is taken from
         * https://github.com/winauth/winauth/blob/master/Authenticator/SteamAuthenticator.cs#L751
         */
        StringBuilder formatted = new StringBuilder();
        for(int i = 0; i < getPasswordLength(); i++) {
            formatted.append(STEAMCHARS[password % STEAMCHARS.length]);
            password /= STEAMCHARS.length;
        }
        return formatted.toString();
    }
}
