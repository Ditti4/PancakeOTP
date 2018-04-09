package de.riditt.pancakeotp.services;

import java.util.List;

import de.riditt.pancakeotp.BasePresenter;
import de.riditt.pancakeotp.data.otpservice.OtpService;

public class ServicesContract {
    interface View {
        void setServices(List<OtpService> serviceList);
    }

    interface Presenter extends BasePresenter<View> {
        void loadServices();
    }
}
