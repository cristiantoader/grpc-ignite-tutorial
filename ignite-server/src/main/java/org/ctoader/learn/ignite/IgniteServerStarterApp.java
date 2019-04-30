package org.ctoader.learn.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

public class IgniteServerStarterApp {

    public static void main(String[] args) {
        Ignite ignite = Ignition.start("ignite-server/src/main/resources/ignite-config.xml");
    }
}
