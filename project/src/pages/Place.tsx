import React, { useEffect } from "react";
import { useState } from "react";
import styled from "styled-components";
import LocationFilter from "../components/LocationFilter";
import { Header } from "../components/Header";
import axios from "axios";
import { useRecoilState } from "recoil";
import {
  locationFilterValue,
  pageInfoData,
  placeInfoData,
} from "../recoil/state";
import PlaceCardComponent from "../components/PlaceCardComponent";
import Loading from "../components/Loading";
import Pagination from "../components/Pagination";
import { useLocation } from "react-router-dom";

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
`;

const PlaceContainer = styled.div`
  margin: 0 20px;
  width: 80%;
`;

const PlaceFilterContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;

  > span {
    font-size: var(--font-base);
    color: var(--black-800);
    font-weight: var(--fw-bold);
  }
  > div {
    margin-right: 3%;
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
  const [placesData, setPlacesData] = useRecoilState(placeInfoData);
  const [placesPageInfo, setPlacesPageInfo] = useRecoilState(pageInfoData);
  const [checkedList] = useRecoilState(locationFilterValue);
  const [sortClick, setSortClick] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [onFilter, setOnFliter] = useState(0);

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
      .get(`/attractions`)
      .then((res) => {
        setIsLoading(false);
        console.log(res.data);
      })
      .catch((err) => console.error(err));
  }, [placesData]);
  const handleSortPlace = (sort: string) => {
    axios
      .post(`/attractions/filter?sort=${sort}`, {
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
            <span>
              총 {placesPageInfo && placesPageInfo.totalElements}개의 명소
            </span>
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
                {placesData &&
                  placesData.map((data, idx) => (
                    <PlaceCardComponent key={idx} data={data} />
                  ))}
              </PlaceBox>
              {placesPageInfo && <Pagination />}
            </>
          )}
        </PlaceContainer>
      </PlaceWrapper>
    </>
  );
};

export default Place;
