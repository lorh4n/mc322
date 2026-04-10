/**
 * Representa a classe base abstrata para todos os adversários enfrentados no jogo.
 * <p>
 * Esta classe herda de {@link Entidade} e define a estrutura fundamental da 
 * inteligência artificial dos inimigos, exigindo que cada tipo específico de inimigo 
 * defina como age no seu turno e como anuncia suas intenções previamente ao jogador.
 * </p>
 */
public abstract class Inimigo extends Entidade {

   /** * O valor de dano base causado pelos ataques físicos padrão deste inimigo. 
    * Acessível de forma protegida para que as classes filhas (ex: Rato, Dragao) 
    * possam utilizá-lo em suas lógicas de combate.
    */
   protected int dano;

   /**
    * Construtor base para a criação de um Inimigo.
    * <p>
    * Como a classe é abstrata, este construtor é chamado através do {@code super()}
    * nas classes filhas para inicializar os atributos principais.
    * </p>
    * * @param nome O nome de exibição do inimigo.
    * @param vida Os pontos de vida iniciais (e máximos) do inimigo.
    * @param dano O valor do dano base do ataque físico.
    */
   protected Inimigo(String nome, int vida, int dano){
      super(nome, vida);
      this.dano = dano;
   }

   /**
    * Executa uma ação de ataque físico básico contra o herói.
    * <p>
    * O dano causado é baseado no atributo {@code dano} do inimigo. Este método 
    * também formata e imprime a ação no console do jogo.
    * </p>
    * * @param alvo O {@link Heroi} que receberá o dano do ataque.
    */
   public void atacar(Heroi alvo) {
      alvo.receberDano(this.dano);
      System.out.printf("║ > %-33s ║%n", getNome() + " atacou causando " + dano + " de dano!");
   }

   /**
    * Define o comportamento principal do inimigo durante o seu turno.
    * <p>
    * Como é um método abstrato, cada inimigo específico deve implementar a sua 
    * própria "Inteligência Artificial" aqui. Um inimigo pode decidir usar o método 
    * {@link #atacar(Heroi)}, curar a si mesmo, ou aplicar um efeito (como Veneno) no herói.
    * </p>
    * * @param alvo O {@link Heroi} que o inimigo está enfrentando.
    * @param publisher O gerenciador de eventos do jogo, necessário caso o inimigo aplique efeitos de status.
    */
   public abstract void executarAcao(Heroi alvo, Publisher publisher);

   /**
    * Retorna a intenção do inimigo para o próximo turno, permitindo que o jogador crie estratégias.
    * <p>
    * Por exemplo, um inimigo pode retornar "Vai atacar (5 de dano)" ou "Vai se defender".
    * A interface do jogo deve exibir este texto antes do jogador tomar suas decisões.
    * </p>
    * * @return Uma string contendo a descrição da próxima ação que o inimigo tomará.
    */
   public abstract String anunciarIntencao();

}