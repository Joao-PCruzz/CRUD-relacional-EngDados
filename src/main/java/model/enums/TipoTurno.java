package model.enums;

//Representa o domínio "universidade.tipo_turno" do banco: ('Matutino', 'Vespertino', 'Noturno', 'Turno Indefinido')
public enum TipoTurno {
    MATUTINO("Matutino"),
    VESPERTINO("Vespertino"),
    NOTURNO("Noturno"),
    TURNO_INDEFINIDO("Turno Indefinido");

    private final String label;

    TipoTurno(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static TipoTurno fromLabel(String label) {
        for (TipoTurno valor : values()) {
            if (valor.label.equalsIgnoreCase(label)) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Turno desconhecido: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}
