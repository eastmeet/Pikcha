import * as Hangul from "hangul-js";
interface AttractionsDataProps {
  name: string;
  id: number;
  address: string;
}
interface AttractionProps {
  info: AttractionsDataProps;
  matchedLetter: number[][];
  exactMatchedLetter: number[][];
}
function getfilteredAttractions(
  AttractionsData: Array<AttractionsDataProps>,
  trimmedSearchValue: string,
  maxSuggest: number
) {
  if (trimmedSearchValue === "")
    return {
      trimmedSearchValue,
      filteredAttractions: [],
      numOfFilteredAttractions: 0,
    };
  let init: Array<AttractionProps> = [];
  const blankNumRemovedValue = trimmedSearchValue.replace(/[0-9 ]/g, "");

  let filteredAttractions = AttractionsData.reduce((acc, attraction) => {
    const searcher = new Hangul.Searcher(blankNumRemovedValue);
    const matchedLetter = Hangul.rangeSearch(
      attraction.name,
      trimmedSearchValue
    );

    if (searcher.search(attraction.name.replace(/[0-9 ]/g, "")) === -1)
      return acc;
<<<<<<< HEAD:project/src/utils.ts
    const exactMatchedLetter = matchedLetter.filter(
=======
    const exactmatchedletter = matchedletter.filter(
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f:project/src/utils/functions.tsx
      (el: any) =>
        attraction.name[el[1]] === trimmedSearchValue.slice(-1) &&
        Hangul.isCompleteAll(blankNumRemovedValue)
    );
    return [
      ...acc,
      {
        info: attraction,
        matchedLetter,
        exactMatchedLetter,
      },
    ];
<<<<<<< HEAD:project/src/utils.ts
  }, init).sort((prev, next) => {
    let diff = prev.matchedLetter[0]?.[0] - next.matchedLetter[0]?.[0];
    let exactDiff = null;
=======
  }, init).sort((a, b) => {
    let diff = a.matchedletter[0]?.[0] - b.matchedletter[0]?.[0];
    let exactDiff: number | null = null;
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f:project/src/utils/functions.tsx

    if (!prev.exactMatchedLetter.length && next.exactMatchedLetter.length)
      return 1;
    if (prev.exactMatchedLetter.length && !next.exactMatchedLetter.length)
      return -1;
    if (prev.exactMatchedLetter.length && next.exactMatchedLetter.length) {
      exactDiff = prev.exactMatchedLetter[0][0] - next.exactMatchedLetter[0][0];
    }

    if (exactDiff) return exactDiff;
    if (diff) return diff;
    return prev.info.name.localeCompare(next.info.name, "ko");
  });

  return {
    filteredAttractions: filteredAttractions.slice(0, maxSuggest + 1),
    numOfFilteredAttractions: filteredAttractions.length,
  };
}

const throttle = (callback: Function, delay: number, e?: MouseEvent) => {
  let timerId: ReturnType<typeof setTimeout> | null = null;
  return () => {
    if (timerId) {
      return;
    }
    timerId = setTimeout(() => {
      callback(e);
      timerId = null;
    }, delay);
  };
};

const delaySetter = (ms: number) => {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
};
export { getfilteredAttractions, throttle, delaySetter };
