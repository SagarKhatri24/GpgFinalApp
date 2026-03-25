package gpg.finalapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLoginData {

    @SerializedName("Status")
    @Expose
    public Boolean status;

    @SerializedName("Message")
    @Expose
    public String message;

    @SerializedName("UserData")
    @Expose
    public List<UserData> userData;

    public class UserData {

        @SerializedName("userid")
        @Expose
        public String userid;

        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("email")
        @Expose
        public String email;

        @SerializedName("contact")
        @Expose
        public String contact;

        @SerializedName("password")
        @Expose
        public String password;

        @SerializedName("gender")
        @Expose
        public String gender;

        @SerializedName("city")
        @Expose
        public String city;
    }
}