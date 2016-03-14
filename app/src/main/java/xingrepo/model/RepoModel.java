package xingrepo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lokesh on 22-02-2016.
 */
public class RepoModel {

    @SerializedName("name")
    private String Name;

    @SerializedName("description")
    private String Description;

    @SerializedName("fork")
    private boolean IsForked;

    @SerializedName("html_url")
    private String RepoUrl;

    @SerializedName("owner")
    private Owner Owner;


    public String getName()
    {
        return this.Name;
    }

    public String getDescription()
    {
        return this.Description;
    }

    public String getRepoUrl()
    {
        return this.RepoUrl;
    }

    public Owner getOwner()
    {
        return this.Owner;
    }

    public boolean getFork()
    {
        return this.IsForked;
    }
}
