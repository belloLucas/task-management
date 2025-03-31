import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

export interface TaskAssignee {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
}

export interface Task {
  id?: number;
  title: string;
  description: string | null;
  assignee: TaskAssignee;
  priority: 'LOW' | 'MEDIUM' | 'HIGH';
  status?: 'TODO' | 'IN_PROGRESS' | 'DONE';
  deadline: string;
  completed: number;
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private apiUrl = '/api/tasks';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const currentUserData = localStorage.getItem('currentUser');
    let token = '';

    if (currentUserData) {
      try {
        const userData = JSON.parse(currentUserData);
        token = userData.token || '';

        if (!token) {
          console.warn('Token não encontrado no objeto currentUser');
        }
      } catch (error) {
        console.error('Erro ao analisar dados do currentUser:', error);
      }
    }

    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: token ? `Bearer ${token}` : '',
    });
  }

  getAllTasks(): Observable<Task[]> {
    return this.http
      .get<Task[]>(this.apiUrl, { headers: this.getHeaders() })
      .pipe(map((tasks) => tasks.map((task) => this.mapTaskStatus(task))));
  }

  getTaskById(id: number): Observable<Task> {
    return this.http
      .get<Task>(`${this.apiUrl}/${id}`, {
        headers: this.getHeaders(),
      })
      .pipe(map((task) => this.mapTaskStatus(task)));
  }

  createTask(taskData: {
    title: string;
    description: string;
    assignee: number;
    priority: 'LOW' | 'MEDIUM' | 'HIGH';
    status?: 'TODO' | 'IN_PROGRESS' | 'DONE';
    deadline: string;
  }): Observable<Task> {
    const payload = {
      ...taskData,
      status: undefined,
    };

    return this.http
      .post<Task>(this.apiUrl, payload, {
        headers: this.getHeaders(),
      })
      .pipe(map((task) => this.mapTaskStatus(task)));
  }

  updateTask(id: number, task: Partial<Task>): Observable<Task> {
    return this.http
      .patch<Task>(`${this.apiUrl}/${id}`, task, {
        headers: this.getHeaders(),
      })
      .pipe(map((task) => this.mapTaskStatus(task)));
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders(),
    });
  }

  private mapTaskStatus(task: Task): Task {
    const status = task.completed === 1 ? 'DONE' : 'IN_PROGRESS';
    return {
      ...task,
      status,
    };
  }
}
