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
package io.ceresdb.common.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link java.util.concurrent.ExecutorService} that witch can print
 * error message for failed execution.
 *
 */
public class LogThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(LogThreadPoolExecutor.class);

    private final int    corePoolSize;
    private final int    maximumPoolSize;
    private final String name;

    public LogThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue, String name) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.name = name;
    }

    public LogThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, String name) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.name = name;
    }

    public LogThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, String name) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.name = name;
    }

    public LogThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                 RejectedExecutionHandler handler, String name) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected void afterExecute(final Runnable r, Throwable t) {
        super.afterExecute(r, t);

        if (t == null && r instanceof Future<?>) {
            try {
                final Future<?> f = (Future<?>) r;
                if (f.isDone()) {
                    f.get();
                }
            } catch (final CancellationException | ExecutionException ee) {
                t = ee.getCause();
            } catch (final InterruptedException ie) {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }
        if (t != null) {
            LOG.error("Uncaught exception in pool: {}, {}.", this.name, super.toString(), t);
        }
    }

    @Override
    protected void terminated() {
        super.terminated();

        LOG.info("ThreadPool is terminated: {}, {}.", this.name, super.toString());
    }

    @Override
    public String toString() {
        return "ThreadPoolExecutor{" + //
               "corePoolSize=" + corePoolSize + //
               ", maximumPoolSize=" + maximumPoolSize + //
               ", name='" + name + '\'' + //
               "} " + super.toString();
    }
}
