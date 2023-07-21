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

package tech.rollw.disk.web.domain.user.service;


import tech.rollw.disk.web.domain.user.AttributedUser;
import tech.rollw.disk.web.domain.user.Role;
import tech.rollw.disk.web.domain.user.common.UserException;
import tech.rollw.disk.common.data.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
public interface UserManageService {
    AttributedUser createUser(String username, String password,
                              String email, Role role, boolean enable);

    AttributedUser getUser(long userId) throws UserException;

    List<? extends AttributedUser> getUsers(Pageable pageable);

    List<? extends AttributedUser> getUsers();
}
