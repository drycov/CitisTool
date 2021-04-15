package cf.homeit.admintool.DataModels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Switch {
    public String switchId, switchIp, switchType, switchVendor, switchModel, switchSysName,
            switchLocation, switchDescr, switchSN, switchSubDescr, authorId, creationTime;

    public Switch() {
    }

    public Switch(String switchId, String switchIp, String switchType, String switchVendor, String switchModel, String switchSysName, String switchLocation,
                  String switchDescr, String switchSN, String switchSubDescr, String authorId, String creationTime) {
        this.switchId = switchId;
        this.switchIp = switchIp;
        this.switchType = switchType;
        this.switchVendor = switchVendor;
        this.switchModel = switchModel;
        this.switchSysName = switchSysName;
        this.switchLocation = switchLocation;
        this.switchDescr = switchDescr;
        this.switchSN = switchSN;
        this.switchSubDescr = switchSubDescr;
        this.authorId = authorId;
        this.creationTime = creationTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("switchId", switchId);
        result.put("switchIp", switchIp);
        result.put("switchType", switchType);
        result.put("switchVendor", switchVendor);
        result.put("switchModel", switchModel);
        result.put("switchSysName", switchSysName);
        result.put("switchLocation", switchLocation);
        result.put("switchDescr", switchDescr);
        result.put("switchSN", switchSN);
        result.put("switchSubDescr", switchSubDescr);
        result.put("authorId", authorId);
        result.put("creationTime", creationTime);
        return result;
    }
}
