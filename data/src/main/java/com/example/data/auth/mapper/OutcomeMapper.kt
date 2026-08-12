package com.example.data.auth.mapper

import com.example.data.core.network.NetworkResponse
import com.example.domain.auth.OutCome

fun <O> NetworkResponse<O>.toOutCome(): OutCome<O> {
    return when (this) {
        is NetworkResponse.Success -> {
            OutCome.Success(data)
        }

        is NetworkResponse.Error -> {
            OutCome.Error(
                message = message,
                cause = cause
            )
        }
    }
}