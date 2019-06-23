package com.tasteland.app.thetasteland.model.web.response;

import com.tasteland.app.thetasteland.utils.HttpMessage;
import lombok.Data;

@Data
public class HttpResponse {

    private HttpMessage message;
    private Object object;

    public HttpResponse(HttpMessage message, Object object) {
        this(message);
        this.object = object;
    }

    public HttpResponse(HttpMessage message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseStatus{" +
                "message='" + message + '\'' +
                ", object=" + object +
                '}';
    }
}
