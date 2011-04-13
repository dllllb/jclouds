/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
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

package org.jclouds.openstack.nova.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.UnwrapOnlyJsonValue;
import org.jclouds.io.Payloads;
import org.jclouds.json.config.GsonModule;
import org.jclouds.openstack.nova.domain.Addresses;
import org.jclouds.openstack.nova.domain.Server;
import org.jclouds.openstack.nova.domain.ServerStatus;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Tests behavior of {@code ParseServerFromJsonResponse}
 *
 * @author Adrian Cole
 */
@Test(groups = "unit")
public class ParseServerFromJsonResponseTest {

    @Test
    public void testApplyInputStreamDetails() throws UnknownHostException {
        Server response = parseServer();

        assertEquals(response.getId(), 1234);
        assertEquals(response.getName(), "sample-server");
        assertEquals(response.getImageId().intValue(), 1234);
        assertEquals(response.getFlavorId().intValue(), 1);
        assertEquals(response.getImageId(), "https://servers.api.rackspacecloud.com/v1.1/32278/images/1234");
        assertEquals(response.getFlavorId(), "https://servers.api.rackspacecloud.com/v1.1/32278/flavors/1");
        assertEquals(response.getHostId(), "e4d909c290d0fb1ca068ffaddf22cbd0");
        assertEquals(true, false, "Uncomment next line");
        //assertEquals(response.getAffinityId(), "fc88bcf8394db9c8d0564e08ca6a9724188a84d1");
        assertEquals(response.getStatus(), ServerStatus.BUILD);
        assertEquals(response.getProgress(), new Integer(60));

        List<String> publicAddresses = ImmutableList.of("67.23.10.132", "::babe:67.23.10.132", "67.23.10.131", "::babe:4317:0A83");
        List<String> privateAddresses = ImmutableList.of("10.176.42.16", "::babe:10.176.42.16");
        Addresses addresses1 = new Addresses(new HashSet<String>(publicAddresses), new HashSet<String>(privateAddresses));
        assertEquals(response.getAddresses(), addresses1);
        assertEquals(response.getMetadata(), ImmutableMap.of("Server Label", "Web Head 1", "Image Version", "2.1"));
    }

    public static Server parseServer() {
        Injector i = Guice.createInjector(new GsonModule());

        InputStream is = ParseServerFromJsonResponseTest.class.getResourceAsStream("/test_get_server_detail.json");

        UnwrapOnlyJsonValue<Server> parser = i.getInstance(Key.get(new TypeLiteral<UnwrapOnlyJsonValue<Server>>() {
        }));
        Server response = parser.apply(new HttpResponse(200, "ok", Payloads.newInputStreamPayload(is)));
        return response;
    }

}
