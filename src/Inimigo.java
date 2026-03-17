public class Inimigo extends Entidade {

   // Metodos

   private int dano;

   Inimigo(String nome, int vida, int dano){
      super(nome, vida);
      this.dano = dano;
   }

   public void atacar(Heroi alvo) {
      alvo.receberDano(this.dano);
   }

}
