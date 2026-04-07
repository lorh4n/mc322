public abstract class Inimigo extends Entidade {

   // Metodos

   protected int dano;

   protected Inimigo(String nome, int vida, int dano){
      super(nome, vida);
      this.dano = dano;
   }

   public void atacar(Heroi alvo) {
      alvo.receberDano(this.dano);
      System.out.printf("║ > %-33s ║%n", getNome() + " atacou causando " + dano + " de dano!");
   }

   public abstract void executarAcao(Heroi alvo, Publisher publisher);

   public abstract String anunciarIntencao();

}
