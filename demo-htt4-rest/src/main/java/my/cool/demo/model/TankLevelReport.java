package my.cool.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TankLevelReport {

    @JsonProperty("ReturnCode")
    private int returnCode;
    @JsonProperty("ReturnMessage")
    private String returnMessage;
    @JsonProperty("TankLevels")
    private List<TankLevel> tankLevels;

    public TankLevelReport() {}

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public List<TankLevel> getTankLevels() {
        return tankLevels;
    }

    public void setTankLevels(List<TankLevel> tankLevels) {
        this.tankLevels = tankLevels;
    }

}