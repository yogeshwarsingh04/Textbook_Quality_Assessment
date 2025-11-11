package com.textbook.model;

public class RatingSummary {

    private int reviewCount = 0;
    private double overallAverage = 0.0;
    private double averageAccuracy = 0.0;
    private double averageClarity = 0.0;
    private double averageEngagement = 0.0;
    private double averageSensitivity = 0.0;

    // (You can auto-generate these)

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public double getOverallAverage() { return overallAverage; }
    public void setOverallAverage(double overallAverage) { this.overallAverage = overallAverage; }

    public double getAverageAccuracy() { return averageAccuracy; }
    public void setAverageAccuracy(double averageAccuracy) { this.averageAccuracy = averageAccuracy; }

    public double getAverageClarity() { return averageClarity; }
    public void setAverageClarity(double averageClarity) { this.averageClarity = averageClarity; }

    public double getAverageEngagement() { return averageEngagement; }
    public void setAverageEngagement(double averageEngagement) { this.averageEngagement = averageEngagement; }

    public double getAverageSensitivity() { return averageSensitivity; }
    public void setAverageSensitivity(double averageSensitivity) { this.averageSensitivity = averageSensitivity; }
}