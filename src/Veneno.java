public class Veneno extends Efeito {
    // Precisamos guardar o publisher para poder desinscrever o efeito quando ele acabar
    private Publisher publisher; 

    public Veneno(Entidade dono, int acumulos, Publisher publisher) {
        super("Veneno", dono, acumulos);
        this.publisher = publisher;
    }

    // Aqui é onde a mágica do Observer acontece!
    @Override
    public void serNotificado(TipoEvento evento) {
        // O Veneno age no FIM do turno do jogador
        if (evento == TipoEvento.FIM_TURNO_JOGADOR) {
            
            System.out.println("\n[EFEITO] " + dono.getNome() + " sofreu " + this.acumulos + " de dano de Veneno!");
            
            // Aplica o dano direto na vida (lembra do método perderVida que criamos?)
            dono.perderVida(this.acumulos);
            
            // Reduz 1 acúmulo por turno
            this.acumulos -= 1;

            // Se o veneno acabou, limpa ele da entidade e do jogo
            if (this.acumulos <= 0) {
                System.out.println("[EFEITO] O Veneno de " + dono.getNome() + " passou!");
                dono.removerEfeito(this);     // Tira da lista da Entidade
                publisher.desinscrever(this); // Tira da lista do App (Publisher)
            }
        }
    }
}