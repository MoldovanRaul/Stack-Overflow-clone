import { UserDTO } from './user-dto';

export interface AnswerDTO {
  id: number;
  text: string;
  creationDateTime: Date;
  picture: string;
  author: UserDTO;
  questionId: number;
  voteCount: number;
}
