public class Regeneracao extends Efeito {
    private Publisher publisher;

    public Regeneracao(Entidade dono, int acumulos, Publisher publisher) {
        super("Regeneração", dono, acumulos);
        this.publisher = publisher;
    }

    @Override
    public void serNotificado(TipoEvento evento) {
        if (evento == TipoEvento.FIM_TURNO_JOGADOR && dono instanceof Heroi) {
            
            System.out.println("\n[EFEITO] " + dono.getNome() + " curou " + this.acumulos + " de HP graças à Regeneração!");
            
            dono.curar(this.acumulos);
            
            // Reduz 1 acúmulo
            this.acumulos -= 1;

            if (this.acumulos <= 0) {
                System.out.println("[EFEITO] A Regeneração de " + dono.getNome() + " acabou.");
                dono.removerEfeito(this);
                publisher.desinscrever(this);
            }
        }
    }
}