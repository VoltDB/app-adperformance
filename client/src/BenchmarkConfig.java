/* This file is part of VoltDB.
 * Copyright (C) 2008-2015 VoltDB Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with VoltDB.  If not, see <http://www.gnu.org/licenses/>.
 */
package client;

import org.voltdb.CLIConfig;

/**
 * Uses CLIConfig class to declaratively state command line options 
 * with defaults and validation.
 */
public class BenchmarkConfig extends CLIConfig {

    // STANDARD BENCHMARK OPTIONS
    @Option(desc = "Comma separated list of the form server[:port] to connect to.")
    String servers = "localhost";

    @Option(desc = "Volt user name")
    public String user = "";

    @Option(desc = "Volt password")
    public String password = "";

    @Option(desc = "Benchmark duration, in seconds.")
    int duration = 20;

    @Option(desc = "Interval for performance feedback, in seconds.")
    long displayinterval = 5;

    @Option(desc = "Warmup duration in seconds.")
    int warmup = 2;

    @Option(desc = "Maximum TPS rate for benchmark.")
    int ratelimit = 100000;

    @Option(desc = "Determine transaction rate dynamically based on latency.")
    boolean autotune = true;

    @Option(desc = "Server-side latency target for auto-tuning.")
    int latencytarget = 6;

    @Option(desc = "Filename to write raw summary statistics to.")
    String statsfile = "";

    // CUSTOM OPTIONS
    @Option(desc = "Number of Sites")
    int sites = 1000;

    @Option(desc = "Pages per Site")
     int pagespersite = 10;

    @Option(desc = "Number of Advertisers")
     int advertisers = 1000;

    @Option(desc = "Campaigns per Site")
     int campaignsperadvertiser = 10;

    @Option(desc = "Creatives per Campaign")
     int creativespercampaign = 10;

    public BenchmarkConfig() {
    }

    public static BenchmarkConfig getConfig(String classname, String[] args) {
        BenchmarkConfig config = new BenchmarkConfig();
        config.parse(classname, args);
        return config;
    }
    
    @Override
    public void validate() {
        if (duration <= 0) exitWithMessageAndUsage("duration must be > 0");
        if (warmup < 0) exitWithMessageAndUsage("warmup must be >= 0");
        if (displayinterval <= 0) exitWithMessageAndUsage("displayinterval must be > 0");
        if (ratelimit <= 0) exitWithMessageAndUsage("ratelimit must be > 0");
        if (latencytarget <= 0) exitWithMessageAndUsage("latencytarget must be > 0");
    }
}
