package in.alertmeu.a4a.models;

public class AddMoneyRequestHistoryDAO {
    String id = "";
    String business_user_id = "";

    String amount = "";
    String mobile_no = "";
    String business_name = "";
    String date_time = "";
    String address = "";
    String company_logo = "";
    String location_name = "";
    String balance_amount = "";
    public AddMoneyRequestHistoryDAO() {

    }

    public AddMoneyRequestHistoryDAO(String id, String business_user_id, String amount, String mobile_no, String business_name, String date_time, String address, String company_logo, String location_name, String balance_amount) {
        this.id = id;
        this.business_user_id = business_user_id;
        this.amount = amount;
        this.mobile_no = mobile_no;
        this.business_name = business_name;
        this.date_time = date_time;
        this.address = address;
        this.company_logo = company_logo;
        this.location_name = location_name;
        this.balance_amount = balance_amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusiness_user_id() {
        return business_user_id;
    }

    public void setBusiness_user_id(String business_user_id) {
        this.business_user_id = business_user_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(String balance_amount) {
        this.balance_amount = balance_amount;
    }
}
