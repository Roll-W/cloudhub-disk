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

package tech.rollw.disk.web.controller.statistics;

import tech.rollw.disk.web.controller.AdminApi;
import tech.rollw.disk.web.domain.statistics.DatedData;
import tech.rollw.disk.web.domain.statistics.StatisticsService;
import tech.rollw.disk.common.HttpResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author RollW
 */
@AdminApi
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics/{statisticsKey}")
    public HttpResponseEntity<Map<String, Object>> getStatistics(
            @PathVariable String statisticsKey) {

        Map<String, Object> stats =
                statisticsService.getStatistics(statisticsKey);
        return HttpResponseEntity.success(stats);
    }

    @GetMapping("/statistics/{statisticsKey}/at")
    public HttpResponseEntity<DatedData> getStatisticsAtDate(
            @PathVariable String statisticsKey,
            @RequestParam LocalDate date) {
        DatedData datedData =
                statisticsService.getStatistics(statisticsKey, date);
        return HttpResponseEntity.success(datedData);
    }

    @GetMapping("/statistics/{statisticsKey}/between")
    public HttpResponseEntity<List<DatedData>> getStatisticsBetweenDate(
            @PathVariable String statisticsKey,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        List<DatedData> datedData =
                statisticsService.getStatistics(statisticsKey, start, end);
        return HttpResponseEntity.success(datedData);
    }
}
