import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth-service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {

  const router = inject(Router);
  const authService = inject(AuthService);
  
  const token = authService.getToken();

  if (!token) {
    console.warn('Acesso negado: Nenhum token encontrado no localStorage.');
    return router.createUrlTree(['/login']); // ou [''] para a home
  }

  const requiredRole = route.data?.['role'];

  if (requiredRole) {
    const userRole = authService.getRole();

    if (userRole !== requiredRole) {
      console.warn(`Acesso negado!`);
      return router.createUrlTree(['']);
    }
  }
  
  return true;
};
