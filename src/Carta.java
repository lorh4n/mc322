public abstract class Carta {
   private String nome;
   private String descricao;
   private int custo;
   
   public Carta(String nome, int custo, String descrição) {
      this.nome = nome;
      this.custo = custo;
      this.descricao = descrição;
   }

   public abstract void usar(Entidade usuario, Entidade alvo, Publisher publisher);

   public boolean precisaAlvo() { return false; }

   public int getCusto() { 
      return custo; 
   }

   public String getNome() { 
      return this.nome; 
   }

   public String getDescricao() { 
      return this.descricao; 
   }
}