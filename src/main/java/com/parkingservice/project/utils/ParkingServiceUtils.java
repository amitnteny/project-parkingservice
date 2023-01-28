package com.parkingservice.project.utils;

public class ParkingServiceUtils {

    public static int getIntForBoolean(boolean isAvailable) {
        return isAvailable ? 1 : 0;
    }

    public static boolean getIntForBoolean(int isAvailable) {
        return isAvailable == 1 ? true : false;
    }

    public static String getAvailableStackKey(long parkingLotId, int vehicleSize) {
        return parkingLotId + "-" + vehicleSize;
    }
}
