package vn.conyeu.restclient;

import org.springframework.http.client.ClientHttpRequestFactory;
import java.time.Duration;

public interface DelegateRequestFactory extends ClientHttpRequestFactory {

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     */
     void setConnectTimeout(Integer connectTimeout);

    /**
     * Set the underlying connect timeout in milliseconds.
     * A value of 0 specifies an infinite timeout.
     */
     void setConnectTimeout(Duration connectTimeout);

    /**
     * Set the underlying read timeout in milliseconds.
     */
     void setReadTimeout(Integer readTimeout);

    /**
     * Set the underlying read timeout as {@code Duration}.
     * <p>Default is 10 seconds.
     */
     void setReadTimeout(Duration readTimeout);
}