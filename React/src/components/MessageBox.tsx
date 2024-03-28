import React from 'react';
import { Message } from '../types/types';

export default function MessageBox({
  message,
  self,
  same
}: {
  message: Message;
  self: Boolean;
  same:boolean
}) {
  return (
    <>
      {!same&&<div className={`Name ${self ? 'Self' : ''}`}>{message.username}</div>}
    <div className={`Message ${self ? 'Self' : ''}`}>
      {message.content}
    </div>
    </>
  );
}
