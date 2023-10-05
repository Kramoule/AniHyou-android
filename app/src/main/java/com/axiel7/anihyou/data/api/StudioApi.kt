package com.axiel7.anihyou.data.api

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.axiel7.anihyou.SearchStudioQuery
import com.axiel7.anihyou.StudioDetailsQuery
import javax.inject.Inject

class StudioApi @Inject constructor(
    private val client: ApolloClient
) {
    fun searchStudioQuery(
        query: String,
        page: Int,
        perPage: Int,
    ) = client
        .query(
            SearchStudioQuery(
                page = Optional.present(page),
                perPage = Optional.present(perPage),
                search = Optional.present(query)
            )
        )

    fun getStudioDetails(
        studioId: Int,
        page: Int,
        perPage: Int,
    ) = client
        .query(
            StudioDetailsQuery(
                studioId = Optional.present(studioId),
                page = Optional.present(page),
                perPage = Optional.present(perPage)
            )
        )
}