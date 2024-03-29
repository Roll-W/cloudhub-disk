/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.rollw.disk.web.domain.user.vo;


import tech.rollw.disk.web.domain.user.AttributedUser;
import tech.rollw.disk.web.domain.user.Role;
import tech.rollw.disk.web.domain.user.UserIdentity;

/**
 * When the user is logged in, the user's personal information is displayed on the page.
 *
 * @author RollW
 */
public record UserCommonDetailsVo(
        long userId,
        Role role,
        String username,
        String nickname,
        String email
) {

    public static UserCommonDetailsVo of(AttributedUser attributedUser) {
        if (attributedUser == null) {
            return null;
        }
        String nickname = attributedUser.getNickname() == null
                ? attributedUser.getUsername()
                : attributedUser.getNickname();

        return new UserCommonDetailsVo(
                attributedUser.getUserId(),
                attributedUser.getRole(),
                attributedUser.getUsername(),
                nickname,
                attributedUser.getEmail()
        );
    }

    public static UserCommonDetailsVo of(UserIdentity userIdentity) {


        return new UserCommonDetailsVo(
                userIdentity.getUserId(),
                userIdentity.getRole(),
                userIdentity.getUsername(),
                null,
                userIdentity.getEmail()
        );
    }
}
