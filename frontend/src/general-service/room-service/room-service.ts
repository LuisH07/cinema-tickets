import { Injectable } from '@angular/core';
import { RoomModel } from '../../app/core/models/room.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  private readonly baseUrl = `${environment.apiUrl}/salas`;

  async getRooms(): Promise<RoomModel[]> {
    const response = await fetch(this.baseUrl);
    return response.json();
  }

  async getRoomById(id: number): Promise<RoomModel> {
    const response = await fetch(`${this.baseUrl}/${id}`);
    return response.json();
  }
}
