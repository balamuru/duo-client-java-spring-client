package com.vbg.auth.service;

import com.vbg.auth.data.AuthResponse;
import com.vbg.auth.data.CheckResponse;
import com.vbg.auth.data.PingResponse;
import com.vbg.auth.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DuoClient {

    public static SimpleDateFormat RFC_2822_DATE_FORMAT = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
    @Autowired
    RestTemplate restTemplate;


    @Value("${duo.sig_version}")
    int sigVersion;

    @Value("${duo.api_hostname}")
    String hostName;

    @Value("${duo.integration_key}")
    String integrationKey;

    @Value("${duo.secret_key}")
    String secretKey;

    private String baseURL;

    private static String signHMAC(String skey, String msg) {
        try {
            byte[] sig_bytes = Util.hmacSha1(skey.getBytes(), msg.getBytes());
            String sig = Util.bytes_to_hex(sig_bytes);
            return sig;
        } catch (Exception e) {
            return "";
        }
    }

    private static String canonRequest(HttpMethod method, String host, String uri, String date, int sig_version, Map<String, String> params)
            throws UnsupportedEncodingException {
        String canon = "";
        if (sig_version == 2) {
            canon += date + "\n";
        }
        canon += method.name() + "\n";
        canon += host.toLowerCase() + "\n";
        canon += uri + "\n";
        canon += createQueryString(params);

        return canon;
    }

    private static String createQueryString(Map<String, String> params)
            throws UnsupportedEncodingException {
        ArrayList<String> args = new ArrayList<String>();
        final ArrayList<String> keys = new ArrayList<>();

        if (params != null) {
            for (String key : params.keySet()) {
                keys.add(key);
            }
            Collections.sort(keys);
        }
        for (String key : keys) {
            String name = URLEncoder
                    .encode(key, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
            String value = URLEncoder
                    .encode(params.get(key), "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
            args.add(name + "=" + value);
        }

        return Util.join(args.toArray(), "&");
    }

    private static String formatDate(Date date) {
        // Could use ThreadLocal or a pool of format objects instead
        // depending on the needs of the application.
        synchronized (RFC_2822_DATE_FORMAT) {
            return RFC_2822_DATE_FORMAT.format(date);
        }
    }

    public PingResponse ping() throws Exception {
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//
//        headers.setDate(System.currentTimeMillis());
//        final HttpEntity<String> entity = new HttpEntity<>("headers", headers);
//
//        final ResponseEntity<PingResponse> resp = restTemplate.exchange(endpoint("/ping"), HttpMethod.GET, entity, PingResponse.class);
//        return resp.getBody();
        return exchange("/ping", HttpMethod.GET, null, PingResponse.class).getBody();
    }

    public CheckResponse check() throws Exception {
        return exchange("/check", HttpMethod.GET, null, CheckResponse.class).getBody();
    }

    public AuthResponse auth(
            final Map<String, String> parameters) throws Exception {
        return exchange("/auth", HttpMethod.POST, parameters, AuthResponse.class).getBody();
    }

    public <T> ResponseEntity<T> exchange(
            String path,
            HttpMethod httpMethod,
            final Map<String, String> parameters, Class<T> responseType) throws Exception {

        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }

        //sign request
        final String resource = resource(path);
        StringBuilder urlSb = new StringBuilder(url(resource));

        String body = ""; //placeholder
        if (parameters != null && !parameters.isEmpty()) {
            final String paramsStr = buildParameterString(parameters);
            if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT) {
                body = paramsStr.substring(1); //append the parms to the body (strip the leading ? char)

            } else if (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.DELETE) {
                urlSb.append(paramsStr); //append the parms to the url
            } else {
                //do nothing - the API docs don't specify what to do
                throw new UnsupportedOperationException(httpMethod.name());
            }
        }

        signRequest(resource, parameters, headers, httpMethod);
        final HttpEntity<String> entity = new HttpEntity<>(body, headers);
        final ResponseEntity<T> resp = restTemplate.exchange(
                urlSb.toString(),
                httpMethod,
                entity,
                responseType);
        return resp;
    }



    //    public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
//                                          HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
//
//        RequestCallback requestCallback = httpEntityCallback(requestEntity, responseType);
//        ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);
//        return execute(url, method, requestCallback, responseExtractor, uriVariables);
//    }
    private String buildParameterString(Map<String, String> parameters) {
        final ArrayList<String> keyList = sortedKeys(parameters);
        final UriComponentsBuilder paramsBuilder = UriComponentsBuilder.newInstance();

        for (String key : keyList) {
            paramsBuilder.queryParam(key, parameters.get(key));
        }

        return paramsBuilder.toUriString();
    }

    private ArrayList<String> sortedKeys(Map<String, String> params) {
        ArrayList<String> keyList = new ArrayList<>(params.keySet());
        Collections.sort(keyList);
        return keyList;
    }

    private void signRequest(String uri, Map<String, String> params, HttpHeaders headers, HttpMethod httpMethod) throws Exception {
        final Date now = new Date();
        String date = formatDate(now);
        if (sigVersion == 2) {
            headers.add("Date", date);
        }
        if (uri.endsWith("/ping")) {
            return; //no need for auth header for ping
        }
        String canon = canonRequest(httpMethod, hostName, uri, date, sigVersion, params);
        String sig = signHMAC(secretKey, canon);
        if (sig.startsWith("ERR")) {
            throw new Exception("Error: " + sig);
        }
        String auth = integrationKey + ":" + sig;
        String header = "Basic " + com.vbg.auth.util.Base64.encodeBytes(auth.getBytes());
        headers.add("Authorization", header);

    }

    private String endpoint(String path) {
        return url(resource(path));
    }

    private String url(String uri) {
        return baseURL() + uri;
    }

    private String resource(String path) {
        return "/auth/v" + sigVersion + path;
    }

    private String baseURL() {
        if (baseURL == null) {
            baseURL = "https://" + hostName;
        }
        return baseURL;
    }


}
