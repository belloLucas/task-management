import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  expirationTime: string;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = '/api/users';

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

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, { headers: this.getHeaders() });
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders(),
    });
  }
}
