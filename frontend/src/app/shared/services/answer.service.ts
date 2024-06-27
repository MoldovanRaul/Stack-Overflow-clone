import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnswerDTO } from '../dto/answer-dto';

@Injectable({
  providedIn: 'root'
})
export class AnswerService {
  private apiUrl = 'http://localhost:8080/answers';

  constructor(private http: HttpClient) {}

  getAnswersByQuestionId(questionId: number): Observable<AnswerDTO[]> {
    return this.http.get<AnswerDTO[]>(`${this.apiUrl}/question/${questionId}`);
  }

  getAnswerById(id: number): Observable<AnswerDTO> {
    return this.http.get<AnswerDTO>(`${this.apiUrl}/${id}`);
  }

  createAnswer(answer: AnswerDTO): Observable<AnswerDTO> {
    return this.http.post<AnswerDTO>(this.apiUrl, answer);
  }

  updateAnswer(answer: AnswerDTO): Observable<AnswerDTO> {
    return this.http.put<AnswerDTO>(`${this.apiUrl}/${answer.id}`, answer);
  }

  deleteAnswer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  upvoteAnswer(answerId: number): Observable<any> { // Update based on your backend implementation
    return this.http.post<any>(`${this.apiUrl}/${answerId}/upvote`, {});
  }

  downvoteAnswer(answerId: number): Observable<any> { // Update based on your backend implementation
    return this.http.post<any>(`${this.apiUrl}/${answerId}/downvote`, {});
  }
}
