package com.example.harumeonglog.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DistanceUtil {

    /**
     * 문자열로 거리 반환
     * @param distance m(미터) 단위
     * @return
     */
    public static String getDistanceWithString(double distance) {
        if (distance > 1000.0) {
            return String.format("%.1f", distance / 1000) + "km";
        }

        return Math.floor(distance) + "m";
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        distance = Math.acos(distance);
        distance = rad2deg(distance);
        return distance * 60 * 1.1515 * 1609.344; // 미터 단위
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
