import React from "react";
import styled from "styled-components";

interface ButtonProps {
  width?: string;
  height?: string;
  backgroundColor?: string;
  border?: string;
  color?: string;
  fontsize?: string;
  hoverBackgroundColor?: string;
  hovercolor?: string;
  text?: string;
  type?: string;
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  margin?: string;
}

const VioletButton = styled.button<ButtonProps>`
  display: flex;
  align-items: center;
  justify-content: center;
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  background-color: var(--purple-300);
  border-radius: var(--br-l);
  border: none;
  color: white;
  font-weight: var(--fw-bold);
  font-size: ${(props) => props.fontsize};
  margin: ${(props) => props.margin};
  cursor: pointer;
  &:hover {
    background-color: var(--purple-400);
    color: ${(props) => props.hovercolor};
  }
`;

const WhiteButton = styled.button<ButtonProps>`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  background-color: var(--purple-300);
  border-radius: var(--br-l);
  border: 1px solid white;
  color: white;
  font-weight: var(--fw-bold);
  font-size: ${(props) => props.fontsize};
  margin: ${(props) => props.margin};
  cursor: pointer;
  &:hover {
    background-color: white;
    color: var(--purple-300);
  }
`;
const GrayButton = styled.button<ButtonProps>`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  background-color: var(--black-300);
  border-radius: var(--br-l);
  border: none;
  color: var(--black-650);
  font-weight: var(--fw-bold);
  font-size: ${(props) => props.fontsize};
  margin: ${(props) => props.margin};
  cursor: pointer;
  &:hover {
    background-color: var(--black-400);
    color: var(--black-650);
  }
  :disabled {
    pointer-events: none;
  }
`;

const NoneButton = styled.button<ButtonProps>`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  background-color: var(--black-250);
  border-radius: var(--br-l);
  border: none;
  font-weight: 400;
  color: var(--black-900);
  font-size: var(--font-sx);
  margin: ${(props) => props.margin};
  :disabled {
    pointer-events: none;
  }
`;
const CustomButton = styled.button<ButtonProps>`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  background-color: ${(props) => props.backgroundColor};
  border-radius: var(--br-l);
  border: none;
  font-weight: var(--fw-bold);
  color: var(--purple-400);
  font-size: var(--font-sm);
  margin: ${(props) => props.margin};
  cursor: pointer;
  :hover {
    color: ${(props) => props.hovercolor};
    background-color: ${(props) => props.hoverBackgroundColor};
  }
`;

const Button = ({
  width,
  height,
  fontsize,
  hovercolor,
  text,
  type,
  onClick,
  margin,
  backgroundColor,
  hoverBackgroundColor,
}: ButtonProps) => {
  return (
    <>
      {type === "violet" ? (
        <VioletButton
          width={width}
          height={height}
          fontsize={fontsize}
          hovercolor={hovercolor}
          onClick={onClick}
          margin={margin}
        >
          {text}
        </VioletButton>
      ) : (
        <></>
      )}
      {type === "white" ? (
        <WhiteButton
          width={width}
          height={height}
          fontsize={fontsize}
          hovercolor={hovercolor}
          onClick={onClick}
          margin={margin}
        >
          {text}
        </WhiteButton>
      ) : (
        <></>
      )}
      {type === "gray" ? (
        <GrayButton
          width={width}
          height={height}
          fontsize={fontsize}
          hovercolor={hovercolor}
          onClick={onClick}
          margin={margin}
          disabled
        >
          {text}
        </GrayButton>
      ) : (
        <></>
      )}
      {type === "none" ? (
        <NoneButton
          width={width}
          height={height}
          fontsize={fontsize}
          onClick={onClick}
          margin={margin}
        >
          {text}
        </NoneButton>
      ) : (
        <></>
      )}
      {type === "enabledGray" ? (
        <GrayButton
          width={width}
          height={height}
          fontsize={fontsize}
          hovercolor={hovercolor}
          onClick={onClick}
          margin={margin}
        >
          {text}
        </GrayButton>
      ) : (
        <></>
      )}
      {type === "custom" ? (
        <CustomButton
          width={width}
          height={height}
          fontsize={fontsize}
          hovercolor={hovercolor}
          backgroundColor={backgroundColor}
          onClick={onClick}
          margin={margin}
          hoverBackgroundColor={hoverBackgroundColor}
        >
          {text}
        </CustomButton>
      ) : (
        <></>
      )}
    </>
  );
};

export default Button;
