package com.elector.Enums;

public enum ServicesEnum {
    election_day_in_memory_service("http://localhost:8788/", true),
    voters_in_memory_service("http://localhost:8789/", true),
    general_jobs_service("http://localhost:8790/", true),
    sms_service("http://localhost:8791/", true),
    elector("http://localhost:8787/", true),
    reports("http://localhost:8793/", true);

    private String url;
    private boolean up;

    ServicesEnum(String url, boolean up) {
        this.url = url;
        this.up = up;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
}
