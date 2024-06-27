import { Routes } from '@angular/router';
import { QuestionListComponent } from './question-list/question-list.component';
import { QuestionDetailComponent } from './question-detail/question-detail.component';
import {AskQuestionComponent} from "./ask-question/ask-question.component";

export const routes: Routes = [
  { path: '', redirectTo: '/questions', pathMatch: 'full' },
  { path: 'questions', component: QuestionListComponent },
  { path: 'questions/:id', component: QuestionDetailComponent },
  { path: 'ask-question', component: AskQuestionComponent }
];
