import React, { useEffect, useState } from 'react';
import { ChatListBoxProps, Message, TextMessage } from '../types/types';

export default function ChatListBox({
  chatID,
  onClickHandler,
  isActive,
  client,
}: ChatListBoxProps) {
  const [firstMessage, setFirstMessage] = useState<Message>();
  const [seen, setSeen] = useState<boolean>();

  const color = isActive ? 'red' : 'black';

  async function getFirstMessage(chatID: string): Promise<Message[]> {
    try {
      const response = await fetch(`http://localhost:8080/chat/${chatID}/0/1`);
      if (!response.ok) return Promise.reject('invalid');
      const data: Message[] = await response.json();
      setFirstMessage(data[0]);
      return data;
    } catch (e) {
      console.log(e);
    }
  }

  //load the initial preview message and subscribe
  //new incomming messages should replace
  //when ever first message is changed, set the seen state
  useEffect(() => {
    if (!firstMessage) {
      getFirstMessage(chatID);
      client.current.subscribe(`/topic/chat/${chatID}`, (message) => {
        const newText: TextMessage = JSON.parse(message.body) as TextMessage;
        setFirstMessage(newText);
      });
    }
    setSeen(isActive);
  }, [firstMessage]);

  return (
    <div
      className="ChatListBox"
      style={{ border: `1px solid ${color}` }}
      onClick={() => {
        onClickHandler();
        setSeen(true);
      }}
    >
      {chatID}
      <div>
        {firstMessage?.username}:{firstMessage?.content}
        {!seen && <input type="checkbox" />}
      </div>
    </div>
  );
}
