# VoltDB Example App: Ad Performance#

Use Case
--------
This application simulates a high velocity stream of events (impressions, clickthroughs, conversions) that are enriched and ingested.  These events are randomly generated in the client, but represent a stream of events that would be received from web traffic.

The "TrackEvent" stored procedure processes these events.  It looks up the corresponding advertiser and campaign based on the creative ID which represents which ad was shown.  It also retrieves the corresponding web site and page based on the inventory ID from the event.  The timestamp and event type fields are converted to aid in aggregation, and all of this data is then inserted into the impression_data table.

Several views maintain real-time aggregations on this table to provide a minutely summary for each advertiser, plus drill-down reports grouped by campaign and creative to show detail-level metrics, costs and rates with real-time accuracy.

Code organization
-----------------
The code is divided into two projects:

- "db": the database project, which contains the schema, stored procedures and other configurations that are compiled into a catalog and run in a VoltDB database.  
- "client": a java client that loads a set of cards and then generates random card transactions a high velocity to simulate card activity.
- "web": a web dashboard client (static html page with dynamic content)

See below for instructions on running these applications.  For any questions, 
please contact fieldengineering@voltdb.com.

Pre-requisites
--------------
Before running these scripts you need to have VoltDB installed, and you should add the voltdb-$(VERSION)/bin directory to your PATH environment variable, for example:

    export PATH="$PATH:$HOME/voltdb-$(VERSION)/bin"


Instructions
------------

1. Start the database in the background

     ./start_db.sh
     
2. Run the client application

    ./run_client.sh

3. Open the web/adperformance.html page in a web browser to view the real-time dashboard

4. To stop the database and clean up temp files

    voltadmin shutdown
    ./clean.sh


Options
-------
You can control the following characteristics of the demo by editing the run_client.sh script to modify the parameters passed into the InvestmentBenchmark java application.

    --duration=120                (benchmark duration in seconds)
    --autotune=true               (true = ignore rate limit, run at max throughput until latency is impacted)
                                  (false = run at the specified rate limit)
    --ratelimit=20000             (when autotune=false, run up to this rate of requests/second)
    --sites=100                   (number of web sites)
    --pagespersite=10             (number of pages per web site)
    --advertisers=100             (number of advertisers)
    --campaignsperadvertiser=10   (number of campaigns per advertiser)
    --creativespercampaign=5      (number of creatives/banners per campaign)

Instructions for running on a cluster
-------------------------------------

Before running this demo on a cluster, make the following changes **on each server**:

1. In start_db.sh, change the voltdb "host" parameter by changing the HOST variable from localhost:
        
    HOST=localhost
    
to the actual name of the **first server** in your cluster:
    
    HOST=voltserver01
    
NOTE: all servers should have the same value for HOST, so they know where to connect to start the cluster.

2. In db/deployment.xml, change hostcount from 1 to 2 (or the number of servers):

    <cluster hostcount="1" sitesperhost="3" kfactor="0" />

4. To start the cluster, run the start script **on each server**:

    ./start_db.sh
    
5. The client can be run on just one server, but to make it connect to all the servers in the cluster, edit run_client.sh to change:

    SERVERS=localhost

to:

    SERVERS=voltserver01,voltserver02

then to run the client:

    ./run_client.sh

