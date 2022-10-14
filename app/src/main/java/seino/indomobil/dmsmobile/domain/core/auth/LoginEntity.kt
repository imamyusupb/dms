/*
 * *
 *    Created by Seino Developer on 10/13/22, 2:05 PM                           *
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

package seino.indomobil.dmsmobile.domain.core.auth

data class LoginEntity(
    var name: String,
    var accessToken: String,
    var refreshToken: String
)