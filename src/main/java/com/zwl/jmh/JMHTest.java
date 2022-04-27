package com.zwl.jmh;

import cn.hutool.core.util.RandomUtil;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 描述：JMH基准测试
 *
 * @author zwl
 * @since 2022/4/27 11:28
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class JMHTest {

  @Benchmark
  public static void testStringKey() {
    // 优化前的代码
    Random random = new Random();
    for (int i = 0; i < 1000; i++) {
      int nextInt = random.nextInt();
    }
  }

  @Benchmark
  public static void testObjectKey() {
    for (int i = 0, j = 1000; i < j; i++) {
      int randomInt = RandomUtil.randomInt();
    }
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(JMHTest.class.getSimpleName()).build();
    new Runner(opt).run();
  }
}
