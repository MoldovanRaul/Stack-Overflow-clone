import { TagDTO } from './tag-dto';
import { AnswerDTO } from './answer-dto';
import { UserDTO } from './user-dto';

export interface QuestionDTO {
  id: number;
  title: string;
  text: string;
  creationDateTime: Date;
  picture: string;
  author: UserDTO;
  tags: TagDTO[];
  answers: AnswerDTO[];
  voteCount: number;
}
