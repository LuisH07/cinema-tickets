export interface ValidationRequest {
  codigo_voucher: string;
}

export interface TicketData {
  cliente: string;
  filme: string;
  sala: string;
  assentos: string[];
  data_hora_entrada: string;
}

export interface ValidationResponse {
  valido: boolean;
  status?: string;
  mensagem: string;
  dados_ingresso?: TicketData;
}

export interface HistoricoItem {
  codigo: string;
  filme: string;
  assentos: string;
}