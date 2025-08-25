package com.bd.bdinfowala.admin.model;

public class AdminService {

    private int id;
    private int categoryId;
    private String categoryName;
    private String serviceName;
    private String description;
    private String features;
    private String price;
    private String days;
    private String imageUrl;
    private String requirementJson;

    // Default constructor
    public AdminService() { }

    // Full constructor
    public AdminService(int id, int categoryId, String categoryName, String serviceName,
                        String description, String features, String price, String days,
                        String imageUrl, String requirementJson) {
        this.id = id;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.serviceName = serviceName;
        this.description = description;
        this.features = features;
        this.price = price;
        this.days = days;
        this.imageUrl = imageUrl;
        this.requirementJson = requirementJson;
    }

    // Getters
    public int getId() { return id; }
    public int getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getServiceName() { return serviceName; }
    public String getDescription() { return description; }
    public String getFeatures() { return features; }
    public String getPrice() { return price; }
    public String getDays() { return days; }
    public String getImageUrl() { return imageUrl; }
    public String getRequirementJson() { return requirementJson; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setDescription(String description) { this.description = description; }
    public void setFeatures(String features) { this.features = features; }
    public void setPrice(String price) { this.price = price; }
    public void setDays(String days) { this.days = days; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setRequirementJson(String requirementJson) { this.requirementJson = requirementJson; }
}
