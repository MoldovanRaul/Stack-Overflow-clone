import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TagDTO } from '../dto/tag-dto';

@Injectable({
  providedIn: 'root'
})
export class TagService {
  private apiUrl = 'http://localhost:8080/tags';

  constructor(private http: HttpClient) {}

  getAllTags(): Observable<TagDTO[]> {
    return this.http.get<TagDTO[]>(this.apiUrl);
  }
}
