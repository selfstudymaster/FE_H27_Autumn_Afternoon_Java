package main;

// 基本情報 H27 秋季 午後 Java 設問のコード
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

public class Main {

}

// [プログラム 1]
/*public*/ class BlockAccessor {
    private final Cache cache;
    private final BlockDevice device;

    public BlockAccessor(Cache.Policy policy) {
        device = BlockDevice.open();
        cache = Cache.createCache(policy);
    }

    public byte[] readBlock(int index) {
        byte[] blockData = cache.getCachedBlockData(index);
        if (blockData == null) {
            blockData = new byte[device.getBlockSize()];
            device.readBlock(index, blockData);
            cache.cacheBlcokData(index, blockData);
        }
        return blockData.clone();
    }
}

// [プログラム 2]
class BlockDevice {
    private final byte[][] blocks = new byte[100][512];

    static BlockDevice open() {
        return new BlockDevice();
    }

    int getBlockSize() {
        return  blocks[0].length; // return  /* (a) */.length;
    }

    void readBlock(int index, byte[] buffer) {
        byte[] block = blocks[index];
        System.arraycopy(block, 0, buffer, 0, block.length);
    }
}

// [プログラム 3]
/*public*/ abstract class Cache {
    public enum Policy {
        FIFO, LRU;
    }

    static Cache createCache(Policy policy) { // static  /* (b) */ createCache(Policy policy)
        switch (policy) {
            case FIFO:
                return new ListBasedCache.Fifo();
            case LRU:
                return new ListBasedCache.Lru();
        }
        throw new UnsupportedOperationException();
    }

    abstract byte[] getCachedBlockData(int index);
    abstract void cacheBlcokData(int index, byte[] blockData);
}

// [プログラム 4]
//import java.util.ArrayList; // 先頭に記述済み
//import java.util.List; // 先頭に記述済み

abstract class ListBasedCache extends Cache {
    final List<Entry> entries = new ArrayList<Entry>();

    private static final int CACHE_SIZE = 20;

    byte[] getCachedBlockData(int index) {
        for (Entry entry : entries) {
            if (entry.getIndex() ==  index) { // if (entry.getIndex() /* (c) */ index)
                hit(entry);
                return entry.getBlockData();
            }
        }
        return null;
    }

    void cacheBlcokData(int index, byte[] blockData) {
        if (entries.size() == CACHE_SIZE) { // if (/* (d) */)
            entries.remove(CACHE_SIZE - 1); // entries.remove(/* (e) */)
        }
        entries.add(0, new Entry(index, blockData));
    }

    abstract void hit(Entry entry);

    private static class  Entry {
        private final int index;
        private final byte[] blockData;

        private Entry(int index, byte[] blockData) {
            this.index = index;
            this.blockData = blockData;
        }

        int getIndex() {
            return index;
        }

        byte[] getBlockData() {
            return blockData;
        }

    }

    static class Fifo extends ListBasedCache { // extends /* (f) */
        void hit(Entry entry) {}
    }

    static class Lru extends ListBasedCache { // extends /* (f) */
        void hit(Entry entry) {
            entries.remove(entry); // entries.remove(/* (g) */);
            entries.add(0, entry);
        }
    }
}

