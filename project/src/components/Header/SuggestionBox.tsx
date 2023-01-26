import {
  useState,
  useEffect,
  SetStateAction,
  Fragment,
  Dispatch,
  MouseEvent,
} from "react";
import {
  AttractionItem,
  AttractionItemContent,
  SuggestionItemWrapper,
} from "./style";
import { TbArrowUpRight as ShortcutIcon } from "react-icons/tb";
import { BiLocationPlus as SearchMoreIcon } from "react-icons/bi";

const MAX_SUGGEST = 5;

interface SuggestionBoxProps {
  trimmedSearchValue: string;
  filteredAttractions: Array<AttractionsProps>;
  numOfFilteredAttractions: number;
  selected: number;
  onInputChange: Dispatch<SetStateAction<string>>;
  onSelectionChange: Dispatch<SetStateAction<number>>;
}

const SuggestionBox = ({
  trimmedSearchValue,
  filteredAttractions,
  numOfFilteredAttractions,
  selected,
  onInputChange,
  onSelectionChange,
}: SuggestionBoxProps) => {
  const [mouseTriggered, setMouseTriggered] = useState(false);

  useEffect(() => {
    if (selected !== -1 && filteredAttractions.length && !mouseTriggered) {
      if (selected !== MAX_SUGGEST + 1)
        onInputChange(filteredAttractions[selected]["info"]["name"]);
      else {
        onInputChange(trimmedSearchValue);
      }
    }
  }, [selected]);

  useEffect(() => {
    if (mouseTriggered) setMouseTriggered(false);
  }, [mouseTriggered]);
<<<<<<< HEAD

=======
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f
  const handleMouseOver = () => {
    setMouseTriggered(true);
    onSelectionChange(MAX_SUGGEST + 1);
  };
  return (
    <SuggestionItemWrapper>
      {filteredAttractions.length ? (
        filteredAttractions.map((el, i) => (
          <SuggestionItem
            attraction={el}
            key={el.info.id}
            trimmedSearchValue={trimmedSearchValue}
            selectedEl={selected === i}
            onSelectionChange={onSelectionChange}
            onMouseEvent={setMouseTriggered}
            order={i}
          />
        ))
      ) : (
        <AttractionItemContent as="li" type="notice">
          {trimmedSearchValue === ""
            ? "검색어를 입력해주세요"
            : "추천 검색어가 없습니다"}
        </AttractionItemContent>
      )}
      {numOfFilteredAttractions > MAX_SUGGEST && (
        <AttractionItem
          selectedEl={selected === MAX_SUGGEST + 1}
          onMouseOver={handleMouseOver}
        >
          <AttractionItemContent as="li" type="more-result">
            <SearchMoreIcon className="more-search" />
            {`"${trimmedSearchValue}"에 대한 모든 검색결과 보기 `}
          </AttractionItemContent>
        </AttractionItem>
      )}
    </SuggestionItemWrapper>
  );
};

interface AttractionsProps {
  info: { name: string; id: number; address: string };
<<<<<<< HEAD
  matchedLetter: number[][];
  exactMatchedLetter: number[][];
=======
  matchedletter: number[][];
  exactmatchedletter: number[][];
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f
}

interface SearchBarItemProps {
  trimmedSearchValue: string;
  attraction: AttractionsProps;
  selectedEl: Boolean;
  onSelectionChange: Dispatch<SetStateAction<number>>;
  onMouseEvent: Dispatch<SetStateAction<boolean>>;
  order: number;
}

const SuggestionItem = ({
  trimmedSearchValue,
<<<<<<< HEAD
  attraction: { info, exactMatchedLetter },
=======
  attraction: { info, exactmatchedletter },
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f
  selectedEl,
  onSelectionChange,
  onMouseEvent,
  order,
}: SearchBarItemProps) => {
  const handleMouseOver = (e: MouseEvent<HTMLDivElement>) => {
    onMouseEvent(true);
    onSelectionChange(order);
  };

<<<<<<< HEAD
  let letterIndex = exactMatchedLetter.flat();
=======
  let letterIndex = exactmatchedletter.flat();
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f

  return (
    <AttractionItem selectedEl={selectedEl} onMouseOver={handleMouseOver}>
      <a href="https://www.naver.com">
        <AttractionItemContent type="name">
          {letterIndex.map((el, i, arr) => {
            let formerIndex = i === 0 ? 0 : arr[i - 1] + 1;
            return i % 2 === 0 ? (
              <Fragment key={i}>{info.name.slice(formerIndex, el)}</Fragment>
            ) : (
              <strong key={i}>{trimmedSearchValue}</strong>
            );
          })}
          {info.name.slice(letterIndex[letterIndex.length - 1] + 1)}
        </AttractionItemContent>
        <AttractionItemContent type="address">
          {info.address}
        </AttractionItemContent>
      </a>
      <ShortcutIcon />
    </AttractionItem>
  );
};

export { SuggestionBox };
