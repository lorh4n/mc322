public class CartaEscudo {
   private String nome;
   private int custo;
   private int buff;

   public CartaEscudo(String nome, int custo, int buff) {
      this.nome = nome;
      this.custo = custo;
      this.buff = buff;
   }

   public void usar(Heroi alvo) {
      alvo.ganharEscudo(buff);
   }
}
