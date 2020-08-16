package in.alertmeu.a4a.utils;

import java.util.ArrayList;

public class Config {
    public static final String BASE_URL = "https://www.alertmeu.com/alertmeu/api/";
    public static final String URL_ADDBAMT = BASE_URL + "user/addBAmt";
    public static final String URL_GETALLREQUESTMONEYADD = BASE_URL + "user/getAllRequestMoneyAdd";
    public static final String URL_CHECKUSERLOGIN = BASE_URL + "user/checkUserLogin";
    public static final String URL_CHECKUSERMOBILELOGIN = BASE_URL + "user/checkAdminUserMobileLogin";
    public static final String URL_MOBILEUSERLOGIN = BASE_URL + "user/mobileAdminUserLogin";
    public static final String URL_ADDUSERBYGOOGLE = BASE_URL + "user/adduserByGoogle";
    public static final String URL_GETALLSLIDEIMAGES = BASE_URL + "user/getAllSlideImages";
    public static final String URL_ADDUSERPLACES = BASE_URL + "user/addAppPlaces";
    public static final String URL_GETALLMYPLACES = BASE_URL + "user/getAllAppLocPlaces";
    public static final String URL_GETAVAILABLEMOBILENUMBER = BASE_URL + "user/getAvailableMobileNumber";
    public static final String URL_DELETEUSERPLACE = BASE_URL + "user/deleteAppplace";
    public static final String URL_UPDATEUPASSWORD = BASE_URL + "user/updateAdminPassword";
    public static final String URL_UPDATEADMINUSERPASSWORD = BASE_URL + "user/updateAdminUserPassword";
    public static final String URL_GETALLMAINCATEGORYBYADMIN = BASE_URL + "user/getAllMainCategoryByAdmin";
    public static final String URL_GETALLSUBCATEGORYBYADMIN = BASE_URL + "user/getAllSubCategoryByAdmin";
    public static final String URL_ADDNEWMAINCATBYADMIN = BASE_URL + "user/addNewMainCatByAdmin";
    public static final String URL_ADDNEWSUBCATBYADMIN = BASE_URL + "user/addNewSubCatByAdmin";
    public static final String URL_ONOFFMAINCATBYADMIN = BASE_URL + "user/onOffMainCatByAdmin";
    public static final String URL_ONOFFSUBCATBYADMIN = BASE_URL + "user/onOffSubCatByAdmin";
    public static final String URL_UPDATENEWMAINCATBYADMIN = BASE_URL + "user/updateNewMainCatByAdmin";
    public static final String URL_UPDATENEWSUBCATBYADMIN = BASE_URL + "user/updateNewSubCatByAdmin";
    public static final String URL_GETALLMONEYADDNAPBYADMIN = BASE_URL + "user/getAllMoneyAddNAPByAdmin";
    public static final String URL_UPDATENABRIADMIN = BASE_URL + "user/updateNABRIAdmin";
    public static final String URL_ADDNABRIADMIN = BASE_URL + "user/addNABRIAdmin";
    // Directory name to store captured images
    public static final String IMAGE_DIRECTORY_NAME = "AlertMeU";

    public static ArrayList<String> VALUE = new ArrayList<String>();


}
