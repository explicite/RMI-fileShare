package com.fileshare.configuration;

import java.nio.file.Paths;

/**
 * @author Jan Paw
 *         Date: 6/10/13
 */
public enum Policy {
    JAR("\\policy\\no.policy"), TEST("\\src\\policy\\no.policy\\");

    private final String url;

    private Policy(String s) {
        this.url = Paths.get("").toAbsolutePath().toString() + s;
    }

    public String url() {
        return url;
    }
}
