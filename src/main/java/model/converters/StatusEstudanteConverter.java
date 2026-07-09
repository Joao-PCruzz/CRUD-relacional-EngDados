package model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import model.enums.StatusEstudante;

@Converter
public class StatusEstudanteConverter implements AttributeConverter<StatusEstudante, String> {

    @Override
    public String convertToDatabaseColumn(StatusEstudante attribute) {
        return attribute == null ? null : attribute.getLabel();
    }

    @Override
    public StatusEstudante convertToEntityAttribute(String dbData) {
        return dbData == null ? null : StatusEstudante.fromLabel(dbData);
    }
}
