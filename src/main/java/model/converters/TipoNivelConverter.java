package model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import model.enums.TipoNivel;

@Converter
public class TipoNivelConverter implements AttributeConverter<TipoNivel, String> {

    @Override
    public String convertToDatabaseColumn(TipoNivel attribute) {
        return attribute == null ? null : attribute.getLabel();
    }

    @Override
    public TipoNivel convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TipoNivel.fromLabel(dbData);
    }
}
