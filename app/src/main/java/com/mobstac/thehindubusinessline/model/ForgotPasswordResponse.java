package com.mobstac.thehindubusinessline.model;

/**
 * Created by 9920 on 10/4/2017.
 */

public class ForgotPasswordResponse {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


    public class Response {
        private int httpCode;
        private String Message;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public void setHttpCode(int httpCode) {
            this.httpCode = httpCode;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }
    }

}
