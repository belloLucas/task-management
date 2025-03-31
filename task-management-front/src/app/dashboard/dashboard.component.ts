import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { TaskService, Task } from '../services/task.service';
import { UserService, User } from '../services/user.service';
import { UserStoreService } from '../../stores/user.store';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DatePipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit, OnDestroy {
  userName: string = '';
  isModalOpen: boolean = false;
  taskForm!: FormGroup;
  users: User[] = [];
  tasks: Task[] = [];
  loading: boolean = false;
  error: string = '';

  isEditMode: boolean = false;
  currentTaskId?: number;
  modalTitle: string = 'Nova Tarefa';
  submitButtonText: string = 'Salvar Tarefa';

  private userSubscription?: Subscription;

  constructor(
    private authService: AuthService,
    private taskService: TaskService,
    private userService: UserService,
    private userStore: UserStoreService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadUserData();
    this.loadTasks();
    this.loadUsers();

    this.userSubscription = this.userStore.currentUser$.subscribe((user) => {
      if (user && user.firstName && user.lastName) {
        this.userName = `${user.firstName} ${user.lastName}`;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

  initForm(): void {
    this.taskForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      assignee: ['', Validators.required],
      priority: ['MEDIUM', Validators.required],
      deadline: ['', Validators.required],
      status: ['TODO'],
    });
  }

  loadUserData(): void {
    const currentUser = this.authService.currentUserValue;

    if (!currentUser) {
      this.router.navigate(['']);
      return;
    }

    this.userStore.updateCurrentUser(currentUser);

    if (currentUser.firstName && currentUser.lastName) {
      this.userName = `${currentUser.firstName} ${currentUser.lastName}`;
    } else {
      this.userName = 'Usuário';
    }
  }

  loadTasks(): void {
    this.loading = true;
    this.taskService.getAllTasks().subscribe({
      next: (data) => {
        this.tasks = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Erro ao carregar tarefas:', err);
        this.error = 'Falha ao carregar tarefas. Por favor, tente novamente.';
        this.loading = false;

        if (err.status === 401) {
          this.authService.logout();
          this.router.navigate(['']);
        }
      },
    });
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        console.log('Usuários carregados:', this.users);
      },
      error: (err) => {
        console.error('Erro ao carregar usuários:', err);
        if (err.status === 401) {
          this.authService.logout();
          this.router.navigate(['']);
        }
      },
    });
  }

  openModal(): void {
    this.isModalOpen = true;
    this.isEditMode = false;
    this.modalTitle = 'Nova Tarefa';
    this.submitButtonText = 'Salvar Tarefa';
    this.taskForm.reset({
      priority: 'MEDIUM',
      status: 'TODO',
    });
    this.error = '';
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.isEditMode = false;
    this.currentTaskId = undefined;
  }

  createTask(): void {
    if (this.taskForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.currentTaskId) {
      this.updateTaskDetails();
    } else {
      this.taskService.createTask(this.taskForm.value).subscribe({
        next: () => {
          this.loading = false;
          this.closeModal();
          this.loadTasks();
        },
        error: (err) => {
          console.error('Erro ao criar tarefa:', err);
          this.error =
            err.error?.message ||
            'Falha ao criar tarefa. Por favor, tente novamente.';
          this.loading = false;

          if (err.status === 401) {
            this.authService.logout();
            this.router.navigate(['']);
          }
        },
      });
    }
  }

  updateTaskDetails(): void {
    if (!this.currentTaskId) return;

    const taskData = {
      title: this.taskForm.value.title,
      description: this.taskForm.value.description,
      assignee: this.taskForm.value.assignee,
      priority: this.taskForm.value.priority,
      deadline: this.taskForm.value.deadline,
    };

    this.taskService.updateTask(this.currentTaskId, taskData).subscribe({
      next: () => {
        this.loading = false;
        this.closeModal();
        this.loadTasks();
      },
      error: (err) => {
        console.error('Erro ao atualizar tarefa:', err);
        this.error =
          err.error?.message ||
          'Falha ao atualizar tarefa. Por favor, tente novamente.';
        this.loading = false;

        if (err.status === 401) {
          this.authService.logout();
          this.router.navigate(['']);
        }
      },
    });
  }

  deleteTask(taskId: number | undefined): void {
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
      this.loading = true;
      this.taskService.deleteTask(taskId!).subscribe({
        next: () => {
          this.loading = false;
          this.loadTasks();
        },
        error: (err) => {
          console.error('Erro ao excluir tarefa:', err);
          this.error =
            err.error?.message ||
            'Falha ao excluir tarefa. Por favor, tente novamente.';
          this.loading = false;

          if (err.status === 401) {
            this.authService.logout();
            this.router.navigate(['']);
          }
        },
      });
    }
  }

  openEditModal(task: Task): void {
    if (!task || !task.id) {
      console.error('Tentativa de editar uma tarefa inválida');
      return;
    }

    this.isEditMode = true;
    this.currentTaskId = task.id;
    this.modalTitle = 'Editar Tarefa';
    this.submitButtonText = 'Atualizar Tarefa';
    this.isModalOpen = true;
    this.error = '';

    this.taskForm.patchValue({
      title: task.title || '',
      description: task.description || '',
      assignee: task.assignee?.id || '',
      priority: task.priority || 'MEDIUM',
      deadline: task.deadline ? this.formatDateForInput(task.deadline) : '',
      status: task.status || 'IN_PROGRESS',
    });
  }

  private formatDateForInput(dateString: string): string {
    try {
      const date = new Date(dateString);
      if (isNaN(date.getTime())) {
        console.warn('Data inválida:', dateString);
        return '';
      }
      return date.toISOString().split('T')[0];
    } catch (error) {
      console.error('Erro ao formatar data:', error);
      return '';
    }
  }

  completeTask(taskId: number | undefined): void {
    if (confirm('Tem certeza que deseja concluir esta tarefa?')) {
      this.loading = true;
      this.taskService.updateTask(taskId!, { completed: 1 }).subscribe({
        next: () => {
          this.loading = false;
          this.loadTasks();
        },
        error: (err) => {
          console.error('Erro ao concluir tarefa:', err);
          this.error =
            err.error?.message ||
            'Falha ao concluir tarefa. Por favor, tente novamente.';
          this.loading = false;

          if (err.status === 401) {
            this.authService.logout();
            this.router.navigate(['']);
          }
        },
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['']);
  }

  getPriorityClass(priority: string | undefined): string {
    if (!priority) {
      return '';
    }

    switch (priority) {
      case 'HIGH':
        return 'priority-high';
      case 'MEDIUM':
        return 'priority-medium';
      case 'LOW':
        return 'priority-low';
      default:
        return '';
    }
  }

  getStatusClass(status: string | undefined): string {
    if (!status) {
      return '';
    }

    switch (status) {
      case 'TODO':
        return 'status-todo';
      case 'IN_PROGRESS':
        return 'status-progress';
      case 'DONE':
        return 'status-done';
      default:
        return '';
    }
  }
}
