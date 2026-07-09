package model.enums;

//Representa o domínio "universidade.tipo_grau" do banco: ('Bacharelado', 'Licenciatura Plena')
public enum TipoGrau {
    BACHARELADO("Bacharelado"),
    LICENCIATURA_PLENA("Licenciatura Plena");

    private final String label;

    TipoGrau(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    //Converte o texto vindo do banco (ou da tela) para o enum correspondente
    public static TipoGrau fromLabel(String label) {
        for (TipoGrau valor : values()) {
            if (valor.label.equalsIgnoreCase(label)) {
                return valor;
            }
        }
        throw new IllegalArgumentException("Grau desconhecido: " + label);
    }

    @Override
    public String toString() {
        return label; //Faz o ComboBox e a TableView exibirem o texto certo automaticamente
    }
}
