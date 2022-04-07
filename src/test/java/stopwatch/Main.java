package stopwatch;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/4/7 9:34
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Stopwatch watch = Stopwatch.get("testWatch");
        System.out.println("停表开始: " + watch.start());
        Thread.sleep((long) (Math.random() * 1000));
        System.out.println("停表记录: " + watch.pin());
        System.out.println("间隔时间: " + watch.getLastInterval());
        Thread.sleep((long) (Math.random() * 1000));
        System.out.println("停表记录: " + watch.pin("开始吃饭"));
        Thread.sleep((long) (Math.random() * 1000));
        System.out.println("停表记录: " + watch.pin("吃完"));
        System.out.println("吃饭时间: " + watch.getPinInterval("开始吃饭", "吃完"));
        System.out.println("停表结束: " + watch.stop());
    }
}
