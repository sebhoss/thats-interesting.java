/*
 * This file is part of TI. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of TI,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.ti.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Delegating invocation handler that calls all given handlers in order they were given.
 */
public final class DelegatingInvocationHandler extends AbstractNullReturningInvocationHandler {

    private final Iterable<InvocationHandler> handlers;

    /**
     * @param handlers The handlers to call.
     */
    public DelegatingInvocationHandler(final Iterable<InvocationHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    protected void invokeHandler(final Object proxy, final Method method, final Object[] args) throws Throwable {
        for (final InvocationHandler handler : handlers) {
            handler.invoke(proxy, method, args);
        }
    }

}
