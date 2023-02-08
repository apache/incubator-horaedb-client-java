/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Copyright 2023 CeresDB Project Authors. Licensed under Apache-2.0.
package io.ceresdb.common.parser;

import io.ceresdb.common.util.ServiceLoader;

public class SqlParserFactoryProvider {

    private static final SqlParserFactory METRIC_PARSER_FACTORY = ServiceLoader //
            .load(SqlParserFactory.class) //
            .firstOrDefault(() -> SqlParserFactory.DEFAULT);

    public static SqlParserFactory getSqlParserFactory() {
        return METRIC_PARSER_FACTORY;
    }
}
