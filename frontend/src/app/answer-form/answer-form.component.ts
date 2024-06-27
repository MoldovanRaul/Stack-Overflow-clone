import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AnswerService } from '../shared/services/answer.service';
import { AnswerDTO } from '../shared/dto/answer-dto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {UserDTO} from "../shared/dto/user-dto";
import {UserService} from "../shared/services/user.service";

@Component({
  selector: 'app-answer-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './answer-form.component.html',
  styleUrls: ['./answer-form.component.scss']
})
export class AnswerFormComponent {
  @Input() questionId!: number;
  answerText: string = '';
  errorMessage: string | null = null;
  currentUser: UserDTO | null = null;

  @Output() answerAdded = new EventEmitter<AnswerDTO>();

  constructor(private answerService: AnswerService, private userService: UserService) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(
      (user) => this.currentUser = user,
      (error) => {
        console.error('Error fetching current user:', error);
      }
    );
  }

  submitAnswer() {
    if (this.answerText.trim() !== '') {

      this.userService.getCurrentUser().subscribe({
        next: (user: UserDTO | null) => {
          if (user) {
            const newAnswer: AnswerDTO = {
              id: 0,
              text: this.answerText,
              creationDateTime: new Date(),
              author: user,
              questionId: this.questionId,
              voteCount: 0,
              picture: ''
            };

            this.answerService.createAnswer(newAnswer)
              .subscribe({
                next: (createdAnswer) => {
                  this.answerAdded.emit(createdAnswer);
                  this.answerText = '';
                  this.errorMessage = null;
                },
                error: (error) => {
                  console.error('Error adding answer:', error);
                  this.errorMessage = 'Failed to add answer. Please try again.';
                }
              });
          } else {
            this.errorMessage = 'You need to be logged in to add an answer.';
          }
        },
        error: (error) => {
          console.error('Error fetching current user:', error);
          this.errorMessage = 'Failed to fetch user details. Please try again.';
        }
      });

    } else {
      this.errorMessage = 'Please enter an answer.';
    }
  }
}
