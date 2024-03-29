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

package tech.rollw.disk.common.conf;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author RollW
 */
public class ClientConfigLoader extends AbstractConfigLoader {
    public static final int RPC_PORT_DEFAULT = 7011;
    public static final int RPC_MAX_INBOUND_SIZE_DEFAULT = 40;

    public static final int WEB_PORT_DEFAULT = 7010;
    public static final String LOG_LEVEL_DEFAULT = "info";
    public static final String LOG_PATH_DEFAULT = "console";

    public static final String FILE_TEMP_PATH_DEFAULT = "tmp/tmp";

    public ClientConfigLoader(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    public int getRpcPort() {
        return getInt(ClientConfigKeys.RPC_PORT, RPC_PORT_DEFAULT);
    }

    public int getRpcPort(int defaultPort) {
        return getInt(ClientConfigKeys.RPC_PORT, defaultPort);
    }

    public int getWebPort() {
        return getInt(ClientConfigKeys.WEB_PORT, WEB_PORT_DEFAULT);
    }

    public int getWebPort(int defaultPort) {
        return getInt(ClientConfigKeys.WEB_PORT, defaultPort);
    }

    public String getTempFilePath() {
        return get(ClientConfigKeys.FILE_TEMP_PATH, FILE_TEMP_PATH_DEFAULT);
    }

    public int getRpcMaxInboundSize() {
        return getInt(ClientConfigKeys.RPC_MAX_INBOUND_SIZE,
                RPC_MAX_INBOUND_SIZE_DEFAULT);
    }

    public String getLogLevel() {
        return get(ClientConfigKeys.LOG_LEVEL, LOG_LEVEL_DEFAULT);
    }

    public String getLogPath() {
        return get(ClientConfigKeys.LOG_PATH, LOG_PATH_DEFAULT);
    }

    public String getMetaServerAddress() {
        return get(ClientConfigKeys.META_ADDRESS, null);
    }

    public String getDatabaseUrl() {
        return get(ClientConfigKeys.DATABASE_URL, null);
    }

    public String getDatabaseUsername() {
        return get(ClientConfigKeys.DATABASE_USERNAME, null);
    }

    public String getDatabasePassword() {
        return get(ClientConfigKeys.DATABASE_PASSWORD, null);
    }

    public static ClientConfigLoader tryOpenDefault(Class<?> loader) throws IOException {
        return new ClientConfigLoader(
                openConfigInput(loader, null)
        );
    }
}
