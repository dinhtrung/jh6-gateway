/*
 * Copyright 2016-2018 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see https://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ft.service.util;

import java.time.*;
import java.util.Date;

import org.bson.Transformer;


public final class JSR310DateTransformer {

    private JSR310DateTransformer() {
    }

    public static class LocalDateToDateTransformer implements Transformer {

        public static final LocalDateToDateTransformer INSTANCE = new LocalDateToDateTransformer();

        private LocalDateToDateTransformer() {
        }

        @Override
        public Object transform(Object source) {
            return source == null ? null : Date.from(((LocalDate) source).atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateTransformer implements Transformer {

        public static final DateToLocalDateTransformer INSTANCE = new DateToLocalDateTransformer();

        private DateToLocalDateTransformer() {
        }

        @Override
        public Object transform(Object source) {
            return source == null ? null : ZonedDateTime.ofInstant(((Date) source).toInstant(), ZoneId.systemDefault())
                .toLocalDate();
        }
    }

    public static class ZonedDateTimeToDateTransformer implements Transformer {

        public static final ZonedDateTimeToDateTransformer INSTANCE = new ZonedDateTimeToDateTransformer();

        private ZonedDateTimeToDateTransformer() {
        }

        @Override
        public Object transform(Object source) {
            return source == null ? null : Date.from(((ZonedDateTime) source).toInstant());
        }
    }

    public static class DateToZonedDateTimeTransformer implements Transformer {

        public static final DateToZonedDateTimeTransformer INSTANCE = new DateToZonedDateTimeTransformer();

        private DateToZonedDateTimeTransformer() {
        }

        @Override
        public Object transform(Object source) {
            return source == null ? null : ZonedDateTime.ofInstant(((Date) source).toInstant(), ZoneId.systemDefault());
        }
    }

    public static class LocalDateTimeToDateTransformer implements Transformer {

        public static final LocalDateTimeToDateTransformer INSTANCE = new LocalDateTimeToDateTransformer();

        private LocalDateTimeToDateTransformer() {
        }

        @Override
        public Object transform(Object source) {
            return source == null ? null : Date.from(((LocalDateTime) source).atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateTimeTransformer implements Transformer {

        public static final DateToLocalDateTimeTransformer INSTANCE = new DateToLocalDateTimeTransformer();

        private DateToLocalDateTimeTransformer() {
        }

        @Override
        public Object transform(Object source) {
            return source == null ? null : LocalDateTime.ofInstant(((Date)source).toInstant(), ZoneId.systemDefault());
        }
    }
}
