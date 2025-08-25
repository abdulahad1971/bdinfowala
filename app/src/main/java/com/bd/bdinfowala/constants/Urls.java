package com.bd.bdinfowala.constants;

public class Urls {

    public  static  String domainUrl = "http://bdinfowala.com/bdinfowala/api/";
    public  static  String imageUrl = "http://bdinfowala.com/bdinfowala/";

    // auth
    public  static  String signUrl = domainUrl +"auth/signup.php";
    public  static  String loginUrl = domainUrl +"auth/login.php";
    public  static  String getProfileUrl = domainUrl +"auth/get_profile.php";
    public  static  String editProfileUrl =  domainUrl +"auth/edit_profile.php";

    public  static  String forgetPasswordUrl = "";


    //............................categories................................................
    public  static  String insertCategoryUrl =  domainUrl +"categories/insert_category.php";

    public  static  String showCategoryUrl =  domainUrl +"categories/show_category.php";
    public  static  String updateCategoryUrl =  domainUrl +"categories/update_category.php";
    public  static  String deleteCategoryUrl =  domainUrl +"categories/delete_category.php";




   //.................................service........................................................
    public  static  String insertServiceUrl =  domainUrl +"service/insert_service.php";
    public  static  String showServiceUrl =  domainUrl +"service/show_service.php";


    public  static  String getServicesByCategory =  domainUrl +"service/get_services_by_category.php";


}
