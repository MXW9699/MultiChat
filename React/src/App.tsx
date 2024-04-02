import React, { useEffect, useState, useRef, useCallback } from 'react';
import { createRoot } from 'react-dom/client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import ChatDisplay from './components/ChatDisplay';
import { RefType, TextMessage, UserInfo } from './types/types';
import ChatListBox from './components/ChatListBox';
import SearchBar from './components/SearchBar';

/**navbar
 *  people online
 *  add chats
 *chatbox
 */
export default function App() {
  const [userID, setUserID] = useState<string>('');
  const [chatList, setChatList] = useState<string[]>([]);
  const [activeChat, setActiveChat] = useState<number>(null);
  const [usersList, setUsersList] = useState<string[]>([]);
  const [connected, setConnected] = useState<boolean>(false);
  const stompClient = useRef<CompatClient | null>(null);
  const searchRef = useRef<RefType>(null);

  const subscribeSelf = useCallback(() => {
    setConnected(true);
    stompClient.current.subscribe(`/topic/user/${userID}`, (message) => {
      console.log(message.body);
      console.log(activeChat);
      setChatList((prev) => [...prev, message.body]);
    });
  }, [userID]);

  const WSURL = 'ws://localhost:8081/ws';
  const BASE_URL = 'http://localhost:8080';

  //fetch user info and set userID and ChatList
  async function getUserInfo(username: string): Promise<UserInfo> {
    try {
      const response = await fetch(`${BASE_URL}/users/${username}`);
      if (!response.ok) return Promise.reject('invalid username');
      const data: UserInfo = await response.json();
      setUserID(data.username);
      setChatList(data.chatList);
      setActiveChat(0);
    } catch (e) {
      console.log(e);
    }
  }

  async function getListOfUsers(): Promise<void> {
    try {
      const response = await fetch(`${BASE_URL}/users`);
      if (!response.ok) throw new Error('could not get list of users');
      else {
        const data = await response.json();
        const listOfUsers = data.map((el: { username: string }) => el.username);
        setUsersList(listOfUsers);
      }
    } catch (e) {
      console.log(e);
    }
  }

  async function newChatHandler(text: string) {
    const names = searchRef.current.getSearch();
    try {
      const memberList = [userID, ...names];
      //add chat to database
      const response = await fetch(`${BASE_URL}/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ names: memberList }),
      });
      if (!response.ok) {
        return Promise.reject('could not make new chat');
      }
      const newChat = await response.json();
      const newChatID = newChat._id;
      const newText = new TextMessage(userID, text, newChatID);
      // send message to new chat
      await fetch(`${BASE_URL}/users/${userID}/chat/${newChatID}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: text,
      });
      stompClient.current.publish({
        destination: `/app/chat/${newChatID}`,
        headers: { newChat: 'true' },
        body: JSON.stringify(newText),
      });
      setActiveChat(chatList.length);
    } catch (e) {
      console.log(e);
    }
  }

  useEffect(() => {
    if (userID == '') {
      const username = prompt('what is your name');
      getUserInfo(username);
      getListOfUsers();
      const client = Stomp.client(WSURL);
      stompClient.current = client;
    } else {
      stompClient.current.connect({}, subscribeSelf);
      stompClient.current.activate();
    }
    return () => {
      stompClient.current.deactivate();
    };
  }, [userID]);

  return (
    <div>
      welcome to the chatroom {userID}
      {connected && (
        <div className="Chat">
          <div className="ChatList">
            {chatList.map((chatID, idx) => {
              return (
                <ChatListBox
                  key={`${chatID}`}
                  chatID={`${chatID}`}
                  isActive={activeChat === idx}
                  client={stompClient}
                  onClickHandler={() => setActiveChat(idx)}
                />
              );
            })}
            <button onClick={() => setActiveChat(null)}>add chat</button>
          </div>
          {activeChat == null && (
            <SearchBar options={usersList} ref={searchRef} />
          )}
          <ChatDisplay
            chatID={activeChat == null ? '' : chatList[activeChat]}
            username={userID}
            client={stompClient}
            newChatHandler={newChatHandler}
          />
        </div>
      )}
    </div>
  );
}

const root = createRoot(document.getElementById('root'));
root.render(<App />);
