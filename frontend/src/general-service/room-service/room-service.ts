import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  private baseUrl = 'http://localhost:3000/salas';

  async getRooms(): Promise<any[]> {
    const response = await fetch(this.baseUrl);
    return response.json();
  }

  async getRoomById(id: number): Promise<any> {
    const response = await fetch(`${this.baseUrl}/${id}`);
    return response.json();
  }
}
