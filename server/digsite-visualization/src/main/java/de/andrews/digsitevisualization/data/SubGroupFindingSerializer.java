package de.andrews.digsitevisualization.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

public class SubGroupFindingSerializer extends StdSerializer<SubGroupFinding> {

    public SubGroupFindingSerializer() {
        this(null);
    }

    protected SubGroupFindingSerializer(Class<SubGroupFinding> t) {
        super(t);
    }

    @Override
    public void serialize(
            SubGroupFinding sGF,
            JsonGenerator jGen,
            SerializerProvider provider) throws IOException {

        jGen.writeStartObject();

        jGen.writeStringField("identification", sGF.getIdentification());
        jGen.writeStringField("basicForm", sGF.getBasicForm());
        jGen.writeStringField("comment", sGF.getComment());
        jGen.writeStringField("definition", sGF.getDefinition());
        jGen.writeStringField("geologicalHorizon", sGF.getGeologicalHorizon());
        jGen.writeStringField("id", sGF.getId());
        jGen.writeStringField("image", sGF.getImage());
        jGen.writeBooleanField("worked", sGF.isWorked());


        ObjectMapper mapper = new ObjectMapper();
        ObjectNode unmappedData = mapper.createObjectNode();

        Map<String, String> dynamicData = sGF.getUnmappedData();
        dynamicData.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                unmappedData.put(key, value);
            }
        });

        jGen.writeObjectField("unmappedData", unmappedData);

        jGen.writeEndObject();
    }
}
