package de.riditt.pancakeotp;

public interface BasePresenter<T> {
    void attachView(T view);
    void detachView();
}
