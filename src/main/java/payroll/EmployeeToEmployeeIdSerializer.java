package payroll;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EmployeeToEmployeeIdSerializer extends JsonSerializer<Employee> {
    @Override
    public void serialize(Employee tmpInt,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        if(tmpInt != null) {
            jsonGenerator.writeNumber(tmpInt.getId());
        } else {
            jsonGenerator.writeNull();
        }
    }
}
