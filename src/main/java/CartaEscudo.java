/**
 * Representa uma carta com foco defensivo no sistema de combate.
 * <p>
 * Esta classe herda de {@link Carta} e implementa a mecânica de conceder
 * pontos de escudo (proteção) à entidade que a utilizou, ajudando a mitigar 
 * danos futuros.
 * </p>
 */
public class CartaEscudo extends Carta {
   private int protecao;

   /**
    * Construtor da Carta de Escudo.
    * * @param nome O nome de exibição da carta.
    * @param custo O valor (em energia/mana) necessário para jogar esta carta.
    * @param descricao O texto descritivo do efeito (ex: "Ganha 5 de Escudo").
    * @param protecao A quantidade de pontos de escudo que será concedida ao usuário.
    */
   public CartaEscudo(String nome, int custo, String descricao, int protecao) {
      super(nome, custo, descricao);
      this.protecao = protecao;
   }

   /**
    * Executa a ação da carta, adicionando os pontos de proteção (escudo) 
    * à entidade que jogou a carta.
    * <p>
    * Note que efeitos de escudo não afetam a entidade {@code alvo}, 
    * sendo aplicados exclusivamente à entidade {@code usuario}.
    * </p>
    * * @param usuario A entidade (Herói ou Inimigo) que está usando a carta e receberá o escudo.
    * @param alvo A entidade adversária (ignorada por esta carta, já que o efeito é no próprio usuário).
    * @param publisher O gerenciador de eventos do jogo, mantido pelo contrato da classe mãe.
    */
   @Override
   public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
      if (usuario != null) {
         usuario.ganharEscudo(protecao);
      }
   }
}