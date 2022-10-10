package stopwatch;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        testStatic();
    }

    private static void testPin() throws InterruptedException {
        Stopwatch watch1 = Stopwatch.get("testWatch");
        Stopwatch watch2 = Stopwatch.get("testWatch2");
        System.out.println("停表开始: " + watch1.start());
        System.out.println("停表开始: " + watch2.start());
        Thread.sleep(123);
        System.out.println("停表记录: " + watch2.pin());
        System.out.println("间隔时间: " + watch2.getLastInterval(TimeUnit.MILLISECONDS));
        Thread.sleep((long) (Math.random() * 1000));
        System.out.println("停表记录: " + watch2.pin("开始吃饭"));
        Thread.sleep((long) (Math.random() * 1000));
        System.out.println("停表记录: " + watch2.pin("吃完"));
        System.out.println("吃饭时间: " + watch2.getPinInterval("开始吃饭", "吃完", TimeUnit.MILLISECONDS));
        System.out.println("吃饭时间: " + watch2.getPinInterval("吃完", TimeUnit.MILLISECONDS));
        System.out.println("pin 0  : " + watch2.getPinInterval(0, TimeUnit.MILLISECONDS));
        System.out.println("pin 1  : " + watch2.getPinInterval(1, TimeUnit.MILLISECONDS));
        System.out.println("pin 2  : " + watch2.getPinInterval(2, TimeUnit.MILLISECONDS));
        System.out.println("停表结束: " + watch2.stop());
        System.out.println("打点名称：" + Arrays.toString(watch2.getPinNameList().toArray()));
        System.out.println("打点时间：" + Arrays.toString(watch1.getTimeline(TimeUnit.MILLISECONDS).toArray()));
        System.out.println("打点时间：" + Arrays.toString(watch2.getIntervalLine(TimeUnit.MILLISECONDS).toArray()));
        watch1.restart();
    }

    private static void testProcess() throws InterruptedException {
        Stopwatch watch = Stopwatch.start("停表");
        Thread.sleep(200);
        watch.pin();
        watch.pause();
        Thread.sleep(200);
        watch.keep();
        watch.pin();
        watch.pause();
        Thread.sleep(200);
        watch.pause();
        Thread.sleep(200);
        watch.keep();
        Thread.sleep(200);
        watch.keep();
        watch.pin();
        watch.stop();
        System.out.println(watch.getIntervalLine(TimeUnit.MILLISECONDS));
        System.out.println(watch.getTimeline(TimeUnit.SECONDS));
    }

    private static void testStatic() {
        Stopwatch w1 = Stopwatch.start("1");
        Stopwatch w2 = Stopwatch.start("2");
        w1.pin();
        w2.pin();
        w1.stop();
        w2.stop();
        System.out.println(Stopwatch.get("1").getTimeline());
        System.out.println(Stopwatch.get("2").getTimeline());
        Stopwatch.remove("1");
        System.out.println(Stopwatch.get("1").getTimeline());

    }
}
