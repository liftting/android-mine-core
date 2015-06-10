package com.xm.cygcore.util.log;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wm on 15/6/10.
 */
public class LogLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String LEVEL_VERBOSE = "VERBOSE";

    public static final String LEVEL_DEBUG = "DEBUG";

    public static final String LEVEL_INFO = "INFO";

    public static final String LEVEL_WARN = "WARN";

    public static final String LEVEL_ERROR = "ERROR";

    public static final LogLevel VERBOSE = new LogLevel(LEVEL_VERBOSE, 1);

    public static final LogLevel DEBUG = new LogLevel(LEVEL_DEBUG, 2);

    public static final LogLevel INFO = new LogLevel(LEVEL_INFO, 3);

    public static final LogLevel WARN = new LogLevel(LEVEL_WARN, 4);

    public static final LogLevel ERROR = new LogLevel(LEVEL_ERROR, 5);

    private static Map<String, LogLevel> mLevels = new HashMap<String, LogLevel>();

    private String name;

    /** 用于标识日志的级别，值越小级别越低. */
    private int level;

    static {
        mLevels.put(LEVEL_VERBOSE, VERBOSE);
        mLevels.put(LEVEL_DEBUG, DEBUG);
        mLevels.put(LEVEL_INFO, INFO);
        mLevels.put(LEVEL_WARN, WARN);
        mLevels.put(LEVEL_ERROR, ERROR);
    }

    public LogLevel(String name, int level) {
        this.name = name;
        this.level = level;
    }

    @SuppressLint("DefaultLocale")
    public static LogLevel getLevel(String key) {
        return mLevels.get(key.toUpperCase());
    }

    public static int compare(LogLevel level1, LogLevel level2) {
        return level1.getLevel() - level2.getLevel();
    }

    public int compare(LogLevel target) {
        return this.getLevel() - target.getLevel();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof LogLevel)) {
            return false;
        }

        return ((LogLevel) obj).name.equals(name)
                && ((LogLevel) obj).level == level;
    }



}
