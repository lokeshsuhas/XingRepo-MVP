package xingrepo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lokesh on 22-02-2016.
 */
public class Owner {
    @SerializedName("login")
    private String Login;
    @SerializedName("html_url")
    private String OwnerUrl;


    public String getLogin() {
        return this.Login;
    }

    public String getUrl() {
        return this.OwnerUrl;
    }
}
