package org.example.campingweather.util;

public class GridConverter {
    public static Grid toGrid(double lat, double lon) {
        // TODO: 실제 변환 로직 넣을 자리
        return new Grid(60, 127);
    }

    public record Grid(int nx, int ny) {}
}
