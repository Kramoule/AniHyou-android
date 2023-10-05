package com.axiel7.anihyou.data.model.review

import com.axiel7.anihyou.ReviewDetailsQuery

fun ReviewDetailsQuery.Review.userAcceptance() =
    if (ratingAmount != null && rating != null) (rating * 100) / ratingAmount else 0