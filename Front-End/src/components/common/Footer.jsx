import React, { useState } from "react";
import { Link, useLocation, useParams, useNavigate } from "react-router-dom";
import styles from "./Footer.module.css";
import Notification from "components/customer/mainPage/Notification";
import { TbHome, TbMessage2Star } from "react-icons/tb";
import { LuSettings } from "react-icons/lu";
import { RxPerson } from "react-icons/rx";
import { PiShoppingCartBold } from "react-icons/pi";
import { BiBell } from "react-icons/bi";
import useLiveStore from "store/live/useLiveStore";
import useTruckStore from "store/users/owner/truckStore";

const Footer = () => {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [role, setRole] = useState(sessionStorage.getItem("role"));
  const [showNotification, setNotification] = useState(false);
  const [activeIcon, setActiveIcon] = useState(""); // 현재 클릭된 아이콘을 저장
  const location = useLocation();
  const { leaveSessionStore } = useLiveStore();
  const { toggleLive } = useTruckStore();
  const navigate = useNavigate();

  const { storeId } = useParams();

  const handleNotificationClick = () => {
    setNotification(!showNotification);
    setActiveIcon("notification");
  };

  const toggleDropdown = () => {
    setIsDropdownOpen(!isDropdownOpen);
    if (activeIcon === "settings") {
      setActiveIcon("");
    } else {
      setActiveIcon("settings");
    }
  };

  const handleIconClick = (icon) => {
    if (location.pathname.startsWith("/live/")) {
      leaveSessionStore();
    }
    setActiveIcon(icon);
  };

  const handleIconClickOwner = (icon, targetPath) => {
    if (location.pathname.startsWith("/live/")) {
      alert("방송을 종료해주세요.");
      return;
    }
    setActiveIcon(icon);
    navigate(targetPath);
  };

  if (role.indexOf("customer") !== -1) {
    return (
      <>
        <footer className={styles.footer}>
          <Link
            to="/mainCustomer"
            className={styles.menuItem}
            onClick={() => handleIconClick("home")}
          >
            <TbHome
              size="35"
              color={
                activeIcon === "home" && location.pathname === "/mainCustomer"
                  ? "#ffcc66"
                  : "black"
              }
            />
          </Link>
          <div className={styles.menuItem} onClick={handleNotificationClick}>
            <BiBell
              size="36"
              color={activeIcon === "notification" ? "#ffcc66" : "black"}
            />
          </div>
          <Link
            to="/cart"
            className={styles.menuItem}
            onClick={() => handleIconClick("cart")}
          >
            <PiShoppingCartBold
              size="34"
              color={
                activeIcon === "cart" && location.pathname === "/cart"
                  ? "#ffcc66"
                  : "black"
              }
            />
          </Link>
          <Link
            to="/mypageCustomer"
            className={styles.menuItem}
            onClick={() => handleIconClick("mypage")}
          >
            <RxPerson
              size="34"
              color={
                activeIcon === "mypage" &&
                location.pathname === "/mypageCustomer"
                  ? "#ffcc66"
                  : "black"
              }
              strokeWidth="0.4"
            />
          </Link>
        </footer>
        {showNotification ? (
          <Notification
            show={showNotification}
            onClose={() => {
              setNotification(false);
              setActiveIcon("");
            }}
          />
        ) : null}
      </>
    );
  } else if (role.indexOf("owner") !== -1) {
    return (
      <footer className={styles.footer}>
        <Link
          to="/mainOwner"
          className={styles.menuItem}
          onClick={(e) => {
            e.preventDefault();
            handleIconClickOwner("home", "/mainOwner");
          }}
        >
          <TbHome
            size="35"
            color={
              activeIcon === "home" && location.pathname === "/mainOwner"
                ? "#b6d4b7"
                : "black"
            }
          />
        </Link>
        <Link
          to="/mypageOwner"
          className={styles.menuItem}
          onClick={(e) => {
            e.preventDefault();
            handleIconClickOwner("mypage", "/mypageOwner");
          }}
        >
          <RxPerson
            size="34"
            color={
              activeIcon === "mypage" && location.pathname === "/mypageOwner"
                ? "#b6d4b7"
                : "black"
            }
            strokeWidth="0.4"
          />
        </Link>
        <Link
          to="/ownerReview"
          className={styles.menuItem}
          onClick={(e) => {
            e.preventDefault();
            handleIconClickOwner("review", "/ownerReview");
          }}
        >
          <TbMessage2Star
            size="35"
            color={
              activeIcon === "review" && location.pathname === "/ownerReview"
                ? "#b6d4b7"
                : "black"
            }
          />
        </Link>
        <div
          className={styles.menuItem}
          onClick={() => {
            toggleDropdown();
          }}
        >
          <LuSettings
            size="35"
            color={activeIcon === "settings" ? "#b6d4b7" : "black"}
          />
          {isDropdownOpen && (
            <div className={styles.dropdown}>
              <Link
                to="/manageTruck"
                onClick={(e) => {
                  e.preventDefault();
                  handleIconClickOwner("settings", "/manageTruck");
                }}
              >
                트럭정보수정
              </Link>
              <Link
                to="/manageSchedule"
                onClick={(e) => {
                  e.preventDefault();
                  handleIconClickOwner("settings", "/manageSchedule");
                }}
              >
                스케줄관리
              </Link>
              <Link
                to="/manageMenu"
                onClick={(e) => {
                  e.preventDefault();
                  handleIconClickOwner("settings", "/manageMenu");
                }}
              >
                메뉴관리
              </Link>
            </div>
          )}
        </div>
      </footer>
    );
  }
  return null;
};

export default Footer;
