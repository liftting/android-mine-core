package com.xm.cygcore.util.log.lib.base;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wm on 15/6/10.
 */
public class XmLoggerLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String LEVEL_VERBOSE = "VERBOSE";

    public static final String LEVEL_DEBUG = "DEBUG";

    public static final String LEVEL_INFO = "INFO";

    public static final String LEVEL_WARN = "WARN";

    public static final String LEVEL_ERROR = "ERROR";

    public static final XmLoggerLevel VERBOSE = new XmLoggerLevel(LEVEL_VERBOSE, 1);

    public static final XmLoggerLevel DEBUG = new XmLoggerLevel(LEVEL_DEBUG, 2);

    public static final XmLoggerLevel INFO = new XmLoggerLevel(LEVEL_INFO, 3);

    public static final XmLoggerLevel WARN = new XmLoggerLevel(LEVEL_WARN, 4);

    public static final XmLoggerLevel ERROR = new XmLoggerLevel(LEVEL_ERROR, 5);

    private static Map<String, XmLoggerLevel> mLevels = new HashMap<String, XmLoggerLevel>();

    private String name;
    private int level;


    static {
        mLevels.put(LEVEL_VERBOSE, VERBOSE);
        mLevels.put(LEVEL_DEBUG, DEBUG);
        mLevels.put(LEVEL_INFO, INFO);
        mLevels.put(LEVEL_WARN, WARN);
        mLevels.put(LEVEL_ERROR, ERROR);
    }

    public XmLoggerLevel(String name, int level) {
        this.name = name;
        this.level = level;
    }

    @SuppressLint("DefaultLocale")
    public static XmLoggerLevel getLevel(String key) {
        return mLevels.get(key.toUpperCase());
    }

    public static int compare(XmLoggerLevel level1, XmLoggerLevel level2) {
        return level1.getLevel() - level2.getLevel();
    }

    public int compare(XmLoggerLevel target) {
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

        if (!(obj instanceof XmLoggerLevel)) {
            return false;
        }

        return ((XmLoggerLevel) obj).name.equals(name)
                && ((XmLoggerLevel) obj).level == level;
    }

}
