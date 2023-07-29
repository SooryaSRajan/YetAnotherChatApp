package quartz.action;

public enum Jobs {
    DELETE_UNVERIFIED_USERS("Unverified user");

    final String jobTitle;

    public String getJobTitle() {
        return jobTitle;
    }

    Jobs(String s) {
        this.jobTitle = s;
    }
}
