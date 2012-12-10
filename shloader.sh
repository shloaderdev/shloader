#!/bin/bash

while true
    do
        java -cp "jar/*" com.shloader.Shloader

        OUT=$?
        if [ $OUT -ne 0 ];then
           echo "Shloader failed. Exit code: $OUT"
           #exit 0
        fi

        echo "sleeping..."
        sleep 900 #30 minutes
    done
