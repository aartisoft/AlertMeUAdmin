package in.alertmeu.a4a.models;


import java.util.ArrayList;

public class MainCatModeDAO {
    private String id;
    private String country_code;
    private String currency_sign;
    private String ads_pricing;
    private String discount;
    private String category_name;
    private String status;

    public MainCatModeDAO() {

    }

    public MainCatModeDAO(String id, String country_code, String currency_sign, String ads_pricing, String discount, String category_name, String status) {
        this.id = id;
        this.country_code = country_code;
        this.currency_sign = currency_sign;
        this.ads_pricing = ads_pricing;
        this.discount = discount;
        this.category_name = category_name;
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

    public String getCurrency_sign() {
        return currency_sign;
    }

    public void setCurrency_sign(String currency_sign) {
        this.currency_sign = currency_sign;
    }

    public String getAds_pricing() {
        return ads_pricing;
    }

    public void setAds_pricing(String ads_pricing) {
        this.ads_pricing = ads_pricing;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return category_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MainCatModeDAO) {
            MainCatModeDAO c = (MainCatModeDAO) obj;
            if (c.getCategory_name().equals(category_name) && c.getId() == id) return true;
        }

        return false;
    }
}