public class CartaDano {

   // Atributos
   
   private String nome;
   private int custo;
   private int dano;


   // Metodos

   public CartaDano(String nome, int custo, int dano) {
      this.nome = nome;
      this.custo = custo;
      this.dano = dano;
   }

   public void usar(Inimigo alvo) {
      alvo.receberDano(dano);
   }

   public int getCusto() {
      return custo;
   }
}
