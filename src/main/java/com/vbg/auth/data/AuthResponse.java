package com.vbg.auth.data;

public class AuthResponse
{
    private Response response;

    private String stat;

    public AuthResponse() {
    }

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
        return "AuthResponse{" +
                "response=" + response +
                ", stat='" + stat + '\'' +
                '}';
    }

    public static class Response
    {
        private String result;

        private String status;

        private String status_msg;

        public Response() {
        }

        public String getResult ()
        {
            return result;
        }

        public void setResult (String result)
        {
            this.result = result;
        }

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public String getStatus_msg ()
        {
            return status_msg;
        }

        public void setStatus_msg (String status_msg)
        {
            this.status_msg = status_msg;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result='" + result + '\'' +
                    ", status='" + status + '\'' +
                    ", status_msg='" + status_msg + '\'' +
                    '}';
        }
    }
}

