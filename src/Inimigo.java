public class Inimigo {
   private String nome;
   private int vida;
   private int escudo;
   private int dano;

   public Inimigo(String nome, int vida, int escudo, int dano) {
      this.nome = nome;
      this.vida = vida;
      this.escudo = escudo;
      this.dano = dano;
   }

   public void receberDano(int dano) {
      if (this.escudo - dano >= 0) {
         this.escudo -= dano;
      } else {
         this.vida += this.escudo - dano;
         this.escudo = 0;
      }
   }

   public void atacar(Heroi heroi) {
      heroi.receberDano(this.dano);
   }

   public boolean estaVivo() {
      return this.vida > 0;
   }

   public String getNome() {
      return nome;
   }

   public int getVida() {
      return vida;
   }

   public int getEscudo() {
      return escudo;
   }
}
