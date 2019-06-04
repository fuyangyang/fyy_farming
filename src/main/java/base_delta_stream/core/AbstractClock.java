package base_delta_stream.core;

import static base_delta_stream.constant.Const.MILL_SEC_IN_DAY;
import static base_delta_stream.constant.Const.MILL_SEC_IN_HOUR;
import static base_delta_stream.constant.Const.MILL_SEC_IN_MINUTE;

/**
 * 时钟
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
     * 用于缓存下一分钟，避免每次计算
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
            updateAllPeriod();
            initialed = true;
        }

        if(reachNextMinute()) {
            onMinute();
            updateMinutePeriod();
            handleElement(t);
            if (reachNextHour()) {
                boolean reachNextDay = reachNextDay();
                onHour(reachNextDay);

                updaterHourPeriod();
                if(reachNextDay) {
                    updateDayPeriod();
                }
            }
        } else {
            handleElement(t);
        }
    }

    private void resetMinute() {

    }

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
    public abstract void onHour(boolean reachNextDay);

    /**
     * 生成一分钟的数据
     */
    public abstract void onMinute() throws Exception;


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

    public Long getNextMinute() {
        return nextMinute;
    }

    public Long getNextHour() {
        return nextHour;
    }

    public Long getNextDay() {
        return nextDay;
    }

    private void updateAllPeriod() {
        updateMinutePeriod();
        updaterHourPeriod();
        updateDayPeriod();
    }

    private void updateMinutePeriod() {
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
