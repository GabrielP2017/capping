package org.example.campingweather.wildfire;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WildfireResponse {

    /** 최상위 “response” 객체 */
    private ResponseWrapper response;

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResponseWrapper {
        private Body body;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {

        /** ""(빈 문자열) ↔ 정상 JSON 모두 처리 */
        @JsonDeserialize(using = ItemsDeserializer.class)
        private ItemWrapper items;
    }

    @Data
    public static class ItemWrapper {

        /** 단일 객체·배열 모두 허용 */
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        private List<Item> item;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String analdate; // "2025-07-17 02"
        private int    d1, d2, d3, d4;
        private String regioncode;
    }


}
