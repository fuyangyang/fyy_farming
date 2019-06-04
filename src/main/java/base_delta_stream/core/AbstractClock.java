package base_delta_stream.core;

import static base_delta_stream.constant.Const.MILL_SEC_IN_DAY;
import static base_delta_stream.constant.Const.MILL_SEC_IN_HOUR;
import static base_delta_stream.constant.Const.MILL_SEC_IN_MINUTE;

/**
 * 负责从消息中提取时间并及时更新时钟

 */
public abstract class AbstractClock<T> implements Clock<T> {

    //volatile is used to udpate a long timestamp as an atomic action
    private volatile long timestamp;

    /**
     * 分时天的变量是否已经初始化
     */
    private boolean initialed = false;

    /**
     * 用于生成文件名
     */
    private Long currentMinute;
    /**
     * 用于缓存下一分钟，避免每次计算，用于结束当前分钟事件
     */
    private Long nextMinute;
    private Long currentHour;
    private Long nextHour;
    private Long currentDay;
    private Long nextDay;

    public AbstractClock() {
    }

    @Override
    public void onElement(T t) throws Exception {
        timestamp = extractTimestamp(t);
        if (!initialed) {
            init();
        }

        if(reachNextMinute()) {
            updateCurrentMinute();
            onMinute(currentMinute);
            handleElement(t);
            if (reachNextHour()) {
                boolean reachNextDay = reachNextDay();
                if(reachNextDay) {
                    updateDayPeriod();
                }
                onHour(reachNextDay, currentDay);
                updaterHourPeriod();
            }
        } else {
            handleElement(t);
        }
    }

    private void init() {
        updateAllPeriod();
        initialed = true;
        initial();
    }

    /**
     * 用户初始化逻辑
     */
    protected abstract void initial();

    protected abstract void handleElement(T t) throws Exception;

    /**
     *
     * @return
     */
    public abstract long extractTimestamp(T t);

    /**
     * 合成小时级数据
     * @param reachNextDay
     */
    public abstract void onHour(boolean reachNextDay, Long nextDay) throws Exception;

    /**
     * 完成上一分钟的收尾，开启下一分钟的准备工作
     * @param nextMinute 下一分钟，可能不是当前分钟加1分钟
     * @throws Exception
     */
    public abstract void onMinute(Long nextMinute) throws Exception;


    private boolean reachNextMinute() {
        return timestamp > nextMinute;
    }

    private boolean reachNextHour() {
        return timestamp > nextHour;
    }

    private boolean reachNextDay() {
        return timestamp > nextDay;
    }

    public Long getCurrentMinute() {
        return currentMinute;
    }

    public Long getCurrentHour() {
        return currentHour;
    }

    public Long getCurrentDay() {
        return currentDay;
    }

    private void updateAllPeriod() {
        updateCurrentMinute();
        updaterHourPeriod();
        updateDayPeriod();
    }

    private void updateCurrentMinute() {
        currentMinute = timestamp - timestamp % MILL_SEC_IN_MINUTE;
        nextMinute = currentMinute + MILL_SEC_IN_MINUTE;
    }

    private void updaterHourPeriod() {
        currentHour = timestamp - timestamp % MILL_SEC_IN_HOUR;
        nextHour = currentHour + MILL_SEC_IN_HOUR;
    }

    private void updateDayPeriod() {
        currentDay = timestamp - timestamp % MILL_SEC_IN_DAY;
        nextDay = currentDay + MILL_SEC_IN_DAY;
    }

}
