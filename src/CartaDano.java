public class CartaDano extends Carta {
   private int dano;

   public CartaDano(String nome, int custo, String descricao, int dano) {
      super(nome, custo, descricao);
      this.dano = dano;
   }

   @Override
   public boolean precisaAlvo() { return true; }

   @Override
   public void usar(Entidade usuario, Entidade alvo, Publisher publisher) {
      if (alvo != null) {
         alvo.receberDano(dano);
      }
   }
}