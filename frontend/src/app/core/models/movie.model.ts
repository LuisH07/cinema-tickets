import { SessionModel } from "./session.model";

export interface MovieModel {
    id: number;
    title: string;
    poster: string;
    backdrop: string;
    classificacao: string;
    duracao: number;
    generos: string[];
    diretores: string[];
    sinopse: string;
    elenco: string[];
}