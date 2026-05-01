/**
 * Cura o herói sempre que uma batalha é vencida.
 */
public class ReliquiaCuraPosBatalha implements Reliquia {

    private final Heroi heroi;

    public ReliquiaCuraPosBatalha(Heroi heroi) {
        this.heroi = heroi;
    }

    @Override
    public String getNome() {
        return "Anel de Sangue";
    }

    @Override
    public String getDescricao() {
        return "Cura 3 HP ao vencer uma batalha.";
    }

    @Override
    public void prepararParaBatalha(Batalha batalha) {
        batalha.inscrever(this);
    }

    @Override
    public void serNotificado(TipoEvento evento) {
        if (evento == TipoEvento.VITORIA_BATALHA) {
            System.out.println("[RELÍQUIA] Anel de Sangue restaurou 3 HP.");
            heroi.curar(3);
        }
    }
}
