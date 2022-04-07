package stopwatch;

import stopwatch.exception.WrongProcessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Verlif
 * @version 1.0
 * @date 2022/4/7 9:13
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
            process = Process.WORKING;
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

    public synchronized long pin(String pinName) {
        if (isWorking()) {
            long time = System.currentTimeMillis();
            pinNameList.add(pinName);
            timeline.add(time);
            return time;
        } else {
            throw new WrongProcessException("Only working stopwatch can pin!");
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
        process = Process.WORKING;
        return start();
    }

    /**
     * 将停表重置到就绪状态，并清空记录线
     */
    public synchronized void reset() {
        timeline.clear();
        pinNameList.clear();
        process = Process.READY;
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
     * 获取记录开始到此刻的间隔
     *
     * @return 停表开始到此刻的间隔
     */
    public long getStartToNow() {
        int size = timeline.size();
        if (size > 0) {
            return System.currentTimeMillis() - timeline.get(0);
        } else {
            throw new WrongProcessException("Only working stopwatch has the whole interval.");
        }
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
            throw new WrongProcessException("Only working stopwatch has the whole interval.");
        }
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

    public ArrayList<Long> getTimeline() {
        return timeline;
    }

    public boolean isWorking() {
        return (process & Process.WORKING) > 0;
    }

    public boolean isReady() {
        return (process & Process.READY) > 0;
    }

    public boolean isStopped() {
        return (process & Process.STOP) > 0;
    }

    public static Stopwatch start(String name) {
        Stopwatch watch = get(name);
        watch.start();
        return watch;
    }

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

    private interface Process {
        /**
         * 就绪状态，此状态下可以start和restart，不能pin和stop
         */
        int READY = 1;

        /**
         * 工作中，此状态下可以pin、stop和restart，start会无效
         */
        int WORKING = 1 << 1;

        /**
         * 结束状态，此状态下可以restart，不能start和pin
         */
        int STOP = 1 << 2;
    }
}
