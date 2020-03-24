package server.alm.services;

import global.L;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Properties;

class DefectsConnMgr {
    protected static final String QC_PROPERTIES = "qc.properties";
    protected static URL urlAuth;
    protected static URL urlSession;
    protected static URL urlLogout;
    protected InputStream resourceFile = null;
    protected Properties props;
    private HttpURLConnection conn;


    DefectsConnMgr() throws IOException {
        props = new Properties();

        try {
            resourceFile = getClass().getClassLoader().getResourceAsStream(QC_PROPERTIES);
            if (resourceFile != null) {
                props.load(resourceFile);
            } else {
                L.LOGGER.info("QC config not found. File: " + QC_PROPERTIES);
                return;
            }

        } catch (Exception e) {
            L.LOGGER.info("Exception: " + e);
        } finally {
            if (resourceFile != null)
                resourceFile.close();
        }

        // setup QC URLs

        // http://t2qcprod2:8080/qcbin/authentication-point/authenticate
        urlAuth = new URL(
                "http" + (props.getProperty("secure").equals("Y") ? "s" : "") + "://" +
                        props.getProperty("host") + ":" + props.getProperty("port") + "/" +
                        props.getProperty("app") + "/" +
                        props.getProperty("auth")
        );

        // http://t2qcprod2:8080/qcbin/rest/site-session
        urlSession = new URL(
                "http" + (props.getProperty("secure").equals("Y") ? "s" : "") + "://" +
                        props.getProperty("host") + ":" + props.getProperty("port") + "/" +
                        props.getProperty("app") + "/" +
                        props.getProperty("session")
        );

        // http://t2qcprod2:8080/qcbin/authentication-point/logout
        urlLogout = new URL(
                "http" + (props.getProperty("secure").equals("Y") ? "s" : "") + "://" +
                        props.getProperty("host") + ":" + props.getProperty("port") + "/" +
                        props.getProperty("app") + "/" +
                        props.getProperty("auth") + "/logout"
        );
    }

    void establishConnection() throws IOException {
        try {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);

            String authEncoded = Base64.encodeBase64String((System.getProperty("tm.user") + ":" + System.getProperty("tm.pass")).getBytes());

            conn = (HttpURLConnection) urlAuth.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + authEncoded);

            conn.getHeaderFields();

            conn = (HttpURLConnection) urlSession.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/xml");
            conn.getHeaderFields();

        } catch (Exception e) {
            L.LOGGER.error("Error establishing connection to QC: " + e);
            conn.disconnect();
        }
    }

    JSONObject getJsonResponse(String sUrl) {
        BufferedReader reader = null;
        URL url = null;
        StringBuilder stringBuilder = new StringBuilder();
        String sResponseLine = "";
        InputStream is = null;
        int returnCode;

        try {
            try {
                url = new URL(sUrl);
            } catch (MalformedURLException e) {
                L.LOGGER.error("Incorrect URL: " + e);
            }
            if (url != null) {
                conn = (HttpURLConnection) url.openConnection();
            }

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            try {
                is = conn.getInputStream();
            } catch (IOException ioe) {
                if (conn instanceof HttpURLConnection) {
                    returnCode = conn.getResponseCode();
                    if (returnCode != 200) {
                        L.LOGGER.error("Server returned code: " + returnCode);
                    }
                }
            }
            if (is != null) {
                reader = new BufferedReader(new InputStreamReader(is));
            }

            if (reader != null) {
                while ((sResponseLine = reader.readLine()) != null)
                    stringBuilder.append(sResponseLine);
            }

            if (reader != null) {
                reader.close();
            }

            sResponseLine = stringBuilder.toString();
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause() instanceof UnknownHostException)
                L.LOGGER.error("Unknown host: " + e);
            else
                L.LOGGER.error("No response: " + e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                L.LOGGER.error("Error retrieving data: " + e);
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
        if (isJSONValid(sResponseLine))
            return new JSONObject((sResponseLine));
        else {
            return null; // throw
        }
    }

    public void logOut() {
        try {
            conn = (HttpURLConnection) urlLogout.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");
            conn.disconnect();
        } catch (Exception e) {
            L.LOGGER.error("Error logging out from QC: " + e);
        }
    }

    private boolean isJSONValid(String sJSON) {
        try {
            new JSONObject(sJSON);
        } catch (JSONException ex) {
            try {
                new JSONArray(sJSON);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
