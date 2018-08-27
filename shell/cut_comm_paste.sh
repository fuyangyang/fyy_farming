#说明：下面示例用到了cut(切行)，paste(合并两个文件，按行追加），comm（两个排好序文件取交差集）
#问题：函数内报错，是整个shell进程退出，还是只是函数退出，函数的返回值等待研究

#!/bin/bash
#判断参数，打usage

file1=$1
file2=$2

//生成格式：queryid_shopid sparsefeature...
uniform() {
    echo "uniform file:"$1

    cat $1 | cut -d "_" -f 1-4 > $1-head
    cat $1 | cut -d " " -f 4- > $1-tail
    paste -d'|' $1-head $1-tail | sort > $1-sort
    rm -f $1-head
    rm -f $1-tail

    wcl $1
    wcl $1-sort
    echo "uniform success!"
}

wcl() {
    echo "cat $1 | wc -l"
    cat $1 | wc -l
}

diff() {
    echo "diff $1 $2"

    comm -12 $1 $2 > libsvm-comm  #两文件都有
    comm -23 $1 $2 > libsvm-left  #$1有，$2无
    comm -23 $2 $1 > libsvm-right #$1无，$2有

    wcl $1
    wcl $2
    wcl libsvm-comm
    wcl libsvm-left
    wcl libsvm-right
    echo "diff success!"
}

uniform $file1
uniform $file2
diff $file1-sort $file2-sort