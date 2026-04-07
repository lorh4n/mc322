public class Rato extends Inimigo {

    public static final String CorOutput = "\u001B[33m";
    public static final String Reset = "\u001B[0m";

    private int acaoIndex = 0;

    public Rato(int vida, int dano) {
        super("Rato", vida, dano);
    }

    @Override
    public void executarAcao(Heroi alvo, Publisher publisher) {
        if (acaoIndex == 0) {
            atacar(alvo);
        } else {
            Efeito veneno = new Veneno(alvo, 2, publisher);
            alvo.aplicarEfeito(veneno, publisher);
            System.out.printf("║ > %-33s ║%n", getNome() + " aplicou 2 de Veneno!");
        }
        acaoIndex = (acaoIndex + 1) % 2;
    }

    @Override
    public String anunciarIntencao() {
        if (acaoIndex == 0) {
            return "Pretende causar " + this.dano + " de dano";
        } else {
            return "Pretende aplicar 2 de Veneno";
        }
    }
}
