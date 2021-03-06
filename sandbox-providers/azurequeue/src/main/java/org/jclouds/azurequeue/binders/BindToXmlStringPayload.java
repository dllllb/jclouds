/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.azurequeue.binders;

import javax.inject.Singleton;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.binders.BindToStringPayload;

/**
 * Adds an payload to a request.
 * 
 * @author Adrian Cole
 */
@Singleton
public class BindToXmlStringPayload extends BindToStringPayload {
   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object payload) {
      return super.bindToRequest(request, new StringBuilder().append("<QueueMessage><MessageText>").append(payload)
            .append("</MessageText></QueueMessage>").toString());
   }
}
