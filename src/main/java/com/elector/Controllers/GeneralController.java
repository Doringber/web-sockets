package com.elector.Controllers;


import com.elector.Annotations.ActivistRestricted;
import com.elector.Annotations.AdminRestricted;
import com.elector.Annotations.GroupManagerRestricted;
import com.elector.Annotations.ObserverRestricted;
import com.elector.Objects.Entities.*;
import com.elector.Objects.General.BaseUser;
import com.elector.Objects.General.ObserverLogItem;
import com.elector.Services.GeneralManager;
import com.elector.Utils.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.elector.Utils.Definitions.*;

@CrossOrigin
@RestController
public class GeneralController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralController.class);

    @Autowired
    private GeneralManager generalManager;

    @Autowired
    private ConfigUtils configUtils;

    @RequestMapping("/api-web-sockets/ping")
    public String ping() {
        return "web-sockets";
    }


    @Autowired
    private Utils utils;


    @RequestMapping("/api-web-sockets/reload-config")
    public String reloadConfig (JSONObject results) {
        boolean error = false;
        try {
            configUtils.reloadConfig();
        } catch (Exception e) {
            LOGGER.error("reloadConfig", e);
            error = true;
        }
        results.put(PARAM_ERROR, error);
        return results.toString();
    }



}
