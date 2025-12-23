package rocksdb;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import utils.FileUtils;

import java.io.File;

/**
 * 目标：
 * 按key去重后，把所有数据scan出来，最后把文件全部删除
 *
 * 参考：
 * https://rocksdb.org.cn/doc/column-families.html
 * https://www.jianshu.com/p/a0088d7e9b97
 * https://www.jianshu.com/p/061927761027
 *
 *
 */
public class RocksDbTest {

    private static final String PATH = "/tmp/rocksdbrepo";
    public static void main(String[] args) {
        try (RocksDB rocksDB = RocksDB.open(PATH)) {
            rocksDB.put("key100001".getBytes(), "value100001".getBytes());
//            rocksDB.put("key1".getBytes(), "value11".getBytes());
//            rocksDB.put("key2".getBytes(), "value2".getBytes());
//            rocksDB.put("key3".getBytes(), "value3".getBytes());


//            get(rocksDB);

            rocksDB.delete("key5".getBytes());
            scanByIterator(rocksDB);

            rocksDB.deleteRange("0".getBytes(), "key100000".getBytes());
            scanByIterator(rocksDB);

            FileUtils.delFile(new File(PATH));

            rocksDB.close();
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    //test ok
    private static void get(RocksDB rocksDB) throws RocksDBException {
        byte[] value = rocksDB.get("key4".getBytes());
        System.out.println("value of key4 is: " + new String(value));
    }

    //core
    private static void scanByIterator(RocksDB rocksDB) {
        RocksIterator iterator = rocksDB.newIterator();
        iterator.seekToFirst();
        while (iterator.isValid()) {
            byte[] key = iterator.key();
            byte[] value = iterator.value();
            System.out.println(new String(key) + ": " + new String(value));
            iterator.next();
        }
        iterator.close();


    }


}
