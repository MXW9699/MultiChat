import React, { useEffect, useRef, useState } from 'react';

export default function SearchBar({ options }: { options: string[] }) {
  const [search, setSearch] = useState(['']);
  const [nameOptions, setNameOptions] = useState(options);
  const [tagIndex, setTagIndex] = useState(0);
  const inputRef = useRef(null);

  function updateSearch(e: any) {
    const searchArray = e.target.value.split(', ');
    setNameOptions(
      options.filter((text) =>
        text
          .toLowerCase()
          .includes(searchArray[searchArray.length - 1].toLowerCase())
      )
    );
    setSearch(searchArray);
  }

  useEffect(() => {
    window.addEventListener('keydown', (e) => {
      if (e.key == 'Tab' && nameOptions[0] && inputRef.current) {
        e.preventDefault()
        const temp = [...search];
        temp[temp.length - 1] = nameOptions[0];
        inputRef.current.value = temp.toString();
        // inputRef.current.select()
        // setSearch(temp);
      }
      console.log(search);
    });
    return () =>
      window.removeEventListener('keydown', (e) => {
        console.log(e);
      });
  });

  return (
    <div>
      <input onChange={updateSearch} ref={inputRef} />
      {nameOptions.map((el) => (
        <div>{el}</div>
      ))}
    </div>
  );
}
