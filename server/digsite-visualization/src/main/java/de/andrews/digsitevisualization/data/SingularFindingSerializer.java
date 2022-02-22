package de.andrews.digsitevisualization.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

public class SingularFindingSerializer extends StdSerializer<SingularFinding> {

    public SingularFindingSerializer() {
        this(null);
    }

    protected SingularFindingSerializer(Class<SingularFinding> t) {
        super(t);
    }

    @Override
    public void serialize(
            SingularFinding sF,
            JsonGenerator jGen,
            SerializerProvider provider) throws IOException {

        jGen.writeStartObject();

        jGen.writeObjectField("vertex", sF.getVertex());
        jGen.writeNumberField("layer", sF.getLayer());
        jGen.writeStringField("geologicalHorizon", sF.getGeologicalHorizon());
        jGen.writeStringField("identification", sF.getIdentification());
        jGen.writeStringField("basicForm", sF.getBasicForm());
        jGen.writeStringField("definition", sF.getDefinition());
        jGen.writeStringField("id", sF.getId());
        jGen.writeStringField("image", sF.getImage());
        jGen.writeStringField("comment", sF.getComment());
        jGen.writeBooleanField("worked", sF.isWorked());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode unmappedData = mapper.createObjectNode();

        Map<String, String> dynamicData = sF.getUnmappedData();
        dynamicData.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                unmappedData.put(key, value);
            }
        });

        jGen.writeObjectField("unmappedData", unmappedData);

        jGen.writeEndObject();
    }
}
