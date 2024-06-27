import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { QuestionDTO } from '../dto/question-dto';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private apiUrl = 'http://localhost:8080/questions';

  constructor(private http: HttpClient) {}

  getAllQuestions(): Observable<QuestionDTO[]> {
    return this.http.get<QuestionDTO[]>(this.apiUrl);
  }

  getQuestionById(id: number): Observable<QuestionDTO> {
    return this.http.get<QuestionDTO>(`${this.apiUrl}/${id}`);
  }

  createQuestion(question: QuestionDTO): Observable<QuestionDTO> {
    return this.http.post<QuestionDTO>(this.apiUrl, question);
  }

  updateQuestion(id: number, question: QuestionDTO): Observable<QuestionDTO> {
    return this.http.put<QuestionDTO>(`${this.apiUrl}/${id}`, question);
  }

  deleteQuestion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  upvoteQuestion(questionId: number): Observable<QuestionDTO> {
    return this.http.post<QuestionDTO>(`${this.apiUrl}/${questionId}/upvote`, {});
  }

  downvoteQuestion(questionId: number): Observable<QuestionDTO> {
    return this.http.post<QuestionDTO>(`${this.apiUrl}/${questionId}/downvote`, {});
  }
}
