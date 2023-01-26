<<<<<<< HEAD
<<<<<<< HEAD
import React, { useEffect } from "react";
=======
import React from "react";
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f
=======
import React from "react";
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f
import { useState } from "react";
import styled from "styled-components";
import LocationFilter from "../components/LocationFilter";
<<<<<<< HEAD
import { Header } from "../components/Header";
import axios from "axios";
import PlaceCardComponent from "../components/PlaceCardComponent";
import Loading from "../components/Loading";
import PaginationComponent from "../components/PaginationComponent";
=======
import { MdModeComment } from "react-icons/md";
import { Header } from "../components/Header";
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f

const PlaceWrapper = styled.div`
  display: flex;
  width: 83.5%;
  margin: 0 auto;
`;

const LocationWrapper = styled.nav`
  min-width: 210px;
  border-radius: var(--br-m);
  overflow: hidden;
  overflow-y: scroll;
  margin-top: 10px;
`;

const PlaceContainer = styled.div`
  margin: 0 20px;
  width: 80%;
`;

const PlaceFilterContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-left: 5px;
  height: 50px;

  > span {
    font-size: var(--font-base);
    color: var(--black-800);
    font-weight: var(--fw-bold);
  }
`;

export const FilterButton = styled.button`
  margin: 0 10px;
  padding-bottom: 3px;
  border: none;
  background-color: transparent;
  color: var(--black-900);
  font-weight: var(--fw-bold);
  cursor: pointer;

  &.active {
    color: var(--purple-400);
    border-bottom: 1px solid black;
  }
`;

const PlaceBox = styled.div`
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-top: 20px;
`;

export interface PlaceType {
  attractionId: number;
  attractionName: string;
  fixedImage: string;
  likes: number;
  numOfPosts: number;
  saves: number;
}

export interface PageInfoType {
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ArrayPlaceType extends Array<PlaceType> {}

const Place = () => {
  const [placesData, setPlacesData] = useState<ArrayPlaceType>();
  const [checkedList, setCheckedlist] = useState<string[]>([]);
  const [sortClick, setSortClick] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [onFilter, setOnFliter] = useState(0);
  const [curPage, setCurPage] = useState(1);

  const sortList: { kor: string; eng: string }[] = [
    {
      kor: "최신순",
      eng: "newest",
    },
    {
      kor: "리뷰순",
      eng: "posts",
    },
    {
      kor: "인기순",
      eng: "likes",
    },
  ];
  const handleSort = (idx: number) => {
    setOnFliter(idx);
  };
  useEffect(() => {
    setIsLoading(true);
    axios
      .get(`/attractions?page=1&size=100`)
      .then((res) => {
        setIsLoading(false);
        setPlacesData(res.data.data);
      })
      .catch((err) => console.error(err));
  }, []);

  const handleSortPlace = (sort: string) => {
    axios
      .post(`/attractions/filter?page=1&size=100&sort=${sort}`, {
        provinces: checkedList,
      })
      .then((res) => {
        setPlacesData(res.data.data);
        setSortClick(!sortClick);
      })
      .catch((err) => console.error(err));
  };
  return (
    <>
<<<<<<< HEAD
      <Header>
        <Header.HeaderTop />
        <Header.HeaderBody />
      </Header>
      <PlaceWrapper>
        <LocationWrapper>
          {placesData && (
            <LocationFilter
              setData={setPlacesData}
              checkedList={checkedList}
              setCheckedList={setCheckedlist}
            />
          )}
        </LocationWrapper>
        <PlaceContainer>
          <PlaceFilterContainer>
            <span>총 {placesData && placesData.length}개의 명소</span>
            <div>
              {sortList.map((sort, idx) => (
                <FilterButton
                  className={onFilter === idx ? "active" : ""}
                  key={idx}
                  onClick={() => {
                    handleSort(idx);
                    handleSortPlace(sort.eng);
                  }}
                >
                  {sort.kor}
                </FilterButton>
              ))}
            </div>
          </PlaceFilterContainer>
          {isLoading ? (
            <Loading />
          ) : (
            <>
              <PlaceBox>
                {placesData && (
                  <PlaceCardComponent
                    placesData={placesData}
                    limit={9}
                    curPage={curPage}
                    width="32%"
                    height="240px"
                  />
                )}
              </PlaceBox>
            </>
          )}
          {placesData && (
            <PaginationComponent
              props={placesData}
              limit={9}
              curPage={curPage}
              setCurPage={setCurPage}
            />
          )}
        </PlaceContainer>
      </PlaceWrapper>
=======
    <Header>
      <Header.HeaderTop />
      <Header.HeaderBody />
    </Header>
    <PlaceWrapper>
      <LocationWrapper>
        <LocationFilter />
      </LocationWrapper>
      <PlaceContainer>
        <PlaceFilterContainer>
          <span>총 {dummy.place.length}개의 명소</span>
          <div>
            {filter.map((filter, idx) => (
              <FilterButton
                className={onFilter === idx ? "active" : ""}
                key={idx}
                onClick={() => filtering(idx)}
              >
                {filter}
              </FilterButton>
            ))}
          </div>
        </PlaceFilterContainer>
        <PlaceBox>
          {dummy.place.map((el) => (
            <div key={el.locationId}>
              <img alt={el.title} src={el.img}></img>
              <PlaceInfo>
                <div className="info-title">
                  {el.title}
                  <div className="info-bookmark-recommend">
                    <BsFillBookmarkFill className="bookmark" /> {el.bookmark}
                    <AiFillHeart className="recommend" /> {el.recommend}
                  </div>
                </div>
                <div className="info-reviewCount">
                  <MdModeComment className="reviewCount" /> 포스트{" "}
                  {el.reviewCount}
                </div>
              </PlaceInfo>
            </div>
          ))}
        </PlaceBox>
      </PlaceContainer>
    </PlaceWrapper>
>>>>>>> f22c72ca01f31001cbc1f954051d1a14eac5230f
    </>
  );
};

export default Place;
