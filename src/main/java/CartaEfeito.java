/**
 * Representa uma carta que aplica efeitos de status (buffs ou debuffs) no sistema de combate.
 * <p>
 * Esta classe herda de {@link Carta} e pode gerar diferentes tipos de efeitos, 
 * como "Veneno" (aplicado aos inimigos) ou "Regeneracao" (aplicado ao próprio usuário).
 * </p>
 */
public class CartaEfeito extends Carta {
    private int acumulos;
    private String tipoEfeito;

    /**
     * Construtor da Carta de Efeito.
     * * @param nome O nome de exibição da carta.
     * @param custo O valor (em energia/mana) necessário para jogar esta carta.
     * @param descricao O texto descritivo da carta (ex: "Aplica 3 de veneno").
     * @param acumulos A quantidade de turnos ou a intensidade do efeito que será aplicado.
     * @param tipoEfeito Uma string que define o tipo do efeito (ex: "Veneno" ou "Regeneracao").
     */
    public CartaEfeito(String nome, int custo, String descricao, int acumulos, String tipoEfeito) {
        super(nome, custo, descricao);
        this.acumulos = acumulos;
        this.tipoEfeito = tipoEfeito;
    }

    /**
     * Informa se esta carta exige a seleção de um alvo.
     * O comportamento é dinâmico: efeitos ofensivos (como Veneno) precisam de um alvo,
     * enquanto efeitos defensivos (como Regeneração) são aplicados no próprio usuário 
     * e não requerem seleção prévia.
     * * @return {@code true} se o tipo de efeito for "Veneno", {@code false} caso contrário.
     */
    @Override
    public boolean precisaAlvo() { 
        return tipoEfeito.equals("Veneno"); 
    }

    /**
     * Executa a ação da carta, instanciando o efeito correspondente e aplicando-o à entidade correta.
     * <p>
     * Se o efeito for "Veneno", ele será aplicado à entidade {@code alvo}.
     * Se o efeito for "Regeneracao", ele será aplicado à entidade {@code usuario}.
     * </p>
     * * @param usuario A entidade (Herói ou Inimigo) que está usando a carta. Receberá o efeito se for de Regeneração.
     * @param alvo A entidade que receberá o efeito se for "Veneno".
     * @param publisher O gerenciador de eventos do jogo, usado para disparar os eventos de aplicação de efeito.
     */
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