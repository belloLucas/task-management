import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';

const authGuard = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.currentUserValue) {
    return true;
  }

  router.navigate(['']);
  return false;
};

export const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [() => authGuard()],
  },
];
