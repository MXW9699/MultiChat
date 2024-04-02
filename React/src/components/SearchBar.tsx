import React, {
  ChangeEvent,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from 'react';
import { forwardRef } from 'react';
import { RefType, SearchBarProps } from '../types/types';

const SearchBar = forwardRef<RefType, SearchBarProps>(function SearchBar(
  { options },
  ref
) {
  const [search, setSearch] = useState<string[]>(['']);
  const [nameOptions, setNameOptions] = useState<string[]>(options);
  const inputRef = useRef<HTMLInputElement>(null);
  const [recommendationsOpen, setRecommendationsOpen] =
    useState<Boolean>(false);

  useImperativeHandle(ref, () => ({
    getSearch: () => search.toString().split(','),
  }));

  function updateSearch(e: ChangeEvent<HTMLInputElement>) {
    const searchArray = e.target.value.split(', ');
    const last = searchArray[searchArray.length - 1];
    const newOptions = options.filter(
      (text) =>
        !searchArray.includes(text) &&
        text.toLowerCase().includes(last.toLowerCase())
    );
    setNameOptions(newOptions);
    setSearch(searchArray);
  }

  function tabAutoFill(e: KeyboardEvent) {
    if (e.key == 'Tab' && document.activeElement == inputRef.current) {
      e.preventDefault();
      if (nameOptions.length) {
        const temp = [...search];
        temp[temp.length - 1] = nameOptions[0];
        inputRef.current.value = temp.join(', ');
      }
    }
  }

  function showRecomendations() {
    document.activeElement == inputRef.current
      ? setRecommendationsOpen(true)
      : setRecommendationsOpen(false);
  }

  function selectName(name: string) {
    const temp = [...search];
    temp[temp.length - 1] = name;
    inputRef.current.value = temp.join(', ');
    setSearch(temp);
    inputRef.current.focus();
  }

  useEffect(() => {
    window.addEventListener('keydown', tabAutoFill);
    window.addEventListener('click', showRecomendations);
    return () => {
      window.removeEventListener('keydown', tabAutoFill);
      window.removeEventListener('click', showRecomendations);
    };
  }, [nameOptions, search]);

  return (
    <div>
      <input onChange={updateSearch} ref={inputRef} />
      <div className="recommendedNamesContainer">
        {recommendationsOpen &&
          nameOptions.map((el) => (
            <div className="recommendedNames" onClick={() => selectName(el)}>
              {el}
            </div>
          ))}
      </div>
    </div>
  );
});

export default SearchBar;
