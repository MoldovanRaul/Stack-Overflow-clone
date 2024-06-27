import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, map, Observable, of, tap, throwError} from 'rxjs';
import { UserDTO } from '../dto/user-dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/users';

  constructor(private http: HttpClient) {}

  private currentUser: UserDTO | null = null;

  getCurrentUser(): Observable<UserDTO | null> {
    if (this.currentUser) {
      return of(this.currentUser);
    } else {
      // If the user isn't cached, fetch it from the backend
      return this.http.get<UserDTO>(`${this.apiUrl}/current`).pipe(
        tap(user => this.currentUser = user),
        catchError(this.handleError)
      );
    }
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.error);
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }

  getAllUsers(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.apiUrl);
  }

  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/${id}`);
  }

  getUserByName(name: string): Observable<UserDTO | null> {
    return this.http.get<UserDTO[]>(`${this.apiUrl}`).pipe(
      map(users => users.find(user => user.name === name) || null)
    );
  }

  createUser(user: UserDTO): Observable<UserDTO> {
    return this.http.post<UserDTO>(this.apiUrl, user);
  }

  updateUser(user: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}/${user.id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
