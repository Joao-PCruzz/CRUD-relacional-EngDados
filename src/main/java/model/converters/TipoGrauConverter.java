package model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import model.enums.TipoGrau;

@Converter
public class TipoGrauConverter implements AttributeConverter<TipoGrau, String> {

    @Override
    public String convertToDatabaseColumn(TipoGrau attribute) {
        return attribute == null ? null : attribute.getLabel();
    }

    @Override
    public TipoGrau convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TipoGrau.fromLabel(dbData);
    }
}
