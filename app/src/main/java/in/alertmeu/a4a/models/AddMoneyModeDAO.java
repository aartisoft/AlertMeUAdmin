package in.alertmeu.a4a.models;


public class AddMoneyModeDAO {
    private String id;
    private String country_code;
    String notification_amount;
    String user_app_referral_amount;
    String business_app_referral_amount;
    String initial_deposit_amount;
    String status;
    public AddMoneyModeDAO() {

    }

    public AddMoneyModeDAO(String id, String country_code, String notification_amount, String user_app_referral_amount, String business_app_referral_amount, String initial_deposit_amount, String status) {
        this.id = id;
        this.country_code = country_code;
        this.notification_amount = notification_amount;
        this.user_app_referral_amount = user_app_referral_amount;
        this.business_app_referral_amount = business_app_referral_amount;
        this.initial_deposit_amount = initial_deposit_amount;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getNotification_amount() {
        return notification_amount;
    }

    public void setNotification_amount(String notification_amount) {
        this.notification_amount = notification_amount;
    }

    public String getUser_app_referral_amount() {
        return user_app_referral_amount;
    }

    public void setUser_app_referral_amount(String user_app_referral_amount) {
        this.user_app_referral_amount = user_app_referral_amount;
    }

    public String getBusiness_app_referral_amount() {
        return business_app_referral_amount;
    }

    public void setBusiness_app_referral_amount(String business_app_referral_amount) {
        this.business_app_referral_amount = business_app_referral_amount;
    }

    public String getInitial_deposit_amount() {
        return initial_deposit_amount;
    }

    public void setInitial_deposit_amount(String initial_deposit_amount) {
        this.initial_deposit_amount = initial_deposit_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}