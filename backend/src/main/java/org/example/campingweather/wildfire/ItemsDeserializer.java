package org.example.campingweather.wildfire;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.Collections;

class ItemsDeserializer extends StdDeserializer<WildfireResponse.ItemWrapper> {

    ItemsDeserializer() { super(WildfireResponse.ItemWrapper.class); }

    @Override
    public WildfireResponse.ItemWrapper deserialize(JsonParser p,
                                                    DeserializationContext ctx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // ① "" → 빈 ItemWrapper 반환
        if (node.isTextual() && node.asText().isBlank()) {
            WildfireResponse.ItemWrapper w = new WildfireResponse.ItemWrapper();
            w.setItem(Collections.emptyList());
            return w;
        }

        // ② 정상 JSON → 그대로 매핑
        ObjectMapper om = (ObjectMapper) p.getCodec();
        return om.treeToValue(node, WildfireResponse.ItemWrapper.class);
    }
}
