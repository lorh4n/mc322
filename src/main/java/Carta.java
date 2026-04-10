/**
 * Representa a classe base para todas as cartas do jogo.
 * <p>
 * Define os atributos fundamentais que qualquer carta deve ter (nome, custo e descrição)
 * e estabelece o contrato para o comportamento de uso da carta durante o combate.
 * </p>
 */
public abstract class Carta {
   private String nome;
   private String descricao;
   private int custo;
   
   /**
    * Construtor padrão da Carta.
    * * @param nome O nome de exibição da carta.
    * @param custo O valor (em energia/mana) necessário para jogar esta carta.
    * @param descrição O texto descritivo que explica o efeito da carta.
    */
   public Carta(String nome, int custo, String descrição) {
      this.nome = nome;
      this.custo = custo;
      this.descricao = descrição;
   }

   /**
    * Executa o efeito principal da carta no jogo.
    * Como é um método abstrato, cada tipo específico de carta (Dano, Cura, etc.) 
    * deve implementar a sua própria lógica de como esse uso afeta o jogo.
    * * @param usuario A entidade (Herói ou Inimigo) que está utilizando a carta.
    * @param alvo A entidade que receberá o efeito da carta (pode ser null se a carta não precisar de alvo).
    * @param publisher O gerenciador de eventos do jogo, usado para notificar o sistema sobre o que a carta fez.
    */
   public abstract void usar(Entidade usuario, Entidade alvo, Publisher publisher);

   /**
    * Informa se a carta requer um alvo específico para ser selecionado antes de ser jogada.
    * Cartas de dano direto normalmente precisam de alvo, enquanto cartas de buff em área não.
    * * @return {@code true} se a carta precisar de um alvo, {@code false} caso contrário. O padrão é false.
    */
   public boolean precisaAlvo() { 
       return false; 
   }

   /**
    * Obtém o custo para jogar a carta.
    * * @return O custo em pontos de energia.
    */
   public int getCusto() { 
      return custo; 
   }

   /**
    * Obtém o nome da carta.
    * * @return O nome da carta.
    */
   public String getNome() { 
      return this.nome; 
   }

   /**
    * Obtém a descrição do efeito da carta.
    * * @return O texto descritivo da carta.
    */
   public String getDescricao() { 
      return this.descricao; 
   }
}