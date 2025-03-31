import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, ReactiveFormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  title = 'task-management-front';
  activeForm: 'login' | 'register' = 'login';
  loginForm!: FormGroup;
  registerForm!: FormGroup;
  loading = false;
  error = '';
  isLoggedIn = false;

  constructor(
    private http: HttpClient,
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForms();

    const currentUser = this.authService.currentUserValue;
    this.isLoggedIn = !!currentUser;

    if (this.isLoggedIn) {
      this.router.navigate(['/dashboard']);
    }

    this.authService.currentUser.subscribe((user) => {
      this.isLoggedIn = !!user;
    });
  }

  initForms(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });

    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  setActiveForm(formType: 'login' | 'register'): void {
    this.activeForm = formType;
    this.error = '';
  }

  isActiveForm(formType: 'login' | 'register'): boolean {
    return this.activeForm === formType;
  }

  onLogin(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    const { email, password } = this.loginForm.value;
    this.authService.login(email, password).subscribe({
      next: () => {
        console.log('Login bem-sucedido!');
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.error =
          error?.error?.message ||
          'Falha no login. Verifique suas credenciais.';
        this.loading = false;
        console.error('Erro de login:', error);
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        console.log('Cadastro bem-sucedido!');
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.error =
          error?.error?.message || 'Falha no cadastro. Tente novamente.';
        this.loading = false;
        console.error('Erro de registro:', error);
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  // Verifica se deve exibir o formulário de login (apenas se não estiver logado)
  shouldShowLoginForm(): boolean {
    return !this.isLoggedIn;
  }
}
