package com.sky.BloomFilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import com.sky.utils.BloomFilterUtils;

public class SetMealBloom extends BloomFilterUtils {
    private static final int EXPECTED_INSERTIONS = 1000;
    private static BitMapBloomFilter bloomFilter = new BitMapBloomFilter(EXPECTED_INSERTIONS);


}
