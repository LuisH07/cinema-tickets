export interface PagamentoRequest {
  sessaoId: number;
  assentosIds: number[];
  valorEsperado: number;
  metodo: 'pix' | 'cartao_credito' | 'cartao_debito ' | 'dinheiro' ; 
  tokenPagamento: string;
}

export interface CompraResumo {
  sessaoId: number;
  filmeTitulo: string;
  filmePoster: string;
  salaNome: string;
  horario: string;
  data: string;
  assentosCodigos: string[];
  assentosIds: number[];
  valorTotal: number;
}