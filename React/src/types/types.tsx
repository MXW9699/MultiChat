import { RefObject } from 'react';
import { CompatClient } from '@stomp/stompjs';

export interface ChatListProps {
  name: string;
}

export interface Message {
  content: string;
  chatID: string;
  username: string;
}

export class TextMessage implements Message {
  content: string;
  chatID: string;
  username: string;
  constructor(username: string, message: string, chatID: string) {
    this.content = message;
    this.username = username;
    this.chatID = chatID;
  }
}

export interface UserInfo {
  username: string;
  chatList: string[];
}

export interface ChatListBoxProps {
  chatID?: string;
  isActive: boolean;
  client: RefObject<CompatClient>;
  onClickHandler: Function;
}

export interface ChatDisplayProps {
  chatID: string;
  username: string;
  client: RefObject<CompatClient>;
  newChatHandler: Function;
}

export interface IndividualMessageProps {
  message: Message;
  isSelf: Boolean;
  isSameSenderAsPrev: boolean;
}

export interface SearchBarProps {
  options: string[];
  // newChatHandler: Function;
}

// Define the type for the ref
export interface RefType {
  getSearch: () => string[]; // Define methods you want to expose
  // Additional methods if needed
}
