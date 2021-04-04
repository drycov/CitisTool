package cf.homeit.admintool.DataModels;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Port {
    public String portId, portType,portSpeed,portStatus,portDest,portDestMac,portDestTarif,authorId,creationTime;
    public Port(){}
    public Port(String portId, String portType, String portSpeed, String portStatus,
                String portDest, String portDestMac, String portDestTarif,String authorId, String creationTime){
       this.portId=portId;
       this.portType=portType;
       this.portSpeed=portSpeed;
       this.portStatus=portStatus;
       this.portDest=portDest;
       this.portDestMac=portDestMac;
       this.portDestTarif = portDestTarif;
        this.authorId =authorId;
        this.creationTime =creationTime;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("portId", portId);
        result.put("portType", portType);
        result.put("portSpeed", portSpeed);
        result.put("portStatus", portStatus);
        result.put("portDest", portDest);
        result.put("portDestMac", portDestMac);
        result.put("portDestTarif", portDestTarif);
        result.put("authorId", authorId);
        result.put("creationTime", creationTime);
        return result;
    }
}
