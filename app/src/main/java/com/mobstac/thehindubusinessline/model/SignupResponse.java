package com.mobstac.thehindubusinessline.model;

/**
 * Created by 9920 on 10/4/2017.
 */

public class SignupResponse {

    private SingUpDetail response;

    public SingUpDetail getSingUpDetail() {
        return response;
    }

    public void setSingUpDetail(SingUpDetail singUpDetail) {
        this.response = singUpDetail;
    }


    public class SingUpDetail {
        private int httpCode;
        private String message;
        private String name;
        private boolean verifiedstatus;

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
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean getVerifiedstatus() {
            return verifiedstatus;
        }

        public void setVerifiedstatus(boolean verifiedstatus) {
            this.verifiedstatus = verifiedstatus;
        }
    }

}
