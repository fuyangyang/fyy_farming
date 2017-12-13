#说明
#改了user@ip即可用,但需要先装好expect工具:sudo yum install tcl tk expect




#!/bin/sh
expect<<EOF
set timeout 30
spawn ssh yangyang.fyy@11.139.186.162
expect "password: "
send "Lenovoali88\r"
expect eof
EOF