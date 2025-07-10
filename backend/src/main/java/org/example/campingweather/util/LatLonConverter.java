package org.example.campingweather.util;

import org.springframework.stereotype.Component;

@Component
public class LatLonConverter {
    private static final double RE = 6371.00877, GRID = 5,
                                 SLAT1 = 30, SLAT2 = 60,
                                 OLON = 126, OLAT = 38,
                                 XO = 43, YO = 136;

    public record NxNy(int nx, int ny) {}

    public NxNy toGrid(double lat, double lon) {
        double D = Math.PI / 180.0, re = RE / GRID,
               sl1 = SLAT1 * D, sl2 = SLAT2 * D,
               olon = OLON * D, olat = OLAT * D;

        double sn = Math.log(Math.cos(sl1) / Math.cos(sl2)) /
                    Math.log(Math.tan(Math.PI*0.25 + sl2*0.5) /
                             Math.tan(Math.PI*0.25 + sl1*0.5));
        double sf = Math.pow(Math.tan(Math.PI*0.25 + sl1*0.5), sn) *
                    Math.cos(sl1) / sn;
        double ro = re * sf / Math.pow(Math.tan(Math.PI*0.25 + olat*0.5), sn);

        double ra = re * sf /
                    Math.pow(Math.tan(Math.PI*0.25 + lat*D*0.5), sn);
        double theta = (lon*D - olon) * sn;
        if (theta >  Math.PI) theta -= 2 * Math.PI;
        if (theta < -Math.PI) theta += 2 * Math.PI;

        int nx = (int) Math.floor(ra * Math.sin(theta)      + XO + 0.5);
        int ny = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        return new NxNy(nx, ny);
    }
    
}
