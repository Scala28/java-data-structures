/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Fork(value = 2)
@Measurement(iterations = 5, time = 15, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 15, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class Map_Benchmark {
    @Param({"100", "5000", "100000"})
    private int N;
    private Map map = new ArrayMap();
    private SortedMap sortedMap = new ArraySortedMap();
    private Object key;
    private static List<Integer> generateRandomList(int size){
        return new Random().ints(size).boxed()
                .collect(Collectors.toList());
    }
    public static List<Integer> generateUniqueRandomList(int size, int min, int max) {
        if (size > (max - min + 1)) {
            throw new IllegalArgumentException("Size must be less than or equal to the range of unique values.");
        }

        Random random = new Random();
        java.util.Set<Integer> uniqueRandomSet = new HashSet<>();

        while (uniqueRandomSet.size() < size) {
            int randomInt = random.nextInt((max - min) + 1) + min;
            uniqueRandomSet.add(randomInt);
        }

        return new ArrayList<>(uniqueRandomSet);
    }
    @Setup(Level.Trial)
    public void setUpList(){
        //map = new ArrayMap();
        //sortedMap = new ArraySortedMap();
        List<Integer> list = generateRandomList(N);
        for(int i=0; i<N; i++){
            map.put(i, list.get(i));
            sortedMap.put(i, list.get(i));
        }
    }
    @Setup(Level.Iteration)
    public void setUpKey(){
        key = new Random().nextInt(N);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object arrayMap() {
        return map.get(key);
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object arraySortedMap(){
        return sortedMap.get(key);
    }

    public static void main(String[] args) throws RunnerException{
        Options opt = new OptionsBuilder()
                .include(Map_Benchmark.class.getSimpleName())
                .forks(2)
                .build();
        new Runner(opt).run();
    }

}
