import React, { FormEvent, useEffect, useRef, useState } from 'react';
import { ChatDisplayProps, Message, TextMessage } from '../types/types';
import MessageBox from './MessageBox';

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
  }, [chatID]);

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  //publish to endpoint update visual
  //reset inputbar
  function sendHandler(e: FormEvent) {
    e.preventDefault();
    if (text == '') return;
    const newText = new TextMessage(username, text, chatID);
    fetch(`${BASE_URL}/users/${username}/chat/${chatID}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: text,
    }).then((res) => {
      console.log('sending message to websocket');
      client.current.publish({
        destination: `/app/chat/${chatID}`,
        body: JSON.stringify(newText),
      });
      setText('');
    });
  }

  return (
    <div className="chatSection">
      <div className="ChatDisplay" ref={ref}>
        {messages.map((message, idx) => {
          const self = message.username == username;
          const same = messages[idx - 1]?.username == message.username;
          return (
            <MessageBox key={idx} message={message} self={self} same={same} />
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
