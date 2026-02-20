export interface Filme {
  id: number;
  titulo: string;
}

export interface Sala {
  id: number;
  nome: string;
}

export interface SessaoRequest {
  filmeId: number;
  salaId: number;
  data: string;
  horario: string;
  classificacao: string;
}