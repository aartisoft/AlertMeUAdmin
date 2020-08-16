package in.alertmeu.a4a.models;


public class SubCatModeDAO {
    private String id;
    private String bc_id;
    String subcategory_name;
    String status;

    public SubCatModeDAO() {

    }

    public SubCatModeDAO(String id, String bc_id, String subcategory_name, String status) {
        this.id = id;
        this.bc_id = bc_id;
        this.subcategory_name = subcategory_name;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBc_id() {
        return bc_id;
    }

    public void setBc_id(String bc_id) {
        this.bc_id = bc_id;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return subcategory_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SubCatModeDAO) {
            SubCatModeDAO c = (SubCatModeDAO) obj;
            if (c.getSubcategory_name().equals(subcategory_name) && c.getId() == id) return true;
        }

        return false;
    }
}