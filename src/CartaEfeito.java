public class CartaEfeito extends Carta {
    private int acumulos;
    private String tipoEfeito;

    public CartaEfeito(String nome, int custo, String descricao, int acumulos, String tipoEfeito) {
        super(nome, custo, descricao);
        this.acumulos = acumulos;
        this.tipoEfeito = tipoEfeito;
    }

    @Override
    public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
        if (tipoEfeito.equals("Veneno") && alvo != null) {
            Efeito veneno = new Veneno(alvo, this.acumulos, publisher);
            alvo.aplicarEfeito(veneno, publisher);
            
        } else if (tipoEfeito.equals("Regeneracao") && usuario != null) {
            // Note que Regeneração é aplicada no USUÁRIO (Herói), e não no alvo (Inimigo)
            Efeito regen = new Regeneracao(usuario, this.acumulos, publisher);
            usuario.aplicarEfeito(regen, publisher);
        }
    }
}