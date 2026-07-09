package model.enums;

//Representa o domínio "universidade.tipo_nivel" do banco: ('Graduação', 'Mestrado', 'Doutorado', 'Lato')
public enum TipoNivel {
    GRADUACAO("Graduação"),
    MESTRADO("Mestrado"),
    DOUTORADO("Doutorado"),
    LATO("Lato");

    private final String label;

    TipoNivel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TipoNivel fromLabel(String label) {
        for (TipoNivel valor : values()) {
            if (valor.label.equalsIgnoreCase(label)) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Nível desconhecido: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}
