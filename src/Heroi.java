public class Heroi {
   private String nome;
   private int vida;
   private int escudo;

   public Heroi(String nome, int vida, int escudo) {
      this.nome = nome;
      this.vida = vida;
      this.escudo = escudo;
   }

   public void receberDano(int dano) {
      if (this.escudo - dano >= 0) {
         this.escudo -= dano;
      } else {
         this.vida += this.escudo - dano;
         this.escudo = 0;
      }
   }

   public void ganharEscudo(int powerup) {
      this.escudo += powerup;
   }

   public void limparEscudo() {
      this.escudo = 0;
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
