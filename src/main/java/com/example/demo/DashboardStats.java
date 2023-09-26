package com.example.demo;

public class DashboardStats {
    private long activeTrueCount;
    private long activeFalseCount;
    private long receiveNotificationsCount;
    private long promotionsCount;
    private long latestPlansCount;
    private long releaseEventsCount;
    private long notReceiveNotificationsCount;
    private long totalPromotionsCount;
    private long totalReleaseEventsCount;
    private long totalLatestPlansCount;
    private long totalNotificationsCount;
    private long totalPromotionsToUsersCount;
    private long totalReleaseEventsToUsersCount;
    private long totalLatestPlansToUsersCount;
    private long totalNotificationSent;

    public DashboardStats() {
        // Default constructor
    }

    public DashboardStats(long activeTrueCount, long activeFalseCount, long receiveNotificationsCount,
                          long promotionsCount, long latestPlansCount, long releaseEventsCount,
                          long notReceiveNotificationsCount,long totalPromotionsCount,long totalReleaseEventsCount, long totalLatestPlansCount,
                          long totalNotificationsCount, long totalPromotionsToUsersCount,
                          long totalReleaseEventsToUsersCount,long totalLatestPlansToUsersCount,long totalNotificationSent) {
        this.activeTrueCount = activeTrueCount;
        this.activeFalseCount = activeFalseCount;
        this.receiveNotificationsCount = receiveNotificationsCount;
        this.promotionsCount = promotionsCount;
        this.latestPlansCount = latestPlansCount;
        this.releaseEventsCount = releaseEventsCount;
        this.notReceiveNotificationsCount = notReceiveNotificationsCount;
        this.totalPromotionsCount=totalPromotionsCount;
        this.totalReleaseEventsCount=totalReleaseEventsCount;
        this.totalLatestPlansCount=totalLatestPlansCount;
        this.totalNotificationsCount=totalNotificationsCount;
        this.totalPromotionsToUsersCount=totalPromotionsToUsersCount;
        this.totalReleaseEventsToUsersCount=totalReleaseEventsToUsersCount;
        this.totalLatestPlansToUsersCount=totalLatestPlansToUsersCount;
        this.totalNotificationSent=totalNotificationSent;;
    }

    public long getTotalPromotionsCount() {
		return totalPromotionsCount;
	}

	public void setTotalPromotionsCount(long totalPromotionsCount) {
		this.totalPromotionsCount = totalPromotionsCount;
	}

	public long getTotalReleaseEventsCount() {
		return totalReleaseEventsCount;
	}

	public void setTotalReleaseEventsCount(long totalReleaseEventsCount) {
		this.totalReleaseEventsCount = totalReleaseEventsCount;
	}

	public long getTotalLatestPlansCount() {
		return totalLatestPlansCount;
	}

	public void setTotalLatestPlansCount(long totalLatestPlansCount) {
		this.totalLatestPlansCount = totalLatestPlansCount;
	}

	public long getTotalNotificationsCount() {
		return totalNotificationsCount;
	}

	public void setTotalNotificationsCount(long totalNotificationsCount) {
		this.totalNotificationsCount = totalNotificationsCount;
	}

	public long getTotalPromotionsToUsersCount() {
		return totalPromotionsToUsersCount;
	}

	public void setTotalPromotionsToUsersCount(long totalPromotionsToUsersCount) {
		this.totalPromotionsToUsersCount = totalPromotionsToUsersCount;
	}

	public long getTotalReleaseEventsToUsersCount() {
		return totalReleaseEventsToUsersCount;
	}

	public void setTotalReleaseEventsToUsersCount(long totalReleaseEventsToUsersCount) {
		this.totalReleaseEventsToUsersCount = totalReleaseEventsToUsersCount;
	}

	public long getTotalLatestPlansToUsersCount() {
		return totalLatestPlansToUsersCount;
	}

	public void setTotalLatestPlansToUsersCount(long totalLatestPlansToUsersCount) {
		this.totalLatestPlansToUsersCount = totalLatestPlansToUsersCount;
	}

	public long getTotalNotificationSent() {
		return totalNotificationSent;
	}

	public void setTotalNotificationSent(long totalNotificationSent) {
		this.totalNotificationSent = totalNotificationSent;
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