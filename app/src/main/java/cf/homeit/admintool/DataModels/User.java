package cf.homeit.admintool.DataModels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String firstName, lastName, middleName, phoneNumber, eMail, creationTime;


    public User() {
    }


    public User(String firstName, String lastName, String middleName,
                String eMail, String phoneNumber, String creationTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.creationTime = creationTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("middleName", middleName);
        result.put("eMail", eMail);
        result.put("lastName", lastName);
        result.put("firstName", firstName);
        result.put("phoneNumber", phoneNumber);
        result.put("creationTime", creationTime);
        return result;
    }
}