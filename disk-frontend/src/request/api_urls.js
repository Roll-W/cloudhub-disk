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

const viteBase = import.meta.env.VITE_BASE_URL;
const httpPrefix = import.meta.env.VITE_HTTP_PREFIX;

export const baseUrl = `${httpPrefix}${viteBase}`;
export const wsBaseUrl = `ws://${viteBase}`;

const prefix = `${baseUrl}/api/v1`;
const adminPrefix = `${baseUrl}/api/v1/admin`;

export const passwordLogin = `${prefix}/user/login/password`;
export const logout = `${prefix}/user/logout`;
export const register = `${prefix}/user/register`;

export const file = (ownerType, ownerId, fileId) =>
    `${prefix}/${ownerType}/${ownerId}/disk/file/${fileId}`;
export const recycles = (ownerType, ownerId) =>
    `${prefix}/${ownerType}/${ownerId}/disk/recycles`;

export const fileType = (ownerType, ownerId, fileType) =>
    `${prefix}/${ownerType}/${ownerId}/disk/file/category/${fileType}`;
export const fileToken = (ownerType, ownerId, fileId) =>
    `${prefix}/${ownerType}/${ownerId}/disk/file/${fileId}/token`;
export const uploadFile = (ownerType, ownerId, folderId) =>
    `${prefix}/${ownerType}/${ownerId}/disk/${folderId}`;
export const getStorageInfo = (ownerType, ownerId, storageType, storageId, admin = false) =>
    `${admin ? adminPrefix : prefix}/${ownerType}/${ownerId}/disk/${storageType}/${storageId}/info`;
export const getStorageAttributes = (ownerType, ownerId, storageType, storageId, admin = false) =>
    `${admin ? adminPrefix : prefix}/${ownerType}/${ownerId}/disk/${storageType}/${storageId}/tags`;
export const getStorageVersions = (ownerType, ownerId, storageType, id, admin = false) =>
    `${admin ? adminPrefix : prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/versions`;
export const getStorageVersion = (ownerType, ownerId, storageType, id, version, admin = false) =>
    `${admin ? adminPrefix : prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/versions/${version}`;
export const getStoragePermissions = (ownerType, ownerId, storageType, id, admin = false) =>
    `${admin ? adminPrefix : prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/permissions`;

export const storagePublicPermission = (ownerType, ownerId, storageType, id) =>
    `${prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/permissions/public`;
export const storageUserPermission = (ownerType, ownerId, storageType, id, userId) =>
    `${prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/permissions/user/${userId}`;

export const folder = (ownerType, ownerId, directory) =>
    `${prefix}/${ownerType}/${ownerId}/disk/folder/${directory}`;

export const foldersIn = (ownerType, ownerId, directory) =>
    `${prefix}/${ownerType}/${ownerId}/disk/folder/${directory}/folders`;
export const storage = (ownerType, ownerId, storageType, id, admin = false) =>
    `${admin ? adminPrefix : prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}`;
export const storageTags = (ownerType, ownerId, storageType, id, admin = false) =>
    storage(ownerType, ownerId, storageType, id, admin) + "/tags";
export const storageName = (ownerType, ownerId, storageType, id) =>
    `${prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/name`;
export const storageParent = (ownerType, ownerId, storageType, id) =>
    storageAction(ownerType, ownerId, storageType, id, "parent");

export const storageAction = (ownerType, ownerId, storageType, id, action) =>
    `${prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/info/${action}`;
export const search = (ownerType, ownerId) =>
    `${prefix}/${ownerType}/${ownerId}/disk/search`;
export const storageShare = (ownerType, ownerId, storageType, id) =>
    `${prefix}/${ownerType}/${ownerId}/disk/${storageType}/${id}/shares`;
export const shareTokenInfo = (token) =>
    `${prefix}/shares/${token}/metadata`;
export const shareToken = (token) =>
    `${prefix}/shares/${token}`;
export const userShare = (id) =>
    `${prefix}/user/shares/${id}`;
export const shareSave = (shareToken, storageType, storageId) =>
    `${prefix}/shares/${shareToken}/save/${storageType}/${storageId}`;

export const shares = (admin = false) =>
    `${admin ? adminPrefix : prefix}/user/shares`;
export const adminShares = () =>
    `${adminPrefix}/shares`;
export const favorites = (id = null) =>
    `${prefix}/user/favorites${id !== null ? `/${id}` : ''}`;
export const userFavorites = (userId, admin = false, id = null) =>
    `${admin ? adminPrefix : prefix}/users/${userId}/favorites${id !== null ? `/${id}` : ''}`;
export const userFavoriteItem = (userId, groupId, itemId = null, admin = false ) =>
    `${admin ? adminPrefix : prefix}/users/${userId}/favorites/${groupId}${itemId !== null ? `/${itemId}` : ''}`;
export const favoriteItem = (groupId, itemId = null) =>
    `${prefix}/user/favorites/${groupId}${itemId !== null ? `/${itemId}` : ''}`;

export const userShares = (userId, admin = false) =>
    `${admin ? adminPrefix : prefix}/users/${userId}/shares`;
export const restricts = (ownerType, ownerId) =>
    `${prefix}/${ownerType}/${ownerId}/statistics/restricts`;
export const restrictByKey = (ownerType, ownerId, key) =>
    `${prefix}/${ownerType}/${ownerId}/statistics/restricts/${key}`;

export const quickfire = (token) =>
    `${prefix}/quickfire/disk/${token}`;

export const getOperationLogsByResource = (resourceType, resourceId) =>
    `${prefix}/${resourceType}/${resourceId}/operations/logs`;

export const getOperationLogsAdmin =
    `${adminPrefix}/operations/logs`;
export const userOperationLogs = (userId) =>
    `${adminPrefix}/users/${userId}/operations/logs`;

export const tagGroups = (admin = false, id = null) =>
    `${admin ? adminPrefix : prefix}/tags/groups${id ? `/${id}` : ''}`;
export const tagGroupsFile = (groupId, admin = false) =>
    `${admin ? adminPrefix : prefix}/tags/groups/${groupId}/infile`;
export const tags = (id = null, admin = false) =>
    `${admin ? adminPrefix : prefix}/tags${id !== null ? `/${id}` : ''}`;
export const tagsKeywords = (id) =>
    `${adminPrefix}/tags/${id}/keywords`;
export const tagGroupsTags = (groupId, tagId = null) =>
    `${adminPrefix}/tags/groups/${groupId}/tags${tagId ? `/${tagId}` : ''}`;

export const files = (admin = false) =>
    `${admin ? adminPrefix : prefix}/disk/files`;
export const userFiles = (userId, admin = false) =>
    `${admin ? adminPrefix : prefix}/users/${userId}/files`;
export const folders = (admin = false) =>
    `${admin ? adminPrefix : prefix}/disk/folders`;
export const storages = (admin = false) =>
    `${admin ? adminPrefix : prefix}/disk/storages`;

export const getCurrentUserOperationLogs = `${prefix}/user/operations/logs`;
export const getCurrentUserLoginLogs = `${prefix}/user/login/logs`;
export const getCurrentUserInfo = `${prefix}/user`;

export const user = (admin = false) =>
    `${admin ? adminPrefix : prefix}/user`;

export const users = (admin = false, id = null) =>
    `${admin ? adminPrefix : prefix}/users${id ? `/${id}` : ''}`;

export const searchUsers = (admin = false) =>
    `${admin ? adminPrefix : prefix}/users/search`;

export const userInfo = (userId, admin = false) =>
    users(admin, userId);

export const currentUserPassword =
    `${prefix}/user/password`;
export const userPassword = (userId) =>
    `${adminPrefix}/users/${userId}/password`;

export const userGroups = (admin = false, id = null) =>
    `${admin ? adminPrefix : prefix}/groups${id !== null ? `/${id}` : ''}`;
export const userGroupSetting = (id) =>
    `${userGroups(true)}/${id}/settings`;
export const userGroupsMembers = (admin, userGroupId) =>
    `${userGroups(admin)}/${userGroupId}/members`;

export const ownerUserGroup = (id, type = 'user', admin = false) =>
    `${admin ? adminPrefix : prefix}/${type}/${id}/groups`;

export const getUsers = `${adminPrefix}/users`;
export const loginLogs = (id = null) =>
    `${adminPrefix}/users/${id ? id + '/' : ''}login/logs`;
export const getErrorLogs = `${adminPrefix}/system/errors`;

export const jobs = `${adminPrefix}/jobs`;
export const statistics = (key) =>
    `${adminPrefix}/statistics/${key}`;
export const serverStatus = `${adminPrefix}/server/cfs/status`;
export const serverStatusSummary = `${adminPrefix}/server/status/summary`;
export const cfsStatus = (serverId) =>
    `${adminPrefix}/server/cfs/status/${serverId}`;
export const serverContainers = (serverId) =>
    `${adminPrefix}/server/cfs/status/${serverId}/containers`;
export const fileServers = `${adminPrefix}/server/cfs/connected`;