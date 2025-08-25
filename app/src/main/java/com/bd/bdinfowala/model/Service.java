package com.bd.bdinfowala.model;

public class Service {
    private int id; // database id
    private int categoryId; // category_id from table
    private String categoryName; // category_name
    private String serviceName; // service_name
    private String description; // description
    private String features; // features
    private String price; // price as String (or use double if needed)
    private String days; // days
    private String imageUrl; // image_url
    private String requirementJson; // requirement_json

    public Service(int id, int categoryId, String categoryName, String serviceName, String description,
                   String features, String price, String days, String imageUrl, String requirementJson) {
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
}
