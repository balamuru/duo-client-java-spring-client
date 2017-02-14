package com.vbg.auth.data;

import com.sun.deploy.net.HttpResponse;
import org.springframework.http.HttpStatus;

public class PingResponse
{
    private Response response;

    private String stat;

    public PingResponse(){}

    public Response getResponse ()
    {
        return response;
    }

    public void setResponse (Response response)
    {
        this.response = response;
    }

    public String getStat ()
    {
        return stat;
    }

    public void setStat (String stat)
    {
        this.stat = stat;
    }

    @Override
    public String toString() {
        return "PingResponse{" +
                "response=" + response +
                ", stat='" + stat + '\'' +
                '}';
    }

    public static class Response
    {
        private long time;

        public Response(){}
        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "time=" + time +
                    '}';
        }
    }
}
