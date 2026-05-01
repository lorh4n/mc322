/**
 * Concede escudo ao herói no começo de cada batalha.
 */
public class ReliquiaEscudoInicial implements Reliquia {

    private final Heroi heroi;
    private boolean ativada;

    public ReliquiaEscudoInicial(Heroi heroi) {
        this.heroi = heroi;
    }

    @Override
    public String getNome() {
        return "Broquel Antigo";
    }

    @Override
    public String getDescricao() {
        return "Concede 4 de escudo no início de cada batalha.";
    }

    @Override
    public void prepararParaBatalha(Batalha batalha) {
        ativada = false;
        batalha.inscrever(this);
    }

    @Override
    public void serNotificado(TipoEvento evento) {
        if (evento == TipoEvento.INICIO_TURNO_JOGADOR && !ativada) {
            ativada = true;
            heroi.ganharEscudo(4);
            System.out.println("[RELÍQUIA] Broquel Antigo concedeu 4 de escudo.");
        }
    }
}
