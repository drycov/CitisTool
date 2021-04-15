package cf.homeit.admintool.DataModels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Vlan {
    public String vlanId,vlanName,vlanType,vlanDescr,valnIpInterface,vlanSubDescr,authorId,creationTime;
    public Vlan(){}
    public Vlan(String vlanId,String vlanName, String vlanType, String vlanDescr,
                String valnIpInterface, String vlanSubDescr,String authorId,String creationTime){
        this.vlanId = vlanId;
        this.vlanName = vlanName;
        this.vlanType = vlanType;
        this.vlanDescr = vlanDescr;
        this.valnIpInterface = valnIpInterface;
        this.vlanSubDescr = vlanSubDescr;
        this.authorId = authorId;
        this.creationTime =creationTime;
    }
    @Exclude
    public Map<String, Object> toMap() {
//        vlanId,vlanName,vlanType,vlanDescr,valnIpInterface,vlanSubDescr,authorid,creationTime
        HashMap<String, Object> result = new HashMap<>();
        result.put("vlanId", vlanId);
        result.put("vlanName", vlanName);
        result.put("vlanType", vlanType);
        result.put("vlanDescr", vlanDescr);
        result.put("valnIpInterface", valnIpInterface);
        result.put("vlanSubDescr", vlanSubDescr);
        result.put("authorId", authorId);
        result.put("creationTime", creationTime);
        return result;
    }
}
