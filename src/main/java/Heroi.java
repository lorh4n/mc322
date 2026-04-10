/**
 * Representa o personagem controlado pelo jogador no sistema de combate.
 * <p>
 * Esta classe herda de {@link Entidade}, o que significa que ela já possui 
 * nativamente todo o comportamento de receber dano, ganhar escudos e sofrer
 * efeitos de status. Futuramente, atributos exclusivos do jogador (como 
 * energia/mana atual ou o baralho de cartas) podem ser adicionados aqui.
 * </p>
 */
public class Heroi extends Entidade {

   // Metodos

   /**
    * Construtor padrão para inicializar o Herói.
    * <p>
    * Repassa as informações básicas para a classe mãe ({@link Entidade}) 
    * para configurar os atributos de combate.
    * </p>
    * * @param nome O nome de exibição do herói.
    * @param vida Os pontos de vida iniciais (que também definirão o HP máximo).
    */
   Heroi(String nome, int vida){
      super(nome, vida);
   }
   
}