#说明
#改了user@ip即可用,但需要先装好expect工具:sudo yum install tcl tk expect
#下面两个脚本都行, 第一个脚本只能登上去,第二个脚本可以登和执行命令,因为interact



#!/bin/sh
expect<<EOF
set timeout 30
spawn ssh yangyang.fyy@11.139.186.162
expect "password: "
send "Lenovoali88\r"
expect eof
EOF



#!/usr/bin/expect
set timeout 30
spawn ssh yangyang.fyy@11.139.186.162
expect "password: "
send "Lenovoali88\r"
interact