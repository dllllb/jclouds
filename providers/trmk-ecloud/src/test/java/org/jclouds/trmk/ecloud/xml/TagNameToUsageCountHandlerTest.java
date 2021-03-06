package org.jclouds.trmk.ecloud.xml;

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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.jclouds.http.functions.BaseHandlerTest;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.http.functions.config.SaxParserModule;
import org.jclouds.trmk.ecloud.xml.TagNameToUsageCountHandler;
import org.jclouds.trmk.vcloud_0_8.TerremarkVCloudPropertiesBuilder;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.name.Names;

/**
 * Tests behavior of {@code TagNameToUsageCountHandler}
 * 
 * @author Adrian Cole
 */
// NOTE:without testName, this will not call @Before* and fail w/NPE during
// surefire
@Test(groups = "unit", testName = "TagNameToUsageCountHandlerTest")
public class TagNameToUsageCountHandlerTest extends BaseHandlerTest {
   @Override
   @BeforeTest
   protected void setUpInjector() {
      injector = Guice.createInjector(new SaxParserModule() {
         @Override
         public void configure() {
            super.configure();
            Properties props = new Properties();
            Names.bindProperties(binder(),
                  checkNotNull(new TerremarkVCloudPropertiesBuilder(props).build(), "properties"));
         }
      });
      factory = injector.getInstance(ParseSax.Factory.class);
      assert factory != null;
   }

   public void testApplyInputStream() {

      InputStream is = getClass().getResourceAsStream("/deviceTags.xml");

      Map<String, Integer> result = factory.create(injector.getInstance(TagNameToUsageCountHandler.class)).parse(is);
      assertEquals(result, ImmutableMap.<String, Integer> of("Tag1", 1, "Tag2", 5, "Tag3", 12));

   }
}
