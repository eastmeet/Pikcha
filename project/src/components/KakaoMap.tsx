import { useEffect } from "react";
//import Geocode from 'react-geocode';
import styled from "styled-components";


declare global {
  interface Window {
    kakao: any;
  }
}

// 안내창 커스텀입니다. 
let info = 
`<div style="width:190px;height:90px;background-color:white;padding:5px 5px;border:1px solid grey;border-radius:5px">
  <h3 style="color:#6255F8; width:100%;height:30px;background-color:#faf7df;line-height:30px">광화문</h3>
  <h3 style="font-size:13px;color:#393939;font-weight:400;padding:6px 0" > 주소 : 종로구 사직로 161</h3>
  <a href="www.naver.com" style="font-size:13px;text-decoration-line:none;margin-left:140px">더보기</a>
</div>`

  // 주소 더미 데이터
  var listData = [
    '종로구 사직로 161', 
    '종로구 세종대로 198',
    '종로구 세종대로 209', 
    '종로구 세종대로 175'
];

interface Map {
  width:string, 
  height:string,
  dataList:string[],
  position:any,
  left:string,
}


const KakaoMap = ({width, height, dataList, position, left}:Map) =>{
  

  useEffect(()=>{
    const container = document.getElementById('map');// 지도를 담을 dom영역

    // center에 위도, 경도 좌표를 설정 
    const options = {
      // center에 위도, 경도 좌표를 설정 
      center: new window.kakao.maps.LatLng(37.573898277022,126.9731314753), // 지도의 중심 좌표
      level:3 // 확대되어 보여지는 레벨  설정 
    };

    // 기본 주소 객체 생성 
    const map = new window.kakao.maps.Map(container,options);

  
    // 주소 - 좌표 변환
    // 주소-좌표 변환 객체를 생성합니다
    var geocoder = new window.kakao.maps.services.Geocoder();
    dataList.forEach(function(addr,index){
      geocoder.addressSearch(addr, function(result:any, status:any) {
        // 정상적으로 검색이 완료됐으면 
         if (status === window.kakao.maps.services.Status.OK) {
            var coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);
            // 결과값으로 받은 위치를 마커로 표시합니다
            var marker = new window.kakao.maps.Marker({
                map: map,
                position: coords
            });

            // 인포 윈도우 설정
            var infowindow = new window.kakao.maps.InfoWindow({
              content: info,
              disableAutoPan: false
            });
          infowindow.open(map, marker);
            // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
            map.setCenter(coords);
        } 
      });  
    })



    // Map Control
    // 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
    var mapTypeControl = new window.kakao.maps.MapTypeControl();
    // 지도에 컨트롤을 추가해야 지도위에 표시됩니다
    // kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
    map.addControl(mapTypeControl, window.kakao.maps.ControlPosition.TOPLEFT);


    // Marker
    // marker로 원하는 위도, 경도의 값을 나타냅니다. 
    // var marker = new window.kakao.maps.Marker({
    //   map:map,
    //   position:options.center
    // })

  },[])

  return(
    <>
    <div id="map" style={{width:width,height:height, position:position,left:left}}></div>
    </>
  )
}
export default KakaoMap;