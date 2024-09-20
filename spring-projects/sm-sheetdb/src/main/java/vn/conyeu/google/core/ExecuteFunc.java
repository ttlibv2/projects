package vn.conyeu.google.core;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import lombok.extern.slf4j.Slf4j;
import vn.conyeu.commons.utils.MapperHelper;

import java.io.IOException;

@FunctionalInterface
public interface ExecuteFunc<T> {
    /**
     * Runs this operation.
     */
    AbstractGoogleClientRequest<T> get() throws IOException;

    static <T> T simple(ExecuteFunc<T> request) {
        try{

            AbstractGoogleClientRequest<T>  requestNew = request.get();
           // System.out.println(MapperHelper.serializeToString(requestNew));
            return requestNew.execute();
        }
        catch (IOException exp) {
            throw new GoogleException(exp.getMessage());
        }
    }
}