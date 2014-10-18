package com.cburch.logisim.util;

import java.util.HashMap;
import java.util.Locale;

import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Given a string, return the locale-specific translation.
 * This class is analogous to GNU gettext.
 *
 * @author Joey Lawrance
 *
 */
public class LocaleString {
  
    private static final Logger logger = LoggerFactory.getLogger( LocaleString.class );
  
    private static LocaleString self = null;
    private String[] sections = ("analyze circuit data draw file gui hex " +
            "log menu opts prefs proj start std tools util").split(" ");
    private HashMap<String,LocaleManager> sourceMap = new HashMap<String,LocaleManager>();
    private LocaleManager util;
    private LocaleString() {
        for (String section : sections) {
            LocaleManager manager = new LocaleManager("logisim", section);
            for (String key : manager.getKeys()) {
                sourceMap.put(key, manager);
            }
            if (section.equals("util")) {
                util = manager;
            }

        }
    }
    protected static LocaleManager getUtilLocaleManager() {
        return getInstance().util;
    }
    private static LocaleString getInstance() {
        if (self == null) {
            self = new LocaleString();
        }
        return self;
    }
    // This shouldn't belong here
    public static Locale[] getLocaleOptions() {
        return getUtilLocaleManager().getLocaleOptions();
    }
    // This shouldn't belong here
    public static JComponent createLocaleSelector() {
        return getUtilLocaleManager().createLocaleSelector();
    }
    public static String _(String s) {
        LocaleManager localeManager = getInstance().sourceMap.get(s);
        if (localeManager == null) {
          logger.error("Could not get string \"" + s + "\".");
          return s;
        }
        return getInstance().sourceMap.get(s).get(s);
    }
    public static String _(String key, String... arg) {
        return String.format(_(key), (Object[])arg);
    }
    public static StringGetter __(final String s) {
        LocaleManager localeManager = getInstance().sourceMap.get(s);
        if (localeManager == null) {
          logger.error("Could not get string \"" + s + "\".");
          return new StringGetter() {
            @Override
            public String toString() {
              return s;
            }
          };
        }
        return localeManager.getter(s);
    }
    public static StringGetter __(String key, String arg) {
        return getInstance().sourceMap.get(key).getter(key, arg);
    }
    public static StringGetter __(String key, StringGetter arg) {
        return __(key, arg.toString());
    }
}
