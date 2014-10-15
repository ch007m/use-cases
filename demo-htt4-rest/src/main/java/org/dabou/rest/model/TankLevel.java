package org.dabou.rest.model;

public class TankLevel {
    public int CustomerId;
    public int LocationId;
    public int DeviceId;
    public int GradeId;
    public String ReadingTime;
    public float TankVolume;
    public float TankVolume_tc;
    public String TankVolumeUOM;
    public float FuelHeight;
    public String FuelHeightUOM;
    public float FuelTemp;
    public String FuelTempUOM;
    public float WaterVolume;
    public String WaterVolumeUOM;
    public float WaterHeight;
    public String WaterHeightUOM;

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public int getLocationId() {
        return LocationId;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

    public int getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(int deviceId) {
        DeviceId = deviceId;
    }

    public int getGradeId() {
        return GradeId;
    }

    public void setGradeId(int gradeId) {
        GradeId = gradeId;
    }

    public String getReadingTime() {
        return ReadingTime;
    }

    public void setReadingTime(String readingTime) {
        ReadingTime = readingTime;
    }

    public float getTankVolume() {
        return TankVolume;
    }

    public void setTankVolume(float tankVolume) {
        TankVolume = tankVolume;
    }

    public float getTankVolume_tc() {
        return TankVolume_tc;
    }

    public void setTankVolume_tc(float tankVolume_tc) {
        TankVolume_tc = tankVolume_tc;
    }

    public String getTankVolumeUOM() {
        return TankVolumeUOM;
    }

    public void setTankVolumeUOM(String tankVolumeUOM) {
        TankVolumeUOM = tankVolumeUOM;
    }

    public float getFuelHeight() {
        return FuelHeight;
    }

    public void setFuelHeight(float fuelHeight) {
        FuelHeight = fuelHeight;
    }

    public String getFuelHeightUOM() {
        return FuelHeightUOM;
    }

    public void setFuelHeightUOM(String fuelHeightUOM) {
        FuelHeightUOM = fuelHeightUOM;
    }

    public float getFuelTemp() {
        return FuelTemp;
    }

    public void setFuelTemp(float fuelTemp) {
        FuelTemp = fuelTemp;
    }

    public String getFuelTempUOM() {
        return FuelTempUOM;
    }

    public void setFuelTempUOM(String fuelTempUOM) {
        FuelTempUOM = fuelTempUOM;
    }

    public float getWaterVolume() {
        return WaterVolume;
    }

    public void setWaterVolume(float waterVolume) {
        WaterVolume = waterVolume;
    }

    public String getWaterVolumeUOM() {
        return WaterVolumeUOM;
    }

    public void setWaterVolumeUOM(String waterVolumeUOM) {
        WaterVolumeUOM = waterVolumeUOM;
    }

    public float getWaterHeight() {
        return WaterHeight;
    }

    public void setWaterHeight(float waterHeight) {
        WaterHeight = waterHeight;
    }

    public String getWaterHeightUOM() {
        return WaterHeightUOM;
    }

    public void setWaterHeightUOM(String waterHeightUOM) {
        WaterHeightUOM = waterHeightUOM;
    }
}
