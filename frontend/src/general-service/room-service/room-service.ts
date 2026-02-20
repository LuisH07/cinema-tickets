import { Injectable } from '@angular/core';
import { RoomModel } from '../../app/core/models/room.model';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  private baseUrl = 'http://localhost:8080/salas';

  async getRooms(): Promise<RoomModel[]> {
    const response = await fetch(this.baseUrl);
    return response.json();
  }

  async getRoomById(id: number): Promise<RoomModel> {
    const response = await fetch(`${this.baseUrl}/${id}`);
    return response.json();
  }
}
