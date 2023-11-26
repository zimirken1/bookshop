import styled from 'styled-components';
import React from 'react';

interface StarProps {
    value: number;
    onClick: () => void;
    onMouseEnter?: () => void;
    onMouseLeave?: () => void;
}

export const Star: React.FC<StarProps> = ({ value, onClick, onMouseEnter, onMouseLeave }) => (
    <StarWrapper value={value} onClick={onClick} onMouseEnter={onMouseEnter} onMouseLeave={onMouseLeave}>
        ★
    </StarWrapper>
);

const StarWrapper = styled.div<StarProps>`
  cursor: pointer;
  display: inline-block;
  overflow: hidden;
  position: relative;
  color: gray;

  &::before {
    content: "★";
    position: absolute;
    top: 0;
    left: 0;
    width: ${props => props.value * 100}%;
    color: gold;
    overflow: hidden;
    white-space: nowrap;
  }
`;