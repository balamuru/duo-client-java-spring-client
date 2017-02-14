package com.vbg.auth.data;

public class CheckResponse {

    private Response response;
    private String stat;

    public CheckResponse() {
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "CheckResponse{" +
                "response=" + response +
                ", stat='" + stat + '\'' +
                '}';
    }

    public static class Response {
        private String time;

        public  Response() {
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "time='" + time + '\'' +
                    '}';
        }
    }

}