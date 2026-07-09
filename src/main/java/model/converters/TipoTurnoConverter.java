package model.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import model.enums.TipoTurno;

@Converter
public class TipoTurnoConverter implements AttributeConverter<TipoTurno, String> {

    @Override
    public String convertToDatabaseColumn(TipoTurno attribute) {
        return attribute == null ? null : attribute.getLabel();
    }

    @Override
    public TipoTurno convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TipoTurno.fromLabel(dbData);
    }
}
