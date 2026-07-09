package model.enums;

//Representa o domínio "universidade.status_estudante" do banco: ('Ativo', 'Cancelada', 'Formando', 'Graduado')
public enum StatusEstudante {
    ATIVO("Ativo"),
    CANCELADA("Cancelada"),
    FORMANDO("Formando"),
    GRADUADO("Graduado");

    private final String label;

    StatusEstudante(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static StatusEstudante fromLabel(String label) {
        for (StatusEstudante valor : values()) {
            if (valor.label.equalsIgnoreCase(label)) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + label);
    }

    @Override
    public String toString() {
        return label;
    }
}
