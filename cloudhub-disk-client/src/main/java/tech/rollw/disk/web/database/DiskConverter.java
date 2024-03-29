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

package tech.rollw.disk.web.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import tech.rollw.disk.web.domain.storagepermission.PermissionType;
import tech.rollw.disk.web.domain.tag.TagKeyword;
import space.lingu.light.DataConverter;

import java.util.*;

/**
 * @author RollW
 */
public class DiskConverter {

    private static final long[] EMPTY_LONG_ARRAY = new long[0];

    @DataConverter
    public static long[] convertFrom(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return EMPTY_LONG_ARRAY;
        }
        String[] split = s.split(",");
        long[] result = new long[split.length];
        for (int i = 0; i < split.length; i++) {
            try {
                result[i] = Long.parseLong(split[i]);
            } catch (NumberFormatException e) {
                return EMPTY_LONG_ARRAY;
            }
        }
        return result;
    }

    @DataConverter
    public static String convertTo(long[] array) {
        if (array == null || array.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (long l : array) {
            builder.append(l).append(",");
        }
        return builder.toString();
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @DataConverter
    public static Map<String, String> convertMapFrom(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return Map.of();
        }
        try {
            return (Map<String, String>) MAPPER.readValue(s, Map.class);
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    @DataConverter
    public static String convertMapTo(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        try {
            return MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @DataConverter
    public static Map<String, Long> convertLongMapFrom(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return Map.of();
        }
        try {
            return (Map<String, Long>) MAPPER.readValue(s, Map.class);
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    @DataConverter
    public static String convertLongMapTo(Map<String, Long> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        try {
            return MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @DataConverter
    public static String convertToPermissionType(List<PermissionType> permissionTypes) {
        if (permissionTypes == null || permissionTypes.isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(",");
        for (PermissionType permissionType : permissionTypes) {
            joiner.add(permissionType.name());
        }
        return joiner.toString();
    }

    @DataConverter
    public static List<PermissionType> convertFromPermissionType(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return List.of();
        }
        String[] split = s.split(",");
        List<PermissionType> permissionTypes = new ArrayList<>();
        for (String s1 : split) {
            permissionTypes.add(PermissionType.valueOf(s1));
        }
        return permissionTypes;
    }

    @DataConverter
    public static String[] convertToArray(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return new String[0];
        }

        return s.split(",");
    }

    @DataConverter
    public static String convertToString(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(",");
        for (String s : array) {
            joiner.add(s);
        }
        return joiner.toString();
    }

    @DataConverter
    public static List<TagKeyword> convertToKeywords(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return List.of();
        }

        // name^value#name^value
        List<TagKeyword> keywords = new ArrayList<>();
        String[] split = s.split("#");
        for (String s1 : split) {
            String[] split1 = s1.split("\\^");
            keywords.add(new TagKeyword(split1[0], Integer.parseInt(split1[1])));
        }
        return keywords;
    }

    @DataConverter
    public static String convertFromKeywords(List<TagKeyword> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return "";
        }

        // name^value#name^value
        StringBuilder builder = new StringBuilder();
        for (TagKeyword keyword : keywords) {
            builder.append(keyword.name()).append("^")
                    .append(keyword.weight())
                    .append("#");
        }
        return builder.toString();
    }

    private DiskConverter() {
    }
}
