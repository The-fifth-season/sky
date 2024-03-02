package com.sky.utils;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BloomFilterUtils {
    private static final int EXPECTED_INSERTIONS = 1000;
    private static BitMapBloomFilter bloomFilter = new BitMapBloomFilter(EXPECTED_INSERTIONS);

    public static boolean isNotInBloom(String key , Long id) {
        return !bloomFilter.contains(key+id);
    }

    public static void addToBloom(String key , List<Long> ids) {
        ids.forEach(id -> bloomFilter.add(key+id));
        //ids.forEach(id -> addToBloom(key, id));
    }

    //通过key和id添加布隆过滤器
    public static void addToBloom(String key , Long id) {
        bloomFilter.add(key+id);
    }

    public void queryFromDatabase(String key) {
        // 模拟查询数据库的逻辑
        System.out.println("Querying from database...");
    }

    //布隆过滤器重新初始化
    public static void reinitBloomFilter(Integer EXPECTED_INSERTIONS){
        bloomFilter = new BitMapBloomFilter(EXPECTED_INSERTIONS);
    }

}
