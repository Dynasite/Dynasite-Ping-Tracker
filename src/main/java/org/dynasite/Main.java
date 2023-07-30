package org.dynasite;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Main {

    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        LOG.info("Starting Dynasite with arguments: " + Arrays.toString(args));
    }

}
