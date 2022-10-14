/*
 * *
 *    Created by Seino Developer on 10/13/22, 2:02 PM                           *
 *    Copyright (c) 2022 DevTeam - New DMS Mobile. All rights reserved.           *
 *                                                                                       *
 *    This project and associated documentation files are limited to be used,            *
 *    reproduced, distributed, copied, modified, merged, published, sublicensed,         *
 *    and/or sold to only authorized staff. Should you find yourself is unauthorized,    *
 *    please do not use this project and its associated documentation files in any       *
 *    kind of intentions and conditions.                                                 *
 *                                                                                       *
 *    In order to obtain access to use and involve in this project, you may proceed      *
 *    to inform the authorized staff. By using and involving in this project, you agree  *
 *    to follow our regulations, terms and conditions.                                   *
 *                                                                                       *
 *    This project and source code may use libraries or frameworks that are released     *
 *    under various Open-Source license. Use of those libraries and frameworks are       *
 *    governed by their own individual licenses.                                         *
 *                                                                                       *
 *    The use of this project and source code follows the guideline as described and     *
 *    explained on confluence seinoindomobil.co.id under DMS Mobile New Platform project.*
 *    Please always refer to the project space to follow the guideline.                  *
 *                                                                                       *
 *    If you have any question, please inform our staff or development leader.           *
 *
 */

package seino.indomobil.dmsmobile.data.source.auth.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import seino.indomobil.dmsmobile.data.common.ResponseWrapper
import seino.indomobil.dmsmobile.data.source.auth.dto.Login
import seino.indomobil.dmsmobile.data.source.auth.network.LoginService
import seino.indomobil.dmsmobile.domain.core.auth.LoginEntity
import seino.indomobil.dmsmobile.domain.core.auth.LoginRepository
import seino.indomobil.dmsmobile.domain.utils.BaseResult
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginService
) : LoginRepository {
    override suspend fun login(loginRequest: Login.LoginRequest): Flow<BaseResult<LoginEntity, ResponseWrapper<Login.LoginResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            val body = response.body()!!

            if (response.isSuccessful) {
                val loginEntity = LoginEntity(
                    name = body.result?.name!!,
                    accessToken = body.result?.accessToken!!,
                    refreshToken = body.result?.refreshToken!!
                )
                emit(BaseResult.Success(loginEntity))
            } else {
                val type = object : TypeToken<Login.LoginResponse>() {}.type
                val loginResponse: Login.LoginResponse =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)

                val wrapper = ResponseWrapper(
                    timestamp = body.timestamp,
                    result = loginResponse,
                    code =  body.code,
                    status = body.status
                )
                emit(BaseResult.Error(wrapper))
            }
        }
    }
}