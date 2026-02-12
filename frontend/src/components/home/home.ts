import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [],
  template: `
    <div class="home-container">
      <p class="welcome-message"> Seja bem vindo! </p>
      <p class="warning-message"> (Sistema em desenvolvimento) </p>
    </div>
  `,
  styleUrl: './home.css',
})
export class Home {

}
