#!/bin/bash

start=$(date +%s)
sleep 2
end=$(date +%s)
echo "cost seconds:"$(( $end - $start ))
