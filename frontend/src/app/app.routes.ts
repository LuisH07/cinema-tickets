import { Routes } from '@angular/router';
import { Login } from '../components/login/login';
import { Home } from '../components/home/home';
import { authGuard } from '../auth/guard/auth-guard';
import { Register } from '../components/register/register';
import { Sessao } from '../components/sessao/sessao';
import { Movie } from '../components/movie/movie';
import { TicketValidation } from '../components/ticket-validation/ticket-validation';

export const routes: Routes = [
    {
        path: '',
        component: Home,
    },
    {
        path: 'login',
        component: Login,
    },
    { 
        path: 'cadastro', 
        component: Register
    },
    {
        path: 'cadastro-sessao', 
        component: Sessao,
        canActivate: [authGuard],
        data: { role: 'ADMIN' }
    },
    {
        path: 'validar-ingresso',
        component: TicketValidation,
        canActivate: [authGuard],
        data: { role: 'ADMIN' }
    },
    {
        path: 'movie/:id',
        component: Movie
    },
];
