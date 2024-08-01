import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './MapCustomer.module.css';
import markerImage from 'assets/images/ft_marker.png'; // 이미지 경로 import
import useFoodTruckStore from 'store/trucks/useFoodTruckStore'; // store.js의 경로에 맞게 수정

function MapCustomer({ selectedType }) {
  const navigate = useNavigate();
  const { foodTrucks, openFoodTruck, getFoodTruck } = useFoodTruckStore();

  useEffect(() => {
    openFoodTruck();
  }, [openFoodTruck]);

  useEffect(() => {
    const apiKey = 'bb662920ed50821a974fe0e873815b8b';
    const script = document.createElement('script');
    script.async = true;
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${apiKey}&autoload=false`;
    document.head.appendChild(script);

    script.onload = () => {
      if (!window.kakao || !window.kakao.maps) {
        console.error('Kakao maps library is not loaded properly.');
        return;
      }

      window.kakao.maps.load(() => {
        const container = document.getElementById('map'); // 지도를 표시할 div
        const options = {
          center: new window.kakao.maps.LatLng(36.3559, 127.3319), // 대전광역시 유성구의 중심 좌표
          level: 3, // 지도의 확대 레벨
        };

        const map = new window.kakao.maps.Map(container, options); // 지도 생성 및 객체 리턴

        const markerImageSize = new window.kakao.maps.Size(45, 45); // 아이콘 이미지 크기
        const markerImageOptions = {
          offset: new window.kakao.maps.Point(22.5, 45) // 아이콘 이미지의 좌표
        };

        const markerImageObj = new window.kakao.maps.MarkerImage(
          markerImage,
          markerImageSize,
          markerImageOptions
        );

        console.log(foodTrucks);

        const addMarker = (location) => {
          console.log(location);
          const latitude = parseFloat(location.latitude);
          const longitude = parseFloat(location.longitude);
          if (isNaN(latitude) || isNaN(longitude)) {
            console.error('Invalid latitude or longitude for location:', location);
            return;
          }

          const markerPosition = new window.kakao.maps.LatLng(latitude, longitude);
          const marker = new window.kakao.maps.Marker({
            position: markerPosition,
            image: markerImageObj // 커스텀 아이콘 설정
          });
          marker.setMap(map);

          const infowindow = new window.kakao.maps.InfoWindow({
            content: `<div style="padding:5px;">${location.name}</div>`
          });

          // InfoWindow를 마커 위에 강제로 표시
          infowindow.open(map, marker);

          window.kakao.maps.event.addListener(marker, 'click', async function() {
            await getFoodTruck(location.storeId);
            navigate(`/foodtruckDetail/${location.storeId}`);
          });
        };

        // Ensure foodTrucks is processed as an array of storeInfoResponseDtos
        const locations = foodTrucks.storeInfoResponseDtos || [];

        locations.forEach(location => {
          addMarker(location);
        });
      });
    };

    script.onerror = () => {
      console.error('카카오맵 불러오기에 실패하였습니다.');
    };

  }, [navigate, foodTrucks, getFoodTruck]);

  return (
    <div className={styles.mapContainer}>
      <div id="map" className={styles.map}></div>
    </div>
  );
}

export default MapCustomer;
