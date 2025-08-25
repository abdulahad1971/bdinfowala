package com.bd.bdinfowala.constants;

public class Urls {

    public  static  String domainUrl = "http://192.168.0.111/bdinfowala/api/";
    public  static  String imageUrl = "http://192.168.0.111/bdinfowala/";

    // auth
    public  static  String signUrl = domainUrl +"auth/signup.php";
    public  static  String loginUrl = domainUrl +"auth/login.php";
    public  static  String getProfileUrl = domainUrl +"auth/get_profile.php";
    public  static  String editProfileUrl =  domainUrl +"auth/edit_profile.php";

    public  static  String forgetPasswordUrl = "";


    // main content
    public  static  String insertCategoryUrl =  domainUrl +"service/insert_category.php";


}
