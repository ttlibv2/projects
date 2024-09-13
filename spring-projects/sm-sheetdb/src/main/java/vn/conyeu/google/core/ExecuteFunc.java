package vn.conyeu.google.core;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;

import java.io.IOException;

@FunctionalInterface
public interface ExecuteFunc<T> {
    /**
     * Runs this operation.
     */
    AbstractGoogleClientRequest<T> get() throws IOException;

    static <T> T simple(ExecuteFunc<T> request) {
        try{return request.get().execute();}
        catch (IOException exp) {
            throw new GoogleException(exp.getMessage());
        }
    }
}