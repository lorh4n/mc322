/**
 * Representa uma carta com foco ofensivo no sistema de combate.
 * <p>
 * Esta classe herda de {@link Carta} e implementa a mecânica de causar dano
 * direto a uma entidade alvo específica.
 * </p>
 */
public class CartaDano extends Carta {
   private int dano;

   /**
    * Construtor da Carta de Dano.
    * * @param nome O nome de exibição da carta.
    * @param custo O valor (em energia/mana) necessário para jogar esta carta.
    * @param descricao O texto descritivo do efeito (ex: "Causa 5 de dano").
    * @param dano A quantidade de pontos de vida que será subtraída do alvo.
    */
   public CartaDano(String nome, int custo, String descricao, int dano) {
      super(nome, custo, descricao);
      this.dano = dano;
   }

   /**
    * Informa que esta carta exige a seleção de um alvo.
    * Como é uma carta de ataque direto, é obrigatório saber quem vai receber o dano.
    * * @return Sempre {@code true} para a CartaDano.
    */
   @Override
   public boolean precisaAlvo() { 
       return true; 
   }

   /**
    * Executa a ação da carta, subtraindo os pontos de vida da entidade alvo
    * com base no valor de dano da carta.
    * * @param usuario A entidade (Herói ou Inimigo) que está usando a carta.
    * @param alvo A entidade que receberá o dano. Só aplica o dano se não for {@code null}.
    * @param publisher O gerenciador de eventos do jogo, mantido pelo contrato da classe mãe.
    */
   @Override
   public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
      if (alvo != null) {
         alvo.receberDano(dano);
      }
   }
}