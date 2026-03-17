public class CartaDano extends Carta {

   // Atributos
   private int dano;

   // Metodos

   public CartaDano(String nome, int custo, String descricao, int dano) {
      super(nome, custo, descricao);
      this.dano = dano;
   }

   public void usar(Entidade alvo) {
      if (alvo instanceof Heroi) {
         ((Inimigo) alvo).receberDano(dano);
      }
   }

}
