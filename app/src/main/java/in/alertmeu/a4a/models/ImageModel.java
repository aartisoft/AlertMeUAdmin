package in.alertmeu.a4a.models;


public class ImageModel {

    private String image_id;
    private String image_description;
    private String image_path;

    public ImageModel() {

    }

    public ImageModel(String image_id, String image_description, String image_path) {
        this.image_id = image_id;
        this.image_description = image_description;
        this.image_path = image_path;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_description() {
        return image_description;
    }

    public void setImage_description(String image_description) {
        this.image_description = image_description;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
