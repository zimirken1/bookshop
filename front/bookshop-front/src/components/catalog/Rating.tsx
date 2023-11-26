import React, {useState} from 'react';
import {Star} from './Star';

interface RatingProps {
    defaultRating?: number;
    onRatingChange?: (rating: number) => void;
}

export const Rating: React.FC<{ value: number, onChange: (newValue: number) => void }> = ({value, onChange}) => {
    const [highlightedStar, setHighlightedStar] = React.useState<number | null>(null);

    return (
        <div>
            {[...Array(5)].map((_, i) => {
                const starValue = highlightedStar !== null ? (highlightedStar > i ? 1 : 0) : (value >= i + 1 ? 1 : (value > i ? value - i : 0));

                return (
                    <Star
                        key={i}
                        value={starValue}
                        onClick={() => onChange(i + 1)}
                        onMouseEnter={() => setHighlightedStar(i + 1)}
                        onMouseLeave={() => setHighlightedStar(null)}
                    />
                );
            })}
        </div>
    );
};