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

@Fork(value = 2)
@Measurement(iterations = 5, time = 15, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 15, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 3, timeUnit = TimeUnit.MINUTES)
@State(Scope.Benchmark)
public class Set_Benchmark {
    @Param({"100", "10000", "25000", "50000", "75000", "100000", "150000"})
    private int N;
    private Set set_1 = new ArraySet();
    private Set set_2 = new ArraySet();
    private SortedSet sortedSet_1 = new ArraySortedSet();
    private SortedSet sortedSet_2 = new ArraySortedSet();
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
    public void setUp(){
        List<Integer> list_1 = generateUniqueRandomList(N, 0, 2*N);
        List<Integer> list_2 = generateUniqueRandomList(N, 0, 2*N);
        set_1.makeEmpty();
        set_2.makeEmpty();
        sortedSet_1.makeEmpty();
        sortedSet_2.makeEmpty();
        for (Integer i: list_1){
            set_1.add(i);
            sortedSet_1.add(i);
        }
        for (Integer i: list_2){
            set_2.add(i);
            sortedSet_2.add(i);
        }
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Set arraySet_union(){
        return ArraySet.union(set_1, set_2);
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Set arraySet_intersection(){
        return ArraySet.intersection(set_1, set_2);
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public Set arraySet_subtraction(){
        return ArraySet.subtraction(set_1, set_2);
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public SortedSet arraySortedSet_union(){
        return ArraySortedSet.union(sortedSet_1, sortedSet_2);
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public SortedSet arraySortedSet_intersection(){
        return ArraySortedSet.intersection(sortedSet_1, sortedSet_2);
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public SortedSet arraySortedSet_subtraction(){
        return ArraySortedSet.subtraction(sortedSet_1, sortedSet_2);
    }
    public static void main(String[] args) throws RunnerException{
        Options opt = new OptionsBuilder()
                .include(Set_Benchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
