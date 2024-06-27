import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { QuestionService } from '../shared/services/question.service';
import { QuestionDTO } from '../shared/dto/question-dto';
import {RouterModule} from "@angular/router";

@Component({
  selector: 'app-question-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.scss']
})
export class QuestionListComponent implements OnInit {
  questions: QuestionDTO[] = [];
  loading: boolean = true;
  errorMessage: string | null = null;

  constructor(private questionService: QuestionService) {}

  ngOnInit(): void {
    this.questionService.getAllQuestions()
      .subscribe({
        next: (questions) => {
          this.questions = questions;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error fetching questions:', error);
          this.loading = false;
          this.errorMessage = 'Error loading questions. Please try again later.';
        }
      });
  }
}


