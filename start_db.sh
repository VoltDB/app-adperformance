#!/usr/bin/env bash

. ./compile.sh

HOST=localhost
LICENSE=$VOLTDB_HOME/voltdb/license.xml
DEPLOY=deployment.xml
COMMAND=""

# use 4.0 syntax or pre-4.0 syntax as appropriate
if [ -f $VOLTDB_HOME/bin/voltdb3 ]; then
    # voltdb3 is only present as of 4.0, so this is 4.0
    COMMAND="voltdb create -H $HOST -d $DEPLOY -l $LICENSE ${CATALOG_NAME}.jar"
else
    # must be pre-4.0
    COMMAND="voltdb create catalog ${CATALOG_NAME}.jar host $HOST deployment $DEPLOY license $LICENSE"
fi

# start database in background
nohup $COMMAND > log/nohup.log 2>&1 &


echo    
echo "-----------------------------------------------------------------"
echo "Starting VoltDB using commands:"
echo "    cd db"
echo "    nohup $COMMAND > log/nohup.log 2>&1 &"
echo "    tail -f log/nohup.log"
echo 
echo "The database will be available when you see:"
echo "Server completed initialization."
echo
echo "Use Ctrl-C to stop tailing the log file.  VoltDB will keep running."
echo 
echo "To stop the database, Ctrl-C and then use the command:"
echo "    voltadmin shutdown"
echo "-----------------------------------------------------------------"
echo

# tail the console output, so you can see when the server starts
tail -f log/nohup.log

