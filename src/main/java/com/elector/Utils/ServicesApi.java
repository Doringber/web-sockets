package com.elector.Utils;

import com.elector.Enums.ConfigEnum;
import com.elector.Enums.ServicesEnum;
import com.elector.Exceptions.ServiceResponseException;
import org.json.JSONObject;

import java.util.Map;

import static com.elector.Utils.Definitions.PARAM_ERROR;

public class ServicesApi {
    public static boolean responseSuccess (JSONObject response) {
        return (!(response.has(PARAM_ERROR) && response.getBoolean(PARAM_ERROR)));
    }

    public static  <T> T requestFromVotersImMemoryService (String url, Map<String, Object> params, String jsonKey) throws ServiceResponseException {
        T response = null;
        if (ConfigUtils.getConfig(ConfigEnum.use_voters_in_memory_service, false)) {
            JSONObject serviceResponse =
                    Utils.invokeUrlGet(
                            String.format("%s/%s?", ConfigUtils.getConfig(ConfigEnum.service_voters_url, "http://localhost:8789/api-voters/"), url), params);
            if (jsonKey != null) {
                if (ServicesApi.responseSuccess(serviceResponse) && serviceResponse.has(jsonKey)) {
                    response = (T) serviceResponse.get(jsonKey);
                }
            } else {
                response = (T) new Object();
            }
        }
        if (response == null) {
            throwServiceException("voters" ,url, params);
        }
        return response;
    }

    public static  <T> T requestFromElectionDayImMemoryService (String url, Map<String, Object> params, String jsonKey) throws ServiceResponseException  {
        T response = null;
        if (ConfigUtils.getConfig(ConfigEnum.use_election_day_in_memory_service, false)) {
            JSONObject serviceResponse =
                    Utils.invokeUrlGet(
                            String.format("%s/%s?", ConfigUtils.getConfig(ConfigEnum.service_election_day_url, "http://localhost:8788/api-election-day/"), url), params);
            if (ServicesApi.responseSuccess(serviceResponse) && serviceResponse.has(jsonKey)) {
                response = (T) serviceResponse.get(jsonKey);
            }
        }
        if (response == null) {
            throwServiceException("election day" ,url, params);
        }
        return response;
    }

    public static  <T> T requestFromElector(String url, Map<String, Object> params, String jsonKey) throws ServiceResponseException  {
        T response = null;
        JSONObject serviceResponse =
                Utils.invokeUrlGet(
                        String.format("%s/%s?", ConfigUtils.getConfig(ConfigEnum.service_elector_url, "http://localhost:8788/api/"), url), params);
        if (jsonKey != null) {
            if (ServicesApi.responseSuccess(serviceResponse) && serviceResponse.has(jsonKey)) {
                response = (T) serviceResponse.get(jsonKey);
            }
        } else {
            response = (T) new Object();
        }
        if (response == null) {
            throwServiceException("elector" ,url, params);
        }
        return response;
    }

    public static  <T> T requestFromSmsService(String url, Map<String, Object> params, String jsonKey) throws ServiceResponseException {
        T response = null;
        JSONObject serviceResponse =
                Utils.invokeUrlGet(
                        String.format("%s/%s?", ConfigUtils.getConfig(ConfigEnum.service_sms_url, "http://localhost:8791/api-sms/"), url), params);
        if (jsonKey != null) {
            if (ServicesApi.responseSuccess(serviceResponse) && serviceResponse.has(jsonKey)) {
                response = (T) serviceResponse.get(jsonKey);
            }
        } else {
            response = (T) new Object();
        }
        if (response == null) {
            throwServiceException("sms" ,url, params);
        }
        return response;
    }

    public static  <T> T requestFromGeneralJobsService(String url, Map<String, Object> params, String jsonKey) throws ServiceResponseException {
        T response = null;
        JSONObject serviceResponse =
                Utils.invokeUrlGet(
                        String.format("%s/%s?", ConfigUtils.getConfig(ConfigEnum.service_jobs_url, "http://localhost:8790/api-jobs/"), url), params);
        if (jsonKey != null) {
            if (ServicesApi.responseSuccess(serviceResponse) && serviceResponse.has(jsonKey)) {
                response = (T) serviceResponse.get(jsonKey);
            }
        } else {
            response = (T) new Object();
        }
        if (response == null) {
            throwServiceException("jobs" ,url, params);
        }
        return response;
    }

    public static  <T> T requestFromReports(String url, Map<String, Object> params, String jsonKey) throws ServiceResponseException{
        T response = null;
        JSONObject serviceResponse =
                Utils.invokeUrlGet(
                        String.format("%s/%s?", ConfigUtils.getConfig(ConfigEnum.service_reports_url, "http://localhost:8793/api-reports/"), url), params);
        if (jsonKey != null) {
            if (ServicesApi.responseSuccess(serviceResponse) && serviceResponse.has(jsonKey)) {
                response = (T) serviceResponse.get(jsonKey);
            }
        } else {
            response = (T) new Object();
        }
        if (response == null) {
            throwServiceException("reports" ,url, params);
        }
        return response;
    }

    private static void throwServiceException (String service, String url,  Map<String, Object> params) throws ServiceResponseException {
        throw new ServiceResponseException(String.format("Service %s response null, url: %s, params: %s", service, url, params));
    }



}
