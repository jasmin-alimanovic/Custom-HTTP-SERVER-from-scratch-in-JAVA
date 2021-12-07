
package com.jasmin.httpserver;

import com.jasmin.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;



public class HttpServer {


    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);


     public static void main(String [] args) {

         LOGGER.info("Server running on port " + 8080 + "...");
         LOGGER.info("Waiting for client ...");
         ServerListenerThread serverListenerThread = null;
         try {
             serverListenerThread = new ServerListenerThread(8080, "/root");
             serverListenerThread.start();
         } catch (IOException e) {
             e.printStackTrace();
         }

     }

}
