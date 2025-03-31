import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../app/services/user.service';

@Injectable({
  providedIn: 'root',
})
export class UserStoreService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);

  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {}

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  updateCurrentUser(user: User): void {
    this.currentUserSubject.next(user);
    sessionStorage.setItem('user_data', JSON.stringify(user));
  }

  clearUserData(): void {
    this.currentUserSubject.next(null);
    sessionStorage.removeItem('user_data');
  }
}
