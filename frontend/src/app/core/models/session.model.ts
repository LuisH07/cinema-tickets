import { MovieModel } from "./movie.model";
import { RoomModel } from "./room.model";

export interface SessionModel {
    id: number;
    filmeId: number;
    salaId: number;
    inicio: string;
    tipo: string;
    filme?: MovieModel;
    sala?: RoomModel;
}