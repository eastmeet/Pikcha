import {
  HeaderWrapper,
  HeaderTop,
  HeaderTopMenu,
  HeaderBody,
  HeaderBodyMenu,
  HeaderBodyWrapper,
  SearchBarWrapper,
  Profile,
} from "./style";
// import { ReactComponent as Logo } from "./../../data/Templogo.svg";
// import logo from "../../../data/logo.png";
import { useNavigate } from "react-router-dom";
import { useRecoilState, useRecoilValue } from "recoil";
import { LoginState, AuthToken, LoggedUser } from "../../recoil/state";
import axios from "axios";
import ButtonForm from "../Button";
import { lazy, ReactNode, MouseEventHandler } from "react";

const SearchBar = lazy(() => import("./SearchBar"));
// import { ReactComponent as Logo } from "./../../data/Templogo.svg";

const IMG_SRC =
  "https://drive.google.com/uc?id=1OmsgU1GLU9iUBYe9ruw_Uy1AcrN57n4g";

const HeaderTopBar = () => {
  const navigate = useNavigate();
  const [isLogin, setIslogin] = useRecoilState(LoginState);
  const [auth, setAuth] = useRecoilState<string>(AuthToken);
  // const [refresh, setRefresh] = useRecoilState<string>(RefreshToken);
  const [loggedUser, setLoggedUser] = useRecoilState<string>(LoggedUser);

  const localLogin = localStorage.getItem("loginStatus");
  if (typeof localLogin === "string") {
    setIslogin(JSON.parse(localLogin));
  }

  const onClickLogout = (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) => {
    e.preventDefault();
    setIslogin(false);
    setAuth("");
    setLoggedUser("");
    axios.defaults.headers.common["Authorization"] = null;
    localStorage.removeItem("Authorization");
    localStorage.setItem("loginStatus", "false");
    localStorage.removeItem("memberId");
    navigate("/");
  };

  return (
    <HeaderTop>
      <HeaderTopMenu>
        {isLogin ? (
          <>
            <li>
              <ButtonForm
                width="70px"
                height="1px"
                text="마이페이지"
                type="none"
              ></ButtonForm>
            </li>
            <li>
              <ButtonForm
                width="50px"
                height="1px"
                text="로그아웃"
                type="none"
                onClick={onClickLogout}
              ></ButtonForm>
            </li>
          </>
        ) : (
          <li>
            <ButtonForm
              width="100px"
              height="1px"
              text="로그인 / 회원가입"
              type="none"
              onClick={() => navigate("/login")}
            ></ButtonForm>
          </li>
        )}
      </HeaderTopMenu>
    </HeaderTop>
  );
};
interface HeaderBodyProps {
  searchBarOn?: boolean;
  defaultValue?: string;
  backgroundOn?: boolean;
  isSuggetionVisible?: boolean;
}
const HeaderBodyBar = ({
  searchBarOn = true,
  defaultValue = "",
  backgroundOn = true,
}: HeaderBodyProps) => {
  const navigate = useNavigate();
  const islogin = useRecoilValue(LoginState);
  return (
    <HeaderBodyWrapper backgroundOn={backgroundOn}>
      <HeaderBody>
        <a
          href="/"
          style={{ height: "70px", display: "flex", alignItems: "center" }}
        >
          <img
            src={process.env.PUBLIC_URL + "/logo.png"}
            alt="logo"
            style={{
              width: "180px",
              height: "30px",
              backgroundSize: "cover",
            }}
          />
        </a>
        <HeaderBodyMenu>
          <li onClick={() => navigate("/attractions")}>명소</li>
          <li onClick={() => navigate("/posts")}>방문리뷰</li>
          <li onClick={() => navigate("/map")}>내 주변 명소 찾기</li>
        </HeaderBodyMenu>
        {searchBarOn && (
          <SearchBarWrapper>
            <SearchBar defaultValue={defaultValue} />
          </SearchBarWrapper>
        )}
        {islogin && (
          <Profile>
            <img src={IMG_SRC} alt="profile" />
          </Profile>
        )}
      </HeaderBody>
    </HeaderBodyWrapper>
  );
};
interface HeaderMainProps {
  children?: ReactNode;
  mouseOverHandler?: MouseEventHandler<HTMLElement>;
  mouseOutHandler?: MouseEventHandler<HTMLElement>;
  isVisible?: boolean;
  headerColor?: string;
}
const HeaderMain = ({
  children,
  mouseOverHandler,
  mouseOutHandler,
  isVisible = true,
  headerColor,
}: HeaderMainProps) => {
  return (
    <HeaderWrapper
      onMouseEnter={mouseOverHandler}
      onMouseLeave={mouseOutHandler}
      isVisible={isVisible}
      headerColor={headerColor}
    >
      {children}
    </HeaderWrapper>
  );
};
export const Header = Object.assign(HeaderMain, {
  HeaderTop: HeaderTopBar,
  HeaderBody: HeaderBodyBar,
});