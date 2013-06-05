package com.fileshare.communication;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;

/**
 * @author Jan Paw
 *         Date: 6/ 4/13
 */
public class Client {
    private static final Logger logger = LogManager.getLogger(Server.class.getName());

    public static void run(String[] args) {
        System.setProperty("java.security.policy", Paths.get("").toAbsolutePath().toString()
                + "\\src\\main\\resources\\client.policy");
    }

    public Client() {
    }
}
