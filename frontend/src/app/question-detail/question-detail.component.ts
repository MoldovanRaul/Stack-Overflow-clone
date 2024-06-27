import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { QuestionService } from '../shared/services/question.service';
import { AnswerService } from '../shared/services/answer.service';
import { QuestionDTO } from '../shared/dto/question-dto';
import { AnswerDTO } from '../shared/dto/answer-dto';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import {catchError, throwError} from "rxjs";
import {AnswerFormComponent} from "../answer-form/answer-form.component";

@Component({
  selector: 'app-question-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, AnswerFormComponent],
  templateUrl: './question-detail.component.html',
  styleUrls: ['./question-detail.component.scss']
})
export class QuestionDetailComponent implements OnInit {
  question: QuestionDTO | undefined;
  answers: AnswerDTO[] = [];
  // newAnswerText: string = '';

  constructor(
    private route: ActivatedRoute,
    private questionService: QuestionService,
    private answerService: AnswerService
  ) { }

  ngOnInit(): void {
    const questionId = Number(this.route.snapshot.paramMap.get('id'));
    this.questionService.getQuestionById(questionId).subscribe(
      (question) => {
        this.question = question;
        this.answers = question.answers;
      },
      (error) => console.error('Error fetching question details:', error)
    );
  }

  onAnswerAdded(newAnswer: AnswerDTO) {
    this.answers.push(newAnswer);
  }

  upvoteQuestion() {
    if (this.question) {
      this.questionService.upvoteQuestion(this.question.id)
        .pipe(
          catchError(error => {
            console.error('Error upvoting question:', error);
            return throwError('An error occurred while upvoting.');
          })
        )
        .subscribe(updatedQuestion => {
          if (updatedQuestion) {
            this.question = updatedQuestion;
          }
        });
    }
  }

  downvoteQuestion() {
    if (this.question) {
      this.questionService.downvoteQuestion(this.question.id)
        .pipe(
          catchError(error => {
            console.error('Error downvoting question:', error);
            return throwError('An error occurred while downvoting.');
          })
        )
        .subscribe(updatedQuestion => {
          if (updatedQuestion) {
            this.question = updatedQuestion;
          }
        });
    }
  }

  upvoteAnswer(answer: AnswerDTO) {
    this.answerService.upvoteAnswer(answer.id)
      .pipe(
        catchError(error => {
          console.error('Error upvoting answer:', error);
          return throwError('An error occurred while upvoting.');
        })
      )
      .subscribe(updatedAnswer => {
        if (updatedAnswer) {
          const index = this.answers.findIndex(a => a.id === updatedAnswer.id);
          if (index > -1) {
            this.answers[index] = updatedAnswer;
          }
        }
      });
  }

  downvoteAnswer(answer: AnswerDTO) {
    this.answerService.downvoteAnswer(answer.id)
      .pipe(
        catchError(error => {
          console.error('Error downvoting answer:', error);
          return throwError('An error occurred while downvoting.');
        })
      )
      .subscribe(updatedAnswer => {
        if (updatedAnswer) {
          const index = this.answers.findIndex(a => a.id === updatedAnswer.id);
          if (index > -1) {
            this.answers[index] = updatedAnswer;
          }
        }
      });
  }
}


