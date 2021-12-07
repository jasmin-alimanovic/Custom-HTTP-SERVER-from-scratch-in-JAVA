package com.jasmin.httpserver.core;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

public class HttpConnectionWorkerThread extends Thread{

    private Socket socket;
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);


    public HttpConnectionWorkerThread(Socket socket) throws IOException {

        this.socket = socket;

    }

    @Override
    public void run() {
        OutputStream out = null;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();

            /*int _byte;
            if(in != null) {
                while ((_byte = in.read()) >= 0) {
                    System.out.print((char)_byte);
                }
            }
            */
            String html = "<!DOCTYPE html>" +
                    "<html lang=\"en\">" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                    "    <title>HTTP Server</title>" +
                    "</head>" +
                    "<body>" +
                    "    <h1>Hello from custom http server</h1>" +
                    "</body>" +
                    "</html>";
            final String CRLF = "\r\n";
            /*Map<String, Integer> data = new HashMap<String, Integer>();
            data.put("test1", 1);
            data.put("test2", 2);
            data.put("test3", 3);
            data.put("test4", 4);*/

            /*String data = "{test4: 4," +
                    "test2: 2," +
                    "test3: 3," +
                    "test1: 1}";*/
            /*HashMap<String, String> data = new HashMap();
            data.put("test1", "1");
            data.put("test2", "2");
            data.put("test3", "3");
            data.put("test4", "4");
            data.put("test5", "5");*/
            //Object json = new JSONObject(data);
            String response = "HTTP/1.1 200 OK" + CRLF +
                    "Content-Length: " + html.getBytes().length + CRLF +
                    CRLF +
                    html +
                    CRLF + CRLF;

            out.write(response.getBytes());


            LOGGER.info(" * Connection Processing Finished.");
        }catch (IOException e){
            LOGGER.error("Problem with communication ", e);
        }finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
