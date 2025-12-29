package core.api.DTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArrayToStringDateTimeDeserializer extends StdDeserializer<String> {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public ArrayToStringDateTimeDeserializer() { super(String.class); }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.START_ARRAY) {
            List<Integer> parts = new ArrayList<>();
            while (p.nextToken() != JsonToken.END_ARRAY) parts.add(p.getIntValue());
            int y=parts.get(0), m=parts.get(1), d=parts.get(2);
            int H=parts.size()>3?parts.get(3):0, M=parts.size()>4?parts.get(4):0, S=parts.size()>5?parts.get(5):0;
            return LocalDateTime.of(y,m,d,H,M,S).format(ISO); // "YYYY-MM-DDTHH:MM:SS"
        } else if (p.currentToken() == JsonToken.VALUE_STRING) {
            return p.getText();
        }
        throw new IOException("FechaSuceso: tipo no soportado: "+p.currentToken());
    }
}
