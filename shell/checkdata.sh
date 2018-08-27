#!/bin/bash


#功能：对两个文件按行进行核对
#方案：先把两个文件的格式调成一致，然后分别排序，最后用comm工具得到单边数据。
#技术：paste是对两个文件进行按行拼接；comm是对两个排好序的文件进行交差集的人操作。

clean() {
    rm $1-sort
    rm $2-sort
    rm libsvm-comm
    rm libsvm-left
    rm libsvm-right
}

#生成统一格式：queryid_shopid sparsefeature...
uniform() {
    echo "uniforming file:"$1

    cat $1 | cut -d "_" -f 1-4 > $1-head
    cat $1 | cut -d " " -f 2- > $1-tail
    paste -d'|' $1-head $1-tail | sort > $1-sort
    rm -f $1-head
    rm -f $1-tail

    wcl $1
    wcl $1-sort
    echo "uniformed successfully!"
}

wcl() {
    echo "cat $1 | wc -l"
    cat $1 | wc -l
}

diff() {
    echo "diffing $1 $2"

    comm -12 $1 $2 > libsvm-comm  #两文件都有
    comm -23 $1 $2 > libsvm-left  #$1有，$2无
    comm -23 $2 $1 > libsvm-right #$1无，$2有

    wcl $1
    wcl $2
    wcl libsvm-comm
    wcl libsvm-left
    wcl libsvm-right
    echo "diffed successfully!"
}

clean
uniform $1
uniform $2
diff $1-sort $2-sort