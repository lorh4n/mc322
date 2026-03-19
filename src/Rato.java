public class Rato extends Inimigo {

    public static final String CorOutput = "\u001B[33m"; // Amarelo
    public static final String Reset = "\u001B[0m";

    public Rato(int vida, int dano) {
        super("Rato", vida, dano);
    }

    @Override
    public String anunciarIntencao() {
        return "Pretende causar " + this.dano + " de dano";
    }

}
