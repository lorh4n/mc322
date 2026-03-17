public abstract class Entidade {

   // Atributos

   private String nome;
   private int vida;
   private int escudo;

   Entidade(String nome, int vida) {
      this.nome = nome;
      this.vida = vida;
      this.escudo = 0;
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

   public int getVida() {
      return vida;
   }

   public int getEscudo() {
      return escudo;
   }

   public boolean estaVivo() {
      return this.vida > 0;
   }

   public String getNome() {
      return nome;
   }

   public void setEscudo(int valor) {
      this.escudo = valor;
   }
}
