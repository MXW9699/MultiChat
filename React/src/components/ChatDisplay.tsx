import React, { FormEvent, useEffect, useRef, useState } from 'react';
import { ChatDisplayProps, Message, TextMessage } from '../types/types';
import IndividualMessage from './IndividualMessage';

/**
 * display message-> dependent on the chat that is selected
 * input
 * send
 */
const BASE_URL = 'http://localhost:8080';

export default function ChatDisplay({
  chatID,
  username,
  client,
  newChatHandler,
}: ChatDisplayProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [text, setText] = useState<string>('');
  const ref = useRef<HTMLDivElement>();

  function getChatMessages() {
    fetch(`${BASE_URL}/chat/${chatID}`)
      .then((res) => res.json())
      .then((res) => setMessages(res));
  }

  function scrollToBottom() {
    ref.current.scrollTop = ref.current.scrollHeight;
  }

  useEffect(() => {
    if (chatID == '' || !chatID) setMessages([]);
    else {
      getChatMessages();
      const subscription = client.current.subscribe(
        `/topic/chat/${chatID}`,
        (message) => {
          const newText: TextMessage = JSON.parse(message.body) as TextMessage;
          setMessages((prev) => [...prev, newText]);
        }
      );
      return () => {
        subscription.unsubscribe();
      };
    }
  }, [chatID]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  //publish to endpoint update visual
  //reset inputbar
  async function sendHandler(e: FormEvent) {
    e.preventDefault();
    if (text == '') return;
    if (chatID == '' || !chatID) newChatHandler(text);
    else {
      const newText = new TextMessage(username, text, chatID);
      await fetch(`${BASE_URL}/users/${username}/chat/${chatID}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: text,
      });
      client.current.publish({
        destination: `/app/chat/${chatID}`,
        body: JSON.stringify(newText),
      });
    }
    setText('');
  }

  return (
    <div className="chatSection">
      <div className="ChatDisplay" ref={ref}>
        {messages.map((message, idx) => {
          const self = message.username == username;
          const same = messages[idx - 1]?.username == message.username;
          return (
            <IndividualMessage
              key={idx}
              message={message}
              isSelf={self}
              isSameSenderAsPrev={same}
            />
          );
        })}
      </div>
      <form onSubmit={(e) => sendHandler(e)}>
        <input
          type="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
        <input type="submit"></input>
      </form>
    </div>
  );
}
