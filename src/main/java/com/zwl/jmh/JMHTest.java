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
 * url: https://mp.weixin.qq.com/s/5AERU6eyrrD6DTaDXi4AAg
 *
 * <p>BenchmarkMode，使用模式，默认是Mode.Throughput，表示吞吐量,其他参数还有AverageTime，表示每次执行时间，SampleTime表示采样时间，SingleShotTime表示只运行一次，用于测试冷启动消耗时间，All表示统计前面的所有指标
 *
 * <p>Warmup 配置预热次数，默认是每次运行1秒，运行10次，我们的例子是运行3次
 *
 * <p>Measurement 配置执行次数，本例是一次运行5秒，总共运行3次。在性能对比时候，采用默认1秒即可，如果我们用jvisualvm做性能监控，我们可以指定一个较长时间运行。
 *
 * <p>Threads 配置同时起多少个线程执行，默认值世 Runtime.getRuntime().availableProcessors()，本例启动1个线程同时执行
 *
 * <p>Fork，代表启动多个单独的进程分别测试每个方法，我们这里指定为每个方法启动一个进程。
 *
 * <p>OutputTimeUnit 统计结果的时间单元，这个例子TimeUnit.SECONDS，我们在运行后会看到输出结果是统计每秒的吞吐量
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
