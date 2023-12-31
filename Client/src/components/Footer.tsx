import { useNavigate } from "react-router-dom";
import {
  FooterContainer,
  FooterText,
  FooterWrapper,
  FooterBottomText,
  LogoContainer,
  MadePeopleContainer,
  FooterBottomContainer,
  LinkIconContainer,
  LinkWrapper,
  EmailContainer,
} from "./FooterStyled";
import { BsGithub as GithubIcon } from "react-icons/bs";
import { CgFigma as FigmaIcon } from "react-icons/cg";
import { IoMailOutline as EmailIcon } from "react-icons/io5";
import { ReactComponent as Logo } from "../data/Logo.svg";
const Footer = () => {
  const navigate = useNavigate();

  return (
    <FooterWrapper>
      <FooterContainer>
        <LogoContainer>
          <Logo onClick={() => navigate("/")} />
        </LogoContainer>
        <MadePeopleContainer>
          <FooterText marginRight="10px">김진주 |</FooterText>
          <FooterText marginRight="10px">박혜수 |</FooterText>
          <FooterText marginRight="10px">이동우 |</FooterText>
          <FooterText marginRight="10px">이상유 |</FooterText>
          <FooterText marginRight="10px">이승현 |</FooterText>
          <FooterText marginRight="10px">정세민</FooterText>
        </MadePeopleContainer>

        <LinkWrapper>
          <LinkIconContainer
            href="https://github.com/codestates-seb/seb41_main_036"
            className="github-icon"
          >
            <GithubIcon />
          </LinkIconContainer>
          <LinkIconContainer
            href="https://www.figma.com/file/iFs2WMfNlUTOA6ILnCljk3/main-project?node-id=0%3A1&t=FKufgjpDJAMZQ80B-1"
            className="figma-icon"
          >
            <FigmaIcon />
          </LinkIconContainer>
          <EmailContainer>
            <EmailIcon />
            명소 등록 & 사이트 문의 :
            <a href="mailto:pikchainc@gmail.com"> &nbsp;pikchainc@gmail.com</a>
          </EmailContainer>
        </LinkWrapper>
        <hr />
        <FooterBottomContainer>
          <FooterBottomText>
            &copy; Copyright 2023. SEB41_036 All rights reserved.
          </FooterBottomText>
          <FooterBottomText>이용약관</FooterBottomText>
          <FooterBottomText>개인정보처리방침</FooterBottomText>
          <FooterBottomText>제휴제안</FooterBottomText>
        </FooterBottomContainer>
      </FooterContainer>
    </FooterWrapper>
  );
};

export default Footer;
