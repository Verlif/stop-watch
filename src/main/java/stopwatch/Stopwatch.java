package stopwatch;

import stopwatch.exception.WrongProcessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 * @version 1.0
 */
public class Stopwatch {

    private static final Map<String, Stopwatch> WATCH_MAP;

    static {
        WATCH_MAP = new HashMap<>();
    }

    private final String name;

    private final ArrayList<String> pinNameList;

    /**
     * pin时间线
     */
    private final ArrayList<Long> timeline;

    /**
     * 停表状态
     */
    private int process;

    /**
     * 累计暂停时间
     */
    private long cumulative;

    /**
     * 临时时间
     */
    private long temp;

    public Stopwatch() {
        this.name = Thread.currentThread().getName();
        this.pinNameList = new ArrayList<>();
        this.timeline = new ArrayList<>();

        process = Process.READY;
    }

    public Stopwatch(String name) {
        this.name = name;
        this.pinNameList = new ArrayList<>();
        this.timeline = new ArrayList<>();

        process = Process.READY;
    }

    /**
     * 获取停表名称
     *
     * @return 停表名称
     */
    public String getName() {
        return name;
    }

    /**
     * 开始停表
     */
    public synchronized long start() {
        if (isReady()) {
            temp = System.nanoTime();
            cumulative = 0;
            process = Process.RUNNING;
            return pin();
        } else {
            throw new WrongProcessException("Only ready stopwatch can start!You can use reset or restart to clear records, then you can start.");
        }
    }

    /**
     * 记录时间点
     */
    public synchronized long pin() {
        return pin(null);
    }

    /**
     * 记录时间点并标记名称
     */
    public synchronized long pin(String pinName) {
        long time = -1;
        if (isRunning()) {
            time = System.nanoTime() - cumulative;
        } else if (isPause()) {
            time = temp - cumulative;
        }
        if (time > 0) {
            pinNameList.add(pinName);
            timeline.add(time);
            return time;
        } else {
            throw new WrongProcessException("Only running stopwatch can pin!");
        }
    }

    /**
     * 停表暂停
     */
    public synchronized void pause() {
        // 仅在计时中生效
        if (isRunning()) {
            temp = System.nanoTime();
            process = Process.PAUSE;
        }
    }

    /**
     * 停表继续
     */
    public synchronized void keep() {
        // 仅在暂停时继续
        if (isPause()) {
            cumulative += System.nanoTime() - temp;
            process = Process.RUNNING;
        }
    }

    /**
     * 停止停表
     */
    public synchronized long stop() {
        long time = pin();
        process = Process.STOP;
        return time;
    }

    /**
     * 将停表重新启动，会清空记录线
     */
    public synchronized long restart() {
        timeline.clear();
        pinNameList.clear();
        process = Process.READY;
        return start();
    }

    /**
     * 将停表重置到就绪状态，并清空记录线
     */
    public synchronized void reset() {
        timeline.clear();
        pinNameList.clear();
        process = Process.READY;
        temp = 0;
        cumulative = 0;
    }

    /**
     * 获取pin时间
     *
     * @param pinName pin名
     * @return pin名对应的时间；当没有此pin名时，返回-1；
     */
    public long getPin(String pinName) {
        int index = pinNameList.indexOf(pinName);
        if (index > -1) {
            return timeline.get(index);
        } else {
            return -1;
        }
    }

    /**
     * 获取pin时间
     *
     * @param index pin序号
     * @return pin序号对应的时间；当没有此pin序号时，返回-1；
     */
    public long getPin(int index) {
        if (timeline.size() < index) {
            return -1;
        }
        return timeline.get(index);
    }

    /**
     * 获取最后一次记录的时间
     *
     * @return 最后一次记录的时间
     */
    public long getLast() {
        if (timeline.size() > 0) {
            return timeline.get(timeline.size() - 1);
        } else {
            throw new WrongProcessException("No record can be return, You can start this to get a new record.");
        }
    }

    /**
     * 获取停表开始的时间
     *
     * @return 开始时间
     */
    public long getStartTime() {
        if (timeline.size() > 0) {
            return timeline.get(0);
        } else {
            throw new WrongProcessException("No record can be return, You can start this to get a new record.");
        }
    }

    /**
     * 获取前两次记录的间隔
     *
     * @return 前两次记录的间隔
     */
    public long getLastInterval() {
        int size = timeline.size();
        if (size > 1) {
            return timeline.get(size - 1) - timeline.get(size - 2);
        } else {
            throw new WrongProcessException("No more record can be computed, You can pin or stop this to get a result.");
        }
    }

    /**
     * 获取前两次记录的间隔
     *
     * @return 前两次记录的间隔
     */
    public double getLastInterval(TimeUnit unit) {
        long i = getLastInterval();
        return turnToUnit(i, unit);
    }

    /**
     * 获取上一次记录到此刻的间隔
     *
     * @return 上一次记录到此刻的间隔
     */
    public long getLastToNow() {
        int size = timeline.size();
        if (size > 0) {
            return System.currentTimeMillis() - timeline.get(size - 1);
        } else {
            throw new WrongProcessException("No more record can be computed, You can start this to get a new record.");
        }
    }

    /**
     * 获取上一次记录到此刻的间隔
     *
     * @return 上一次记录到此刻的间隔
     */
    public double getLastToNow(TimeUnit unit) {
        long i = getLastToNow();
        return turnToUnit(i, unit);
    }

    /**
     * 获取记录开始到此刻的间隔
     *
     * @return 停表开始到此刻的间隔
     */
    public long getStartToNow() {
        int size = timeline.size();
        if (size > 0) {
            return System.currentTimeMillis() - timeline.get(0);
        } else {
            throw new WrongProcessException("Only running stopwatch has the whole interval.");
        }
    }

    /**
     * 获取记录开始到此刻的间隔
     *
     * @return 停表开始到此刻的间隔
     */
    public double getStartToNow(TimeUnit unit) {
        long i = getStartToNow();
        return turnToUnit(i, unit);
    }

    /**
     * 获取记录开始到最后一次记录的间隔
     *
     * @return 停表开始到上一次记录的间隔
     */
    public long getWholeInterval() {
        int size = timeline.size();
        if (size > 0) {
            return timeline.get(size - 1) - timeline.get(0);
        } else {
            throw new WrongProcessException("Only running stopwatch has the whole interval.");
        }
    }

    /**
     * 获取记录开始到最后一次记录的间隔
     *
     * @return 停表开始到上一次记录的间隔
     */
    public double getWholeInterval(TimeUnit unit) {
        long i = getWholeInterval();
        return turnToUnit(i, unit);
    }

    /**
     * 获取pin时间间隔
     *
     * @param from 开始的pin名
     * @param to   截止的pin名
     * @return pin的时间间隔；当from或是to任意一个pin名不存在时，返回-1；
     */
    public long getPinInterval(String from, String to) {
        long f = getPin(from);
        if (f == -1) {
            return -1;
        }
        long t = getPin(to);
        if (t == -1) {
            return -1;
        }
        return t - f;
    }

    /**
     * 获取pin时间间隔
     *
     * @param from 开始的pin名
     * @param to   截止的pin名
     * @param unit 时间间隔
     * @return pin的时间间隔；当from或是to任意一个pin名不存在时，返回-1；
     */
    public double getPinInterval(String from, String to, TimeUnit unit) {
        long i = getPinInterval(from, to);
        return turnToUnit(i, unit);
    }

    /**
     * 获取pin距离上一次pin的间隔时间
     *
     * @param pin pin名
     * @return 间隔时间
     */
    public long getPinInterval(String pin) {
        long f = getPin(pin);
        if (f == -1) {
            return -1;
        }
        int i = pinNameList.indexOf(pin);
        if (i < 1) {
            return -1;
        }
        long t = getPin(i - 1);
        if (t == -1) {
            return -1;
        }
        return f - t;
    }

    /**
     * 获取pin距离上一次pin的间隔时间
     *
     * @param pin  pin名
     * @param unit 时间单位
     * @return 间隔时间
     */
    public double getPinInterval(String pin, TimeUnit unit) {
        long i = getPinInterval(pin);
        return turnToUnit(i, unit);
    }

    /**
     * 获取pin时间间隔
     *
     * @param from 开始的pin序号
     * @param to   截止的pin序号
     * @return pin的时间间隔；当from或是to任意一个pin序号不存在时，返回-1；
     */
    public long getPinInterval(int from, int to) {
        if (timeline.size() < from || timeline.size() < to) {
            return -1;
        }
        return timeline.get(to) - timeline.get(from);
    }

    /**
     * 获取pin时间间隔
     *
     * @param from 开始的pin序号
     * @param to   截止的pin序号
     * @param unit 时间单位
     * @return pin的时间间隔；当from或是to任意一个pin序号不存在时，返回-1；
     */
    public double getPinInterval(int from, int to, TimeUnit unit) {
        long i = getPinInterval(from, to);
        return turnToUnit(i, unit);
    }

    /**
     * 获取pin距离上一次pin的间隔时间
     *
     * @param pin pin序号
     * @return 间隔时间
     */
    public long getPinInterval(int pin) {
        long f = getPin(pin);
        if (f == -1) {
            return -1;
        }
        if (pin < 1) {
            return -1;
        }
        long t = getPin(pin - 1);
        if (t == -1) {
            return -1;
        }
        return f - t;
    }

    /**
     * 获取pin距离上一次pin的间隔时间
     *
     * @param pin  pin序号
     * @param unit 时间单位
     * @return 间隔时间
     */
    public double getPinInterval(int pin, TimeUnit unit) {
        long i = getPinInterval(pin);
        return turnToUnit(i, unit);
    }

    /**
     * 获取原始时间线
     *
     * @return 原始时间线
     */
    public ArrayList<Long> getTimeline() {
        return timeline;
    }

    /**
     * 获取原始时间线
     *
     * @param unit 时间单位
     * @return 原始时间线
     */
    public ArrayList<Double> getTimeline(TimeUnit unit) {
        ArrayList<Double> list = new ArrayList<>();
        for (Long t : timeline) {
            list.add(turnToUnit(t, unit));
        }
        return list;
    }

    /**
     * 获取pin名列表
     *
     * @return pin名列表
     */
    public ArrayList<String> getPinNameList() {
        return new ArrayList<>(pinNameList);
    }

    /**
     * 获取间隔时间线
     */
    public ArrayList<Long> getIntervalLine() {
        ArrayList<Long> list = new ArrayList<>();
        Long last = null;
        for (Long t : timeline) {
            if (last != null) {
                list.add(t - last);
            }
            last = t;
        }
        return list;
    }

    /**
     * 获取间隔时间线
     */
    public ArrayList<Double> getIntervalLine(TimeUnit unit) {
        ArrayList<Double> list = new ArrayList<>();
        Long last = null;
        for (Long t : timeline) {
            if (last != null) {
                list.add(turnToUnit(t - last, unit));
            }
            last = t;
        }
        return list;
    }

    public boolean isRunning() {
        return (process & Process.RUNNING) > 0;
    }

    public boolean isPause() {
        return (process & Process.PAUSE) > 0;
    }

    public boolean isReady() {
        return (process & Process.READY) > 0;
    }

    public boolean isStopped() {
        return (process & Process.STOP) > 0;
    }

    private double turnToUnit(long i, TimeUnit unit) {
        if (i < 1) {
            return i;
        }
        switch (unit) {
            case DAYS:
                return i / 86400000000000D;
            case HOURS:
                return i / 3600000000000D;
            case MINUTES:
                return i / 60000000000D;
            case SECONDS:
                return i / 1000000000D;
            case MILLISECONDS:
                return i / 1000000D;
            case MICROSECONDS:
                return i / 1000D;
            default:
                return i;
        }
    }

    /**
     * 开始一个停表
     *
     * @param name 停表名称
     * @return 对应名称的停表。若不存在此停表则创建此停表，并执行start后返回
     */
    public static Stopwatch start(String name) {
        Stopwatch watch = get(name);
        watch.start();
        return watch;
    }

    /**
     * 获取停表
     *
     * @param name 获取的停表名称
     * @return 对应名称的停表。若不存在此停表则创建此停表并返回
     */
    public static Stopwatch get(String name) {
        synchronized (WATCH_MAP) {
            Stopwatch watch = WATCH_MAP.get(name);
            if (watch == null) {
                watch = new Stopwatch(name);
                WATCH_MAP.put(name, watch);
            }
            return watch;
        }
    }

    /**
     * 移除停表
     *
     * @param name 停表名称
     * @return 移除的停表对象。没有此名称的停表则返回null
     */
    public static Stopwatch remove(String name) {
        return WATCH_MAP.remove(name);
    }

    private interface Process {
        /**
         * 就绪状态，此状态下可以start和restart，无法pin、pause、continue和stop
         */
        int READY = 1;

        /**
         * 计时中，此状态下可以pin、pause、stop和restart，无法start和continue
         */
        int RUNNING = 1 << 1;

        /**
         * 暂停中，此状态下可以restart、stop、pin和continue，无法start
         */
        int PAUSE = 1 << 2;

        /**
         * 结束状态，此状态下只允许restart
         */
        int STOP = 1 << 3;
    }
}
