package com.pds.chambitas.util.services;

import java.io.IOException;
import java.net.SocketException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {

    private int reintentos;

    public RetryInterceptor() {}

    public RetryInterceptor(int reintentos) {
        this.reintentos = reintentos;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = null;
        Boolean responseOK = false;
        int tryCount = 0;

        while (!responseOK && tryCount < this.reintentos) {
            try {
                System.out.println("Intentando hacer el request al server. Intento: "+tryCount);
                response = chain.proceed(request);
                responseOK = response.isSuccessful() | (response.code() >= 400 && response.code() <= 499);
            } catch (Exception e) {
                System.out.println("Request "+tryCount+" sin exito");
            } finally {
                tryCount++;
            }
        }

        if (response == null) {
            throw new SocketException("Error no especificado en la red");
        }

        return response;
    }

}
