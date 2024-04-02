import React from 'react';
import { IndividualMessageProps } from '../types/types';

export default function IndividualMessage({
  message,
  isSelf,
  isSameSenderAsPrev,
}: IndividualMessageProps) {
  return (
    <>
      {!isSameSenderAsPrev && (
        <div className={`Name ${isSelf ? 'Self' : ''}`}>{message.username}</div>
      )}
      <div className={`Message ${isSelf ? 'Self' : ''}`}>{message.content}</div>
    </>
  );
}
