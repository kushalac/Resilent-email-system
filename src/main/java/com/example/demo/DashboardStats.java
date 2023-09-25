package com.example.demo;

public class DashboardStats {
    private long activeTrueCount;
    private long activeFalseCount;
    private long receiveNotificationsCount;
    private long promotionsCount;
    private long latestPlansCount;
    private long releaseEventsCount;
    private long notReceiveNotificationsCount;

    public DashboardStats() {
        // Default constructor
    }

    public DashboardStats(long activeTrueCount, long activeFalseCount, long receiveNotificationsCount,
                          long promotionsCount, long latestPlansCount, long releaseEventsCount,
                          long notReceiveNotificationsCount) {
        this.activeTrueCount = activeTrueCount;
        this.activeFalseCount = activeFalseCount;
        this.receiveNotificationsCount = receiveNotificationsCount;
        this.promotionsCount = promotionsCount;
        this.latestPlansCount = latestPlansCount;
        this.releaseEventsCount = releaseEventsCount;
        this.notReceiveNotificationsCount = notReceiveNotificationsCount;
    }

    public long getActiveTrueCount() {
        return activeTrueCount;
    }

    public void setActiveTrueCount(long activeTrueCount) {
        this.activeTrueCount = activeTrueCount;
    }

    public long getActiveFalseCount() {
        return activeFalseCount;
    }

    public void setActiveFalseCount(long activeFalseCount) {
        this.activeFalseCount = activeFalseCount;
    }

    public long getReceiveNotificationsCount() {
        return receiveNotificationsCount;
    }

    public void setReceiveNotificationsCount(long receiveNotificationsCount) {
        this.receiveNotificationsCount = receiveNotificationsCount;
    }

    public long getPromotionsCount() {
        return promotionsCount;
    }

    public void setPromotionsCount(long promotionsCount) {
        this.promotionsCount = promotionsCount;
    }

    public long getLatestPlansCount() {
        return latestPlansCount;
    }

    public void setLatestPlansCount(long latestPlansCount) {
        this.latestPlansCount = latestPlansCount;
    }

    public long getReleaseEventsCount() {
        return releaseEventsCount;
    }

    public void setReleaseEventsCount(long releaseEventsCount) {
        this.releaseEventsCount = releaseEventsCount;
    }

    public long getNotReceiveNotificationsCount() {
        return notReceiveNotificationsCount;
    }

    public void setNotReceiveNotificationsCount(long notReceiveNotificationsCount) {
        this.notReceiveNotificationsCount = notReceiveNotificationsCount;
    }
}
