/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.aws.ec2.xml;

import java.net.URI;
import java.util.Map;

import javax.annotation.Resource;

import org.jclouds.aws.ec2.domain.Region;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.logging.Logger;

import com.google.common.collect.Maps;

/**
 * 
 * @author Adrian Cole
 */
public class DescribeRegionsResponseHandler extends ParseSax.HandlerWithResult<Map<Region, URI>> {
   private StringBuilder currentText = new StringBuilder();

   private Map<Region, URI> regionEndpoints = Maps.newHashMap();
   private Region region;
   private URI regionEndpoint;
   @Resource
   protected Logger logger = Logger.NULL;

   public Map<Region, URI> getResult() {
      return regionEndpoints;
   }

   public void endElement(String uri, String name, String qName) {
      if (qName.equals("regionName")) {
         try {
            region = Region.fromValue(currentText.toString().trim());
         } catch (IllegalArgumentException e) {
            logger.warn(e, "unsupported region: %s", currentText.toString().trim());
            region = Region.UNKNOWN;
         }
      } else if (qName.equals("regionEndpoint")) {
         regionEndpoint = URI.create(String.format("https://%s", currentText.toString().trim()));
      } else if (qName.equals("item")) {
         regionEndpoints.put(region, regionEndpoint);
         this.region = null;
         this.regionEndpoint = null;
      }
      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}