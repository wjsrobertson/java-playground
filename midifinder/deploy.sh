#!/bin/bash

mvn clean package
scp target/midifinder-1.0.0-bin.tar.gz will@baby:/opt/xylophones
ssh will@baby 'cd /opt/xylophones; chmod u+rw midifinder-1.0.0-bin.tar.gz; tar -zxvf /opt/xylophones/midifinder-1.0.0-bin.tar.gz; rm midifinder-1.0.0-bin.tar.gz; chmod a+x midifinder-1.0.0/midifinder.sh'
