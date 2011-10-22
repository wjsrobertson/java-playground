#/bin/bash

CP='conf'
for FILE in lib/*
do
  CP="$CP:$FILE"
done

java $JAVA_OPTS -cp $CP net.xylophones.midifinder.Application