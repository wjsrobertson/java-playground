#!/bin/bash

CP='conf'
for FILE in lib/*
do
  CP="$CP:$FILE"
done

JMX_PORT=7000
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=$JMX_PORT"

java $JAVA_OPTS -cp $CP net.xylophones.midifinder.Application
