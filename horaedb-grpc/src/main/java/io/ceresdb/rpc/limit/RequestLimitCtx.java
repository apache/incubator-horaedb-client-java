/*
 * Copyright 2023 CeresDB Project Authors. Licensed under Apache-2.0.
 */
package io.ceresdb.rpc.limit;

public interface RequestLimitCtx {

    String partitionKey();
}
