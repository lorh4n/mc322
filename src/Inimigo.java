public abstract class Inimigo extends Entidade {

   // Metodos

   protected int dano;

   protected Inimigo(String nome, int vida, int dano){
      super(nome, vida);
      this.dano = dano;
   }

   public void atacar(Heroi alvo) {
      alvo.receberDano(this.dano);
   }

   public abstract String anunciarIntencao();

}
