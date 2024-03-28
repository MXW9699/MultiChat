import React, { useEffect, useState, useRef } from 'react';
import { createRoot } from 'react-dom/client';
import { CompatClient, Stomp } from '@stomp/stompjs';
import ChatDisplay from './components/ChatDisplay';
import { UserInfo } from './types/types';
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
  const stompClient = useRef<CompatClient | null>(null);

  const WSURL = 'ws://localhost:8081/ws';

  //fetch user info and set userID and ChatList
  async function getUserInfo(username: string): Promise<UserInfo> {
    try {
      const response = await fetch(`http://localhost:8080/users/${username}`);
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
      const response = await fetch('http://localhost:8080/users');
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

  function newChatHandler() {
    setActiveChat(null);
    getListOfUsers();
    // fetch('http://localhost:8080/chat', {
    //   method: 'POST',
    //   headers: { 'Content-Type': 'application/json' },
    //   body: JSON.stringify(['Mitch', 'Jon', 'Bob']),
    // })
    //   .then((res) => {
    //     if (res.ok) return res.json();
    //     else return Promise.reject('could not make new chat');
    //   })
    //   .then((res) => console.log(res))
    //   .catch((e) => console.log(e));
  }

  useEffect(() => {
    const username = prompt('what is your name');
    getUserInfo(username);
    const client = Stomp.client(WSURL);
    stompClient.current = client;
    stompClient.current.activate();
  }, []);

  return (
    <div>
      welcome to the chatroom {userID}
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
          <button onClick={() => newChatHandler()}>add chat</button>
        </div>
        {activeChat != null
          ? userID != '' && (
              <ChatDisplay
                chatID={chatList[activeChat]}
                username={userID}
                client={stompClient}
              />
            )
          : <SearchBar options={usersList}/>}
      </div>
    </div>
  );
}

const root = createRoot(document.getElementById('root'));
root.render(<App />);
