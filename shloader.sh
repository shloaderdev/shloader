#!/bin/bash

while true
    do
        java -cp "target/shloader-1.0-SNAPSHOT.jar:target/lib/*" com.shloader.Shloader

        OUT=$?
        if [ $OUT -ne 0 ];then
           echo "Shloader failed. Exit code: $OUT"
           #exit 0
        fi

        echo "sleeping..."
        sleep 900
    done
