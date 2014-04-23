/*
 * Copyright (C) 2014 Dell, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dell.doradus.service.rest;

import com.dell.doradus.common.RESTResponse;
import com.dell.doradus.common.UNode;

/**
 * Provides a {@link RESTCallback} variant interface that allows the input entity to be
 * passed as a {@link UNode} object while returning the command response as a
 * {@link RESTResponse}. The subclass must implement {@link #invokeUNodeIn(UNode)}, which
 * is called when the callback is invoked.
 */
public abstract class UNodeInCallback extends RESTCallback {

    // Declared "final" so subclass does not attempt to override.
    @Override
    public final RESTResponse invoke() {
        UNode inNode = null;
        if (m_request.getContentLength() > 0) {
            inNode = UNode.parse(m_request.getInputBody(), m_request.getInputContentType());
        }
        return invokeUNodeIn(inNode);
    }   // invoke

    /**
     * The subclass must implement this method, processing the REST request with the given
     * parameters.
     * 
     * @param inNode        Root node of a UNode tree created by parsing the input entity
     *                      with the specified content-type. If there is no input entity,
     *                      this parameter will be null.
     * @return              {@link RESTResponse} to be returned to the client.
     */
    public abstract RESTResponse invokeUNodeIn(UNode inNode);
    
}   // abstract class UNodeInCallback
