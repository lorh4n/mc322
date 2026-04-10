import java.util.ArrayList;
import java.util.List;

/**
 * Representa a classe base para todos os participantes do combate no jogo.
 * <p>
 * Qualquer personagem (seja um Herói ou um Inimigo) herda desta classe.
 * Ela centraliza a lógica de gerenciamento de pontos de vida (HP), escudos 
 * e a lista de efeitos de status ativos (buffs/debuffs) que afligem a entidade.
 * </p>
 */
public abstract class Entidade {

   private String nome;
   private int vida;
   private int escudo;
   private int vida_inicial;
   
   /** Lista de efeitos de status ativos nesta entidade. */
   private List<Efeito> efeitos = new ArrayList<>();

   /**
    * Construtor padrão da Entidade.
    * O valor da vida passada também define a {@code vida_inicial} (HP máximo),
    * para impedir que curas ultrapassem a vida original do personagem.
    * * @param nome O nome da entidade.
    * @param vida Os pontos de vida iniciais (e máximos) da entidade.
    */
   Entidade(String nome, int vida) {
      this.nome = nome;
      this.vida = vida;
      this.vida_inicial = vida;
      this.escudo = 0;
   }

   /**
    * Aplica um novo efeito de status à entidade.
    * <p>
    * Se a entidade já possuir um efeito com o mesmo nome, os acúmulos serão somados.
    * Se for um efeito inédito, ele será adicionado à lista e automaticamente inscrito 
    * no {@link Publisher} para começar a reagir aos eventos do jogo.
    * </p>
    * * @param novoEfeito O efeito a ser aplicado (ex: Veneno, Regeneração).
    * @param publisher O gerenciador de eventos do jogo.
    */
   public void aplicarEfeito(Efeito novoEfeito, Publisher publisher) {
      boolean jaTemEfeito = false;

      for (Efeito e : efeitos) {
         if (e.getNome().equals(novoEfeito.getNome())) {
            e.adicionarAcumulos(novoEfeito.getAcumulos());
            System.out.println("> " + this.getNome() + " recebeu mais " + novoEfeito.getAcumulos() + " acúmulos de " + e.getNome() + "!");
            jaTemEfeito = true;
            break;
         }
      }

      if (!jaTemEfeito) {
         efeitos.add(novoEfeito);
         publisher.inscrever(novoEfeito); // Inscreve no ciclo do jogo
         System.out.println("> " + this.getNome() + " foi afligido por " + novoEfeito.getString() + "!");
      }
   }

   /**
    * Remove um efeito da lista de efeitos ativos da entidade.
    * Este método deve ser chamado quando os acúmulos de um efeito chegam a zero.
    * * @param e O efeito a ser removido.
    */
   public void removerEfeito(Efeito e) {
      efeitos.remove(e);
   }

   /**
    * Busca um efeito ativo pelo nome.
    * @param nome O nome do efeito procurado (ex: "Veneno").
    * @return O efeito encontrado, ou {@code null} se não houver.
    */
   public Efeito buscarEfeito(String nome) {
      for (Efeito e : efeitos) {
         if (e.getNome().equals(nome)) {
            return e;
         }
      }
      return null;
   }

   /**
    * Subtrai pontos de vida diretamente da entidade, **ignorando** qualquer escudo.
    * <p>
    * Ideal para ser usado por efeitos de status contínuos, como Veneno, 
    * onde a armadura/escudo não oferece proteção.
    * </p>
    * * @param dano O valor de vida a ser perdido.
    */
   public void perderVida(int dano) {
      this.vida -= dano;
      if(this.vida <= 0) this.vida = 0;
   }

   /**
    * Processa o recebimento de dano provindo de ataques físicos ou cartas de dano.
    * <p>
    * A lógica de mitigação ocorre na seguinte ordem:
    * 1. O dano atinge o {@code escudo} primeiro.
    * 2. Se o dano for maior que o escudo, o escudo é quebrado (vai a 0).
    * 3. O dano excedente penetra e é subtraído da {@code vida}.
    * </p>
    * * @param dano O valor total do dano recebido antes da mitigação.
    */
   public void receberDano(int dano) {
      if (this.escudo >= dano) {
         System.out.println(" > Escudo absorveu " + dano + " de dano! (Escudo: " + this.escudo + " → " + (this.escudo - dano) + ")");
         this.escudo -= dano;
      } else {
         int danoPenetrou = dano - this.escudo;
         if (this.escudo > 0) {
            System.out.println(" > Escudo absorveu " + this.escudo + " de dano! (Escudo: " + this.escudo + " → 0)");
            System.out.println(" > " + danoPenetrou + " de dano passou pelo escudo!");
         }
         this.vida -= danoPenetrou;
         if (this.vida < 0) this.vida = 0;
         this.escudo = 0;
      }
   }

   /**
    * Concede pontos de armadura (escudo) à entidade, protegendo-a de danos futuros.
    * * @param powerup A quantidade de pontos de escudo a ser adicionada.
    */
   public void ganharEscudo(int powerup) {
      this.escudo += powerup;
   }

   /**
    * Obtém os pontos de vida atuais da entidade.
    * @return A quantidade de HP atual.
    */
   public int getVida() { 
      return vida; 
   }

   /**
    * Obtém os pontos de escudo atuais da entidade.
    * @return A quantidade de escudo atual.
    */
   public int getEscudo() { 
      return escudo; 
   }

   /**
    * Verifica se a entidade ainda possui pontos de vida.
    * @return {@code true} se a vida for maior que 0, {@code false} se for derrotada.
    */
   public boolean estaVivo() { 
      return this.vida > 0; 
   }

   /**
    * Obtém o nome da entidade.
    * @return O nome da entidade.
    */
   public String getNome() { 
      return nome;
   }

   /**
    * Define diretamente o valor do escudo (geralmente usado para zerar o escudo no início do turno).
    * @param valor O novo valor do escudo.
    */
   public void setEscudo(int valor) { 
      this.escudo = valor; 
   }

   /**
    * Recupera pontos de vida da entidade.
    * <p>
    * A cura não permite que a vida ultrapasse o valor da {@code vida_inicial} (HP máximo) 
    * definido no momento da criação da entidade.
    * </p>
    * * @param valor A quantidade de pontos de vida a ser recuperada.
    */
   public void curar(int valor) {
      this.vida += valor;
      if(this.vida > this.vida_inicial) this.vida = this.vida_inicial;
      System.out.println("> " + this.getNome() + " recuperou " + valor + " de HP!");
   }
}