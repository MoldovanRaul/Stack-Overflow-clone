import {Component, OnInit} from '@angular/core';
import { QuestionService } from '../shared/services/question.service';
import { QuestionDTO } from '../shared/dto/question-dto';
import { TagService } from '../shared/services/tag.service';
import { UserService } from '../shared/services/user.service';
import { catchError, throwError } from 'rxjs';
import { TagDTO } from '../shared/dto/tag-dto';
import { UserDTO } from '../shared/dto/user-dto';
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-ask-question',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ask-question.component.html',
  styleUrls: ['./ask-question.component.scss']
})
export class AskQuestionComponent implements OnInit{
  question: QuestionDTO = {
    id: 0,
    title: '',
    text: '',
    creationDateTime: new Date(),
    picture: '',
    author: { id: 1, name: 'Current User' },
    tags: [],
    answers: [],
    voteCount: 0
  };

  tagString: string = '';
  errorMessage: string | null = null;

  constructor(
    private questionService: QuestionService,
    private tagService: TagService,
    private userService: UserService,
    private router: Router
  ) {}

  allTags: TagDTO[] =[];

  ngOnInit(): void {
    this.tagService.getAllTags().subscribe(
      (tags) => {
        this.allTags = tags;
      },
      (error) => {
        console.error('Error fetching tags:', error);
      }
    );
  }

  submitQuestion() {
    this.question.tags = this.tagString
      .split(',')
      .map(tagName => tagName.trim())
      .filter(tagName => tagName !== '')
      .map(tagName => {
        const existingTag = this.allTags.find(tag => tag.name.toLowerCase() === tagName.toLowerCase());
        return existingTag ? existingTag : { id: 0, name: tagName };
      });

    this.userService.getUserByName("Current User").subscribe({
      next: (user: UserDTO | null) => {
        if (user) {
          this.question.author = user;
          this.questionService.createQuestion(this.question).subscribe(async (createdQuestion) => {
            try {
              await this.router.navigate(['/questions', createdQuestion.id]);
            } catch (error) {
              console.error('Error navigating after question creation:', error);
              this.errorMessage = 'Error creating question. Please try again later.';
            }
          });
        } else {
          this.errorMessage = 'User not found.';
        }
      },
      error: (error) => {
        console.error('Error getting user details:', error);
        this.errorMessage = 'Error getting user details. Please try again later.';
      }
    });
  }
}
