package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Objects.Entities.ConfigObject;
import com.elector.Services.GeneralManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.elector.Utils.Definitions.MINUTE;
import static com.elector.Utils.Utils.hasText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ConfigUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtils.class);

    @Autowired
    private GeneralManager generalManager;

    private static List<ConfigObject> config = new ArrayList<>();

    @PostConstruct
    private void initialize() {
        config = generalManager.getList(ConfigObject.class);
        LOGGER.info("Config is initialized with properties size {}", config.size());
    }

    @Scheduled(fixedRate = 5 * MINUTE)
    public void reloadConfig () {
        config = generalManager.getList(ConfigObject.class);
        LOGGER.info("config reloaded");
    }

    public static List<ConfigObject> getConfig() {
        return config;
    }

    @SuppressWarnings("unchecked")
    public static  <T> T getConfig(ConfigEnum configEnum, T def) {
        String key = configEnum.toString();
        String value = String.valueOf(def);
        for (ConfigObject con : config) {
            if (con.getConfigKey().equals(key)) {
                String configValue = con.getConfigValue();
                if (hasText(configValue)) {
                    value = configValue;
                }
            }
        }
        T genericValue = (T) value;
        if (def instanceof Integer) {
            genericValue = (T)Integer.valueOf(value);
        } else if (def instanceof Boolean) {
            genericValue = (T)Boolean.valueOf(value);
        } else if (def instanceof Double) {
            genericValue = (T)Double.valueOf(value);
        }
        return genericValue;
    }



}
